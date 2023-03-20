# Spring boot 集成 netty http 客户端
## 1.0.0版本

作用：
1. 提供常用工具组件库，所有工具方法均可直接静态调用，无需注入。

   ```
   CnaNettyHttpClientUtil：提供restful 请求支持及日志打印，本地并发压测，10W并发比CnaHttpClientUtil快2分钟。性能更好建议使用
   
   CnaRedisUtil ：分布式缓存、分布式锁
   
   CnaMd5Util ：MD5加密
   
   CnaAesUtil ：对称加密，提供16进制及BASE64编码格式
   
   CnaHttpClientUtil：提供restful 请求支持及日志打印 ， 提供忽略ssl认证请求方法
   
   CnaCommonUrlUtil ：提供统一地址配置管理，可在yaml进行地址管理，缓存采用redisson LocalCachedMap , 一台客户端修改后，会通过redis订阅发布机制同步到其他客户端，实现去中心化轻量级地址配置中心。
   
   CnaSysConfigUtil ：自定义配置工具，配置到yaml的配置可直接通过静态方法获取。提供获取系统IP，ApplicationName，ProfilesActive等通过工具方法。
   
   CnaCodeUtil ：提供单机的UUID、ID、GUID、雪花ID的生成，其中GUID可生成N位随机字符串，可配置含数字 + 大小写,可以用于任意验证码密码等随机字符生成。
   
   BusinessException：自定义业务异常
   
   HttpCodeConstant：HTTP code码定义
   
   ResponseResult：统一响应包装类
   ```

2. 客户端配置

   pom.xml 引入依赖

   ```xml
   <dependency>
       <groupId>cn.cnaworld.framework</groupId>
       <artifactId>cnaworld-spring-boot-starter</artifactId>
       <version>{latest}</version>
   </dependency>
   ```

3. 调用方式

   ```
   package cn.cnaworld.base;
   
   import cn.cnaworld.base.domain.student.model.dto.StudentWithTeacherlistDto;
   import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulBaseType;
   import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulEntityType;
   import cn.cnaworld.framework.infrastructure.utils.http.netty.CnaNettyHttpClientUtil;
   import org.junit.jupiter.api.Test;
   import org.springframework.boot.test.context.SpringBootTest;
   
   import java.util.HashMap;
   import java.util.Map;
   
   @SpringBootTest
   class CnaworldNettyClientApplicationTests {
   
       @Test
       void nettyClient() {
           Map<String,Object> paramsMap = new HashMap<>();
           Map<String,String> headerMap = new HashMap<>();
           StudentWithTeacherlistDto studentWithTeacherlistDto = new StudentWithTeacherlistDto();
           studentWithTeacherlistDto.setStudentId(0L);
           studentWithTeacherlistDto.setStudentName("张三");
           studentWithTeacherlistDto.setTeacherId(0L);
           //可发送GET、DELETE请求默认为GET
           String result = CnaNettyHttpClientUtil.send("http://127.0.0.1:8080/student/list");
           //全参发送方式
           result = CnaNettyHttpClientUtil.send("http://127.0.0.1:8080/student/list",paramsMap,headerMap,RestFulBaseType.GET);
           //可发送POST、PUT、PATCH请求默认为POST
           result = CnaNettyHttpClientUtil.sendEntity("http://127.0.0.1:8080/student/StudentAndTeacherlist",studentWithTeacherlistDto);
           //全参发送方式
           result = CnaNettyHttpClientUtil.sendEntity("http://127.0.0.1:8080/student/StudentAndTeacherlist",studentWithTeacherlistDto,paramsMap,headerMap,RestFulEntityType.POST);
       }
   }
   ```

4. 性能对比

   测试环境：win10 64位、i7-1065G7  1.30GHz 、16G内存 

   两个工具分别请求同一个接口，接口包含一个单表查询。

   并发10个线程，每个线程轮询请求10000次，共计100000次统计结果如下。万次请求平均快了12秒，10万次请求快了120秒。

   可参考源码调整微服务间resttemplate、feign的底层http客户端实现。

   | TPS/毫秒     | CnaNettyHttpClientUtil | CnaHttpClientUtil | 对比         |
   | ------------ | ---------------------- | ----------------- | ------------ |
   | 10000        | 61583                  | 74598             | 13015        |
   | 10000        | 61783                  | 75116             | 13333        |
   | 10000        | 61972                  | 75260             | 13288        |
   | 10000        | 62032                  | 75439             | 13407        |
   | 10000        | 62072                  | 75540             | 13468        |
   | 10000        | 63232                  | 75899             | 12667        |
   | 10000        | 63549                  | 75929             | 12380        |
   | 10000        | 63577                  | 75984             | 12407        |
   | 10000        | 63610                  | 76021             | 12411        |
   | 10000        | 63625                  | 76080             | 12455        |
   | 合计：100000 | 合计：627035           | 合计：755866      | 合计：128831 |
