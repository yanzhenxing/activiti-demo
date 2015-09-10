package com.quauq.yanzhenxing.activiti.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quauq.yanzhenxing.activiti.service.UserService;

@Controller
@RequestMapping("/")
public class TestController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("login")
	public String login(Model model){
		Subject subject=SecurityUtils.getSubject();
		if (subject!=null&&subject.isAuthenticated()) {
			return "redirect:/";
		}
		return "modules/sys/login";
	}
	
	@RequestMapping("index")
	public String index(Model model){
//		userService.add();
		model.addAttribute("name", "闫振兴");
		return "index";
	}
}
