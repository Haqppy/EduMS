package com.atguigu.ems.test;

import java.util.ArrayList;
import java.util.List;

import com.atguigu.ems.entities.City;

public class TestJsonAction {

	private List<City> cities = null;
	
	public List<City> getCities() {
		return cities;
	}
	
	public String test(){
		cities = new ArrayList<>();

		cities.add(new City(1001, "AA"));
		cities.add(new City(1002, "BB"));
		cities.add(new City(1003, "CC"));
		cities.add(new City(1004, "DD"));
		cities.add(new City(1005, "EE"));
		
		return "success";
	}
	
}
