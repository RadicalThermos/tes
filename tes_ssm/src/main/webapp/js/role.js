//@ sourceURL=role.js
var roleID;
$(function() {
	findRoles(1);//第一次要查询第一页数据
	//给搜索添加click事件
	$("#role_search_button").click(function() {
		//alert("fff");
		findRoles(1);
	});
	//给新增角色框里的确认按钮添加提交事件
	$("#addRole_confirm").click(function () {
		addRole();
	});
	$("#update_confirm").click(function () {
		//alert("aaa");
		updateRole();
	});
	$("#delete_confirm").click(function () {
		//alert("aaa");
		deleteRole();
	});
});
function deleteRole() {
	//alert("delete");
	$.ajax({
		url:basePath+"role/deleteRole/"+roleID,
		type:"delete",
		dataType:"json",
		success:function(result){
			//alert(result.status);
			if(result.status==1){
				//删行
				$("#role_"+roleID).remove();
				//关框
				$(".bs-example-modal-sm").modal("hide");
			}
		},
		error:function(){
			alert("请求失败!");
			//关框
			$(".bs-example-model-sm").modal("hide");
		}
		
	});
}
function daleteClick(RID) {
	roleID = RID;
}
function updateRole() {
	var newRoleName = $("#role_name").val();
	//发送ajax异步请求
	$.ajax({
		url:basePath+"role/updateRole",
		type:"post",
		data:{"id":roleID,"name":newRoleName},
		dataType:"json",
		success:function(result){
			if(result.status==1){
				//更改页面值
				$("#role_"+roleID).find("td:eq(2)").text(newRoleName);
				//关闭模态框
				$("#editRole").modal("toggle");
			}
		},
		error:function(){
			alert("请求失败!");
		}
	});
}
	
function updateClick(RID) {
	//alert(RID);
	roleID = RID;
	var Rname = $("#role_"+roleID).find("td:eq(2)").text();
	$("#role_name").val(Rname);
}
function addRole() {
	//alert("aaa");
	//获取新角色数据
	var roleName = $("#roleName").val();
	//发送ajax异步请求
	$.ajax({
		url:basePath+"role/addRole/"+roleName,
		type:"post",
		dataType:"json",
		success:function(result){
			if(result.status==1){
				alert(result.message);		
			}
		},
		error:function(){
			alert("请求失败qq");
		}
	});
}
function findRoles(currentPage) {

	//alert("aaa");
	//处理模糊关键字
	var roleKeyword = $("#role_search").val();
	if(roleKeyword==null||roleKeyword==''){
		roleKeyword = "undefined";
	}
	
	$.ajax({
		url:basePath+"role/findRolesByPage",
		type:"get",
		data:{"currentPage":currentPage,"roleKeyword":roleKeyword},
		dataType:"json",
		success:function(result){
			//alert("bbb");
			//alert(result.status);
			if(result.status==1){
				//查询成功,页面局部刷新
				//清空表格,清空分页组件
				$("#role_tbody").html("");
				$("#role_page").html("");
				//给表格添加数据dom
				var page = result.data;
				var roles = page.data;
				$(roles).each(function(n,role) {
					//n代表循环到第几个对象,是一个数字,从0开始
					//value是一个对象,正在循环的那个对象
					if(role.name!='超级管理员'&& role.name!='学员'&&role.name!='讲师'){
						//需要加上修改和删除的超链接
						//字符串的拼接dom编程(即利用文档对象的增删改查)
						var tr = '<tr id="role_'+role.id+'">'+
				              		'<td>'+(n+1)+'</td>'+
				              		'<td>'+role.id+'</td>'+
				              		'<td>'+role.name+'</td>'+
				              		'<td>'+
				              			'<a href="" onclick="updateClick(\''+role.id+'\')" data-toggle="modal" data-target="#editRole" ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>编辑</a>'+
				                		'<a href="" onclick="deleteClick(\''+role.id+'\')" data-toggle="modal" data-target=".bs-example-modal-sm"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>'+
				                	'</td>'+
				              	'</tr>';
						$("#role_tbody").append(tr);
					}else{
						//不需要加上修改和删除的超链接
						var tr = '<tr id = "role_'+role.id+'"><td>'+(n+1)+'</td><td>'+role.id+'</td><td>'+role.name+'</td><td></td></tr>';
						$("#role_tbody").append(tr);
					}
				});
				//分页
				if(page.totalPage>1){
					//处理前一页
					var previous ='<li>'+
				          			'<a href="javascript:findRoles('+page.previousPage+')" aria-label="Previous">'+
				          			'<span aria-hidden="true">&laquo;</span>'+
				          			'</a>'+
				          		  '</li>';
					$("#role_page").append(previous);
					//处理中间的超链接
					$(page.aNum).each(function (n,num) {
						var middle = '<li><a href="javascript:findRoles('+num+')">'+num+'</a></li>';
						$("#role_page").append(middle);
					});
					//处理后一页
					var next = '<li>'+
				          		'<a href="javascript:findRoles('+page.nextPage+')" aria-label="Next">'+
				          		'<span aria-hidden="true">&raquo;</span>'+
				          		'</a>'+
				          		'</li>';
					$("#role_page").append(next);
				}
			}else if(result.status==0){
				alert("没有查询到数据");
			}
		},
		error:function(){
			alert("请求失败");
		}
	});

}




























