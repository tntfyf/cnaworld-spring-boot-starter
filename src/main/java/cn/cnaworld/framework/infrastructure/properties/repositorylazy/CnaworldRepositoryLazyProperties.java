package cn.cnaworld.framework.infrastructure.properties.repositorylazy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 懒加载属性
 * @author Lucifer
 * @date 2023/6/20
 * @since 1.0.5
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix="cnaworld.repository-lazy")
public class CnaworldRepositoryLazyProperties {

    /**
     * 默认开启
     */
    private boolean enable = true;

    /**
     * 聚合目录
     */
    private String aggPackage;

}



