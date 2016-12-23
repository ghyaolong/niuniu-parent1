package com.mouchina.web.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.JsonUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.base.utils.UtilMaskCode;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.advert.AdvertWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.UserInviteVo;
import com.mouchina.web.service.api.vo.UsersRelationVo;

@Controller
@RequestMapping("/user/social")
public class UserSocialController {
	private Logger logger = Logger.getLogger(getClass());

	@Resource
	UserWebService userWebService;
	@Resource
	AdvertWebService advertWebService;

	@Resource
	SocialWebService socialWebService;

	@Resource
	UserInviteService userInviteService;
	@Resource
	UsersRelationService usersRelationService;
	@Resource
	UserAgentService userAgentService;
	
	@Resource
	UsersRelationService userRelationService;
	@Autowired
	private TaskHistoryService taskHistoryService;

	@RequestMapping(value = "/fans/del", method = { RequestMethod.GET, RequestMethod.POST })
	public String fansDel(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {

		return "";
	}

	@RequestMapping(value = "/collect", method = { RequestMethod.GET, RequestMethod.POST })
	public String collect(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			modelMap.put("result", "1");
			Map<String,Object> map = new HashMap<>();
			map.put("relationUserId", user.getId());
			map.put("relationType", 1);
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			ListRange<UsersRelation> listRange = socialWebService.selectListRangeUsersRelation(map);
			ListRange<UsersRelationVo> listRangeVo = new ListRange<UsersRelationVo>();

			if (listRange.getData() != null) {
				List<UsersRelationVo> listVo = new ArrayList<UsersRelationVo>();
				for (UsersRelation usersRelation : listRange.getData()) {
					UsersRelationVo usersRelationVo = new UsersRelationVo();
					BeanUtils.copyProperties(usersRelation, usersRelationVo);

					if (usersRelation.getEventId() > 0) {
						Advert advert = advertWebService.selectAdvert(usersRelation.getEventId());
						if (advert != null) {
							usersRelationVo.setAdvertContent(advert.getAdvertContent());
							usersRelationVo.setAdvertName(advert.getAdvertName());
							/*
							 * usersRelationVo.setWebSite(advert.getWebsite());
							 */
						}

						UserResult<Business> businessUserResult = userWebService
								.getBusinessByUserId(usersRelation.getUserId());
						if (businessUserResult.getState() == 1) {
							Business business = businessUserResult.getContent();
							usersRelationVo.setBusinessName(business.getBusinessName());
							usersRelationVo.setBusinessQq(business.getBusinessQq());
							usersRelationVo.setBusinessDescb(business.getDescb());
							usersRelationVo.setBusinessSite(business.getBusinessSite());
							usersRelationVo.setBusinessTel(business.getBusinessTel());
							usersRelationVo.setBusinessWeibo(business.getBusinessWeixin());
							usersRelationVo.setBusinessWeixin(business.getBusinessWeixin());
							usersRelationVo.setBusinessAddress(business.getBusinessAddress());
							usersRelationVo.setBusinessId(business.getId());
							usersRelationVo.setBusinessLogo(business.getBusinessLogo());

						}

					}

					Map<String,Object> fansMap = new HashMap<>();
					fansMap.put("userId", usersRelationVo.getUserId());
					fansMap.put("relationUserId", usersRelationVo.getRelationUserId());
					fansMap.put("relationType", 2);
					int count = socialWebService.selectListUsersRelationCount(fansMap);
					usersRelationVo.setIsFans(count > 0 ? 1 : 0);

					listVo.add(usersRelationVo);
				}

				listRangeVo.setPage(listRange.getPage());
				listRangeVo.setData(listVo);
			}

			modelMap.put("collectList", listRangeVo.getData());
			modelMap.put("page", listRangeVo.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";

	}

	@RequestMapping(value = "/fans/add", method = { RequestMethod.GET, RequestMethod.POST })
	public String fansAdd(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Long userId,
			String loginKey) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(userId);

			if (userBusinessResult.getState() == 1 && userBusinessResult.getContent().getUserId() != user.getId()) {
				Map<String,Object> fansMap = new HashMap<>();
				fansMap.put("userId", userBusinessResult.getContent().getUserId());
				fansMap.put("relationUserId", user.getId());
				fansMap.put("relationType", 2);
				SocialResult<List<UsersRelation>> socialfansResult = usersRelationService
						.selectListPageUsersRelation(fansMap);
				if (socialfansResult.getContent() == null || socialfansResult.getContent().size() == 0) {
					UsersRelation usersRelationFans = new UsersRelation();
					usersRelationFans.setRelationUserId(user.getId());
					usersRelationFans.setRelationUserNickName(user.getNickName());
					usersRelationFans.setRelationUserAvatar(user.getAvatar());
					usersRelationFans.setRelationType((byte) 2);
					usersRelationFans.setRelationUserSex((byte)user.getSex().intValue());
					usersRelationFans.setUserId(userBusinessResult.getContent().getUserId());
					usersRelationFans.setCreateTime(new Date());
					usersRelationFans.setEventId((long) 0);
					usersRelationService.addUsersRelation(usersRelationFans);

					modelMap.put("result", "1");
					return "";

				}
			}
		}

		modelMap.put("result", "0");
		modelMap.put("errorCode", Constants.ERROR_CODE_100005);
		return "";
	}

	/**
	 * 收藏广告
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/collect/add", method = { RequestMethod.GET, RequestMethod.POST })
	public String collectAdd(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {

			Long advertId = Long.valueOf(request.getParameter("advertId"));
			if (StringUtil.isEmpty(request.getParameter("loginKey"))
					|| StringUtil.isEmpty(request.getParameter("advertId"))) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				return "";
			}

			Advert advert = advertWebService.selectAdvert(advertId);

			if (advert != null) {
				Map<String, Object> selectMap = new HashMap<String, Object>();

				selectMap.put("eventId", advert.getId());
				selectMap.put("userId", advert.getUserId());
				selectMap.put("relationUserId", user.getId());
				selectMap.put("relationType", 1);
				selectMap.put("page", new Page(begin, 10));

				SocialResult<List<UsersRelation>> socialSelectResult = usersRelationService
						.selectListPageUsersRelation(selectMap);
				if (socialSelectResult.getContent() == null || socialSelectResult.getContent().size() == 0) {

					Map<String, Object> map = new HashMap<String, Object>();
					UsersRelation usersRelation = new UsersRelation();

					usersRelation.setRelationUserId(user.getId());
					usersRelation.setRelationType((byte) 1);
					usersRelation.setUserId(advert.getUserId());
					usersRelation.setRelationUserNickName(advert.getAdvertName());

					String advertString = advert.getAdvertContent();
					if (StringUtil.isNotEmpty(advertString)) {
						try {
							Map advertContentMap = JsonUtils.jsonToObject(advertString, Map.class);
							if (advertContentMap.containsKey("photo")) {
								List photoList = (List) advertContentMap.get("photo");
								if (photoList.size() > 0) {
									usersRelation.setRelationUserAvatar(photoList.get(0).toString());
								}
							}
						} catch (Exception e) {
							System.out.println("－－－－－－－－advertString=" + advertString);
						}
					}

					/* 收藏方 成为 广告方的粉丝 */
					usersRelation.setCreateTime(new Date());
					usersRelation.setEventId(advert.getId());
					usersRelationService.addUsersRelation(usersRelation);

					modelMap.put("result", "1");
					return "";
				} else {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100005);
					return "";
				}
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 关注or取消关注(人)
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/addOrRemoveRelationUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String AddOrRemoveRelationUser(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");//关注者loginKey
		String followedUserId = request.getParameter("followedUserId");//被关注者id
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(request.getParameter("followedUserId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.put("errorMsg", "没有被关注者信息!");
			return "";
		}

		User followUser = userWebService.getUserByLoginKey(loginKey);//关注者
		UserResult<User> followedUserResult = userWebService.selectUserByUserId(Long.valueOf(followedUserId));//被关注者
		if (followUser != null && followedUserResult.getState() == 1) {
			User followedUser = followedUserResult.getContent();//被关注者User
			Map<String, Object> selectMap = new HashMap<String, Object>();
			selectMap.put("userId", followedUser.getId());//被关注者id
			selectMap.put("relationUserId", followUser.getId());//操作人id
			selectMap.put("relationType", 2);
			SocialResult<UsersRelation> userRelationResult = usersRelationService.selectUsersRelation(selectMap);
			if (userRelationResult.getState() != 1) {
				//进行关注
				Map<String, Object> map = new HashMap<String, Object>();
				UsersRelation usersRelation = new UsersRelation();

				usersRelation.setRelationUserId(followUser.getId());
				usersRelation.setRelationType((byte) 2);//代表粉丝
				usersRelation.setUserId(followedUser.getId());
				usersRelation.setRelationUserNickName(followUser.getNickName());

				usersRelation.setRelationUserAvatar(followUser.getAvatar());
				usersRelation.setCreateTime(new Date());
				SocialResult<UsersRelation> relationAddResult = usersRelationService.addUsersRelation(usersRelation);
				
				if(relationAddResult.getState() == 1){
					followUser.setFollowCount(followUser.getFollowCount() + 1);//关注者关注人数加1
					followedUser.setFansCount(followedUser.getFansCount() + 1);//被关注者粉丝数加1
					userWebService.updateUser(followUser);
					userWebService.updateUser(followedUser);
					
					modelMap.put("result", "1");
					taskHistoryService.insertTask(followUser.getId(), (long)6, "1");
				}else{
					modelMap.put("result", "0");
				}
				
			} else {
				//取消关注
				SocialResult<Integer> relationDelResult = usersRelationService.deleteUsersRelation(userRelationResult.getContent().getId());
				if(relationDelResult.getState() > 0){
					if(followUser.getFollowCount()>0){
						followUser.setFollowCount(followUser.getFollowCount() - 1);//关注者关注人数减1
						userWebService.updateUser(followUser);
					}
					if(followedUser.getFansCount()>0){
						followedUser.setFansCount(followedUser.getFansCount() - 1);//被关注者粉丝数减1
						userWebService.updateUser(followedUser);
					}
					
					modelMap.put("result", "1");
				}else{
					modelMap.put("result", "0");
					modelMap.put("errorMsg", "取消关注失败!");
				}
			}
			
			boolean flag =userRelationService.isAFansB(followUser.getId(), Long.parseLong(followedUserId));
			if(flag == true){
				modelMap.put("state", 1);
			}else{
				modelMap.put("state", 0);
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}
	
	@RequestMapping(value = "/collect/del", method = { RequestMethod.GET, RequestMethod.POST })
	public String collectDel(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {

		return "";
	}

	@RequestMapping(value = "/invite/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String inviteList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();

		User currentUser = userWebService.getUserByLoginKey(loginKey);

		if (currentUser != null) {
			modelMap.put("result", "1");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", new Page(begin, 10));
			map.put("userId", currentUser.getId());
			ListRange<UserInvite> listRange = socialWebService.selectListRangeUserInvite(map);

			ListRange<UserInviteVo> listRangeVo = new ListRange<UserInviteVo>();
			List<UserInviteVo> listVo = new ArrayList<UserInviteVo>();
			if (listRange.getData() != null) {

				for (UserInvite userInvite : listRange.getData()) {
					UserInviteVo userInviteVo = new UserInviteVo();
					BeanUtils.copyProperties(userInvite, userInviteVo);

					UserResult<User> userResult = userWebService.selectUserByUserId(userInvite.getNewUserId());

					if (userResult.getState() == 1) {
						User userTmp = userResult.getContent();
						// if(null == userTmp.getFristLoginTime()){
						// continue;
						// }
						userInviteVo.setNewUserNickName(userTmp.getNickName());
						userInviteVo.setNewUserAvatar(userTmp.getAvatar());
						userInviteVo.setNewUserSex((byte) userTmp.getSex().intValue());
						userInviteVo.setNewUserPhone(userTmp.getPhone());
						// 掩码：手机号码
						userInviteVo.setViewUserPhone(UtilMaskCode.telephone(userTmp.getPhone()));
					}
					listVo.add(userInviteVo);
				}
			}
			listRangeVo.setData(listVo);
			modelMap.put("inviteList", listRangeVo.getData());
			modelMap.put("page", listRange.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}
	
	
	@RequestMapping(value = "/friendsList", method = { RequestMethod.GET, RequestMethod.POST })
	public String friendsList(HttpServletRequest request, ModelMap modelMap){
		String loginKey = request.getParameter("loginKey");
		if(StringUtil.isEmpty(loginKey)){
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		Integer begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		
		if(user != null){
			
			long currentUserId= user.getId();//当前用户id
			
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("userId",currentUserId);
			sqlMap.put("order", "create_time desc");
			sqlMap.put("page", new Page(begin, 10));
			List<UserInvite> currentUserFriendsList = socialWebService.selectListUserInvite(sqlMap);
			int friendsCount =socialWebService.selectListUserInviteCount(sqlMap);
			sqlMap.remove("userId");
			sqlMap.put("newUserId", currentUserId);
			List<UserInvite> currentUserParentsList = socialWebService.selectListUserInvite(sqlMap);
			int parentCount =socialWebService.selectListUserInviteCount(sqlMap);
			List friendsAndParentList =new ArrayList<>(); //存当前用户邀请的好友和邀请当前用户的人的集合
			
			int currentUserFriendsAndParentCount = friendsCount+parentCount;
			
			if(currentUserParentsList != null && currentUserParentsList.size() > 0){
//				currentUserFriendsAndParentCount++;
				
				Map<String,Object> parentMap = new HashMap<String,Object>();
				
				UserInvite invite = currentUserParentsList.get(0);//邀请新用户的用户的invite对象
				
				long parentuserId =invite.getUserId(); //邀请新用户的用户ID
				User parentUser = userWebService.selectUserByUserId(parentuserId).getContent();//邀请新用户的用户
				if(parentUser != null){
					String parentUserAvatar = parentUser.getAvatar();
					String parentNickName = parentUser.getNickName();
					Integer parentSex = parentUser.getSex();
					Integer level = userAgentService.selectUserCertificateLevel(parentuserId);//用户认证标识
					String province =parentUser.getProvince();
					String city =parentUser.getCity();
					if(province ==null){province ="";};
					if(province != null && province.endsWith("省")){
						province =province.substring(0, province.length()-1);
					}
					if(city ==null){city ="";};
					if(city !=null && city.endsWith("市")){
						city =city.substring(0, city.length()-1);
					}
					String address = province +city;//邀请新用户的用户地址
					
					parentMap.put("avatar", parentUserAvatar);
					parentMap.put("nickName", parentNickName);
					parentMap.put("sex", parentSex);
					parentMap.put("level", level);
					parentMap.put("address", address);
					parentMap.put("isParent", 1);
					parentMap.put("relationUserId", parentuserId);
					
					friendsAndParentList.add(parentMap);
				}
			}
			{
				Map friendMap;
				if(currentUserFriendsList != null && currentUserFriendsList.size() > 0){
					
					for(UserInvite invite : currentUserFriendsList){
//						currentUserFriendsAndParentCount++;
						friendMap = new HashMap<>();
						long friendUserId =invite.getNewUserId(); //当前好友id
						
						Integer level = userAgentService.selectUserCertificateLevel(friendUserId);//用户认证标识
						User parentUser = userWebService.selectUserByUserId(friendUserId).getContent();//邀请新用户的用户
						if(parentUser != null){
							String friendUserAvatar = parentUser.getAvatar();
							String friendNickName = parentUser.getNickName();
							Integer friendSex = parentUser.getSex();
							
							String province =parentUser.getProvince();
							String city =parentUser.getCity();
							if(province ==null){province ="";};
							if(province != null && province.endsWith("省")){
								province =province.substring(0, province.length()-1);
							}
							if(city ==null){city ="";};
							if(city != null && city.endsWith("市")){
								city =city.substring(0, city.length()-1);
							}
							String address = province +city;//邀请新用户的用户地址
							
							friendMap.put("avatar", friendUserAvatar);
							friendMap.put("nickName", friendNickName);
							friendMap.put("sex", friendSex);
							friendMap.put("level", level);
							friendMap.put("address", address);
							friendMap.put("isParent", 0);
							friendMap.put("relationUserId", friendUserId);
							
							friendsAndParentList.add(friendMap);
						}
						
					}
				}
			}
			
			
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("friendsMap",friendsAndParentList);
			modelMap.put("count", currentUserFriendsAndParentCount);
			
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

}
