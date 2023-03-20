package cn.cnaworld.framework.infrastructure.utils.http.netty;


import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulBaseType;
import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulEntityType;

import java.util.Map;

/**
 * @author Lucifer
 * @date 2023/3/12
 * @since 1.0.0
 */
public class CnaNettyHttpClientUtil {

    private static final NettyHttpClient NETTY_HTTP_CLIENT = new NettyHttpClient();

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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,null, RestFulBaseType.GET);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,paramsMap,null, restFulBaseType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,null, restFulBaseType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,paramsMap,null, RestFulBaseType.GET);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,paramsMap,headerMap, RestFulBaseType.GET);
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
     * @param restFulBaseType restFul协议类型
     * @return 响应报文
     */
    public static String send(String url, Map<String,Object> paramsMap , Map<String,String> headerMap,RestFulBaseType restFulBaseType)  {
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,paramsMap,headerMap, restFulBaseType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,null,null, RestFulEntityType.POST);
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
    public static String sendEntity(String url)  {
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,null,null, RestFulEntityType.POST);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,paramsMap,null, restFulEntityType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,null,null, restFulEntityType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,null,null, restFulEntityType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,paramsMap,headerMap, restFulEntityType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,null,headerMap, restFulEntityType);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,paramsMap,null, RestFulEntityType.POST);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,null,paramsMap,headerMap, RestFulEntityType.POST);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,null,headerMap, RestFulEntityType.POST);
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
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,paramsMap,headerMap, RestFulEntityType.POST);
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
     * @param restFulEntityType restFul协议类型
     * @return 响应报文
     */
    public static String sendEntity(String url , Object dto , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulEntityType restFulEntityType)  {
        return NETTY_HTTP_CLIENT.sendHttpRequestByNetty(url,dto,paramsMap,headerMap, restFulEntityType);
    }

}
