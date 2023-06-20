package cn.cnaworld.framework.infrastructure.component.repositorylazy.annotation;



import cn.cnaworld.framework.infrastructure.component.repositorylazy.CnaRepositoryLazyProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仓储懒加载注解
 * @author Lucifer
 * @date 2023/6/20
 * @since 1.0.5
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CnaLazy {

   Class<? extends CnaRepositoryLazyProcessor> LazyProcessor();

}

