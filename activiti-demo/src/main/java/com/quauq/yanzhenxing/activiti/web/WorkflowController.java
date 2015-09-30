package com.quauq.yanzhenxing.activiti.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quauq.yanzhenxing.activiti.convertor.DateEditor;
import com.quauq.yanzhenxing.activiti.entity.Role;
import com.quauq.yanzhenxing.activiti.service.RoleService;
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
	private RoleService roleService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private FormService formService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * controller中Date类型转化
	 * 
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	@RequestMapping(value = "predeploy", method = RequestMethod.GET)
	public String preDeploy() {
		return "modules/workflow/predeploy";
	}

	/**
	 * 启动流程页面
	 * 
	 * @deprecated
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "process/start/form", method = RequestMethod.GET)
	public String startForm(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("key") String key) {
		model.addAttribute("key", key);
		// String businessId="test";
		// workflowService.startProcess(SystemUtils.obtainLoginName(), key,
		// businessId, null);
		return "modules/workflow/startform";
	}
	
	/**
	 * 流程启动表单
	 * @param processDefinitionId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="process/start/getform/{processDefinitionId}",method=RequestMethod.GET)
	public String readStartForm(Model model,  @PathVariable("processDefinitionId")String processDefinitionId) throws Exception{
		
		StartFormData startFormData=formService.getStartFormData(processDefinitionId);
		
		model.addAttribute("startFormData", startFormData);
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("hasStartFormKey", false);
		model.addAttribute("key", startFormData.getProcessDefinition().getKey());
		
		return "modules/workflow/start-process-form";
	}

	/**
	 * 我的待办任务
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "task/todo", method = RequestMethod.GET)
	public String taskTodo(Model model, HttpServletRequest request, HttpServletResponse response) {

		String loginName = SystemUtils.obtainLoginName();

//		List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(loginName).list();
		List<Role> roles=roleService.obtainRoles(loginName);
		List<String> groupIds=new ArrayList<String>();
		for (Role role : roles) {
			groupIds.add(role.getId());
		}
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroupIn(groupIds).list();
		List<Task> tasks2=taskService.createTaskQuery().taskCandidateOrAssigned(loginName).list();
		tasks.addAll(tasks2);
		model.addAttribute("tasks", tasks);
		return "modules/workflow/task-list";
	}

	/**
	 * 签收任务
	 * 
	 * @param taskId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="task/claim/{taskId}")
	public String claim(@PathVariable("taskId")String taskId,RedirectAttributes redirectAttributes){
		
        String userId = SystemUtils.obtainLoginName();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        
		return "redirect:/workflow/task/todo";
	}
	
    /**
     * 读取用户任务的表单字段
     */
    @RequestMapping(value = "task/handle/{taskId}")
    public String readTaskForm(Model model,@PathVariable("taskId") String taskId) throws Exception {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        if (taskFormData.getFormKey() != null) {
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            model.addAttribute("task", task);
            model.addAttribute("taskFormData", renderedTaskForm);
            model.addAttribute("hasFormKey", true);
        } else {
        	model.addAttribute("taskFormData", taskFormData);
        }
        return "modules/workflow/task-form";
    }
	
	/**
	 * 启动请假流程
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param key
	 * @param startDate
	 * @param endDate
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "process/start", method = RequestMethod.POST)
	public String start(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("key") String key, @RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate, @RequestParam(value="reason",required=false) String reason) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(key).latestVersion().singleResult();

		if (processDefinition == null) {
			throw new RuntimeException("没有对应的流程定义：key：" + key);
		}

		String loginName = SystemUtils.obtainLoginName();
		/**
		 * 设置启动用户
		 */
		identityService.setAuthenticatedUserId(loginName);

		Map<String, String> variables = new HashMap<String, String>();
		
		if (startDate!=null) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDateStr = sdf.format(startDate);
			String endDateStr = sdf.format(endDate);
			
			// 启动流程
			variables.put("startDate", startDateStr);
			variables.put("endDate", endDateStr);
			variables.put("reason", reason);
		}
		variables.put("assigneeList", "staff,leader,admin");
		
		ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);

		if (processInstance == null) {
			throw new RuntimeException("流程启动失败：key：" + key);
		}

		return "redirect:/workflow/deploylist";
	}

	/**
	 * 完成任务
	 * @param model
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value="task/complete/{taskId}",method=RequestMethod.POST)
	public String completeTask(Model model,@PathVariable("taskId")String taskId,HttpServletRequest request){
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		//从request中获取表单数据中的可写的字段
		List<FormProperty> formProperties=taskFormData.getFormProperties();
		Map<String, String> formValues=new HashMap<String, String>();
		for (FormProperty formProperty : formProperties) {
			if (formProperty.isWritable()) {//只获取可写字段的值
				String value=request.getParameter(formProperty.getId());
				formValues.put(formProperty.getId(), value);
			}
		}
		//提交用户任务表单并完成任务
		formService.submitTaskFormData(taskId, formValues);
		
		return "redirect:/workflow/task/todo";
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
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param resourceName
	 *            资源名称
	 */
	@RequestMapping(value = "process/resource")
	public void readResource(@RequestParam("procDefId") String procDefId,
			@RequestParam("resource") String resourceName, HttpServletResponse response) throws Exception {
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
			JsonNode editorNode = new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
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
