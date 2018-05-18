package com.sxkl.cloudnote.editor.define;

import java.util.HashMap;
import java.util.Map;

public class MIMEType {
	
	@SuppressWarnings("serial")
	public static final Map<String, String> types = new HashMap<String, String>() {
	};

	public static String getSuffix(String mime) {
		return (String) types.get(mime);
	}
}
