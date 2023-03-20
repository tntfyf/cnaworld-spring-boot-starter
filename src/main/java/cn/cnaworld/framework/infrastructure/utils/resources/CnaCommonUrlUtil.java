package cn.cnaworld.framework.infrastructure.utils.resources;


import cn.cnaworld.framework.infrastructure.properties.commonurl.CnaworldCommonUrlProperties;
import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方地址配置工具
 * @author Lucifer
 * @date 2023/3/8
 * @since 1.0.0
 */
@Slf4j
public class CnaCommonUrlUtil {

	static Map<String, String> localCachedMap = null;

	@Autowired(required = false)
	private RedissonClient redissonClient;

	/**
	 * 	解决static方法 调用注入对象的方法
	 */
    @Autowired
    private CnaworldCommonUrlProperties commonUrl;

    private static CnaworldCommonUrlProperties commonUrlProperties;

    @PostConstruct
    public void init() {
    	commonUrlProperties = commonUrl;
		initCache();
		CnaLogUtil.info(log,"CnaCommonUrlUtil initialized");
    }

	/**
	 * 初始化缓存
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 */
	private void initCache(){
		if (redissonClient!=null) {
			localCachedMap = redissonClient.getLocalCachedMap("cnaworld:common-url", LocalCachedMapOptions.defaults());
		}else {
			localCachedMap= new HashMap<>();
		}
		initCommonUrl(localCachedMap);
	}

	/**
	 * 初始化缓存
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param localCachedMap 缓存集合
	 */
	private void initCommonUrl(Map<String , String> localCachedMap) {

		if (commonUrlProperties != null &&  commonUrlProperties.getHostName() !=null ) {
			Map<String, CnaworldCommonUrlProperties.HostEntity> hostEntityMap = commonUrlProperties.getHostName();
			if(hostEntityMap != null){
				hostEntityMap.forEach((k, v)->{
					String hostValue=v.getHost();
					Map<String, String> pathMap = v.getPathName();
					if (pathMap != null && pathMap.size()>0) {
						pathMap.forEach((pathName, pathValue) -> {
							String key = k +pathName;
							String value = hostValue+pathValue;
							localCachedMap.put(key, value);
						});
					}else {
						localCachedMap.put(k, k);
					}
				});
			}
		}

	}

	/**
	 * 获取第三方公共地址
	 * 匹配不到抛出运行时异常
	 * @param hostName 地址名称
	 * @return 完整地址
	 */
	public static String getCommonUrl(String hostName) {
		return  getCommonUrl(hostName, null,false);
	}

	/**
	 * 获取第三方公共地址
	 * 匹配不到抛出运行时异常
	 * @param hostName 地址名称
	 * @param pathName 路径名称
	 * @return 完整地址
	 */
	public static String getCommonUrl(String hostName, String pathName) {
		return  getCommonUrl(hostName, pathName,false);
	}


	/**
	 * 获取第三方公共地址
	 * @param hostName 地址名称
	 * @param alwaysReturn true：匹配不到返回null false：匹配不到抛出运行时异常
	 * @return 完整地址
	 */
	public static String getCommonUrl(String hostName , boolean alwaysReturn) {
		return  getCommonUrl(hostName, null,alwaysReturn);
	}

	/**
	 * 获取第三方公共地址
	 * @param hostName 地址名称
	 * @param pathName 路径名称
	 * @param alwaysReturn true：匹配不到返回null false：匹配不到抛出运行时异常
	 * @return 完整地址
	 */
	public static String getCommonUrl(String hostName, String pathName,boolean alwaysReturn) {

		if (StringUtils.isBlank(hostName)) {
			return handleReturn("hostName 不能为空" , alwaysReturn);
		}

        String key = hostName + pathName;
		if(ObjectUtils.isNotEmpty(localCachedMap)){
			if (localCachedMap.containsKey(key)) {
			    return localCachedMap.get(key);
			}
		}

		Map<String, CnaworldCommonUrlProperties.HostEntity> hostEntityMap = commonUrlProperties.getHostName();
		if(hostEntityMap == null){
			return handleReturn("请检查 cnaworld.common-url.host-name 地址配置" , alwaysReturn);
		}
		String urlkey;
		if(hostEntityMap.containsKey(hostName)) {
			StringBuilder urlBuffer=new StringBuilder();
			CnaworldCommonUrlProperties.HostEntity hostEntity = hostEntityMap.get(hostName);
			if (hostEntity !=null && StringUtils.isNotBlank(hostEntity.getHost())) {
				urlkey=hostName;
				urlBuffer.append(hostEntity.getHost());
				if(StringUtils.isNotBlank(pathName)){
					Map<String, String> pathMap = hostEntity.getPathName();
					if(ObjectUtils.isNotEmpty(pathMap)){
						if (pathMap.containsKey(pathName)) {
							String path = pathMap.get(pathName);
							if (StringUtils.isNotBlank(path)) {
								urlkey=urlkey+pathName;
								urlBuffer.append(path);
							}else {
								return handleReturn(" path 配置为空 ：" + pathName, alwaysReturn);
							}
						}else{
							return handleReturn("未匹配到 path-name 配置 ：" + pathName, alwaysReturn);
						}
					}else {
						return handleReturn("请检查 cnaworld.common-url.host-name.path-name 地址配置 : " + hostName, alwaysReturn);
					}
				}
			}else {
				return handleReturn(" host 配置为空 ：" + hostName, alwaysReturn);
			}
			String urlValue = urlBuffer.toString();
			localCachedMap.put(urlkey,urlValue);
			return urlValue;
		} else {
			return handleReturn("未匹配到 host-name 配置 ：" + hostName, alwaysReturn);
		}
	}

	private static String handleReturn(String message, boolean alwaysReturn) {
		if (!alwaysReturn) {
			CnaLogUtil.error(log,message);
			throw new RuntimeException(message);
		}else {
			return null;
		}
	}
}

