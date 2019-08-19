package com.sxkl.cloudnote.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import com.sxkl.cloudnote.common.entity.Address;

public class IPUtils {

    public static final String getIPAddr(final HttpServletRequest request) {
//		if(request == null){
//			throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
//		}
//		String ipString = request.getHeader("x-forwarded-for");
//		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
//			ipString = request.getHeader("Proxy-Client-IP");
//		}
//		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
//			ipString = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
//			ipString = request.getRemoteAddr();
//		}
//		final String[] arr = ipString.split(",");
//		for(final String str : arr){
//			if (!"unknown".equalsIgnoreCase(str)) {
//				ipString = str;
//				break;
//			}
//		}
//		return ipString;
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    public static Address getAddress(String ip) throws IOException, GeoIp2Exception {
        String path = PropertyUtil.class.getClassLoader().getResource("GeoLite2-City.mmdb").getPath();
        // 创建 GeoLite2 数据库
        File database = new File(path);
        // 读取数据库内容
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        InetAddress ipAddress = InetAddress.getByName(ip);
        // 获取查询结果
        CityResponse response = reader.city(ipAddress);
        // 获取国家信息
        Country country = response.getCountry();
        // 获取省份
        Subdivision subdivision = response.getMostSpecificSubdivision();
        // 获取城市
        City city = response.getCity();
//		Postal postal = response.getPostal();
        Location location = response.getLocation();
        Address address = new Address();
        address.setCountry(country.getNames().get("zh-CN"));
        address.setProvince(subdivision.getNames().get("zh-CN"));
        address.setCity(city.getNames().get("zh-CN"));
        address.setLatitude(location.getLatitude());
        address.setLongitude(location.getLongitude());
        return address;
    }

    public static String getCityForWeather(final HttpServletRequest request) throws Exception {
        String ip = getIPAddr(request);
        Address address = getAddress(ip);
        return address.getCity();
    }
}
