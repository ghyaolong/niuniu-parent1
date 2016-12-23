package com.mouchina.admin.controller.feedback;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.social.Feedback;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.social.FeedBackService;
import com.mouchina.moumou_server_interface.view.FeedBackResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 16/2/16.
 */
@Controller
@RequestMapping("/feedback")
public class FeedBackController {
    @Resource
    FeedBackService feedBackService;

    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String orderList( ModelMap modelMap ) {
        return "feedback/list";
    }
    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String orderQuery( HttpServletRequest request, ModelMap modelMap ) throws UnsupportedEncodingException {
        Map map = new HashMap();
        Page page = new Page(0, 10);
        int begin = 0;

        if (request.getParameter("begin") != null) {
            begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
        }
        page.setBegin(begin);
        map.put("page", page);
        map.put("order", "create_time desc");
        FeedBackResult<ListRange<Feedback>> feedbackListRange = feedBackService.selectListFeedbackPage(map);
        modelMap.put("feedBacks", feedbackListRange.getContent());

        return "";
    }

}
