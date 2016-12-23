package com.mouchina.web.controller.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;

@Controller
@RequestMapping("/user/userComment")
public class UserCommentController {
	
	@Resource
	UserCommentService userCommentService;
	
	@Resource
	AdvertService advertService;
	
	@Resource
	UserWebService userWebService;
	
//	@RequestMapping(value="/list")
//	public String getUserCommentBySourceId(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
//		
//		String sourceId=request.getParameter("advertId");//用户评论表的sourceId
//		String loginKey=request.getParameter("loginKey");
//		
//		if(StringUtil.isEmpty(sourceId)){
//			modelMap.put("result", "0");
//			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
//			return "";
//		}
//		
//		if(StringUtil.isEmpty(loginKey)){
//			modelMap.put("result", "0");
//			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
//			return "";
//		}
//		
//		User user = userWebService.getUserByLoginKey(loginKey);
//		if(user == null){
//			modelMap.put("result", Constants.ERROR_ERROR);
//			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
//			return "";
//		}
//		
//		Integer begin = 0;
//		String beginStr = request.getParameter("begin");
//		if (beginStr != null && !beginStr.isEmpty()){
//			begin = Integer.valueOf(beginStr);
//		}
//
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("sourceId",sourceId);
//		map.put("order", "create_time desc");
//		map.put("page", new Page(begin, 10));
//		SocialResult<ListRange<UserComment>> userCommentPage = userCommentService.selectListUserCommentPage(map);
//		
//		
//		List<UserComment> listUserComment = userCommentPage.getContent().getData();
//		List<Map<String,Object>> userCommentMaplist=new ArrayList<Map<String,Object>>();
//		for (UserComment userComment : listUserComment) {
//			
//			Map<String,Object> userCommentMap=new HashMap<String,Object>();
//			
////			User commenterUser=userCommentService.getUser(userComment.getUserId());
//			userCommentMap.put("commenterUser", userComment.getNickName());//评论人
//			if(userComment.getParentId()==Integer.parseInt(sourceId)){
//				userCommentMap.put("commenteredUser", "");//被评论人
//			}else{
//				String commentedNickName = userCommentService.selectUserComment(userComment.getParentId()).getContent().getNickName();
//				userCommentMap.put("commenteredUser", commentedNickName);//被评论人
//			}
//			userCommentMap.put("userCommentId",userComment.getId());//评论Id
//			userCommentMap.put("content", userComment.getContent());//评论内容
//			userCommentMap.put("sourceId",sourceId);
//			userCommentMaplist.add(userCommentMap);
//			
//			
//		}
//		ListRange listRange=new ListRange();
//		listRange.setData(userCommentMaplist);
//		SocialResult<ListRange<HashMap<String,Object>>> userCommentMapListResult=  new SocialResult<ListRange<HashMap<String,Object>>>();
//		userCommentMapListResult.setContent(listRange);
//		modelMap.put("userCommentMapList", userCommentMaplist);
//		modelMap.put("result", Constants.ERROR_OK);
//		modelMap.put("page", new Page(begin, 10));
//		return "";
//		
//	}
	
	
	
	

}
