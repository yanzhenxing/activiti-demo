package com.quauq.yanzhenxing.activiti.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quauq.yanzhenxing.activiti.service.UserService;
import com.quauq.yanzhenxing.activiti.service.WorkflowService;
import com.quauq.yanzhenxing.activiti.utils.SystemUtils;

/**
 * 工作流controller
 * 
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
	private WorkflowService workflowService;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "predeploy", method = RequestMethod.GET)
	public String preDeploy() {
		return "modules/workflow/predeploy";
	}

	/**
	 * 启动流程
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "process/start", method = RequestMethod.GET)
	public String StartProcess(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("key") String key) {
		String businessId="test";
		workflowService.startProcess(SystemUtils.obtainLoginName(), key, businessId, null);
		return "redirect:/workflow/deploylist";
	}

	/**
	 * 显示已经部署的流程列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deploylist", method = RequestMethod.GET)
	public String deployList(Model model) {

		List<ProcessDefinition> list = workflowService.obtainProcessDefinitions(null);

		model.addAttribute("processDefinitions", list);

		return "modules/workflow/deploylist";
	}
	
    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "process/resource")
    public void readResource(@RequestParam("procDefId") String procDefId, @RequestParam("resource") String resourceName, HttpServletResponse response)
            throws Exception {
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(procDefId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
	
    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "model/export/{modelId}")
    public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
        	org.activiti.engine.repository.Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
            
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
        	log.error("导出model的xml文件失败：modelId={}", modelId, e);
        }
    }
	/**
	 * 部署流程
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/deploy")
	public String deploy(@RequestParam(value = "file", required = false) MultipartFile file) {
		// String exportDir="";
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

			List<Deployment> list = repositoryService.createDeploymentQuery().processDefinitionKey("process1").list();
			System.out.println(list.size());
			// List<ProcessDefinition> list =
			// repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			// System.out.println(list.size());
			// for (ProcessDefinition processDefinition : list) {
			// WorkflowUtils.exportDiagramToFile(repositoryService,
			// processDefinition, exportDir);
			// }

		} catch (Exception e) {
			log.error("error on deploy process, because of file input stream", e);
		}

		return "redirect:/workflow/deploylist";
	}
}
