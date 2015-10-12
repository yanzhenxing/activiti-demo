/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.quauq.yanzhenxing.activiti.service.ext;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.quauq.yanzhenxing.activiti.entity.Role;
import com.quauq.yanzhenxing.activiti.entity.User;
import com.quauq.yanzhenxing.activiti.service.RoleService;
import com.quauq.yanzhenxing.activiti.service.UserService;

/**
 * 自定义 Group服务 
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Service
public class ActGroupEntityService extends GroupEntityManager {

	@Autowired
	private UserService userService;//系统的用户服务
	
	@Autowired
	private RoleService roleService;//系统的角色服务

	public Group createNewGroup(String groupId) {
		return new GroupEntity(groupId);
	}

	public void insertGroup(Group group) {
//		getDbSqlSession().insert((PersistentObject) group);
		throw new RuntimeException("not implement method.");
	}

	public void updateGroup(GroupEntity updatedGroup) {
//		CommandContext commandContext = Context.getCommandContext();
//		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
//		dbSqlSession.update(updatedGroup);
		throw new RuntimeException("not implement method.");
	}

	public void deleteGroup(String groupId) {
//		GroupEntity group = getDbSqlSession().selectById(GroupEntity.class, groupId);
//		getDbSqlSession().delete("deleteMembershipsByGroupId", groupId);
//		getDbSqlSession().delete(group);
		throw new RuntimeException("not implement method.");
	}

	public GroupQuery createNewGroupQuery() {
//		return new GroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
		throw new RuntimeException("not implement method.");
	}

//	@SuppressWarnings("unchecked")
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
//		return getDbSqlSession().selectList("selectGroupByQueryCriteria", query, page);
		throw new RuntimeException("not implement method.");
	}

	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
//		return (Long) getDbSqlSession().selectOne("selectGroupCountByQueryCriteria", query);
		throw new RuntimeException("not implement method.");
	}

	public List<Group> findGroupsByUser(String userId) {
//		return getDbSqlSession().selectList("selectGroupsByUserId", userId);
		List<Group> list = Lists.newArrayList();
		User user = userService.obtainUser(userId);
		if (user != null){
			List<Role> roles=roleService.obtainRoles(userId);
			for (Role role : roles){
				list.add(toActivitiGroup(role));
			}
		}
		return list;
	}
	
	private  GroupEntity toActivitiGroup(Role role){
		if (role == null){
			return null;
		}
		GroupEntity groupEntity = new GroupEntity();
		groupEntity.setId(role.getId());
		groupEntity.setName(role.getName());
		groupEntity.setType(role.getType());
		groupEntity.setRevision(1);
		return groupEntity;
	}
	
	public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
//		return getDbSqlSession().selectListWithRawParameter("selectGroupByNativeQuery", parameterMap, firstResult, maxResults);
		throw new RuntimeException("not implement method.");
	}

	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
//		return (Long) getDbSqlSession().selectOne("selectGroupCountByNativeQuery", parameterMap);
		throw new RuntimeException("not implement method.");
	}

}