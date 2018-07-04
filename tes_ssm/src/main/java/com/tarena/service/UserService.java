package com.tarena.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.tarena.entity.User;
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

	/**
	 * 添加用户
	 * @param user
	 * @param roleID
	 * @param addHeadPicture
	 * @param request
	 * @param response
	 */
	public void addUser(User user, String roleID, MultipartFile addHeadPicture, HttpServletRequest request,
			HttpServletResponse response);
	
	
}
