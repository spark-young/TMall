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

import java.util.UUID;

import com.alibaba.druid.stat.TableStat.Mode;
import com.alibaba.fastjson.JSONObject;
import com.myTmall.service.LoginService;
import com.utils.QrcodePool;
import com.utils.QrcodeScan;

@RestController
@RequestMapping("/TMallLogin")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@GetMapping
	public ModelAndView loginning(){
		ModelAndView view = new ModelAndView("/page/login");
		return view;
	}
	@CrossOrigin
	@RequestMapping("/login_success")
	public ModelAndView loginSuccess(){
		ModelAndView view = new ModelAndView("/page/login-success");
		return view;
	}
	@CrossOrigin
	@RequestMapping("qrcodeLogin")
	public String qrcodeLogin(){
		String uuid = UUID.randomUUID().toString();
		System.out.println("-----------------"+uuid+"-----------------");
		QrcodePool.cacheMap.put(uuid, new QrcodeScan());
		return uuid;
	}
	@CrossOrigin
	@RequestMapping("/scan")
	public boolean Scan(@RequestParam("uuid") String uuid){
		if(QrcodePool.cacheMap != null && !QrcodePool.cacheMap.isEmpty()){
			QrcodePool.cacheMap.get(uuid).scanSuccess();
			return true;
		}
		return false;
	}
	@CrossOrigin
	@RequestMapping("/pool")
	public boolean pool(@RequestParam("uuid") String uuid){
		System.out.println(uuid);
		QrcodeScan pool = QrcodePool.cacheMap.get(uuid);
		System.out.println("get pool "+ pool.toString());
		new Thread(new ScanCounter(uuid, pool)).start();
		if(QrcodePool.cacheMap != null && !QrcodePool.cacheMap.isEmpty())
			return pool.getScanStatus();
		return false;
	}
	@CrossOrigin
	@RequestMapping("/qrcode-scan")
	public ModelAndView qrcodeScan(){
		ModelAndView view = new ModelAndView("/page/qrcode-scan");
		return view;
	}

	@CrossOrigin
	@PostMapping
	public boolean login(@RequestBody JSONObject user) {
		return loginService.login(user);
	}
}

class ScanCounter implements Runnable{

	private String uuid;
	private QrcodeScan scan;
	public ScanCounter(String uuid,QrcodeScan scan){
		this.uuid = uuid;
		this.scan = scan;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		notifyPool(uuid, scan);
	}
	public synchronized void notifyPool(String uuid, QrcodeScan scan) {
        if (scan != null) scan.notifyPool();
    }

}
