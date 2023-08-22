# cnaworld spring boot starter 业务核心组件工具库
作用：
1. 集成mybatis-plus 、redis 、aop 、 log 组件库

   ```xml
   <!--详细用法，请参见：https://github.com/tntfyf/cnaworld-mybatis-plus-->
   <dependency>
             <groupId>cn.cnaworld.framework</groupId>
             <artifactId>mybatis-plus</artifactId>
             <version>{latest}</version>
   </dependency>
   
   <!--详细用法，请参见：https://github.com/tntfyf/cnaworld-redis-->
   <dependency>
            <groupId>cn.cnaworld.framework</groupId>
            <artifactId>redis</artifactId>
            <version>{latest}</version>
   </dependency>
   
   <!--详细用法，请参见：https://github.com/tntfyf/cnaworld-aop-->
   <dependency>
             <groupId>cn.cnaworld.framework</groupId>
             <artifactId>aop</artifactId>
             <version>{latest}</version>
   </dependency>
   ```
   
2. 提供常用工具组件库，所有工具方法均可直接静态调用，无需注入。

   ```
   CnaRedisUtil ： 分布式缓存、分布式锁
   
   CnaLogUtil ： 可针对包和类切换打印等级日志
   
   CnaMd5Util ： MD5加密
   
   CnaAesUtil ： 对称加密，提供16进制及BASE64编码格式
   
   CnaHttpClientUtil：提供restful 请求支持及日志打印 ， 提供忽略ssl认证请求方法
   
   CnaNettyHttpClientUtil：提供restful 请求支持及日志打印 ， 本地并发压测，10W并发比CnaHttpClientUtil快2分钟。性能更好建议使用
   
   CnaCommonUrlUtil ：提供统一地址配置管理，可在yaml进行地址管理，缓存采用redisson LocalCachedMap , 一台客户端修改后，会通过redis订阅发布机制同步到其他客户端，实现去中心化轻量级地址配置中心。
   
   CnaSysConfigUtil ：自定义配置工具，配置到yaml的配置可直接通过静态方法获取。提供获取系统IP，ApplicationName，ProfilesActive等通过工具方法。
   
   CnaCodeUtil ：提供单机的UUID、ID、GUID、雪花ID的生成，其中GUID可生成N位随机字符串，可配置含数字 + 大小写,可以用于任意验证码密码等随机字符生成。
   
   BusinessException：自定义业务异常
   
   HttpCodeConstant：HTTP code码定义
   
   ResponseResult：统一响应包装类
    
   @CnaLazy ： 聚合懒加载
   
   ```

3. 客户端配置

   pom.xml 引入依赖

   ```xml
   <dependency>
       <groupId>cn.cnaworld.framework</groupId>
       <artifactId>cnaworld-spring-boot-starter</artifactId>
       <version>{latest}</version>
   </dependency>
   ```

   application.yml 配置

   ```yaml
   cnaworld:
     common-url: #通用地址
       host-name: #地址名称集合
         baidu: #自定义地址名称
           host: "https://www.baidu.com" #自定义地址
           path-name: #路径名称
             query: "/s?wd=enum" #自定义路径query
             view: "/v?wd=enum" #自定义路径view
     system-config:
       config-name:
         sever-code: "test-core"  #自定义配置 #通用配置
     log: #控制各个模块的日志等级
       log-properties:
         - path-name: cn.cnaworld.base.domain.student.controller
           log-level: error
         - path-name: cn.cnaworld.base.domain.student
           log-level: warn
         - path-name: cn.cnaworld.base.domain.student.controller.StudentController
           log-level: error
         - path-name: cn.cnaworld.framework.infrastructure.config
           log-level: warn
     mybatis-plus:
       auto-field-encrypt: true #使用对象进行CRUD时，若字段上存在@CnaFieldEncrypt注解则会自动加密解密
       auto-insert-fill: true #数据初始化时默认填充，insert时，自动获取fill-strategy-field 中的属性进行填充。
       function-extension: true #扩展逻辑删除相关方法，提供逻辑恢复和直接删除扩展方法。默认true开启。
       snow-flake: true #提供16位雪花ID实现。默认true开启。
       optimistic-locker: true #提供乐观锁实现 OptimisticLockerInnerInterceptor。默认true开启。
       update-optimistic-locker-field: true #数据更新时对乐观锁字段累增后填充。默认true开启。
       fill-strategy-field: #数据初始化时默认填充属性集合
         - field-name: "createTimeDb" #需填充的entity字段名称，需要注解 @TableField(value = "create_time_db",fill = FieldFill.INSERT) 中开启fill = FieldFill.INSERT
           field-class: java.time.LocalDateTime #默认填充字段类型，Date 、Timestamp 、 LocalDateTime 默认取当前时间，Long 、Integer 默认取0。
         - field-name: "updateTimeDb"
           field-class: java.time.LocalDateTime #填充值处理器，实现FieldProcessor的getFieldValue方法，返回值作为属性填充值。
         - field-name: "deletedDb"
           field-value: false #填充值    
       field-encrypt: #使用对象进行CRUD时，若字段上存在@CnaFieldEncrypt注解则会自动加密解密
         algorithm: aes #String 类型字段加密算法
         keys: Ssadasa123dsfsda21sdasd #加密密钥
         encrypt-algorithm-processor: cn.cnaworld.framework.infrastructure.component.mybatisplus.processor.stringprocessor.impl.AESEncryptProcessor #自定义实现处理器 
     aop: 
       #@CnaAopLog默认实现开关，默认为true,可关闭。
       #若切面列表出现@CnaAopLog默认表达式，则以下方配置为准，但依然受此开关影响
       default-enable: true
       #切面列表
       properties:
         #配置切面表达式语法可以切包，方法等 根据业务开启环绕处理器和异常处理器
         - execution: "execution(* cn.cnaworld.framework..*.*Controller.*(..))"
           #默认logback实现 可不配置
           processor-class: cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor
           #默认CnaworldAopSlf4jProcessor实现日志等级默认为info级别,自定义实现无效
           log-level: info
           #前置处理器开关配置，默认为true 开启
           pre-processor: true
           #后置处理器开关配置，默认为true 开启
           post-processor: true
           #异常处理器开关配置，默认为true 开启
           error-processor: true
           #环绕处理器开关配置，默认为true 开启
           around-processor: true
           #配置注解方式支持配置注解切面，可使用自定义注解 根据业务开启环绕处理器和异常处理器
         - execution: "@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)"
           #自定义本地数据库实现
           processor-class: cn.cnaworld.framework.infrastructure.component.operatelog.CnaworldAopOperateLogProcessor
           #前置处理器开关关闭
           pre-processor: false
           #后置处理器开关关闭
           post-processor: false        
     redis:
       enable: true #默认为true ，false 将关闭CnaRedisUtil实例创建
     repository-lazy: #DDD聚合中聚合根与实体，实体与实体之间的懒加载查询
       enable: true #总开关
       agg-package: "cn/cnaworld/base/domain/*/model/*" #聚合包路径
   ```

4. 项目启动时进行注册

   ```lua
   [2023-03-21 21 01:18:49.679] - [main] - [INFO] - [c.c.f.i.u.l.CnaLogUtil                   ]- [CnaLogUtil.java               :77]  : cnaworld log 包路径:cn.cnaworld.base.domain.student.controller 日志等级调整为 : ERROR 
   [2023-03-21 21 01:18:49.680] - [main] - [INFO] - [c.c.f.i.u.l.CnaLogUtil                   ]- [CnaLogUtil.java               :77]  : cnaworld log 包路径:cn.cnaworld.base.domain.student 日志等级调整为 : WARN 
   [2023-03-21 21 01:18:49.680] - [main] - [INFO] - [c.c.f.i.u.l.CnaLogUtil                   ]- [CnaLogUtil.java               :77]  : cnaworld log 包路径:cn.cnaworld.base.domain.student.controller.StudentController 日志等级调整为 : ERROR 
   [2023-03-21 21 01:18:49.682] - [main] - [INFO] - [f.i.c.CnaworldAopBeanFactoryPostProcessor]- [CnaLogUtil.java               :160]  : cnaworld aop register start
   [2023-03-21 21 01:18:49.694] - [main] - [INFO] - [f.i.c.CnaworldAopBeanFactoryPostProcessor]- [CnaLogUtil.java               :160]  : cnaworld aop register @annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog) success
   [2023-03-21 21 01:18:49.694] - [main] - [INFO] - [f.i.c.CnaworldAopBeanFactoryPostProcessor]- [CnaLogUtil.java               :160]  : cnaworld aop register initialized
   [2023-03-21 21 01:18:51.461] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ]- [CnaLogUtil.java               :160]  : cnaworld mybatis-plus optimistic-locker initialized
   [2023-03-21 21 01:18:51.467] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ]- [CnaLogUtil.java               :160]  : cnaworld mybatis-plus update-optimistic-locker-field initialized
   [2023-03-21 21 01:18:51.506] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ]- [CnaLogUtil.java               :160]  : cnaworld mybatis-plus auto-insert-fill initialized
   [2023-08-22 22 16:41:48.210] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ] - [MybatisPlusConfig.java        :57]  : cnaworld mybatis-plus auto-field-encrypt initialized
   [2023-03-21 21 01:18:51.519] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ]- [CnaLogUtil.java               :160]  : cnaworld mybatis-plus extend method initialized
   [2023-03-21 21 01:18:51.526] - [main] - [INFO] - [c.c.f.i.c.m.c.MybatisPlusConfig          ]- [CnaLogUtil.java               :160]  : cnaworld mybatis-plus 16-snowflake initialized
   [2023-03-21 21 01:26:08.784] - [main] - [INFO] - [c.c.f.i.u.l.CnaLogUtil                   ]- [CnaLogUtil.java               :35]  : CnaLogUtil initialized
   [2023-03-21 21 01:26:08.788] - [main] - [INFO] - [c.c.f.i.u.r.CnaSysConfigUtil             ]- [CnaLogUtil.java               :160]  : CnaSysConfigUtil initialized
   [2023-03-21 21 01:26:08.869] - [main] - [INFO] - [c.c.f.i.u.r.CnaCommonUrlUtil             ]- [CnaLogUtil.java               :160]  : CnaCommonUrlUtil initialized
   [2023-03-21 21 01:26:08.901] - [main] - [INFO] - [c.c.f.i.u.r.CnaRedisUtil                 ]- [CnaLogUtil.java               :160]  : CnaRedisUtil initialized
   [2023-06-20 20 15:00:08.371] - [main] - [INFO] - [c.c.f.i.c.r.I.AggEntityLazyInitialize    ] - [AggEntityLazyInitialize.java  :46]  : cnaworld repository lazy start
   [2023-06-20 20 15:00:08.378] - [main] - [INFO] - [c.c.f.i.c.r.I.AggEntityLazyInitialize    ] - [AggEntityLazyInitialize.java  :80]  : cnaworld repository lazy goodsExt success
   [2023-06-20 20 15:00:08.379] - [main] - [INFO] - [c.c.f.i.c.r.I.AggEntityLazyInitialize    ] - [AggEntityLazyInitialize.java  :80]  : cnaworld repository lazy goods success
   [2023-06-20 20 15:00:08.379] - [main] - [INFO] - [c.c.f.i.c.r.I.AggEntityLazyInitialize    ] - [AggEntityLazyInitialize.java  :86]  : cnaworld repository lazy initialized
   ```

5. 调用方式

   ```java
   //获取自定义地址 baidu
   String baidu = CnaCommonUrlUtil.getCommonUrl("baidu");
   //获取自定义地址 baidu + query
   String baiduQuery = CnaCommonUrlUtil.getCommonUrl("baidu","query");
   //获取自定义配置 sever-code
   String severCode = CnaSysConfigUtil.getCnaConfigByName("sever-code");  
   
   //其他组件使用可跳转至github查阅
   
   ```

6. 聚合懒加载使用样例

   ```java
   public class Order extends OrdersPo {
       //聚合根中为实体加入懒加载注解，LazyProcessor为必填项，需要指名懒加载逻辑的执行器
       @CnaLazy(LazyProcessor = OrderLazy.LazyGetGoods.class)
       private Goods goods;
   }
   ```

   ```java
   public class Goods extends GoodsPo implements Serializable {
       //实体中也可以懒加载实体
       @CnaLazy(LazyProcessor = OrderLazy.LazyGetGoodsExt.class)
       private GoodsExt goodsExt;
   
   }
   ```

   ```java
   @Service
   @Transactional
   public class OrderRepositoryImpl implements OrderRepository {
       //仓储实现类或者工厂类中使用懒加载方式
       public Order getOrderLazy(Long orderId) {
           //仓储实际调用ORM框架进行查询
           OrdersPo ordersPo = iOrdersPoService.getById(orderId);
           //使用懒加载对象工厂生成代理类，使用代理对象可以触发懒加载，若不需要懒加载，可以忽略此行代码
           Order order = AggEntityLazyFactory.initAggEntity(Order.class);
           CnaBeanCopierUtil.copy(ordersPo, order);
           return order;
   
        }
   }
   
   ```

   ```java
   //懒加载逻辑的执行器包装类
   public class OrderLazy {
       //懒加载实际逻辑执行器内部类，一个聚合下的建议写到一个包装类中，实际执行器需要实现CnaRepositoryLazyProcessor接口
       //聚合根懒加载实体
       public static class LazyGetGoods implements CnaRepositoryLazyProcessor {
           //懒加载遵循调用类事务
           @Override
           public Object processing(Object o) {
               //懒加载业务逻辑查询
               IGoodsPoService iGoodsPoService= CnaSpringBeanUtil.getBean(IGoodsPoService.class);
               Order order=(Order) o;
               if(ObjectUtils.isNotEmpty(order.getOrderId())){
                   QueryWrapper<GoodsPo> queryWrapper = new QueryWrapper<>();
                   queryWrapper.eq("goods_order_id",order.getOrderId());
                   GoodsPo goodsPo = iGoodsPoService.getOne(queryWrapper);
                   if(ObjectUtils.isNotEmpty(goodsPo)){
                       //PO到DO转换，返回聚合需要的DO实体
                       return CnaBeanCopierUtil.copy(goodsPo, Goods.class);
                   }
               }
               //懒加载查询一次后，不管是否结果为空都不在进行查询
               return null;
           }
        //实体懒加载实体
        public static class LazyGetGoodsExt implements CnaRepositoryLazyProcessor {
           //懒加载遵循调用类事务
           @Override
           public Object processing(Object o) {
               IGoodsExtPoService iGoodsExtPoService= CnaSpringBeanUtil.getBean(IGoodsExtPoService.class);
               Goods goods=(Goods) o;
               if(ObjectUtils.isNotEmpty(goods.getGoodsId())){
                   QueryWrapper<GoodsExtPo> queryWrapper = new QueryWrapper<>();
                   queryWrapper.eq("goods_id",goods.getGoodsId());
                   GoodsExtPo goodsExtPo = iGoodsExtPoService.getOne(queryWrapper);
                   if(ObjectUtils.isNotEmpty(goodsExtPo)){
                       //PO到DO转换
                       return CnaBeanCopierUtil.copy(goodsExtPo, GoodsExt.class);
                   }
               }
               return null;
           }
       }
     }
   ```

​		 
