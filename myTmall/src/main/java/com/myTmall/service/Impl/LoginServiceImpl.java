package com.myTmall.service.Impl;

import com.myTmall.mapper.LoginMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.myTmall.service.LoginService;
import com.utils.QrcodePool;
import com.utils.QrcodeScan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	@Autowired
	private LoginMapper loginMapper;

	@Override
	public boolean login(JSONObject user) {
		String type = user.getString("type");
		switch (type) {
			case "name":
				return loginMapper.loginWithName(user.getString("name"), user.getString("password")).size() == 1 ? true
						: false;
			case "phone":
				return loginMapper.loginWithPhone(user.getString("phone"), user.getString("password")).size() == 1
						? true
						: false;
			case "email":
				return loginMapper.loginWithEmail(user.getString("email"), user.getString("password")).size() == 1
						? true
						: false;
		}
		return false;
	}

	@Override
	public String qrcodeUUID() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("-----------------" + uuid + "-----------------");
		QrcodePool.cacheMap.put(uuid, new QrcodeScan());
		return uuid;
	}

	// 将uuid登录状态设为true并绑定用户
	public boolean scan(String uuid, String username) {
		// Qrcode池不存在或为空或没有key为uuid时返回false
		if (QrcodePool.cacheMap != null && !QrcodePool.cacheMap.isEmpty() && QrcodePool.cacheMap.containsKey(uuid)) {
			QrcodeScan scan = QrcodePool.cacheMap.get(uuid);
			scan.scanSuccess();
			scan.setLoginUser(username);
			return true;
		}
		return false;
	}

	// 这里获取登录时手机端绑定的uuid和username信息，使用之后需将这个uuid对应的QrcodeScan remove
	@Override
	public String loginSuccess(String uuid) {
		if (QrcodePool.cacheMap != null && !QrcodePool.cacheMap.isEmpty()) {
			QrcodeScan scan = QrcodePool.cacheMap.get(uuid);
			System.out.println(scan.getLoginUser() + " 成功登录");
			String username = scan.getLoginUser();
			QrcodePool.cacheMap.remove(uuid);
			return username;
		}
		return null;
	}

	public Flux<ServerSentEvent<String>> pool(String uuid) {
		System.out.println(uuid+" 等待扫描");
		if (QrcodePool.cacheMap != null && !QrcodePool.cacheMap.isEmpty()) {
			QrcodeScan pool = QrcodePool.cacheMap.get(uuid);
			new Thread(new ScanCounter(uuid, pool)).start();
			//只发送1个
				return Flux.range(1,1)
						//这里使用interval在扫码成功之后会出现异常，使用range则不会
//				return Flux.interval(Duration.ofSeconds(1))
						.map(data -> ServerSentEvent.<String>builder()
								.event("message")
								.id("1")
								.data("" + pool.getScanStatus()).build());
		}
		return null;
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
