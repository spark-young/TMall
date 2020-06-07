package com.myTmall.service;

import com.alibaba.fastjson.JSONObject;

public interface LoginService {
	public boolean login(JSONObject user);
	public String qrcodeUUID();
}
