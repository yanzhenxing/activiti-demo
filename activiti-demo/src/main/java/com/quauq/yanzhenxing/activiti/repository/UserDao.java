package com.quauq.yanzhenxing.activiti.repository;

import org.springframework.data.repository.CrudRepository;

import com.quauq.yanzhenxing.activiti.entity.User;

public interface UserDao extends CrudRepository<User, String> {

}
