// Sales Report by Date
var data;
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalItems;

$(document).ready(function() {
	
	setupButtonEventHandlers("_date", loadSalesReportByDate);
});

function validateDateRange() {
	days = calculateDays();

	startDateField.setCustomValidity("");

	if (days >= 7 && days <= 30) {
		loadSalesReportByDate("custom");
	} else {
		startDateField.setCustomValidity("Dates must be in the range of 7..30 days");
		startDateField.reportValidity();
	}
	
}

function calculateDays() {
	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;

	differenceInMilliseconds = endDate - startDate;
	return differenceInMilliseconds / MILLISECONDS_A_DAY;
	
}

function initCustomDateRange() {
	toDate = new Date();
	endDateField.valueAsDate = toDate;

	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
	
}

function loadSalesReportByDate(period) {
	
	if (period == "custom") {
		startDate = $("#startDate_date").val();
		endDate = $("#endDate_date").val();

		requestURL = contextPath + "reports/sales_by_date/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/sales_by_date/" + period;		
	}

	$.get(requestURL, function(responseJSON) {
		prepareChartDataForSalesReportByDate(responseJSON);
		customizeChartForSalesReportByDate(period);
		formatChartData(data, 1, 2);
		drawChartForSalesReportByDate(period);
		setSalesAmount(period, '_date', "Total Orders");
	});
}

function prepareChartDataForSalesReportByDate(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Orders');

	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;

	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.ordersCount);
	});
}

function customizeChartForSalesReportByDate(period) {
	chartOptions = {
		title: getChartTitle(period),
		'height': 360,
		legend: {position: 'top'},

		series: {
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
			2: {targetAxisIndex: 1},
		},

		vAxes: {
			0: {title: 'Sales Amount', format: 'currency'},
			1: {title: 'Number of Orders'}
		}
	};
}

function drawChartForSalesReportByDate() {
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
	salesChart.draw(data, chartOptions);
}

 