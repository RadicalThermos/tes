package com.tarena.dao;

import java.util.List;

import com.tarena.entity.User;
import com.tarena.vo.Page;

public interface UserMapper {

	public String login(User user);

	public int getCount(Page page);

	public List<User> findUsersByPage(Page page);

}
