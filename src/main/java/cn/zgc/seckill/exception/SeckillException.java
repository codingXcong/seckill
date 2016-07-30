package cn.zgc.seckill.exception;

/**
 * Spring声明式事务，RuntimeException才能使事务回滚
 */
public class SeckillException extends RuntimeException{
	public SeckillException(String msg){
		super(msg);
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
