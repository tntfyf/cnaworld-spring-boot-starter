package cn.cnaworld.framework.infrastructure.component.repositorylazy.cglib;

import cn.cnaworld.framework.infrastructure.component.repositorylazy.CnaRepositoryLazyProcessor;
import cn.cnaworld.framework.infrastructure.component.repositorylazy.Initialize.AggEntityLazyInitialize;
import cn.cnaworld.framework.infrastructure.component.repositorylazy.annotation.CnaLazy;
import cn.cnaworld.framework.infrastructure.utils.bean.CnaBeanCopierUtil;
import cn.cnaworld.framework.infrastructure.utils.cglib.AggEntityLazyFactory;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 仓储懒加载代理处理器
 * @author Lucifer
 * @date 2023/6/20
 * @since 1.0.5
 */
@Slf4j
public class LazyProcessInvocation implements MethodInterceptor {

    /**
     *
     *设置过期时间为30分钟
     *ExpirationPolicy.CREATED)
     *设置过期策略为创建或更新值后
     */
   static ExpiringMap<String, String> onlyOneCallMap = ExpiringMap.builder().maxSize(100000).expiration(30, TimeUnit.MINUTES)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .build();

    /**
     * All generated proxied methods call this method instead of the original method.
     * The original method may either be invoked by normal reflection using the Method object,
     * or by using the MethodProxy (faster).
     *
     * @param obj    "this", the enhanced object
     * @param method intercepted Method
     * @param args   argument array; primitive types are wrapped
     * @param proxy  used to invoke super (non-intercepted method); may be called
     *               as many times as needed
     * @return any value compatible with the signature of the proxied method. Method returning void will ignore this value.
     * @throws Throwable any exception may be thrown; if so, super method will not be invoked
     * @see MethodProxy
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class<?> proxyClass = obj.getClass();
        String hashCode = obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        //隐式调用直接放行
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if(method.getName().startsWith("set")
                || "toString".equals(method.getName())
                || "hashCode".equals(method.getName())
                || "toString".equals(stackTrace[2].getMethodName())
                || "cn.cnaworld.framework.infrastructure.utils.bean.CnaBeanCopierUtil".equals(stackTrace[3].getClassName())
                || onlyOneCallMap.containsKey(hashCode)){
            return proxy.invokeSuper(obj,args);
        }

        //找到被代理类
        Class<?> userClass = ClassUtils.getUserClass(proxyClass);
        //得到懒加载实体缓存
        //判断是否命中实体
        if (AggEntityLazyInitialize.aggLazyEntityHashMap.containsKey(userClass.getName())) {
            //获得属性缓存
            Map<String, String> fieldsMap = AggEntityLazyInitialize.aggLazyEntityHashMap.get(userClass.getName());
            //判断是否命中属性
            if(fieldsMap.containsKey(method.getName())){
                //根据方法名称拿到缓存的属性
                //使用实体通过注解拿到懒加载业务处理类
                //通过反射修改属性值
                //修改前先判断属性是否为空，为空进行懒加载
                String fieldName = fieldsMap.get(method.getName());
                Field currentField = userClass.getDeclaredField(fieldName);
                currentField.setAccessible(true);
                if(ObjectUtils.isEmpty(currentField.get(obj))){
                    CnaLazy cnaLazy = currentField.getAnnotation(CnaLazy.class);
                    if(ObjectUtils.isNotEmpty(cnaLazy)) {
                        Class<? extends CnaRepositoryLazyProcessor> lazyProcessor = cnaLazy.LazyProcessor();
                        Object resource= lazyProcessor.newInstance().processing(obj);
                        onlyOneCallMap.put(hashCode,"");
                        Object result = resource;
                        if(ObjectUtils.isNotEmpty(result)){
                            Field[] sonFields = result.getClass().getDeclaredFields();
                            boolean sonFieldsLazy=false;
                            for(Field sonField:sonFields){
                                if(ObjectUtils.isNotEmpty(sonField.getAnnotation(CnaLazy.class))){
                                    sonFieldsLazy=true;
                                    break;
                                }
                            }
                            if (sonFieldsLazy){
                                result = AggEntityLazyFactory.initAggEntity(result.getClass());
                                CnaBeanCopierUtil.copy(resource, result);
                            }
                            currentField.set(obj,result);
                        }
                    }
                }
                currentField.setAccessible(false);
            }
        }
        return proxy.invokeSuper(obj,args);
    }
}
