$(document).ready(function() {
	$("#addProduct").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		url = link.attr('href');

		$('#addProductModal').on('shown.bs.modal', function() {
		    $(this).find('iframe').attr('src', url)
		});

		$('#addProductModal').modal();		
	});

	$("#productList").on("click", ".link-remove", function(e) {
		e.preventDefault();
		deleteProduct($(this));
	});

	$("#productList").on("click", ".link-move-left", function(e) {
		e.preventDefault();
		moveProductToLeft($(this));
	});	

	$("#productList").on("click", ".link-move-right", function(e) {
		e.preventDefault();
		moveProductToRight($(this));
	});	
});

function deleteProduct(link) {
	colNumber = link.attr('colNumber');
	colId = "col" + colNumber;
	$("#" + colId).remove();	
}

function addProduct(productId) {
	if (isProductAlreadyAdded(productId)) {
		alert("The productId " + productId + " is already added.");
	} else {
		insertProductCode(productId);
		$('#addProductModal').modal("hide");		
	}
}

function isProductAlreadyAdded(productId) {
	productExisted = false;

	$(".product-id-hidden").each(function(e) {
		aProductId = $(this).val();
		if (aProductId == productId) {
			productExisted = true;
			return;
		}
	});

	return productExisted;
}

function insertProductCode(productId) {
	nextCount = $(".product-id-hidden").length + 1;

	url = contextPath + "products/get/" + productId;

	$.get(url, function(productJson) {
		/* use this line if images stored on localhost */
		//productImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath;

		/* use this line if images stored on S3 */		
		productImagePath = productJson.imagePath;

		productName = productJson.name;

		htmlCode = generateProductCode(productId, productName, productImagePath, nextCount);

		$("#productList").append(htmlCode);

	});
}

function generateProductCode(productId, productName, productImagePath, nextCount) {
	colId = "col" + nextCount;

	productCode = `
		<div class="col-sm-2" id="${colId}" draggable="true" ondragstart="dragProduct(event)">
			<input type="hidden" name="productId" value="${productId}" class="product-id-hidden" />
			<div class="mt-1">
				<a class="fas fa-trash icon-dark link-remove" href="" colNumber="${nextCount}"></a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a class="fas fa-chevron-left icon-dark link-move-left" href="" title="Move this product to the left" colNumber="${nextCount}"></a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a class="fas fa-chevron-right icon-dark link-move-right" href="" title="Move this product to the right" colNumber="${nextCount}"></a>				
			</div>					
			<div><img src="${productImagePath}" height="100px" /></div>
			<div>
				<b>${productName}</b>
			</div>
		</div>	
	`;

	return productCode;
}

function dragProduct(event) {
	ev.dataTransfer.setData("text", ev.target.id);	
}

function moveProductToLeft(link) {
	colNumber = link.attr('colNumber');
	colId = "col" + colNumber;
	thisProduct = $("#" + colId);
	leftProduct = thisProduct.prev();
	thisProduct.insertBefore(leftProduct);
}

function moveProductToRight(link) {
	colNumber = link.attr('colNumber');
	colId = "col" + colNumber;
	thisProduct = $("#" + colId);
	rightProduct = thisProduct.next();
	thisProduct.insertAfter(rightProduct);
}