package com.atguigu.ems.utils;

public class UploadError {
	
	//从 1 开始
	public int rowNumber;
	//字段名
	public String fieldName;
	
	public UploadError(int rowNumber, String fieldName) {
		this.rowNumber = rowNumber;
		this.fieldName = fieldName;
	}
}
