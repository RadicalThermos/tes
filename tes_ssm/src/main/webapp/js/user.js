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
});
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
			 			rolename.substring(0,rolename.length-1);
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






















