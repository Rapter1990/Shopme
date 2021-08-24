function checkEmailUnique(form) {
	customerId = $("#id").val();
	customerEmail = $("#email").val();
	csrfValue = $("input[name='_csrf'").val();

	// url = "[[@{/customers/check_email}]]";
	url = moduleURL + "/check_email";
	
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