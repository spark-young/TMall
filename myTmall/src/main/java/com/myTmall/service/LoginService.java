package com.myTmall.service;

import com.alibaba.fastjson.JSONObject;

public interface LoginService {
	public boolean login(JSONObject user);
	public String qrcodeUUID();
	public boolean scan(String uuid,String username);
	public String loginSuccess(String uuid);
	public boolean pool(String uuid);
}
