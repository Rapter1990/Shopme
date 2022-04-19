function clearFilter() {
	window.location = moduleURL;
}

function handleDetailLinkClick(linkClass, modalId) {
	$(linkClass).on("click", function(e) {
		e.preventDefault();
		linkDetailURL = $(this).attr("href");
		$(modalId).modal("show").find(".modal-content").load(linkDetailURL);
	});		
}

function handleDefaultDetailLinkClick() {
	handleDetailLinkClick(".link-detail", "#detailModal");	
} 