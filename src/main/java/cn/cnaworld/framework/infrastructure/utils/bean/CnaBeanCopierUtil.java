package cn.cnaworld.framework.infrastructure.utils.bean;

import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * bean拷贝工具
 * @author Lucifer
 * @date 2023/5/28
 * @since 1.0.5
 */
@Slf4j
public class CnaBeanCopierUtil {

    // 创建一个map来存储BeanCopier
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * List深拷贝 List对象->类
     *
     * @param sourceList 源集合
     * @param targetClass 目标集合
     * @param <S> 源对象
     * @param <T> 目标对象
     * @return List<T> 目标集合
     */
    public static <S,T> List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        // 判断空指针异常
        if (ObjectUtils.isEmpty(sourceList)){
            return null;
        }
        return sourceList.stream().map(src -> copy(src, targetClass)).collect(Collectors.toList());

    }

    /**
     * List深拷贝 List对象->类
     *
     * @param sourceList 源集合
     * @param targetClass 目标集合
     * @param <S> 源对象
     * @param <T> 目标对象
     * @return List<T> 目标集合
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass , Converter converter) {
        // 判断空指针异常
        if (ObjectUtils.isEmpty(sourceList)){
            return null;
        }
        return sourceList.stream().map(src -> copy(src, targetClass,converter)).collect(Collectors.toList());

    }

    /**
     * 深拷贝 对象->类
     *
     * @param source 源对象
     * @param targetClass 目标对象类
     * @param <T> 目标对象
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        return copy(source,targetClass,null);
    }


    /**
     * 深拷贝 对象->类
     *
     * @param source 源对象
     * @param targetClass 目标对象类
     * @param <T> 目标对象
     * @param converter 转换器
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> targetClass , Converter converter) {
        // 用来判断空指针异常
        Objects.requireNonNull(targetClass);
        T dest = null;
        try {
            dest = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
           CnaLogUtil.error(log,"对象实例化异常",e);
           throw new RuntimeException("对象实例化异常");
        }
        copy(source, dest ,converter);
        return dest;
    }

    /**
     * 深拷贝 对象->对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copy(Object source, Object target) {
        copy(source,target,null);
    }


    /**
     * 深拷贝 对象->对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param converter 转换器
     */
    public static void copy(Object source, Object target, Converter converter) {
        if (source == null) {
            return;
        }
        // 用来判断空指针异常
        Objects.requireNonNull(target);
        String key = getKey(source, target , converter);
        BeanCopier beanCopier;
        // 判断键是否存在，不存在就将BeanCopier插入到map里，存在就直接获取
        if (!BEAN_COPIER_MAP.containsKey(key)) {
            if (converter!=null){
                beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
            }else {
                beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            }
            BEAN_COPIER_MAP.put(key, beanCopier);
        } else {
            beanCopier = BEAN_COPIER_MAP.get(key);
        }
        if (converter!=null){
            beanCopier.copy(source, target, converter);
        }else {
            beanCopier.copy(source, target, null);
        }
    }

    /**
     * 获取Map Key
     *
     * @param source 源对象
     * @param target 目标对象
     * @return Key
     */
    private static String getKey(Object source, Object target , Converter converter) {
        if (converter != null) {
            return source.getClass().getName() + target.getClass().getName() + converter.getClass().getName();
        }else {
            return source.getClass().getName() + target.getClass().getName();
        }
    }

}
