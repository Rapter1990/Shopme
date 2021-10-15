$(document).ready(function(){
	
	$("#productList").on("click", ".linkRemove", function(e) {
		
		e.preventDefault();
		
		if (doesOrderHaveOnlyOneProduct()) {
			showWarningModal("Could not remove product. The order must have eat least one product.");
		} else {
			removeProduct($(this));		
			updateOrderAmounts();	
		}
	});
});

function doesOrderHaveOnlyOneProduct() {
	productCount = $(".hiddenProductId").length;
	return productCount == 1;
}

function removeProduct(link) {
	
	rowNumber = link.attr("rowNumber");
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
	
	// Redefine produdct index after deleting.
	// When there are three products and delete second one(1,2,3)  ,  third product replace second one and its index value will be 2.
	// When there are two products and delete first one (1,2), second one replace first one and its index value will be 1.
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
} 

