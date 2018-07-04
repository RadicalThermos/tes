package com.tarena.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tarena.dao.UserMapper;
import com.tarena.entity.User;
import com.tarena.service.UserService;
import com.tarena.util.PageUtil;
import com.tarena.vo.Page;
import com.tarena.vo.Result;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name = "userMapper")
	private UserMapper userMapper;
	@Resource(name="pageUtil")
	private PageUtil pageUtil;
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

	@Override
	public Result findUsersByPage(Page page) {
		Result result = new Result();
		//处理角色类型
		String roleType = page.getRoleType();//all,讲师,学员,管理员
		page.setRoleType("all".equals(roleType)?"%%":"%"+roleType+"%");
		page.setPageSize(pageUtil.getPageSize());
		page.setUserKeyword("undefined".equals(page.getUserKeyword())?
				"%%":"%"+page.getUserKeyword()+"%");
		//****************获取总记录数
		int totalCount = this.userMapper.getCount(page);
		page.setTotalCount(totalCount);
		//计算总页数
		int totalPage = (totalCount%page.getPageSize()==0)?
				(totalCount/page.getPageSize()):(totalCount/page.getPageSize()+1);
		page.setTotalPage(totalPage);
		//计算上一页
		page.setPreviousPage((page.getCurrentPage()==1)?page.getCurrentPage():page.getCurrentPage()-1); 
		//计算后一页
		page.setNextPage((page.getCurrentPage()==totalPage)?page.getCurrentPage():page.getCurrentPage()+1); 
		//****************获取当前页数据
		List<User> users = this.userMapper.findUsersByPage(page);
		page.setData(users);
		//计算分页条组件多少超链接
		page.setaNum(pageUtil.getFenYe_a_Num(page.getCurrentPage(), page.getPageSize(), totalCount, totalPage));
		for (User user : users) {
			System.out.println(user);
		}
		result.setStatus(1);
		result.setData(page);
		return result;
	}

	@Override
	public void addUser(User user, String roleID, MultipartFile addHeadPicture, HttpServletRequest request,
			HttpServletResponse response) {
		//判断文件是否存在,不存在要提示客户端
		
		//获取文件所有的信息
		
		//文件类型是否允许
		
		//文件大小是否允许
		
		//开始上传
		
		//构建User对象
		
		//添加user对象进数据库
		
		//添加用户和角色的关联表
		
		//提示用户添加成功
	}
}





































