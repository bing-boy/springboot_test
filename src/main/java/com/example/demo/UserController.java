package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserResponsitory userResponsitory;

	@Autowired
	public UserController(UserResponsitory userResponsitory) {
		this.userResponsitory = userResponsitory;
	}

	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public String getUser(@PathVariable("userName") String userName, Model model) {
		List<User> userList = userResponsitory.findByName(userName);
		if (userList != null) {
			model.addAttribute("users", userList);
		}
		return "userList";
	}

	@RequestMapping(value = { "/", "/{userName}" }, method = RequestMethod.POST)
	public String addUser(User user) {
		logger.info("addUser-" + user.getName());
		user.setName(user.getName());
		userResponsitory.save(user);
		return "redirect:/user/";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String ListUser(Model model) {
		logger.info("ListUser(Model model)");
		List<User> userList = userResponsitory.findAll();
		if (userList != null) {
			model.addAttribute("users", userList);
		}
		for (User user : userList) {
			logger.info(user.getName());
		}
		return "userList";
	}

}
