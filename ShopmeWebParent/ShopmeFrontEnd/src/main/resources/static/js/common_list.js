function clearFilter() {
	window.location = moduleURL;
}

function handleDetailLink(linkClass, modalId) {
	$(linkClass).on("click", function(e) {
		e.preventDefault();
		linkDetailURL = $(this).attr("href");
		$(modalId).modal("show").find(".modal-content").load(linkDetailURL);
	});		
}