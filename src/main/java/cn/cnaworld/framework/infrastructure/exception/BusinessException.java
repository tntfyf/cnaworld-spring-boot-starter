/**
 * 
 */
package cn.cnaworld.framework.infrastructure.exception;

/**
 * 业务异常
 * @author Lucifer
 * @date 2023/3/9
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public BusinessException() {
        super();
    }
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
    }
}
