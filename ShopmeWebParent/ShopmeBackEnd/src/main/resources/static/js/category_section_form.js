var chosenCategoriesDropDown;
var allCategoriesDropDown;

$(document).ready(function() {
	chosenCategoriesDropDown = $('#chosenCategories');
	allCategoriesDropDown = $('#categories');

	$("#buttonAddCategory").on("click", function(e) {
		addSelectedCategory();
	});

	$("#linkRemoveCategory").on("click", function(e) {
		e.preventDefault();
		removeAChosenCategory();
	});

	$("#linkMoveCatUp").on("click", function(e) {
		e.preventDefault();
		moveAChosenCategoryUp();
	});		

	$("#linkMoveCatDown").on("click", function(e) {
		e.preventDefault();
		moveAChosenCategoryDown();
	});	
});

function addSelectedCategory() {
	allCategoriesDropDown.children('option:selected').each(function() {
		selectedCategory = $(this);
		catId = selectedCategory.val();

		catName = selectedCategory.text().replace(/-/g, "");

		dropdownChosenCategories = $('#chosenCategories');

		if (!isCategoryAdded(catId)) {
			$("<option>").val(catId + "-0").text(catName).appendTo(dropdownChosenCategories);			
		}

	});		
}

function isCategoryAdded(catId) {
	isAdded = false;

	chosenCategoriesDropDown.children('option').each(function() {
		chosenCategory = $(this);
		chosenCatId = chosenCategory.val().split("-")[0];

		if (catId == chosenCatId) {
			isAdded = true;
			return;
		}
	});

	return isAdded;
}

function removeAChosenCategory() {
	chosenCatId = chosenCategoriesDropDown.val();

	$("#chosenCategories option[value='" + chosenCatId + "']").remove();	
}

function moveAChosenCategoryUp() {
	selectedChosenCategory = $("#chosenCategories option:selected");
	if (selectedChosenCategory != null) {
		chosenCategoryAbove = selectedChosenCategory.prev();
		selectedChosenCategory.insertBefore(chosenCategoryAbove);
	}
}

function moveAChosenCategoryDown() {
	selectedChosenCategory = $("#chosenCategories option:selected");
	if (selectedChosenCategory != null) {
		chosenCategoryBelow = selectedChosenCategory.next();
		selectedChosenCategory.insertAfter(chosenCategoryBelow);
	}	
}

function processBeforeSubmit() {
	chosenCategoriesDropDown.children('option').each(function() {
		$(this).prop('selected', true);
	});	
}