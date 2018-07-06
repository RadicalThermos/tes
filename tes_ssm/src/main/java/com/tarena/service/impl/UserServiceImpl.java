package com.tarena.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tarena.dao.UserMapper;
import com.tarena.entity.User;
import com.tarena.entity.UserRole;
import com.tarena.service.UserService;
import com.tarena.util.CommonValue;
import com.tarena.util.ExcelUtil;
import com.tarena.util.PageUtil;
import com.tarena.util.PrintWriterUtil;
import com.tarena.util.UUIDUtil;
import com.tarena.util.UploadUtil;
import com.tarena.vo.Page;
import com.tarena.vo.Result;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name = "userMapper")
	private UserMapper userMapper;
	@Resource(name = "pageUtil")
	private PageUtil pageUtil;

	@Override
	public Result login(String loginName, String loginPassword) {
		Result rs = new Result();
		User user = new User();
		user.setLoginName(loginName);
		user.setPassword(loginPassword);
		String userID = userMapper.login(user);
		if (userID != null) {
			rs.setStatus(1);
			rs.setMessage("登录成功");
		} else {
			rs.setStatus(0);
			rs.setMessage("登录失败");
		}
		return rs;
	}

	@Override
	public Result findUsersByPage(Page page) {
		Result result = new Result();
		// 处理角色类型
		String roleType = page.getRoleType();// all,讲师,学员,管理员
		page.setRoleType("all".equals(roleType) ? "%%" : "%" + roleType + "%");
		page.setPageSize(pageUtil.getPageSize());
		page.setUserKeyword("undefined".equals(page.getUserKeyword()) ? "%%" : "%" + page.getUserKeyword() + "%");
		// ****************获取总记录数
		int totalCount = this.userMapper.getCount(page);
		page.setTotalCount(totalCount);
		// 计算总页数
		int totalPage = (totalCount % page.getPageSize() == 0) ? (totalCount / page.getPageSize())
				: (totalCount / page.getPageSize() + 1);
		page.setTotalPage(totalPage);
		// 计算上一页
		page.setPreviousPage((page.getCurrentPage() == 1) ? page.getCurrentPage() : page.getCurrentPage() - 1);
		// 计算后一页
		page.setNextPage((page.getCurrentPage() == totalPage) ? page.getCurrentPage() : page.getCurrentPage() + 1);
		// ****************获取当前页数据
		List<User> users = this.userMapper.findUsersByPage(page);
		page.setData(users);
		// 计算分页条组件多少超链接
		page.setaNum(pageUtil.getFenYe_a_Num(page.getCurrentPage(), page.getPageSize(), totalCount, totalPage));
		for (User user : users) {
			System.out.println(user);
		}
		result.setStatus(1);
		result.setData(page);
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void addUser(User user, String roleID, MultipartFile addHeadPicture, HttpServletRequest request,
			HttpServletResponse response) {
		// **********************文件上传
		// 获取上传文件的服务器路径
		String realPath = request.getServletContext().getRealPath("/head");
		File realPathFile = new File(realPath);
		if (!realPathFile.exists())
			realPathFile.mkdir();
		// 创建用户UUID
		String uuid = UUIDUtil.getUUID();
		String imageFileName = null;
		// 判断文件是否存在,不存在要提示客户端
		if (addHeadPicture == null || addHeadPicture.isEmpty()) {
			// PrintWriterUtil.printMessageToClient(response,
			// "请选择文件!");//业务也可以随时向客户端发数据,只要有response
			user.setHead("default.png");
			return;
		} else {
			// 文件存在
			// 获取文件相关信息
			// 原始文件名
			String originalFilename = addHeadPicture.getOriginalFilename();
			System.out.println("originalFilename:" + originalFilename);
			String name = addHeadPicture.getName();
			System.out.println("name" + name);
			// 内容类型
			String contentType = addHeadPicture.getContentType();
			System.out.println("contentType:" + contentType);
			long size = addHeadPicture.getSize();
			// 字节数
			System.out.println("size:" + size);
			// File serverPath = new File(realPath,originalFilename);

			// 文件上传(无法控制大小,淘汰)
			// addHeadPicture.transferTo(serverPath);
			// 自己写一个文件上传的工具(能上传,能改变大小(缩放),加水印)
			if (!CommonValue.contentTypes.contains(contentType)) {
				PrintWriterUtil.printMessageToClient(response, "文件类型不匹配");
				return;
			}
			if (size > 4194304) {
				PrintWriterUtil.printMessageToClient(response, "文件应该小于4M!");
				return;
			}
			boolean flag = UploadUtil.uploadImage(addHeadPicture, uuid, true, 640, realPath);
			if (!flag) {
				PrintWriterUtil.printMessageToClient(response, "文件上传失败!请稍后重试");
				return;
			}
			// 说明图片上传成功
			// 得到图片路径
			String originalExtendName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			imageFileName = uuid + "." + originalExtendName;
			user.setHead(imageFileName);

			// 开始文件上传
		}

		// ***************************将user对象,role对象,userRole对象存入数据库
		try {
			// 能执行到此说明图片完毕
			// user中包含前端提交的数据和刚刚处理的数据
			user.setId(uuid);
			// 用户的数据进数据表
			this.userMapper.addUser(user);
			UserRole userRole = new UserRole();
			userRole.setUserId(uuid);
			userRole.setRoleId(roleID);
			this.userMapper.addUserRole(userRole);
			// 提示用户添加成功
			PrintWriterUtil.printMessageToClient(response, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 数据库操作失败,图片上传成功
			File file = new File(realPath + File.separator + imageFileName);
			if (file.exists()) {
				file.delete();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] export_User() {
		byte[]data=null;
		//获取数据
		List<User> users = this.userMapper.findAllUsers();
		//用自定义工具把users转换成字节数组,以备下载
		if(users!=null&&users.size()>0){
			data = ExcelUtil.write2Excel(users);
			//把字节数组下载到客户端
		}
		return data;
	}

	@Override
	public Result login_shiro(String loginName, String loginPassword) {
		//loginName loginPassword是用户数据,准备把这两个数据交给安全管理中心
		//把这两个数据封装给shiro中的某个对象中
		Result result=new Result();
		//Shiro的登陆操作   获取用户对象
		Subject subject = SecurityUtils.getSubject();
		
		//将用户的数据封装为令牌(票)
		UsernamePasswordToken token = new UsernamePasswordToken(loginName,loginPassword);
		
		try {
			//通过用户实现登陆 
			subject.login(token); 
			
			//获取真实的用户对象
			User u = (User) subject.getPrincipal();
			
			//session.setAttribute("sessionUser", user);
			subject.getSession().setAttribute("sessionUser", u);
			//证明用户名和密码正确
			result.setStatus(1);
			result.setMessage("登录成功!");
			
		} catch (AuthenticationException e) {
			e.printStackTrace();  //打印异常信息
			//证明用户名和密码错误
			result.setStatus(0);
			result.setMessage("登录失败!");
		}
		return result;
	}
}























