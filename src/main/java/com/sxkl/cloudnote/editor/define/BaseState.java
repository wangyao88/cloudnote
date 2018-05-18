package com.sxkl.cloudnote.editor.define;

import java.util.Iterator;
import java.util.Map;

import org.apache.curator.shaded.com.google.common.collect.Maps;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.editor.Encoder;

public class BaseState implements State {
	
	private boolean state = false;
	private String info = null;
	private Map<String, String> infoMap = Maps.newHashMap();

	public BaseState() {
		this.state = true;
	}

	public BaseState(boolean state) {
		setState(state);
	}

	public BaseState(boolean state, String info) {
		setState(state);
		this.info = info;
	}

	public BaseState(boolean state, int infoCode) {
		setState(state);
		this.info = AppInfo.getStateInfo(infoCode);
	}

	public boolean isSuccess() {
		return this.state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setInfo(int infoCode) {
		this.info = AppInfo.getStateInfo(infoCode);
	}

	public String toJSONString() {
		return toString();
	}

	public String toString() {
		String key = null;
		String stateVal = isSuccess() ? AppInfo.getStateInfo(0) : this.info;

		StringBuilder builder = new StringBuilder();

		builder.append("{\"state\": \"" + stateVal + "\"");

		Iterator<String> iterator = this.infoMap.keySet().iterator();
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			String value = (String) this.infoMap.get(key);
			if("url".equals(key)){
				value = Constant.DOMAIN + value;
			}
			builder.append(",\"" + key + "\": \"" + value + "\"");
		}
		builder.append("}");
		return Encoder.toUnicode(builder.toString());
	}

	public void putInfo(String name, String val) {
		this.infoMap.put(name, val);
	}

	public void putInfo(String name, long val) {
//		putInfo(name, val);
		this.infoMap.put(name, val+"");
	}
}
