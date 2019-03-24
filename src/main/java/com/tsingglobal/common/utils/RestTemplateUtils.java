package com.tsingglobal.common.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
 
 
public class RestTemplateUtils {
 
	static final RestTemplate INSTANCE = new RestTemplate();
	
	static final RestTemplateUtils obj = new RestTemplateUtils();
 
	private RestTemplateUtils() {}
	
	/**
	 * 单例实例
	 */
	public static RestTemplateUtils getInstance() {
		return obj;
	}
 
	/**
	 * 
	 * 发送post请求
	 * 
	 * @param url
	 *            请求URL地址
	 * @param data
	 *            json数据
	 * @param token
	 *            RSA加密token【RSA({PlatformCode+TenantCode+DateTime.Now()})//平台代码
	 *            +会员/租户代码+当前时间，然后进行RSA加密】
	 */
	public static String post(String url, String data, String token)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Accpet-Encoding", "gzip");
		headers.add("Content-Encoding", "UTF-8");
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Token", token);
 
		HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
		return INSTANCE.postForObject(url, formEntity, String.class);
	}
	
	/**
	 * 
	 * 发送post请求
	 * 
	 * @param url
	 *            请求URL地址
	 * @param data
	 *            json数据
	 * @param token
	 *            RSA加密token【RSA({PlatformCode+TenantCode+DateTime.Now()})//平台代码
	 *            +会员/租户代码+当前时间，然后进行RSA加密】
	 * @param platformCode
	 *            平台编码{平台分配}
	 * @param tenantCode
	 *            租户号{平台分配}
	 */
	public static String post(String url, String data, String token,String platformCode,String tenantCode)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Accpet-Encoding", "gzip");
		headers.add("Content-Encoding", "UTF-8");
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Token", token);
		headers.add("PlatformCode", platformCode);
		headers.add("TenantCode", tenantCode);
 
		HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
		return INSTANCE.postForObject(url, formEntity, String.class);
	}
	
	/**
	 * get根据url获取对象
	 */
	public String get(String url) {
		return INSTANCE.getForObject(url, String.class,
				new Object[] {});
	}
 
	/**
	 * getById根据ID获取对象
	 */
	public String getById(String url, String id) {
		return INSTANCE.getForObject(url, String.class,
				id);
	}
 
	/**
	 * post提交对象
	 */
	public String post(String url, String data) {
//		return INSTANCE.postForObject(url, null,
//				String.class, data);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Accpet-Encoding", "gzip");
		headers.add("Content-Encoding", "UTF-8");
		headers.add("Content-Type", "application/json; charset=UTF-8");
 
		System.out.println("data="+data);
		HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
		return INSTANCE.postForObject(url, formEntity, String.class);
	}
 
	/**
	 * put修改对象
	 */
	public void put(String url, String data) {
		INSTANCE.put(url, null, data);
	}
 
	/**
	 * delete根据ID删除对象
	 */
	public void delete(String url, String id) {
		INSTANCE.delete(url, id);
	}
}
