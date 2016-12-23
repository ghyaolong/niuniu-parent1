package com.mouchina.moumou_server_dubbo.provider.social;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.social.FeedbackMapper;
import com.mouchina.moumou_server.entity.social.Feedback;
import com.mouchina.moumou_server_interface.social.FeedBackService;
import com.mouchina.moumou_server_interface.view.FeedBackResult;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by douzy on 16/2/16.
 */
public class FeedBackServiceSupport implements FeedBackService {
    @Resource
    FeedbackMapper feedbackMapper;

    @Override
    public FeedBackResult<ListRange< Feedback>> selectListFeedbackPage(Map map){
        FeedBackResult<ListRange<Feedback>> feedBackResult = new FeedBackResult<ListRange<Feedback>>();
        ListRange<Feedback> listRange = new ListRange<Feedback>();
        int count = feedbackMapper.selectCount(map);
        Page page = (Page) map.get("page");
        page.setCount(count);
        listRange.setPage(page);
        listRange.setData(feedbackMapper.selectPageList(map));
        feedBackResult.setContent(listRange);
        feedBackResult.setState(1);
        return feedBackResult;

    }

    @Override
    public Integer insertFeedback(Feedback feedback) {
        return feedbackMapper.insertSelective(feedback);
    }
}
