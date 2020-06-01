package com.myTmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.myTmall.service.LoginService;

@RestController
@RequestMapping("/TMallLogin")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@RequestMapping("/login_success")
	public ModelAndView loginSuccess(){
		ModelAndView view = new ModelAndView("login-success");
		return view;
	}

	@CrossOrigin
	@PostMapping
	public boolean login(@RequestBody JSONObject user) {
		return loginService.login(user);
	}
}
