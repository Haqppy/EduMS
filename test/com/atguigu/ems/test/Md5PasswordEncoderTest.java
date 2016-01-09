package com.atguigu.ems.test;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class Md5PasswordEncoderTest {
	
	public static void main(String[] args) {
		
		Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		String password = passwordEncoder.encodePassword("123456", "AAAAAA");
		System.out.println(password);
		
		password = passwordEncoder.encodePassword("123456", "AABBCC2");
		System.out.println(password);
	}
	
}
