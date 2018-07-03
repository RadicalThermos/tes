package com.tarena.service;

import com.tarena.entity.Role;
import com.tarena.vo.Page;
import com.tarena.vo.Result;

public interface RoleService {

	/**
	 * 分页查询的业务方法
	 * @param page
	 * @return
	 */
	public Result findRolesByPage(Page page);

	/**
	 * 添加角色
	 * @param roleName
	 * @return
	 */
	public Result addRole(String roleName);

	/**
	 * 更新角色
	 * @param role
	 * @return
	 */
	public Result updateRole(Role role);

	/**
	 * 删除角色
	 * @param roleID
	 * @return
	 */
	public Result deleteRole(String roleID);
	
}
