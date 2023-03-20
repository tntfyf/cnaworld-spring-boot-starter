package cn.cnaworld.framework.infrastructure.utils.http;

import cn.cnaworld.framework.infrastructure.common.statics.constants.HttpCodeConstant;
import cn.cnaworld.framework.infrastructure.common.statics.constants.HttpStatusMessageConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 通用返回结果包装类
 * @author Lucifer
 * @date 2023/3/10
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class ResponseResult<T> {
	
	private Integer code;
	
	private String message;
	
	private T data;
 
    public ResponseResult(Integer code, String message) {
    	this.code=code;
    	this.message=message;
    }

    public ResponseResult(Integer code, String message, T data) {
    	this.code=code;
    	this.message=message;
        if (ObjectUtils.isNotEmpty(data)) {
        	this.data=data;
        }
   }

    /**
     * 成功
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(){
        return new ResponseResult<>(HttpCodeConstant.SUCCESS, HttpStatusMessageConstant.SUCCESS_MESSAGE);
    }

    /**
     * 成功
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param message 信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(String message){
        return new ResponseResult<>(HttpCodeConstant.SUCCESS, message);
    }
    
    /**
     * 成功
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(HttpCodeConstant.SUCCESS, HttpStatusMessageConstant.SUCCESS_MESSAGE, data);
    }

    /**
     * 成功
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param message 信息
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(HttpCodeConstant.SUCCESS, message, data);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error() {
        return new ResponseResult<>(HttpCodeConstant.ERROR, HttpStatusMessageConstant.FAILED_MESSAGE);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param message 信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(HttpCodeConstant.ERROR, message);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(T data) {
        return new ResponseResult<>(HttpCodeConstant.ERROR, HttpStatusMessageConstant.FAILED_MESSAGE,data);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param code 响应码
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(Integer code) {
        return new ResponseResult<>(code, HttpStatusMessageConstant.FAILED_MESSAGE);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param code 响应码
     * @param message 信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code,message);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param message 信息
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(String message,T data) {
        return new ResponseResult<>(HttpCodeConstant.ERROR, message,data);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param code 响应码
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(Integer code,T data) {
        return new ResponseResult<>(code, HttpStatusMessageConstant.FAILED_MESSAGE,data);
    }

    /**
     * 失败
     * @author Lucifer
     * @date 2023/3/10
     * @since 1.0.0
     * @param code 响应码
     * @param message 信息
     * @param data 响应体
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(Integer code, String message, T data) {
        return new ResponseResult<>(code,message,data);
    }

}
