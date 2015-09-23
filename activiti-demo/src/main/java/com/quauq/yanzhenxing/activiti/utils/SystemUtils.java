package com.quauq.yanzhenxing.activiti.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.quauq.yanzhenxing.activiti.entity.User;
import com.quauq.yanzhenxing.activiti.service.UserService;

/**
 * 系统工具
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
public class SystemUtils {

	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static User obtainLoginUser(){
		Subject subject=SecurityUtils.getSubject();
		if (subject!=null&&subject.isAuthenticated()) {
			String loginName=(String)subject.getPrincipal();
			UserService userService=SpringContextHolder.getBean("userService");
			User user=userService.obtainUser(loginName);
			return user;
		}
		return null;
	}
	
	/**
	 * 获取当前登录用户名
	 * @return
	 */
	public static String obtainLoginName(){
		User user=obtainLoginUser();
		if (user!=null) {
			return user.getLoginName();
		}
		return null;
	}
}
