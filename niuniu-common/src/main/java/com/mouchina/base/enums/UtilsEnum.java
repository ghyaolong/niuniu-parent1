package com.mouchina.base.enums;

/**
 * 枚举工具类
 * @author houbinfei
 *
 */
public final class UtilsEnum {
	/**
	 * 通过枚举 key 查询 枚举
	 * @param cls 枚举 Class
	 * @param key 值
	 * @return IEnum
	 */
	public static <T extends IEnum> IEnum enumByKey(Class<T> cls,String key){
		/**
		 * 校验数据
		 */
		if(cls == null){ throw new RuntimeException("Class<T extends IEnum> is null...");}
		
		for (IEnum temp : cls.getEnumConstants()) {
			if(temp.key().equals(key)){
				return temp;
			}
		}
		throw new RuntimeException(String.format("IEnum【%s】 中 不存在 key={1}...", cls.getName(),key));
	}
	
	/**
	 * 通过枚举 signature 查询 枚举
	 * @param cls 枚举 Class
	 * @param signature 值
	 * @return IEnum
	 */
	public static <T extends IEnum> IEnum enumBySignature(Class<T> cls,String signature){
		/**
		 * 校验数据
		 */
		if(cls == null){ throw new RuntimeException("Class<T extends IEnum> is null...");}
		
		for (IEnum temp : cls.getEnumConstants()) {
			if(temp.signature().equals(signature)){
				return temp;
			}
		}
		throw new RuntimeException(String.format("IEnum【%s】 中 不存在 value=%S...", cls.getName(),signature));
	}
	
	/**
	 * 通过枚举 remote 查询 枚举
	 * @param cls 枚举 Class
	 * @param remote 值
	 * @return IEnum
	 */
	public static <T extends ILREnum> IEnum enumByRemote(Class<T> cls,String remote){
		/**
		 * 校验数据
		 */
		if(cls == null){ throw new RuntimeException("Class<T extends IEnum> is null...");}
		
		for (ILREnum temp : cls.getEnumConstants()) {
			if(temp.remote().equals(remote)){
				return temp;
			}
		}
		throw new RuntimeException(String.format("IEnum【%s】 中 不存在 value=%S...", cls.getName(),remote));
	}
	
	/**
	 * 通过远程(remote) 查找 本地(key)
	 * @param cls
	 * @param remote
	 * @param msg 提示消息
	 * @return
	 */
	public static <T extends ILREnum> String keyByRemote(Class<T> cls,final String remote,final String msg){
		try {
			return enumByRemote(cls, remote).key();
		} catch (Exception ex) {
			throw new RuntimeException(String.format(msg, remote));
		}
	}
	
	/**
	 * 本地(key) 查找 通过远程(remote)
	 * @param cls
	 * @param local
	 * @param msg 提示消息
	 * @return
	 */
	public static <T extends ILREnum> String remoteByKey(Class<T> cls,final String key,final String msg){
		try {
			return ((ILREnum)enumByKey(cls, key)).remote();
		} catch (Exception ex) {
			throw new RuntimeException(String.format(msg, key));
		}
	}
}
