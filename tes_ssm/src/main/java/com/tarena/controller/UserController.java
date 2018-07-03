package com.tarena.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.service.UserService;
import com.tarena.vo.Page;
import com.tarena.vo.Result;

@Controller
@RequestMapping("user/")
public class UserController {

	@Autowired
	private UserService userService;
	@RequestMapping(value="login/{loginName}/{password}",method = RequestMethod.GET)
	@ResponseBody
	public Result login(
			@PathVariable("loginName") String loginName ,
			@PathVariable("password")String loginPassword,
			HttpSession session){
		System.out.println(loginName+""+loginPassword);
		Result result = null;
		result = this.userService.login(loginName,loginPassword);
		if(result.getStatus()==1){
			session .setAttribute("loginName", loginName);
		}
		System.out.println(result.getData());
		System.out.println("状态码"+result.getStatus());
		System.out.println(result.getMessage());
		return result;
	}
	@RequestMapping(value="findUsersByPage",method = RequestMethod.GET)
	@ResponseBody
	public Result findUsersByPage(Page page){
		System.out.println(page.getCurrentPage()+":"+page.getUserKeyword()+":"+page.getRoleType());
		Result result = new Result();
		result = this.userService.findUsersByPage(page);
		//result.setStatus(1);
		return result;
	}
}














