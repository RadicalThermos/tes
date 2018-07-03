package com.tarena.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.vo.Result;

@Controller
@RequestMapping("main/")
public class MainController {
	@RequestMapping(value="logout",method = RequestMethod.POST)
	@ResponseBody
	public Result logout(HttpSession session){
		System.out.println(123);
		Result result = new Result();
		session.invalidate();
		result.setStatus(1);
		return result;
	}

}
