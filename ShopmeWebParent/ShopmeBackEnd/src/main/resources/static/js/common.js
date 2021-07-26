
$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
	
	customizeDropDownMenu();
	customizeTabs();
}); 


function customizeDropDownMenu() {
	$(".navbar .dropdown").hover(
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
		},
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
		}
	);

	$(".dropdown > a").click(function() {
		location.href = this.href;
	});
} 

function customizeTabs() {
	// Javascript to enable link to tab
	var url = document.location.toString();
	if (url.match('#')) {
	    $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
	} 

	// Change hash for page-reload
	$('.nav-tabs a').on('shown.bs.tab', function (e) {
	    window.location.hash = e.target.hash;
	})	
}