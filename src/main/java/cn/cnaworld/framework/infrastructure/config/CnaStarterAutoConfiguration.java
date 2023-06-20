package cn.cnaworld.framework.infrastructure.config;


import cn.cnaworld.framework.infrastructure.properties.commonurl.CnaworldCommonUrlProperties;
import cn.cnaworld.framework.infrastructure.properties.repositorylazy.CnaworldRepositoryLazyProperties;
import cn.cnaworld.framework.infrastructure.utils.resources.CnaCommonUrlUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 自动装配类
 * @author Lucifer
 * @date 2023/2/10
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties({CnaworldCommonUrlProperties.class, CnaworldRepositoryLazyProperties.class})
@Import(value = {CnaCommonUrlUtil.class})
public class CnaStarterAutoConfiguration {}