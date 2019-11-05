$(function() {

	var con_w = $(".mark-main").width()
		, list_w = con_w / 5
		, mar_l = list_w / 2;
	$(".system_list").css("width", list_w);
	/* $(".click_con").css("margin-left", -mar_l); */
	var stye_list = $(".mark-main").width() + 10;
	var system = ($(".system_list").length) * stye_list;
	$(".click_con").css("width", system + 'px');
	$(".hover_R").click(function() {
		var index = $(".new_cur").index() + 1;
		var system1 = $(".system_list").length;
		var wid1 = $(".system_list").width() + 10;
		//960
		var uwid = $(".mark-main").scrollLeft();
		var val_ = uwid + wid1;
		$(".new_cur").next().addClass("new_cur").siblings().removeClass("new_cur");
		$(".mark-main").animate({
			scrollLeft: wid1
		}, function() {
			$(".click_con .system_list:first").insertAfter($(".click_con .system_list:last"));
			$(".mark-main").scrollLeft(0);
		});
	});
	$(".hover_L").click(function() {
		var index = $(".new_cur").index();
		var wid1 = $(".system_list").width() + 10;
		var uwid = $(".mark-main").scrollLeft();
		var val_ = uwid - wid1;
		$(".new_cur").prev().addClass("new_cur").siblings().removeClass("new_cur");
		$(".mark-main").scrollLeft(wid1);
		$(".click_con .system_list:last").insertBefore($(".click_con .system_list:first"));
		$(".mark-main").animate({
			scrollLeft: 0
		});
	})

});