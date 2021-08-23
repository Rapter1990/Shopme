var dropdownCountries;
var dropdownStates;

$(document).ready(function() {
	dropdownCountries = $("#country");
	dropdownStates = $("#listStates");

	dropdownCountries.on("change", function() {
		loadStates4Country();
		$("#state").val("").focus();
	});	

	loadStates4Country();

	$('#buttonCancel').click(function() { 
		window.location = "[[@{/customers}]]";	
	});		
});

function loadStates4Country() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();

	url = "[[@{/states/list_by_country/}]]" + countryId;

	$.get(url, function(responseJson) {
		dropdownStates.empty();

		$.each(responseJson, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dropdownStates);
		});
	}).fail(function() {
		showErrorModal("Error loading states/provinces for the selected country.");
	})	
}	