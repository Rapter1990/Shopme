package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public List<User> listAll() {
		List<User> userList = new ArrayList<>();
		userRepo.findAll().forEach(userList::add);
		return userList;
	}
}
