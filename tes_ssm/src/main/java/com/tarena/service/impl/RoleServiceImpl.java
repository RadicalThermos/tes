package com.tarena.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tarena.dao.RoleMapper;
import com.tarena.entity.Role;
import com.tarena.service.RoleService;
import com.tarena.util.PageUtil;
import com.tarena.util.UUIDUtil;
import com.tarena.vo.Page;
import com.tarena.vo.Result;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource(name="roleMapper")
	private RoleMapper roleMapper;
	@Resource(name="pageUtil")
	private PageUtil pageUtil;

	@Override
	public Result findRolesByPage(Page page) {
		Result result = new Result();
		//处理模糊关键字
		page.setRoleKeyword(page.getRoleKeyword().equals("undefined")?"%%":"%"+page.getRoleKeyword()+"%");
		//设置每页数据个数
		page.setPageSize(this.pageUtil.getPageSize());
		//获取总记录数,
		int totalCount = this.roleMapper.getTotalCount(page.getRoleKeyword());
		page.setTotalCount(totalCount);
		//计算总页数,
		int totalPage = (totalCount%page.getPageSize())==0?
				totalCount/page.getPageSize():totalCount/page.getPageSize()+1;
		page.setTotalPage(totalPage);
		//前一页,
		if(page.getCurrentPage()==1){
			page.setPreviousPage(1);
		}else{
			page.setPreviousPage(page.getCurrentPage()-1);
		}
		//后一页,
		if(page.getCurrentPage()==totalPage){
			page.setNextPage(totalPage);
		}else{
			page.setNextPage(page.getCurrentPage()+1);
		}
		//当前页数据
		List<Role> roles = this.roleMapper.getRolesByPage(page);
		page.setData(roles);
		//获取分页组件中的超链接个数
		page.setaNum(pageUtil.getFenYe_a_Num(page.getCurrentPage(), 
				page.getPageSize(), totalCount, totalPage));
		//验证page是否正确赋值
		System.out.println(page);
		result.setStatus(1);
		result.setData(page);
		result.setMessage("数据查询到了");
		return result;
	}

	@Override
	public Result addRole(String roleName) {
		Result result = new Result();
		Role role = new Role();
		role.setId(UUIDUtil.getUUID());
		role.setName(roleName);
		this.roleMapper.addRole(role);
		result.setStatus(1);
		result.setMessage("添加角色成功!");
		result.setData(role);
		return result;
	}
}











