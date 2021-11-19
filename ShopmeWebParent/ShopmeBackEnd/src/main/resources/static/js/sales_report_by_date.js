// Sales Report by Date
var data;
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalOrders;

$(document).ready(function() {
	$(".button-sales-by-date").on("click", function() {

		$(".button-sales-by-date").each(function(e) {
			$(this).removeClass('btn-primary').addClass('btn-light');
		});

		$(this).removeClass('btn-light').addClass('btn-primary');

		period = $(this).attr("period");		
		loadSalesReportByDate(period);
	});
});

function loadSalesReportByDate(period) {
	requestURL = contextPath + "reports/sales_by_date/" + period;

	$.get(requestURL, function(responseJSON) {
		prepareChartData(responseJSON);
		customizeChart(period);
		drawChart(period);
	});
}

function prepareChartData(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Orders');

	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalOrders = 0;

	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalOrders += parseInt(reportItem.ordersCount);
	});
}

function customizeChart(period) {
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

	var formatter = new google.visualization.NumberFormat({
		prefix: '$'
	});

	formatter.format(data, 1);
	formatter.format(data, 2);
}

function drawChart(period) {
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
	salesChart.draw(data, chartOptions);

	$("#textTotalGrossSales").text("$" + $.number(totalGrossSales, 2));
	$("#textTotalNetSales").text("$" + $.number(totalNetSales, 2));

	days = getDays(period);

	$("#textAvgGrossSales").text("$" + $.number(totalGrossSales / days, 2));
	$("#textAvgNetSales").text("$" + $.number(totalNetSales / days, 2));
	$("#textTotalOrders").text(totalOrders);
}

function getChartTitle(period) {
	if (period == "last_7_days") return "Sales in Last 7 Days";
	if (period == "last_28_days") return "Sales in Last 28 Days";

	return "";
}

function getDays(period) {
	if (period == "last_7_days") return 7;
	if (period == "last_28_days") return 28;

	return 7;
} 