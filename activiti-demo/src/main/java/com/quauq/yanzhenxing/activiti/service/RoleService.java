package com.quauq.yanzhenxing.activiti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.quauq.yanzhenxing.activiti.entity.Role;
import com.quauq.yanzhenxing.activiti.repository.RoleDao;

/**
 * 角色服务类
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 保存一个角色
	 * @param role
	 */
	@Transactional(readOnly=false)
	public void save(Role role){
		Assert.notNull(role,"role should not be null!");
		Assert.hasText(role.getId(), "role.id should not be empty!");
		Assert.hasText(role.getName(), "role.name should not be empty!");
		Assert.hasText(role.getType(), "role.type should not be empty!");
		roleDao.save(role);
	}
	
	
	/**
	 * 根据用户登录名获取用户的角色列表
	 * @param userLoginName
	 * @return
	 */
	public List<Role> obtainRoles(String userLoginName){
		Assert.hasText(userLoginName, "userLoginName should not be empty!");
		return roleDao.findByUserLoginName(userLoginName);
	}
}
