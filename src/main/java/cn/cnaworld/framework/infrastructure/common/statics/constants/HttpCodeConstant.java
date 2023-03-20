package cn.cnaworld.framework.infrastructure.common.statics.constants;

/**
 * HttpCode码
 * @author Lucifer
 * @date 2023/3/10
 * @since 1.0.0
 */
public class HttpCodeConstant {

    /**
     * 操作成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 对象创建成功 已创建。成功请求并创建了新的资源
     */
    public static final Integer CREATED = 201;

    /**
     * 请求已经被接受 已接受。已经接受请求，但未处理完成
     */
    public static final Integer ACCEPTED = 202;

    /**
     * 非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
     */
    public static final Integer  NON_AUTHORITATIVE_INFORMATION = 203;

    /**
     * 操作已经执行成功，但是没有返回数据 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
     */
    public static final Integer NO_CONTENT = 204;

    /**
     * 重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
     */
    public static final Integer RESET_CONTENT = 205;

    /**
     * 部分内容。服务器成功处理了部分GET请求
     */
    public static final Integer PARTIAL_CONTENT = 206;

    /**
     * 多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
     */
    public static final Integer MULTIPLE_CHOICES = 300;

    /**
     * 资源已被移除 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
     */
    public static final Integer MOVED_PERM = 301;

    /**
     * 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
     */
    public static final Integer FOUND = 302;

    /**
     * 重定向 查看其它地址。与301类似。使用GET和POST请求查看
     */
    public static final Integer SEE_OTHER = 303;

    /**
     * 资源没有被修改 未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
     */
    public static final Integer NOT_MODIFIED = 304;

    /**
     * 使用代理。所请求的资源必须通过代理访问
     */
    public static final Integer USE_PROXY = 305;

    /**
     * 已经被废弃的HTTP状态码
     */
    public static final Integer UNUSED = 306;

    /**
     * 临时重定向。与302类似。使用GET请求重定向
     */
    public static final Integer TEMPORARY_REDIRECT = 307;

    /**
     * 参数列表错误（缺少，格式不匹配） 客户端请求的语法错误，服务器无法理解
     */
    public static final Integer BAD_REQUEST = 400;

    /**
     * 未授权 请求要求用户的身份认证
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 保留，将来使用
     */
    public static final Integer PAYMENT_REQUIRED = 402;

    /**
     * 访问受限，授权过期 服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    public static final Integer FORBIDDEN = 403;

    /**
     * 资源，服务未找到 服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
     */
    public static final Integer NOT_FOUND = 404;

    /**
     * 不允许的http方法 客户端请求中的方法被禁止
     */
    public static final Integer BAD_METHOD = 405;

    /**
     * 服务器无法根据客户端请求的内容特性完成请求
     */
    public static final Integer NOT_ACCEPTABLE = 406;

    /**
     * 请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
     */
    public static final Integer PROXY_AUTHENTICATION_REQUIRED = 407;

    /**
     * 服务器等待客户端发送的请求时间过长，超时
     */
    public static final Integer REQUEST_TIME_OUT = 408;

    /**
     * 资源冲突，或者资源被锁 服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突
     */
    public static final Integer CONFLICT = 409;

    /**
     * 客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
     */
    public static final Integer GONE = 410;

    /**
     * 服务器无法处理客户端发送的不带Content-Length的请求信息
     */
    public static final Integer LENGTH_REQUIRED = 411;

    /**
     * 客户端请求信息的先决条件错误
     */
    public static final Integer PRECONDITION_FAILED = 412;

    /**
     * 由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
     */
    public static final Integer REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * 请求的URI过长（URI通常为网址），服务器无法处理
     */
    public static final Integer REQUEST_URI_TOO_LARGE = 414;

    /**
     * 不支持的数据，媒体类型 服务器无法处理请求附带的媒体格式
     */
    public static final Integer UNSUPPORTED_TYPE = 415;

    /**
     * 客户端请求的范围无效
     */
    public static final Integer REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    /**
     * 服务器无法满足Expect的请求头信息
     */
    public static final Integer EXPECTATION_FAILED = 417;
    
    /**
     * 不支持的数据，媒体类型 表示服务器要求请求必须带上条件
     */
    public static final Integer PRECONDITION_REQUIRED = 428;

    /**
     * 系统内部错误 服务器内部错误，无法完成请求
     */
    public static final Integer ERROR = 500;

    /**
     * 接口未实现 服务器不支持请求的功能，无法完成请求
     */
    public static final Integer NOT_IMPLEMENTED = 501;

    /**
     * 作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应
     */
    public static final Integer BAD_GATEWAY = 502;

    /**
     * 由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
     */
    public static final Integer SERVICE_UNAVAILABLE = 503;

    /**
     * 充当网关或代理的服务器，未及时从远端服务器获取请求
     */
    public static final Integer GATEWAY_TIME_OUT = 504;

    /**
     * 服务器不支持请求的HTTP协议的版本，无法完成处理
     */
    public static final Integer HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * 系统出现业务异常
     */
    public static final Integer BUSINESS_ERROR = 1001;

    /**
     * 系统出现断言异常
     */
    public static final Integer ASSERT_ERROR = 1002;

}
