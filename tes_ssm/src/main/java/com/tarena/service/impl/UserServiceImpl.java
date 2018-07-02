package com.tarena.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tarena.dao.UserMapper;
import com.tarena.entity.User;
import com.tarena.service.UserService;
import com.tarena.vo.Result;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name = "userMapper")
	private UserMapper userMapper;

	@SuppressWarnings("unused")
	@Override
	public Result login(String loginName, String loginPassword) {
		Result rs = new Result();
		User user = new User();
		user.setLoginName(loginName);
		user.setPassword(loginPassword);
		String userID = userMapper.login(user);
		if(userID!=null){
			rs.setStatus(1);
			rs.setMessage("登录成功");
		}else {
			rs.setStatus(0);
			rs.setMessage("登录失败");
		}
		return rs;
	}
}
