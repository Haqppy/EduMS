package com.atguigu.ems.utils;

import java.util.List;

public class Navigation {

	private String id;
	private String text;
	private String state;
	private String url;

	private List<Navigation> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Navigation> getChildren() {
		return children;
	}

	public void setChildren(List<Navigation> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "\nNavigation [id=" + id + ", text=" + text + ", state=" + state
				+ ", url=" + url + ", children=" + children + "]\n";
	}

}
