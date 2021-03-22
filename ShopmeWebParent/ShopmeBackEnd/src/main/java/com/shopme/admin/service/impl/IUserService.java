package com.shopme.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopme.admin.error.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public interface IUserService {

	public List<User> listAll();
	
	public List<Role> listRoles();
	
	public User save(User user);
	
	public boolean isEmailUnique(Integer id, String email);
	
	public User get(Integer id) throws UserNotFoundException;
	
	public void delete(Integer id) throws UserNotFoundException;
	
	public void updateUserEnabledStatus(Integer id, boolean enabled);
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword);
	
	public User getByEmail(String email);
	
	public User updateAccount(User userInForm);
}
