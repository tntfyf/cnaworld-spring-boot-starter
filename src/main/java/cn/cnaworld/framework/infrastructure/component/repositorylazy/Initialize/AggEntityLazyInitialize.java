package cn.cnaworld.framework.infrastructure.component.repositorylazy.Initialize;

import cn.cnaworld.framework.infrastructure.component.repositorylazy.annotation.CnaLazy;
import cn.cnaworld.framework.infrastructure.properties.repositorylazy.CnaworldRepositoryLazyProperties;
import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 启动时缓存聚合中被懒加载注解注释的实体
 * @author Lucifer
 * @date 2023/6/20
 * @since 1.0
 */
@Slf4j
public class AggEntityLazyInitialize implements ApplicationRunner {
    @Autowired
    private CnaworldRepositoryLazyProperties cnaworldRepositoryLazyProperties;

    /**
     * 聚合中被懒加载注解注释的实体
     */
    public static Map<String,Map<String,String>> aggLazyEntityHashMap  = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CnaLogUtil.info(log,"cnaworld repository lazy start");
        if(!cnaworldRepositoryLazyProperties.isEnable()){
            CnaLogUtil.debug(log,"cnaworld repository lazy enable is false");
            return;
        }
        if(StringUtils.isBlank(cnaworldRepositoryLazyProperties.getAggPackage())){
            CnaLogUtil.debug(log,"cnaworld repository lazy aggPackage not configured");
            return;
        }
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String path = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+cnaworldRepositoryLazyProperties.getAggPackage()+"/*.class";
        Resource[] resources = resourcePatternResolver.getResources(path);
        MetadataReaderFactory metadata = new SimpleMetadataReaderFactory();
        if (ObjectUtils.isNotEmpty(resources)){
            for (Resource resource : resources) {
                MetadataReader metadataReader = metadata.getMetadataReader(resource);
                ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                sbd.setResource(resource);
                sbd.setSource(resource);
                beanDefinitions.add(sbd);
            }
            if(ObjectUtils.isNotEmpty(beanDefinitions)){
                for (BeanDefinition beanDefinition : beanDefinitions) {
                    String classname = beanDefinition.getBeanClassName();
                    Class<?> aClass = Class.forName(classname);
                    Field[] declaredFields = aClass.getDeclaredFields();
                    Map<String,String> fieldsMap = new HashMap<>();
                    for (Field declaredField:declaredFields){
                        Annotation annotation = declaredField.getAnnotation(CnaLazy.class);
                        if (annotation!=null){
                            String methodName = "get" + declaredField.getName().substring(0,1).toUpperCase()+declaredField.getName().substring(1);
                            fieldsMap.put(methodName,declaredField.getName());
                            aggLazyEntityHashMap.put( aClass.getName(),fieldsMap);
                            CnaLogUtil.info(log,"cnaworld repository lazy {} success" , declaredField.getName());
                        }
                    }
                }
            }
        }
        CnaLogUtil.info(log,"cnaworld repository lazy initialized");
    }
}
