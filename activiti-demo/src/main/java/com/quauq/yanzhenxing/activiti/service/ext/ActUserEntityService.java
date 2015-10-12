/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.quauq.yanzhenxing.activiti.service.ext;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.quauq.yanzhenxing.activiti.entity.Role;
import com.quauq.yanzhenxing.activiti.service.RoleService;
import com.quauq.yanzhenxing.activiti.service.UserService;

/**
 * 自定义user 服务
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Service
public class ActUserEntityService extends UserEntityManager {

	@Autowired
	private UserService userService;//系统的用户服务
	
	@Autowired
	private RoleService roleService;//系统的角色服务
	
	public User createNewUser(String userId) {
		return new UserEntity(userId);
	}

	public void insertUser(User user) {
//		getDbSqlSession().insert((PersistentObject) user);
		throw new RuntimeException("not implement method.");
	}

	public void updateUser(UserEntity updatedUser) {
//		CommandContext commandContext = Context.getCommandContext();
//		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
//		dbSqlSession.update(updatedUser);
		throw new RuntimeException("not implement method.");
	}

	public UserEntity findUserById(String userId) {
//		return (UserEntity) getDbSqlSession().selectOne("selectUserById", userId);
		return toActivitiUser(userService.obtainUser(userId));
	}

	private UserEntity toActivitiUser(com.quauq.yanzhenxing.activiti.entity.User user){
		if (user == null){
			return null;
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getLoginName());
		userEntity.setFirstName(user.getName());
		userEntity.setLastName(StringUtils.EMPTY);
		userEntity.setPassword(user.getPassword());
		userEntity.setEmail(user.getEmail());
		userEntity.setRevision(1);
		return userEntity;
	}
	
	public void deleteUser(String userId) {
//		UserEntity user = findUserById(userId);
//		if (user != null) {
//			List<IdentityInfoEntity> identityInfos = getDbSqlSession().selectList("selectIdentityInfoByUserId", userId);
//			for (IdentityInfoEntity identityInfo : identityInfos) {
//				getIdentityInfoManager().deleteIdentityInfo(identityInfo);
//			}
//			getDbSqlSession().delete("deleteMembershipsByUserId", userId);
//			user.delete();
//		}
		throw new RuntimeException("not implement method.");
	}

	public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
//		return getDbSqlSession().selectList("selectUserByQueryCriteria", query, page);
		throw new RuntimeException("not implement method.");
	}

	public long findUserCountByQueryCriteria(UserQueryImpl query) {
//		return (Long) getDbSqlSession().selectOne("selectUserCountByQueryCriteria", query);
		throw new RuntimeException("not implement method.");
	}

	public List<Group> findGroupsByUser(String userId) {
//		return getDbSqlSession().selectList("selectGroupsByUserId", userId);
		List<Group> list = Lists.newArrayList();
		com.quauq.yanzhenxing.activiti.entity.User user = userService.obtainUser(userId);
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
	public UserQuery createNewUserQuery() {
//		return new UserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
		throw new RuntimeException("not implement method.");
	}

	public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
//		Map<String, String> parameters = new HashMap<String, String>();
//		parameters.put("userId", userId);
//		parameters.put("key", key);
//		return (IdentityInfoEntity) getDbSqlSession().selectOne("selectIdentityInfoByUserIdAndKey", parameters);
		throw new RuntimeException("not implement method.");
	}

	public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
//		Map<String, String> parameters = new HashMap<String, String>();
//		parameters.put("userId", userId);
//		parameters.put("type", type);
//		return (List) getDbSqlSession().getSqlSession().selectList("selectIdentityInfoKeysByUserIdAndType", parameters);
		throw new RuntimeException("not implement method.");
	}

	public Boolean checkPassword(String userId, String password) {
//		User user = findUserById(userId);
//		if ((user != null) && (password != null) && (password.equals(user.getPassword()))) {
//			return true;
//		}
//		return false;
		throw new RuntimeException("not implement method.");
	}

	public List<User> findPotentialStarterUsers(String proceDefId) {
//		Map<String, String> parameters = new HashMap<String, String>();
//		parameters.put("procDefId", proceDefId);
//		return (List<User>) getDbSqlSession().selectOne("selectUserByQueryCriteria", parameters);
		throw new RuntimeException("not implement method.");

	}

	public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
//		return getDbSqlSession().selectListWithRawParameter("selectUserByNativeQuery", parameterMap, firstResult, maxResults);
		throw new RuntimeException("not implement method.");
	}

	public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
//		return (Long) getDbSqlSession().selectOne("selectUserCountByNativeQuery", parameterMap);
		throw new RuntimeException("not implement method.");
	}
	
}
