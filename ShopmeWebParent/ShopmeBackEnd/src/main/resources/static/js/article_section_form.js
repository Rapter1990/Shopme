var chosenArticlesDropDown;
var allArticlesDropDown;

$(document).ready(function() {
	chosenArticlesDropDown = $('#chosenArticles');
	allArticlesDropDown = $('#articles');

	$("#buttonAddArticle").on("click", function(e) {
		addSelectedArticle();
	});

	$("#linkRemoveArticle").on("click", function(e) {
		e.preventDefault();
		removeAChosenArticle();
	});

	$("#linkMoveArticleUp").on("click", function(e) {
		e.preventDefault();
		moveAChosenArticleUp();
	});		

	$("#linkMoveArticleDown").on("click", function(e) {
		e.preventDefault();
		moveAChosenArticleDown();
	});	
});

function addSelectedArticle() {
	allArticlesDropDown.children('option:selected').each(function() {
		selectedArticle = $(this);

		articleId = selectedArticle.val();		
		articleTitle = selectedArticle.text();

		dropdownChosenArticles = $('#chosenArticles');

		if (!isArticleAdded(articleId)) {
			$("<option>").val(articleId + "-0").text(articleTitle).appendTo(dropdownChosenArticles);
		}

	});		
}

function isArticleAdded(articleId) {
	isAdded = false;

	chosenArticlesDropDown.children('option').each(function() {
		chosenArticle = $(this);
		chosenArticleId = chosenArticle.val().split("-")[0];

		if (articleId == chosenArticleId) {
			isAdded = true;
			return;
		}
	});

	return isAdded;
}

function removeAChosenArticle() {
	chosenArticleId = chosenArticlesDropDown.val();

	$("#chosenArticles option[value='" + chosenArticleId + "']").remove();
}

function moveAChosenArticleUp() {
	selectedChosenArticle = $("#chosenArticles option:selected");
	if (selectedChosenArticle != null) {
		chosenArticleAbove = selectedChosenArticle.prev();
		selectedChosenArticle.insertBefore(chosenArticleAbove);
	}
}

function moveAChosenArticleDown() {
	selectedChosenArticle = $("#chosenArticles option:selected");
	if (selectedChosenArticle != null) {
		chosenArticleBelow = selectedChosenArticle.next();
		selectedChosenArticle.insertAfter(chosenArticleBelow);
	}	
}

function processBeforeSubmit() {
	chosenArticlesDropDown.children('option').each(function() {
		$(this).prop('selected', true);
	});

} 