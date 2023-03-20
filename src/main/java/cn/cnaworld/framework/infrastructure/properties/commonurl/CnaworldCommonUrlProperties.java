package cn.cnaworld.framework.infrastructure.properties.commonurl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * CommonUrl配置
 * @author Lucifer
 * @date 2023/3/8
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix="cnaworld.common-url")
public class CnaworldCommonUrlProperties {


    private Map<String,HostEntity> hostName;

    @Getter
    @Setter
    @ToString
    public static class HostEntity {

        private String host;

        private Map<String,String> pathName;

    }
}



