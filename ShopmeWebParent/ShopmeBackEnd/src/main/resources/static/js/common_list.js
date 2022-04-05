$(document).ready(function() {
	$(".link-delete").on("click", function(e) {
		e.preventDefault();
		showDeleteConfirmModal($(this), entityName);
	});
});


function clearFilter() {
	window.location = moduleURL;	
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");

	$("#yesButton").attr("href", link.attr("href"));	
	$("#confirmText").text("Are you sure you want to delete this "
							 + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal();	
}

function handleDetailLinkClick(cssClass, modalId) {
	$(cssClass).on("click", function(e) {
		e.preventDefault();
		linkDetailURL = $(this).attr("href");
		$(modalId).modal("show").find(".modal-content").load(linkDetailURL);
	});		
}

function handleDefaultDetailLinkClick() {
	handleDetailLinkClick(".link-detail", "#detailModal");	
}