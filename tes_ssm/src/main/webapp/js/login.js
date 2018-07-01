//@ sourcrURL = login.js
$(function () {
	console.log("login.js");
	//每次加载html时都要从cookie中取出用户名毛病赋值给用户名框
	$("#inputName").val(getCookie("loginName"));
	$(".container form").submit(function () {
		return login();
	});
});
function login() {
	alert("login()");
	//获取页面数据
	var loginName= $("#inputName").val();
	var password = $("#inputPassword").val();
	var remember = $(".container form input[type=checkbox]").get(0).checked;
	alert(loginName+"  "+password + "  " +remember);
	//根据页面数据异步请求
	$.ajax({
		url:basePath+"user/login/"+loginName+"/"+password,
		type:"get",
		dataType:"json",
		success:function(result){
			if(result.status==1){
				if(remember){
					//记住用户名 loginName
					addCookie("loginName",loginName,5);
				}
				//登录成功 location对象是浏览器的地址栏对象
				window.location.href="index.html";
			}else if(result.status==0){
				//登录失败
				alert(result.message);
			}
		},
		error:function(){
			alert("请求失败");
		}
	});
	return false;
}