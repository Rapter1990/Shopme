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

	function checkEmailUnique(form) {
		customerId = $("#id").val();
		customerEmail = $("#email").val();
		csrfValue = $("input[name='_csrf'").val();

		url = "[[@{/customers/check_email}]]";
		params = {id : customerId, email: customerEmail, _csrf: csrfValue};

		$.post(url, params, function(response) {
			if (response == "OK") {
				form.submit();
			} else if (response == "Duplicated") {
				showWarningModal("There is another customer having the email " + customerEmail);
			} else {
				showErrorModal("Unknown response from server");
			}			
		}).fail(function() {
			showErrorModal("Could not connect to the server");	
		});

		return false;
	}	