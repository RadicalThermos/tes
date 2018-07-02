//@ sourceURL=role.js
$(function() {
	findRoles(1);//第一次要查询第一页数据
	//给搜索添加click事件
	$("#rolePanel .row button").click(function() {
		findRoles(1);
	});
	//给新增角色框里的确认按钮添加提交事件
	$("#addRole_submit").click(function () {
		return addRole();
	});
});
function addRole() {
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
			alert("请求失败");
		}
	});
	return false;
}
function findRoles(currentPage) {
	//处理模糊关键字
	var roleKeyword = $("#rolePanel .row input[type=text]").val();
	if(roleKeyword==null||roleKeyword==''){
		roleKeyword = "undefined";
	}
	
	$.ajax({
		url:basePath+"role/findRolesByPage",
		type:"get",
		data:{"currentPage":currentPage,"roleKeyword":roleKeyword},
		dataType:"json",
		success:function(result){
			if(result.status==1){
				//查询成功,页面局部刷新
				//清空表格,清空分页组件
				$("#role_table tbody").html("");
				$("#role_page").html("");
				//给表格添加数据dom
				var page = result.data;
				var roles = page.data;
				$(roles).each(function(n,role) {
					//n代表循环到第几个对象,是一个数字,从0开始
					//value是一个对象,正在循环的那个对象
					if(role.name!='超级管理员'&&role.name!='学员'&&role.name!='讲师'){
						//需要加上修改和删除的超链接
						//字符串的拼接dom编程(即利用文档对象的增删改查)
						var tr = '<tr>'+
				              		'<td>'+(n+1)+'</td>'+
				              		'<td>'+role.id+'</td>'+
				              		'<td>'+role.name+'</td>'+
				              		'<td>'+
				              			'<a href="" data-toggle="modal" data-target="#editRole" ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>编辑</a>'+
				                		'<a href="" data-toggle="modal" data-target=".bs-example-modal-sm"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>'+
				                	'</td>'+
				              	'</tr>';
						$("#role_table tbody").append(tr);
					}else{
						//不需要加上修改和删除的超链接
						var tr = '<tr><td>'+(n+1)+'</td><td>'+role.id+'</td><td>'+role.name+'</td><td></td></tr>';
						$("#role_table tbody").append(tr);
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
			alert();
		}
	});
}




























