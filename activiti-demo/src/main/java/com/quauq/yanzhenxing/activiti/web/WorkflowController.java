package com.quauq.yanzhenxing.activiti.web;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quauq.yanzhenxing.activiti.service.UserService;
import com.quauq.yanzhenxing.activiti.utils.WorkflowUtils;

/**
 * 工作流controller
 * @description 
 * @author yanzhenxing
 * @createDate 2015年9月16日
 */
@Controller
@RequestMapping("workflow")
public class WorkflowController {
	private static Logger log = LoggerFactory.getLogger(WorkflowController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ObjectMapper objectMapper;
	@RequestMapping(value="predeploy",method=RequestMethod.GET)
	public String preDeploy(){
		return "modules/workflow/predeploy";
	}
	
	 @RequestMapping(value = "/deploy")
	    public String deploy(@RequestParam(value = "file", required = false) MultipartFile file) {
//		 	String exportDir="";
	        String fileName = file.getOriginalFilename();

	        try {
	            InputStream fileInputStream = file.getInputStream();
	            Deployment deployment = null;

	            String extension = FilenameUtils.getExtension(fileName);
	            if (extension.equals("zip") || extension.equals("bar")) {
	                ZipInputStream zip = new ZipInputStream(fileInputStream);
	                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
	            } else {
	                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
	            }

	            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
	            System.out.println(list.size());
//	            for (ProcessDefinition processDefinition : list) {
//	                WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
//	            }

	        } catch (Exception e) {
	        	log.error("error on deploy process, because of file input stream", e);
	        }

	        return "redirect:/";
	    }
}
