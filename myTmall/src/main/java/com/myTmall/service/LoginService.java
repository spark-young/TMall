package com.myTmall.service;

import com.alibaba.fastjson.JSONObject;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface LoginService {
	public boolean login(JSONObject user);
	public String qrcodeUUID();
	public boolean scan(String uuid,String username);
	public String loginSuccess(String uuid);
	public Flux<ServerSentEvent<String>> pool(String uuid);
}
