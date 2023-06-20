package cn.cnaworld.framework.infrastructure.utils.cglib;

import cn.cnaworld.framework.infrastructure.component.repositorylazy.cglib.LazyProcessInvocation;
import net.sf.cglib.proxy.Enhancer;

/**
 * 初始化聚合实体代理对象
 * @author Lucifer
 * @date 2023/6/20
 * @since 1.0.5
 */
public class AggEntityLazyFactory {

    @SuppressWarnings("unchecked")
    public static  <T>  T initAggEntity(Class<T> clazz){
        // 通过CGLIB动态代理获取代理对象的过程
        // 创建Enhancer对象，类似于JDK动态代理的Proxy类
        Enhancer enhancer = new Enhancer();
        // 设置目标类的字节码文件
        enhancer.setSuperclass(clazz);
        // 设置回调函数
        enhancer.setCallback(new LazyProcessInvocation());
        // create方法正式创建代理类
        return (T) enhancer.create();
    }

}
