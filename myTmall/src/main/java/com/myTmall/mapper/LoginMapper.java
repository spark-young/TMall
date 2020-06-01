package com.myTmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.myTmall.entity.User;

public interface LoginMapper {
	@Select("select * from myTmall.user where name=#{name} and password=#{password}")
	public List<User> loginWithName(String name,String password);
	@Select("select * from myTmall.user where phone=#{phone} and password=#{password}")
	public List<User> loginWithPhone(String phone,String password);
	@Select("select * from myTmall.user where email=#{email} and password=#{password}")
	public List<User> loginWithEmail(String email,String password);
}
