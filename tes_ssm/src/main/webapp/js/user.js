//@ sourceURL=user.js
var userID;
var roleType = "all";//设置默认初始值
$(function(){
	//alert("load user");
	findUsers(1);//第一次加载,获取所有数据的第一页
	$("#role_type button").click(function () {
		roleType = $(this).text();
		if(roleType=="全部"){
			roleType = "all";
		}
		//alert(roleType);
		findUsers(1);
	});
	$("#user_add").click(function (e) {
		//alert("aaaaaa");
		e.preventDefault();
		findAllRoles();
	});
	$("#addUserPanel form").submit(function () {
		return addUser();
	});
});
//添加用户
function addUser() {
	//获取要添加的新数据
	var loginName = $("#inputEmail").val();
	var password = $("#inputPassword").val();
	var password2 = $("#inputPassword2").val();
	var nickName = $("#nickName").val();
	var age = $("#age").val();
	var roleID = $("#roleCategory").val();
	var sex = $("#addUserPanel form input[name=user-type]").val();
	//alert(loginName+" "+password+" "+password2+" "+nickName+" "+age+" "+" "+roleID+" "+sex);
	if(password!=password2){
		return false;
	}
	if(age<=0){
		return false;
	}
	//异步请求
	$.ajaxFileUpload({
		url:basePath+"user/newUser",
		secureuri:false,
		fileElementId:"addHeadPicture",//文件域的ID
		type:"post",
		data:{
			"loginName":loginName,
			"password":password,
			"nickName":nickName,
			"age":age,
			"sex":sex,
			"roleID":roleID
			},
		dataType:"text",
		success:function(data,status){
			//alert(data);
			data=data.replace(/<PRE.*?>/g,'');
			data=data.replace("<PRE>",'');
			data=data.replace("</PRE>",'');
			data=data.replace(/<pre.*?>/g,'');
			data=data.replace("<pre>",'');
			data=data.replace("</pre>",'');
			alert(data);
		},
		error:function(){
			alert("请求失败!");
		}
	});
	return false;
}
function findAllRoles() {
	$.ajax({
		url:basePath+"role/findAllRoles",
		type:"get",
		dataType:"json",
		success:function(result){
			if(result.status==1){
				//alert(result.status+"  "+result.message);
				if(result.status==1){
					$("#roleCategory").html("");
					$("#roleCategory").append('<option></option>');
					var roles = result.data;
					$(roles).each(function (n,role) {
						//alert(role.name);
						//alert(role.id);
						var roleOption='<option value = "'+role.id+'">'+role.name+'</option>';
						$("#roleCategory").append(roleOption);
					});
				}else if(result.status){
					alert(result.message);
				}
			}
		},
		error:function(){
			alert("请求失败!");
		}
		
	});
}

function findUsers(currentPage) {
	//alert("findUsers");
	//处理模糊关键字
	var userKeyword = $("#user_search").val();
	if(userKeyword==null||userKeyword==''){
		userKeyword = "undefined";
	}
	
	$.ajax({
		url:basePath+"user/findUsersByPage",
		type:"get",
		data:{"currentPage":currentPage,"userKeyword":userKeyword,"roleType":roleType},
		dataType:"json",
		success:function(result){
			//alert("ajax");
			//alert(result.status);
			if(result.status==1){
				$("#user_tbody").html("");
				$("#user_page").html("");
				//处理数据
				var page = result.data;
			 	var users = page.data;
			
			 	$(users).each(function(index,user) {
			 		var roles = user.roles;
			 		var rolename ='';
			 		$(roles).each(function(n,role) {
						rolename += role.name+",";
					});
			 		if(rolename==''){
			 			rolename = '无角色';
			 		}else{
			 			rolename = rolename.substring(0,rolename.length-1);
			 		}
			 		
					var tr = '<tr>'+
		                '<td>'+(index+1)+'</td>'+
		                '<td>'+user.loginName+'</td>'+
		                '<td>'+user.nickName+'</td>'+
		                '<td>'+user.loginType+'</td>'+
		                '<td>'+user.score+'</td>'+
		                '<td>'+new Date(user.regDate).toLocaleDateString().replace("/","-").replace("/","-")+'</td>'+
		                '<td>'+user.isLock+'</td>'+
		                '<td>'+rolename+'</td>'+
		                '<td>'+
		                  '<a href="" data-toggle="modal" data-target="#editUser"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>编辑</a>'+
		                  '<a href="" data-toggle="modal" data-target=".bs-example-modal-sm"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>'+
		                '</td>'+
		              '</tr>';
					$('#user_tbody').append(tr);
				});
			 	

				//处理分页
				if(page.totalPage>1){
					//创建分页条组件
					//处理前一页
					var previous = '<li>'+
			            			'<a href="javascript:findUsers('+page.previousPage+')" aria-label="Previous">'+
			            			'<span aria-hidden="true">&laquo;</span>'+
			            			'</a>'+
			            			'</li>';
					$("#user_page").append(previous);
					//处理中间的超链接
					$(page.aNum).each(function(n,value) {
						var middle = '<li><a href="javascript:findUsers('+value+')">'+value+'</a></li>'; 
						$("#user_page").append(middle);
					});
					//处理后一页
					var next = '<li>'+
			            		'<a href="javascript:findUsers('+page.nextPage+')" aria-label="Next">'+
			            		'<span aria-hidden="true">&raquo;</span>'+
			            		'</a>'+
			            		'</li>';
					$("#user_page").append(next);
				}
			}
		},
		error:function(){
			alert("请求失败");
		}
	});
}






















