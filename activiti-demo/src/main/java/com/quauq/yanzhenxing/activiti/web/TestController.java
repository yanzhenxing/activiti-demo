package com.quauq.yanzhenxing.activiti.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quauq.yanzhenxing.activiti.service.UserService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("login")
	public String login(Model model){
		return "modules/sys/login";
	}
	
	@RequestMapping("index")
	public String index(Model model){
//		userService.add();
		model.addAttribute("name", "闫振兴");
		return "index";
	}
}
