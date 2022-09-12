var chosenBrandsDropDown;
var allBrandsDropDown;

$(document).ready(function() {
	chosenBrandsDropDown = $('#chosenBrands');
	allBrandsDropDown = $('#brands');

	$("#buttonAddBrand").on("click", function(e) {
		addSelectedBrand();
	});

	$("#linkRemoveBrand").on("click", function(e) {
		e.preventDefault();
		removeAChosenBrand();
	});

	$("#linkMoveBrandUp").on("click", function(e) {
		e.preventDefault();
		moveAChosenBrandUp();
	});		

	$("#linkMoveBrandDown").on("click", function(e) {
		e.preventDefault();
		moveAChosenBrandDown();
	});	
});

function addSelectedBrand() {
	allBrandsDropDown.children('option:selected').each(function() {
		selectedBrand = $(this);

		brandId = selectedBrand.val();		
		brandName = selectedBrand.text();

		dropdownChosenBrands = $('#chosenBrands');

		if (!isBrandAdded(brandId)) {
			$("<option>").val(brandId + "-0").text(brandName).appendTo(dropdownChosenBrands);
		}

	});		
}

function isBrandAdded(brandId) {
	isAdded = false;

	chosenBrandsDropDown.children('option').each(function() {
		chosenBrand = $(this);
		chosenBrandId = chosenBrand.val().split("-")[0];

		if (brandId == chosenBrandId) {
			isAdded = true;
			return;
		}
	});

	return isAdded;
}

function removeAChosenBrand() {
	chosenBrandId = chosenBrandsDropDown.val();

	$("#chosenBrands option[value='" + chosenBrandId + "']").remove();
}

function moveAChosenBrandUp() {
	selectedChosenBrand = $("#chosenBrands option:selected");
	if (selectedChosenBrand != null) {
		chosenBrandAbove = selectedChosenBrand.prev();
		selectedChosenBrand.insertBefore(chosenBrandAbove);
	}
}

function moveAChosenBrandDown() {
	selectedChosenBrand = $("#chosenBrands option:selected");
	if (selectedChosenBrand != null) {
		chosenBrandBelow = selectedChosenBrand.next();
		selectedChosenBrand.insertAfter(chosenBrandBelow);
	}	
}

function processBeforeSubmit() {
	chosenBrandsDropDown.children('option').each(function() {
		$(this).prop('selected', true);
	});

}