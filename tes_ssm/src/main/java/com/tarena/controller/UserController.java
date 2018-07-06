package com.tarena.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tarena.entity.User;
import com.tarena.service.UserService;
import com.tarena.util.ExcelUtil;
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
		//result = this.userService.login(loginName,loginPassword);
		result = this.userService.login_shiro(loginName,loginPassword);
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
	@RequestMapping(value="newUser",method = RequestMethod.POST)
	public void addUser(User user,String roleID,MultipartFile addHeadPicture,
			HttpServletRequest request,HttpServletResponse response){
		//System.out.println("add--->"+addHeadPicture);
		this.userService.addUser(user,roleID,addHeadPicture,request,response);
	}
	@RequestMapping(value="exportUserExcel",method = RequestMethod.GET)
	public void export_user(HttpServletRequest request,HttpServletResponse response){
		byte[] data = this.userService.export_User();
		System.out.println(data);
		if(data!=null){
			try {
				//把字节数据下载到客户端
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment;fileName=alluser.xls");
				response.setContentLength(data.length);
				OutputStream os=response.getOutputStream();
				os.write(data);
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}














