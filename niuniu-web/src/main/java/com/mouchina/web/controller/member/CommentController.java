package com.mouchina.web.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserHelper;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.CommentVo;

@Controller
@RequestMapping("/user/social/comment")
public class CommentController {

	@Resource
	UserCommentService userCommentService;

	@Resource
	SocialWebService socialWebService;

	@Resource
	AdvertService advertService;

	@Resource
	UserWebService userWebService;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, int sysId)
			throws UnsupportedEncodingException {

		modelMap.put("sysId", sysId);
		return "social/comment/list";
	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	public String listQuery(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Long sourceId) throws UnsupportedEncodingException {

		Map<String, Object> map = new HashMap<>();
		int begin = 0;
		int pageSize = 3;
		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
			pageSize = 10;
		}
		map.put("page", new Page(begin, pageSize));
		map.put("sourceId", sourceId);
		map.put("state", 1);
		map.put("commentType", 1);

		String sort = "default";
		String order = "sequence desc,create_time desc";
		if (request.getParameter("sort") != null) {
			sort = request.getParameter("sort");
		}

		switch (sort) {
		case "latest":
			order = "sequence desc";
			break;
		case "hots":
			order = "like desc,create_time desc";
			break;
		case "default":
			order = "sequence desc,create_time desc";
			break;
		default:
			break;

		}
		map.put("order", order);

		if (request.getParameter("sourceId") != null) {
			map.put("sourceId", request.getParameter("sourceId"));
		}
		ListRange<CommentVo> userCommentListRange = socialWebService.getListRangeCommentVo(map, "userCommentListRange");
		if (userCommentListRange != null) {
			modelMap.put("userCommentList", userCommentListRange.getData());
			modelMap.put("page", userCommentListRange.getPage());
			modelMap.put("result", 1);
		} else {
			modelMap.put("result", 0);

		}
		return "";
	}

	@RequestMapping(value = "/my/list/query", method = RequestMethod.GET)
	public String myListQuery(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Integer parentId) throws UnsupportedEncodingException {

		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		//int result = 0;
		if (user != null) {

			Map<String, Object> map = new HashMap<>();
			int begin = 0;
			int pageSize = 3;
			if (request.getParameter("begin") != null) {
				begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
				pageSize = 10;
			}
			map.put("page", new Page(begin, pageSize));
			map.put("userId", user.getId());
			map.put("state", 1);

			String sort = "default";
			String order = "sequence desc,create_time desc";
			if (request.getParameter("sort") != null) {
				sort = request.getParameter("sort");
			}

			switch (sort) {
			case "latest":
				order = "sequence desc";
				break;
			case "hots":
				order = "like desc,create_time desc";
				break;
			case "default":
				order = "sequence desc,create_time desc";
				break;
			default:
				break;

			}
			map.put("order", order);

			if (request.getParameter("parentId") != null) {
				map.put("parentId", request.getParameter("parentId"));
			}
			ListRange<CommentVo> userCommentListRange = socialWebService.getListRangeCommentVoNoCache(map);
			if (userCommentListRange != null && userCommentListRange.getData() != null) {
				modelMap.put("userCommentList", userCommentListRange.getData());
				modelMap.put("page", userCommentListRange.getPage());

			}
			modelMap.put("result", 1);
		} else {

			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
		}
		return "";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String newnews(UserComment userComment, ModelMap modelMap, HttpServletResponse response,
			HttpServletRequest request) {

		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("content"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		int result = 0;
		if (user != null) {

			userComment.setUserAvatar(user.getAvatar());
			userComment.setNickName(user.getNickName());
			userComment.setUserId(user.getId());
			userComment.setSex((byte) user.getSex().intValue());
			SocialResult<UserComment> socialResult = userCommentService.addUserComment(userComment);

			if (socialResult.getState() == 1) {
				CommentVo commentVo = new CommentVo();
				BeanUtils.copyProperties(socialResult.getContent(), commentVo);

				if (userComment.getSourceId() > 0 && userComment.getParentId() > 0) {
					AdvertResult<Advert> advertResult = advertService.selectAdvert(userComment.getSourceId());

					if (advertResult.getState() == 1) {
						Advert advert = advertResult.getContent();
						Advert newAdvet = new Advert();
						newAdvet.setId(advert.getId());
						// newAdvet.setCommentSize(advert.getCommentSize() + 1);
						advertService.updateAdvert(newAdvet);
					}

				}

				result = 1;
			}
		}
		modelMap.remove("userComment");

		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/like/{id}", method = RequestMethod.GET)
	public String like(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) throws UnsupportedEncodingException {
		// Map<String, Object> attributes= ((CasAuthenticationToken)
		// SecurityContextHolder.getContext().getAuthentication()).getAssertion().getPrincipal().getAttributes();
		// String userName=attributes.get("userName").toString();
		// news.setId(id);
		// adminNewsService.deleteNews(id);
		SocialResult<UserComment> socialResult = userCommentService.selectUserComment(id);
		int result = 0;
		if (socialResult.getState() == 1) {
			UserComment userComment = socialResult.getContent();
			userComment.setLikeSize(userComment.getLikeSize() + 1);
			userCommentService.updateUserComment(userComment);
			result = 1;
		}

		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/nolike/{id}", method = RequestMethod.GET)
	public String nolike(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) throws UnsupportedEncodingException {
		// Map<String, Object> attributes= ((CasAuthenticationToken)
		// SecurityContextHolder.getContext().getAuthentication()).getAssertion().getPrincipal().getAttributes();
		// String userName=attributes.get("userName").toString();
		// news.setId(id);
		// adminNewsService.deleteNews(id);
		SocialResult<UserComment> socialResult = userCommentService.selectUserComment(id);
		int result = 0;
		if (socialResult.getState() == 1) {
			UserComment userComment = socialResult.getContent();
			userComment.setNoLikeSize(userComment.getNoLikeSize() + 1);
			userCommentService.updateUserComment(userComment);
			result = 1;
		}

		modelMap.put("result", result);
		return "";
	}

	/**
	 * 点赞/取消赞
	 * 
	 * @param userComment
	 * @param modelMap
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/praise", method = { RequestMethod.GET, RequestMethod.POST })
	public String commentPraise(UserComment userComment, ModelMap modelMap, HttpServletResponse response,
			HttpServletRequest request) {
		logger.info("loginKey=" + request.getParameter("loginKey"));
		logger.info("sourceId=" + request.getParameter("sourceId"));
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("sourceId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		int result = 0;
		if (user != null) {
			userComment.setUserId(user.getId());
			userComment.setCommentType((byte) 2);
			userComment.setNickName(user.getNickName());
			userComment.setSex((byte) user.getSex().intValue());
			userComment.setUserAvatar(user.getAvatar());
			Map<String, Boolean> praiseAndCommentFlagMap = userCommentService.selectUserPraiseAndComment(user.getId(),
					userComment.getSourceId());
			if (praiseAndCommentFlagMap.get("praiseFlag") == true) {
				result = userCommentService.deleteUserCommentPraise(userComment);
				if (result == 0) {
					modelMap.put("errorCode", Constants.ERROR_CODE_500003);
				}
			} else {
				result = userCommentService.insertUserCommentPraise(userComment);
			}
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 删除自己的评论
	 * 
	 * @param userComment
	 * @param modelMap
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteComment", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteComment(UserComment userComment, ModelMap modelMap, HttpServletResponse response,
			HttpServletRequest request) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("commentId"))
				|| StringUtil.isEmpty(request.getParameter("sourceId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");
		String id = request.getParameter("commentId");
		User user = userWebService.getUserByLoginKey(loginKey);
		int result = 0;
		if (user != null) {
			userComment.setId(Long.parseLong(id));
			userComment.setUserId(user.getId());
			userComment.setCommentType((byte) 1);
			result = userCommentService.deleteUserComment(userComment);
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		// result 1、成功 0、错误
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 用户评论
	 * 
	 * @param userComment
	 * @param modelMap
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addComment", method = { RequestMethod.GET, RequestMethod.POST })
	public String addComment(UserComment userComment, ModelMap modelMap, HttpServletResponse response,
			HttpServletRequest request) {
		// logger.info("loginKey="+request.getParameter("loginKey"));
		// logger.info("sourceId="+request.getParameter("sourceId"));
		// logger.info("content="+request.getParameter("content"));
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(request.getParameter("content"))
				|| StringUtil.isEmpty(request.getParameter("sourceId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");
		// Long sourceId = Long.parseLong(request.getParameter("sourceId"));
		String parentId = request.getParameter("parentId");
		// 查询被评论用户昵称（对广告评论不传parentId）
		if (parentId != null && !parentId.isEmpty()) {
			UserResult<User> userResult = userWebService.selectUserByUserId(Long.parseLong(parentId));
			if (userResult.getContent().getNickName() == null) {
				userComment.setParentNickName("");
			} else {
				userComment.setParentNickName(userResult.getContent().getNickName());
			}
		}
		int result = 0;
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) { 
			/* 增加封停用户判断 */
			if(user.getState().intValue() == UserHelper.USER_STATE_2.getMarker().intValue()){
				modelMap.put("errorCode", Constants.ERROR_CODE_100103);
			}else{
				userComment.setUserAvatar(user.getAvatar());
				userComment.setNickName(user.getNickName());
				userComment.setUserId(user.getId());
				userComment.setSex((byte) user.getSex().intValue());
				userComment.setCommentType((byte) 1);
				SocialResult<UserComment> socialResult = userCommentService.addUserComment(userComment);
				if (socialResult.getState() == 1) {
					Advert advert = new Advert();
					advert.setId(userComment.getSourceId());
					advert = advertService.selectByAdvertId(advert.getId());
					if (advert != null) {
						result = advertService.updateAdvertCommentSize(advert);
						modelMap.put("userComment", socialResult.getContent());
					} else {
						modelMap.put("errorCode", Constants.ERROR_CODE_500004);
					}
				}
			}
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.put("result", result);
		return "";
	}
	
}