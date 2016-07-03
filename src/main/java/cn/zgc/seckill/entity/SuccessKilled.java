package cn.zgc.seckill.entity;

import java.util.Date;

/**
 * 秒杀成功实体
 * @author zgc
 */
public class SuccessKilled {
	private long seckillId;

	private long userPhone;

	private short state;

	private Date creteTime;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Date getCreteTime() {
		return creteTime;
	}

	public void setCreteTime(Date creteTime) {
		this.creteTime = creteTime;
	}
	
	
}
