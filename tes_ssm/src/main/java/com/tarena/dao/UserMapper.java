package com.tarena.dao;

import java.util.List;

import com.tarena.entity.User;
import com.tarena.entity.UserRole;
import com.tarena.vo.Page;

public interface UserMapper {

	public String login(User user);

	public int getCount(Page page);

	public List<User> findUsersByPage(Page page);

	//添加用户t_user
	public void addUser(User user);

	//添加用户角色中间表t_user_role
	public void addUserRole(UserRole userRole);

}
