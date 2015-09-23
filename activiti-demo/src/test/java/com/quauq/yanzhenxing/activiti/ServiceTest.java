package com.quauq.yanzhenxing.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
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
		workflowService.deleteProcIns("10001", "测试");
	}
	
	/**
	 * 测试删除部署的流程
	 */
	@Test
	@Rollback(false)
	public void testDeleteDeployment(){
		workflowService.deleteDeployment("55001", true);
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
	
    /**
     * 部门领导和人事全部审批通过
     */
    @Test
//    @Transactional
//    @Deployment(resources = "classpath*:/processes/leave6.bpmn20.xml")
    public void allApproved() throws Exception {

        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").latestVersion().singleResult();

        // 设置当前用户
        String currentUserId = "staff";
        identityService.setAuthenticatedUserId(currentUserId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> variables = new HashMap<String, String>();
        Calendar ca = Calendar.getInstance();
        String startDate = sdf.format(ca.getTime());
        ca.add(Calendar.DAY_OF_MONTH, 2); // 当前日期加2天
        String endDate = sdf.format(ca.getTime());

        // 启动流程
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);
        assertNotNull(processInstance);

        // 部门领导审批通过
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("leader").singleResult();
        variables = new HashMap<String, String>();
        variables.put("deptLeaderApprove", "true");
        formService.submitTaskFormData(deptLeaderTask.getId(), variables);

        // 人事审批通过
        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        variables = new HashMap<String, String>();
        variables.put("hrApprove", "true");
        formService.submitTaskFormData(hrTask.getId(), variables);

        // 销假（根据申请人的用户ID读取）
        Task reportBackTask = taskService.createTaskQuery().taskAssignee(currentUserId).singleResult();
        variables = new HashMap<String, String>();
        variables.put("reportBackDate", sdf.format(ca.getTime()));
//        variables.put("result", "ok");
        formService.submitTaskFormData(reportBackTask.getId(), variables);

        // 验证流程是否已经结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();
        assertNotNull(historicProcessInstance);

        // 读取历史变量
        Map<String, Object> historyVariables = packageVariables(processInstance);

        // 验证执行结果
        assertEquals("ok", historyVariables.get("result"));
    }
    
    /**
     * 读取历史变量并封装到Map中
     */
    private Map<String, Object> packageVariables(ProcessInstance processInstance) {
        Map<String, Object> historyVariables = new HashMap<String, Object>();
        List<HistoricVariableInstance> list=historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
        for (HistoricVariableInstance historicVariableInstance : list) {
        	historyVariables.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
        	System.out.println(historicVariableInstance.getVariableName()+":"+ historicVariableInstance.getValue());
		}
        
//        List<HistoricDetail> list = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
//        for (HistoricDetail historicDetail : list) {
//            if (historicDetail instanceof HistoricFormProperty) {
//                // 表单中的字段
//                HistoricFormProperty field = (HistoricFormProperty) historicDetail;
//                historyVariables.put(field.getPropertyId(), field.getPropertyValue());
//                System.out.println("form field: taskId=" + field.getTaskId() + ", " + field.getPropertyId() + " = " + field.getPropertyValue());
//            } else if (historicDetail instanceof HistoricVariableUpdate) {
//                HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
//                historyVariables.put(variable.getVariableName(), variable.getValue());
//                System.out.println("variable: " + variable.getVariableName() + " = " + variable.getValue());
//            }
//        }
        return historyVariables;
    }
}
