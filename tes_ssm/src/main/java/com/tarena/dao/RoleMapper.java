package com.tarena.dao;

import java.util.List;

import com.tarena.entity.Role;
import com.tarena.vo.Page;

public interface RoleMapper {

	/**
	 * 分页查询带有模糊条件的角色信息
	 * @param roleKeyword
	 * @return
	 */
	public int getTotalCount(String roleKeyword);

	public List<Role> getRolesByPage(Page page);

	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	public int addRole(Role role);

}
