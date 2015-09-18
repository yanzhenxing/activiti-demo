package com.quauq.yanzhenxing.activiti.repository;

import org.springframework.data.repository.CrudRepository;

import com.quauq.yanzhenxing.activiti.entity.UserRole;

/**
 * 用户角色关系Dao
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
public interface UserRoleDao extends CrudRepository<UserRole, String> {

}
