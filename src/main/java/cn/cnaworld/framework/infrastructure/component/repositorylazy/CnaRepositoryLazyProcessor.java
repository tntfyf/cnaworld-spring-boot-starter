package cn.cnaworld.framework.infrastructure.component.repositorylazy;

/**
 * 懒加载逻辑执行器
 * @author lenovo
 * @date 2023/6/20
 * @since 1.0.5
 */
public interface CnaRepositoryLazyProcessor {

    Object processing(Object o);

}
