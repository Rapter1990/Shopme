// Common Javascript code for product reviews/rating
$(document).ready(function() {
	formatRatingNumber();
});

$(".product-detail-rating-star").rating({
	displayOnly: true,
	hoverOnClear: false,
	showCaption: false,
	theme: 'krajee-svg'
});

function formatRatingNumber() {
	ratingText = $("#ratingNumber").text();
	formattedRating = $.number(ratingText, 2, decimalSeparator, thousandSeparator);
	$("#ratingNumber").text(formattedRating);
} 