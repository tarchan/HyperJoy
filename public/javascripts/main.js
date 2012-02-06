$(function()
{
	window.scrollTo(0, 0);

	$('.nav li').click(function(){
		$('.nav li').removeClass("active");
		$(this).addClass('active');
	});
});
