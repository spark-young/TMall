package com.myTmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.myTmall.service.LoginService;

@RestController
@RequestMapping("/TMallLogin")
public class LoginController {
	@Autowired
	private LoginService loginService;
	//直接映射到login.html
	@GetMapping
	public ModelAndView loginning(){
		ModelAndView view = new ModelAndView("/page/login");
		return view;
	}
	@CrossOrigin
	@RequestMapping("/login_success")
	public ModelAndView loginSuccess(@RequestParam("uuid")String uuid){
		ModelAndView view =  new ModelAndView("page/login-success");
		view.addObject("username", loginService.loginSuccess(uuid));
		return view;
	}
	//由PC端请求这个路径，返回二维码的uuid
	@CrossOrigin
	@RequestMapping("qrcodeLogin")
	public String qrcodeLogin(){
		return loginService.qrcodeUUID();
	}
	//手机端提交扫码登陆结果
	@CrossOrigin
	@RequestMapping("/scan")
	public boolean Scan(@RequestParam("uuid") String uuid,@RequestParam("username") String username){
		return loginService.scan(uuid,username);
	}
	//PC端轮询中访问的路径，获取指定uuid是否被扫描
	@CrossOrigin
	@RequestMapping("/pool")
	public boolean pool(@RequestParam("uuid") String uuid){
		return loginService.pool(uuid);
	}
	@CrossOrigin
	@RequestMapping("/qrcode-scan")//手机确认登录页面
	public ModelAndView qrcodeScan(@RequestParam("uuid") String uuid){
		ModelAndView view = new ModelAndView("/page/qrcode-scan");
		view.addObject("uuid", uuid);
		return view;
	}
	// 手机端确认登录页面
	@RequestMapping("/qrcode-scan-success")
	public ModelAndView qrcodeScanSuccess(){
		ModelAndView view = new ModelAndView("/page/qrcode-scan-success");
		return view;
	}
	//密码登录
	@CrossOrigin
	@PostMapping
	public boolean login(@RequestBody JSONObject user) {
		return loginService.login(user);
	}
}

