package com.myTmall.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.myTmall.mapper.LoginMapper;
import com.myTmall.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public boolean login(JSONObject user) {
		String type = user.getString("type");
		switch(type){
			case "name": return loginMapper.loginWithName(user.getString("name"), user.getString("password")).size()==1?true:false;
			case "phone": return loginMapper.loginWithPhone(user.getString("phone"), user.getString("password")).size()==1?true:false;
			case "email": return loginMapper.loginWithEmail(user.getString("email"), user.getString("password")).size()==1?true:false;
		}
		return false;
	}
}
