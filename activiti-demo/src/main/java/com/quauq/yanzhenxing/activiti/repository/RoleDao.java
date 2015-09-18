package com.quauq.yanzhenxing.activiti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.quauq.yanzhenxing.activiti.entity.Role;

/**
 * 角色dao
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
public interface RoleDao extends CrudRepository<Role, String> {

	/**
	 * 根据用户id获取用户所拥有的角色列表
	 * @param loginName
	 * @return
	 */
	@Query(value="select r.* from role r,user_role ur where r.id=ur.role_id and ur.user_id = :loginName and r.del_flag=0",nativeQuery=true)
	List<Role> findByUserLoginName(@Param("loginName")String loginName);
}
