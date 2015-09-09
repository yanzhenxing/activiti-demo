package com.quauq.yanzhenxing.activiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.yanzhenxing.activiti.entity.User;
import com.quauq.yanzhenxing.activiti.repository.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void add(){
		User user=new User();
		user.setId("yanzhenxing");
		user.setName("闫振兴");
		userDao.save(user);
	}
}
