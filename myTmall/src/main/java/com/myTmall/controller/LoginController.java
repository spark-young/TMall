package com.myTmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.myTmall.service.LoginService;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/TMallLogin")
public class LoginController {
	@Autowired
	private LoginService loginService;
	//直接映射到login.html
	@GetMapping
	@ResponseBody
	public ModelAndView loginning(){
		ModelAndView view = new ModelAndView("/page/login");
		return view;
	}
	//扫码成功后跳转
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
	public Flux<ServerSentEvent<String>> pool(@RequestParam("uuid") String uuid){
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
	@CrossOrigin
	@RequestMapping("login_pwd_success")
	public ModelAndView loginPwdSuccess(@RequestParam("username") String username){
		ModelAndView view =  new ModelAndView("page/login-success");
		view.addObject("username", username);
		return view;
	}
}

