package com.quauq.yanzhenxing.activiti.repository;

import org.springframework.data.repository.CrudRepository;

import com.quauq.yanzhenxing.activiti.entity.User;

/**
 * userDao
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
public interface UserDao extends CrudRepository<User, String> {

	/**
	 * 通过登录名查找用户
	 * @param loginName
	 * @param delFlag
	 * @return
	 */
	User findByLoginNameAndDelFlag(String loginName,boolean delFlag);
	
	
}
