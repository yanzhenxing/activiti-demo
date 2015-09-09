package com.quauq.yanzhenxing.activiti.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping("index")
	public String index(Model model){
		model.addAttribute("name", "闫振兴");
		return "index";
	}
}
