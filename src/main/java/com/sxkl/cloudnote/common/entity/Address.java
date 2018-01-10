package com.sxkl.cloudnote.common.entity;

import lombok.Data;

/**
 * @author: wangyao
 * @date:2018年1月10日 上午11:07:00
 */
@Data
public class Address {
	
	private String country;
	private String province;
	private String city;
	private Double latitude;
	private Double longitude;
}
