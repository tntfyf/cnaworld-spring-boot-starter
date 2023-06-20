package cn.cnaworld.framework.infrastructure.utils.bean;

import org.springframework.context.ApplicationContext;

/**
 * spring bean 加载工具
 * @author Lucifer
 * @date 2023/5/28
 * @since 1.0.5
 */

public class CnaSpringBeanUtil {

    private static ApplicationContext context;

    public static void set(ApplicationContext  applicationContext) {
        context = applicationContext;
    }

    /**
     * 通过字节码获取
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * 通过BeanName获取
     */
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 通过beanName和字节码获取
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }

}
