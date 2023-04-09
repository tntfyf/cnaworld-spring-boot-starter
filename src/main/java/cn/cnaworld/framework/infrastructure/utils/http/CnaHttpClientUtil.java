package cn.cnaworld.framework.infrastructure.utils.http;

import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulBaseType;
import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulEntityType;
import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.Map;

/**
 * HTTP客户端工具
 * @author Lucifer
 * @date 2023/3/9
 * @since 1.0.0
 */
@Slf4j
public class CnaHttpClientUtil {

	private CnaHttpClientUtil() {

	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @return 响应报文
	 */
	public static String send(String url)  {
		return sendIgnoreSsl(url,null,null,RestFulBaseType.GET,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param restFulBaseType restFul协议类型
	 * @return 响应报文
	 */
	public static String send(String url , Map<String,Object> paramsMap ,RestFulBaseType restFulBaseType)  {
		return send(url,paramsMap,null,restFulBaseType);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulBaseType restFul协议类型
	 * @return 响应报文
	 */
	public static String send(String url ,RestFulBaseType restFulBaseType)  {
		return send(url,null,null,restFulBaseType);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @return 响应报文
	 */
	public static String send(String url, Map<String,Object> paramsMap)  {
		return send(url,paramsMap,null,RestFulBaseType.GET);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String send(String url, Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return send(url,paramsMap,headerMap,RestFulBaseType.GET);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url)  {
		return sendIgnoreSsl(url,null,null,RestFulBaseType.GET,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param restFulBaseType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url ,Map<String,Object> paramsMap ,RestFulBaseType restFulBaseType)  {
		return sendIgnoreSsl(url,paramsMap,null,restFulBaseType,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulBaseType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url ,RestFulBaseType restFulBaseType)  {
		return sendIgnoreSsl(url,null,null,restFulBaseType,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulBaseType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url, Map<String,Object> paramsMap , Map<String,String> headerMap,RestFulBaseType restFulBaseType)  {
		return sendIgnoreSsl(url,paramsMap,headerMap,restFulBaseType,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url, Map<String,Object> paramsMap)  {
		return sendIgnoreSsl(url,paramsMap,null,RestFulBaseType.GET,null);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求，忽略Ssl认证
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url, Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return sendIgnoreSsl(url,paramsMap,headerMap,RestFulBaseType.GET,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @return 响应报文
	 */
	public static String sendEntity(String url,Object dto)  {
		return sendEntity(url,dto,null,null,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @return 响应报文
	 */
	public static String sendEntity(String url )  {
		return sendEntity(url,null,null,null,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Map<String,Object> paramsMap ,RestFulEntityType restFulEntityType)  {
		return sendEntity(url,null,paramsMap,null,restFulEntityType);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntity(String url ,RestFulEntityType restFulEntityType)  {
		return sendEntity(url,null,null,null,restFulEntityType);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Object dto ,RestFulEntityType restFulEntityType)  {
		return sendEntity(url,dto,null,null,restFulEntityType);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Map<String,Object> paramsMap , Map<String,String> headerMap,RestFulEntityType restFulEntityType)  {
		return sendEntity(url,null,paramsMap,headerMap,restFulEntityType);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Object dto , Map<String,String> headerMap,RestFulEntityType restFulEntityType)  {
		return sendEntity(url,dto,null,headerMap,restFulEntityType);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @return 响应报文
	 */
	public static String sendEntity(String url, Map<String,Object> paramsMap)  {
		return sendEntity(url,null,paramsMap,null,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return sendEntity(url,null,paramsMap,headerMap,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Object dto , Map<String,String> headerMap)  {
		return sendEntity(url,dto,null,headerMap,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return sendEntity(url,dto,paramsMap,headerMap,RestFulEntityType.POST);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url)  {
		return sendEntityIgnoreSsl(url,null,null,null,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto)  {
		return sendEntityIgnoreSsl(url,dto,null,null,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Map<String,Object> paramsMap ,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,null,paramsMap,null,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url ,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,null,null,null,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto ,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,dto,null,null,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Map<String,Object> paramsMap , Map<String,String> headerMap,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,null,paramsMap,headerMap,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto , Map<String,String> headerMap,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,dto,null,headerMap,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap,RestFulEntityType restFulEntityType)  {
		return sendEntityIgnoreSsl(url,dto,paramsMap,headerMap,restFulEntityType,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url, Map<String,Object> paramsMap)  {
		return sendEntityIgnoreSsl(url,null,paramsMap,null,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return sendEntityIgnoreSsl(url,null,paramsMap,headerMap,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto , Map<String,String> headerMap)  {
		return sendEntityIgnoreSsl(url,dto,null,headerMap,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap)  {
		return sendEntityIgnoreSsl(url,dto,paramsMap,headerMap,RestFulEntityType.POST,null);
	}

	/**
	 * 发送RestFul 支持请求体的请求，忽略Ssl认证
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType 忽略协议
	 * @param agreement 忽略协议
	 * @return 响应报文
	 */
	public static String sendEntityIgnoreSsl(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulEntityType restFulEntityType, String agreement)  {
		CloseableHttpClient httpClient = initHttpClient(true,agreement);
		return sendRequestEntity(url, dto, paramsMap, headerMap, restFulEntityType, httpClient);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType 忽略协议
	 * @return 响应报文
	 */
	public static String sendEntity(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulEntityType restFulEntityType)  {
		CloseableHttpClient httpClient = initHttpClient(false,null);
		return sendRequestEntity(url, dto, paramsMap, headerMap, restFulEntityType, httpClient);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulBaseType 忽略协议
	 * @return 响应报文
	 */
	public static String send(String url , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulBaseType restFulBaseType)  {
		CloseableHttpClient httpClient = initHttpClient(false,null);
		return sendRequest(url, paramsMap, headerMap, restFulBaseType, httpClient);
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求 忽略ssl
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulBaseType 忽略协议
	 * @return 响应报文
	 */
	public static String sendIgnoreSsl(String url , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulBaseType restFulBaseType,String agreement)  {
		CloseableHttpClient httpClient = initHttpClient(true,agreement);
		return sendRequest(url, paramsMap, headerMap, restFulBaseType, httpClient);
	}

	/**
	 * 发送RestFul 支持请求体的请求
	 * 支持 POST PUT PATCH ，默认POST
	 * @author Lucifer
	 * @date 2023/3/10
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param dto 请求参数体
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulEntityType restFul协议类型
	 * @param httpClient 请求体
	 * @return 返回报文
	 */
	private static String sendRequestEntity(String url, Object dto, Map<String, Object> paramsMap, Map<String, String> headerMap, RestFulEntityType restFulEntityType, CloseableHttpClient httpClient) {
		url = processedUrl(url, paramsMap);
		HttpEntityEnclosingRequestBase httpRequest;
		switch (restFulEntityType) {
			case PUT:
				httpRequest = new HttpPut(url);
				break;
			case PATCH:
				httpRequest = new HttpPatch(url);
				break;
			case POST:
			default:
				httpRequest = new HttpPost(url);
				break;
		}
		processedRequestEntity(httpRequest, headerMap, dto);
		CnaLogUtil.debug(log,"准备发送{}请求，url : {} , dto : {}, paramsMap : {} , headerMap : {}",restFulEntityType,url,dto,paramsMap,headerMap);
		String response = getResponse(httpClient, httpRequest);
		CnaLogUtil.debug(log,"{}请求发送完毕，url : {} , dto : {}, paramsMap : {} , headerMap : {} , response : {}",restFulEntityType,url,dto,paramsMap,headerMap,response);
		return response;
	}

	/**
	 * 发送 RestFul 支持不支持请求体的请求
	 * 支持 DELETE GET ，默认 GET
	 * @author Lucifer
	 * @date 2023/3/10
	 * @since 1.0.0
	 * @param url 请求URL
	 * @param paramsMap 请求参数
	 * @param headerMap 请求头
	 * @param restFulBaseType restFul协议类型
	 * @param httpClient 请求体
	 * @return 返回报文
	 */
	private static String sendRequest(String url, Map<String, Object> paramsMap,
									  Map<String, String> headerMap, RestFulBaseType restFulBaseType, CloseableHttpClient httpClient) {
		url = processedUrl(url, paramsMap);
		HttpRequestBase httpRequest;
		switch (restFulBaseType) {
			case DELETE:
				httpRequest = new HttpDelete(url);
				break;
			case GET:
			default:
				httpRequest = new HttpGet(url);
				break;
		}
		processedRequest(httpRequest, headerMap);
		CnaLogUtil.debug(log,"准备发送{}请求，url : {} , paramsMap : {} , headerMap : {}",restFulBaseType,url,paramsMap,headerMap);
		String response = getResponse(httpClient, httpRequest);
		CnaLogUtil.debug(log,"{}请求发送完毕，url : {} , paramsMap : {} , headerMap : {} , response : {}",restFulBaseType,url,paramsMap,headerMap,response);
		return response;
	}

	/**
	 * 获取响应报文
	 * @author Lucifer
	 * @date 2023/3/10
	 * @since 1.0.0
	 * @param httpClient 请求实体
	 * @param httpRequest 请求体
	 * @return 响应报文
	 */
	private static String getResponse(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpRequest);
			return processedResponse(response);
		} catch (ParseException | IOException e) {
			CnaLogUtil.error(log,"http 请求发送失败 , URL : {} , Caused by : {}",httpRequest.getURI(),e.getMessage(),e);
		} finally {
			closeClient(httpClient, response);
		}
		return null;
	}

	/**
	 * 初始化请求体
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param ignoreSsl 忽略Ssl认证
	 * @param agreement 忽略协议
	 * @return HttpClient
	 */
	private static CloseableHttpClient initHttpClient(boolean ignoreSsl,String agreement){
		CloseableHttpClient httpClient;
		if (ignoreSsl) {
			SSLContext sslcontext = createIgnoreVerifySsl(agreement);
			//设置协议http和https对应的处理socket链接工厂的对象
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslcontext))
					.build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			httpClient = HttpClients.custom().setConnectionManager(connManager).build();
		}else {
			httpClient = HttpClientBuilder.create().build();
		}
		return httpClient;
	}

	/**
	 * 处理url
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param url 请求地址
	 * @param paramsMap 参数
	 * @return url
	 */
	public static String processedUrl(String url, Map<String,Object> paramsMap){
		StringBuilder urlBuffer=new StringBuilder(url);
		if(paramsMap != null && paramsMap.size()>0) {
			urlBuffer.append("?");
			for(Map.Entry<String, Object> entry : paramsMap.entrySet()){
				urlBuffer
						.append(entry.getKey())
						.append("=")
						.append(entry.getValue())
						.append("&");
			}
			url= urlBuffer.substring(0,urlBuffer.length()-1);
		}
		return url;
	}

	/**
	 * 处理请求体
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param httpEntity 请求体
	 * @param headerMap 请求头
	 * @param dto 请求参数体
	 */
	private static void processedRequestEntity(HttpEntityEnclosingRequestBase httpEntity, Map<String,String> headerMap, Object dto){
		processedRequestConfigAndHeader(httpEntity, headerMap);
		if(dto!=null) {
			String jsonString = JSON.toJSONString(dto);
			StringEntity entity = new StringEntity(jsonString, "UTF-8");
			httpEntity.setEntity(entity);
		}
	}

	/**
	 * 处理请求体
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param httpEntity 请求体
	 * @param headerMap 请求头
	 */
	private static void processedRequest(HttpRequestBase httpEntity, Map<String,String> headerMap){
		processedRequestConfigAndHeader(httpEntity, headerMap);
	}

	private static void processedRequestConfigAndHeader(HttpRequestBase httpEntity, Map<String, String> headerMap) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(1000).setSocketTimeout(6000).build();
		if(headerMap != null && headerMap.size()>0) {
			for(Map.Entry<String,String> entry : headerMap.entrySet()){
				httpEntity.setHeader(entry.getKey(),entry.getValue());
			}
		}
		httpEntity.setConfig(requestConfig);
		httpEntity.setHeader("content-type", "application/json;charset=utf8");
	}


	/**
	 * 处理响应体
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param response 响应体
	 * @return 返回值
	 */
	private static String processedResponse(CloseableHttpResponse response) throws IOException {
		// 从响应模型中获取响应实体
		String result= null;
		HttpEntity responseEntity = response.getEntity();
		CnaLogUtil.debug(log,"响应状态为 : {}" , response.getStatusLine());
		if (responseEntity != null) {
			result= EntityUtils.toString(responseEntity);
			CnaLogUtil.debug(log,"响应内容长度为 : {}， 响应内容为 : {}" , responseEntity.getContentLength() , result);
		}
		return result;
	}

	/**
	 * 忽略HTTPS认证
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param agreement 忽略协议
	 * @return SSLContext
	 */
	private static SSLContext createIgnoreVerifySsl(String agreement) {
		if(StringUtils.isBlank(agreement)){
			agreement="TLSv1.2";
		}
		SSLContext sc;
		try {
			sc = SSLContext.getInstance(agreement);
			// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
			X509TrustManager trustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
						String paramString) {
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
						String paramString) {
				}
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sc.init(null, new TrustManager[]{trustManager},null);
		} catch (Exception e) {
			CnaLogUtil.error(log,"HTTP 验证绕过处理失败 ： {}",e.getMessage(),e);
			throw new RuntimeException("验证绕过处理失败！");
		}
		return sc;
	}

	/**
	 * 关闭HttpClient
	 * @author Lucifer
	 * @date 2023/3/9
	 * @since 1.0.0
	 * @param httpClient 请求体
	 * @param response 响应体
	 */
	private static void closeClient(CloseableHttpClient httpClient, CloseableHttpResponse response) {
		try {
			// 释放资源
			if (httpClient != null) {
				httpClient.close();
			}
			if (response != null) {
				response.close();
			}
		} catch (IOException e) {
			CnaLogUtil.error(log,"HTTP 资源释放失败 ： {}",e.getMessage(),e);
		}
	}

}
