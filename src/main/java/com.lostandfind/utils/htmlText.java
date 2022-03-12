package com.lostandfind.utils;

public class htmlText {
	//  返回页面Html携带的6位随机码
	public static String html(String code) {

		String html = "<h3>【找易找校园失物招领平台】<h3><br/>"+
				"<p>您的验证码为：<p><br/>"+
				"<h3 style='color:red;'>" + code + "</h3><br/>";
		return html;
	}

	public static String connectHtml(){
		String html = "<h3>【找易找校园失物招领平台】<h3><br/>"+
				"<p>您有未读消息，快去看看吧！<p><br/>";
		return html;
	}
}
