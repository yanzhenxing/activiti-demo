package com.quauq.yanzhenxing.activiti.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.quauq.yanzhenxing.activiti.entity.User;
import com.quauq.yanzhenxing.activiti.entity.UserRole;
import com.quauq.yanzhenxing.activiti.repository.UserDao;
import com.quauq.yanzhenxing.activiti.repository.UserRoleDao;

/**
 * 用户服务类
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Transactional
	public void add(){
		User user=new User();
		user.setLoginName("yanzhenxing");
		user.setName("闫振兴");
		userDao.save(user);
	}
	
	/**
	 * 根据用户登录名查找用户
	 * @param loginName
	 * @return
	 */
	public User obtainUser(String loginName){
		Assert.hasText(loginName, "loginName should not be empty!");
		return userDao.findByLoginNameAndDelFlag(loginName, false);
	}
	
	/**
	 * 为用户添加角色
	 * @param loginName
	 * @param roleId
	 */
	@Transactional
	public void addRole(String loginName,String roleId){
		Assert.hasText(loginName, "loginName should not be empty!");
		Assert.hasText(roleId, "roleId should not be empty!");
		UserRole userRole=new UserRole();
		userRole.setId(UUID.randomUUID().toString());
		userRole.setRoleId(roleId);
		userRole.setUserId(loginName);
		userRoleDao.save(userRole);
	}
}
