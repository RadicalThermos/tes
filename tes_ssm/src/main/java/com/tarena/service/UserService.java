package com.tarena.service;

import com.tarena.vo.Page;
import com.tarena.vo.Result;

public interface UserService {

	public Result login(String loginName, String loginPassword);

	/**
	 * 用户的分页
	 * @param page 当前页 模糊关键字 角色类型
	 * @return 
	 */
	public Result findUsersByPage(Page page);
	
	
}
