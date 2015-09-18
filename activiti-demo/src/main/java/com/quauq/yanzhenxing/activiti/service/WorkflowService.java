package com.quauq.yanzhenxing.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

/**
 * 工作流相关服务
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Service
public class WorkflowService {

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	/**
	 * 查看流程定义，每个定义取最高版本
	 * @param category
	 * @return
	 */
	public List<ProcessDefinition> obtainProcessDefinitions(String category){
		
	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().orderByProcessDefinitionKey().asc();
	    
	    if (StringUtils.isNotBlank(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    return processDefinitionQuery.list();
	}
	
	/**
	 * 启动流程
	 * @param loginUser 登录用户名
	 * @param procDefKey 流程定义KEY
	 * @param businessId 流程业务id
	 * @param vars			流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String loginUser,String procDefKey, String businessId, Map<String, Object> vars) {
		Assert.hasText(loginUser, "loginUser should not be empty!");
		Assert.hasText(procDefKey, "procDefKey should not be empty!");
		Assert.hasText(businessId, "businessId should not be empty!");
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(loginUser);
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessId, vars);
		
		return procIns.getId();
	}
	
	/**
	 * 删除部署的流程实例
	 * @param procInsId 流程实例ID
	 * @param deleteReason 删除原因，可为空
	 */
	@Transactional(readOnly = false)
	public void deleteProcIns(String procInsId, String deleteReason){
		Assert.hasText(procInsId, "procInsId should not be empty!");
		runtimeService.deleteProcessInstance(procInsId, deleteReason);
	}
}
