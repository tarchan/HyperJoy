$(function()
{
	//window.scrollTo(0, 1);

	// タブを選択
	$('.nav li').click(function(){
		$('.nav li').removeClass("active");
		$(this).addClass('active');
	});

	// キーワードを選択
	$('form input[type=text]').focus(function(){
		$(this).select();
	});

	// 情報を見る
	$('#songlist > div').click(function(){
		alert('曲番号：'+$(this).attr('data-tid')+'\n曲　名：'+$(this).attr('data-title')+'\n歌　手：'+$(this).attr('data-artist'));
	});
});
