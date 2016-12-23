package com.mouchina.moumou_server_interface.system;

import java.util.List;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.system.SystemMessage;

public interface SystemMessageService {
	/**
	 * 发送系统消息
	 */
	int insertSystemMessage(SystemMessage systemMessage);

	/**
	 * 查询系统消息浏览记录
	 * 
	 * @param id
	 * @return
	 */
	List<SystemMessage>  selectSystemMessageByUserId(SystemMessage systemMessage);

	/**
	 * 保存阅读系统消息用户记录
	 * @return
	 */
	int insertUserReadMessage(SystemMessage systemMessage);
	/**
	 * 获取用户所有未读系统消息
	 * @param user
	 * @return
	 */
	List<SystemMessage> selectUserNotReadMessage(User user);
	/**
	 * 获取用户已读系统消息
	 * @param user
	 * @return
	 */
	List<SystemMessage> selectUserReadMessage(User user);
	/**
	 * 查询所有系统消息
	 */
	List<SystemMessage> selectAllMessage(Long userId);
	/**
	 * 修改用户浏览记录
	 * @param systemMessage
	 * @return
	 */
	int updateUserReadMessage(SystemMessage systemMessage);
}
