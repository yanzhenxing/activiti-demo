package com.quauq.yanzhenxing.activiti;

import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.quauq.yanzhenxing.activiti.context.GlobalContext;
import com.quauq.yanzhenxing.activiti.entity.Role;
import com.quauq.yanzhenxing.activiti.entity.User;
import com.quauq.yanzhenxing.activiti.service.RoleService;
import com.quauq.yanzhenxing.activiti.service.UserService;
import com.quauq.yanzhenxing.activiti.service.WorkflowService;

/**
 * 系统服务类测试
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
public class ServiceTest extends BaseTest{

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Test
	public void testObtainUser(){
		User user=userService.obtainUser("admin");
		assertEquals("user wrong", "admin", user.getLoginName());
	}
	
	@Test
	public void testObtainProcessDefinitionList(){
		List<ProcessDefinition> processDefinitions=workflowService.obtainProcessDefinitions(null);
		for (ProcessDefinition processDefinition : processDefinitions) {
			System.out.println(processDefinition.getKey()+":"+processDefinition.getVersion());
		}
	}
	
	/**
	 * 测试启动流程
	 */
	@Test
	@Rollback(false)
	public void testStartProcess(){
		workflowService.startProcess("admin", "process", "test:111", null);
	}
	
	/**
	 * 测试删除流程实例
	 */
	@Test
	@Rollback(false)
	public void testDeleteProcIns(){
		workflowService.deleteProcIns("17501", "测试");
	}
	
	@Test
	@Rollback(false)
	public void testDeleteDeployment(){
		
	}
	
	@Test
	@Rollback(false)
	public void testInitialRoles(){
		Role admin=new Role();
		admin.setId(GlobalContext.ROLE_ADMIN);
		admin.setName("管理员");
		admin.setType("common");
		roleService.save(admin);
		
		Role user=new Role();
		user.setId(GlobalContext.ROLE_USER);
		user.setName("用户");
		user.setType("common");
		roleService.save(user);
		
		Role staff=new Role();
		staff.setId(GlobalContext.ROLE_STAFF);
		staff.setName("员工");
		staff.setType("common");
		roleService.save(staff);
		
		Role leader=new Role();
		leader.setId(GlobalContext.ROLE_LEADER);
		leader.setName("领导");
		leader.setType("common");
		roleService.save(leader);
	}
	
	@Test
	@Rollback(false)
	public void testAddUserRole(){
		String loginName="admin";
		userService.addRole(loginName, GlobalContext.ROLE_ADMIN);
		userService.addRole(loginName, GlobalContext.ROLE_USER);
		userService.addRole(loginName, GlobalContext.ROLE_LEADER);
	}
}
