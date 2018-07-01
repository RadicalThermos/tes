package com.tarena.service;

import com.tarena.vo.Result;

public interface UserService {

	Result login(String loginName, String loginPassword);
	
	
}
