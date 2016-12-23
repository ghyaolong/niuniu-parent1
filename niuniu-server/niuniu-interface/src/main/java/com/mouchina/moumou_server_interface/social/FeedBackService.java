package com.mouchina.moumou_server_interface.social;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.social.Feedback;
import com.mouchina.moumou_server_interface.view.FeedBackResult;

import java.util.Map;

/**
 * Created by douzy on 16/2/16.
 */
public interface FeedBackService {
    FeedBackResult<ListRange< Feedback>> selectListFeedbackPage(Map map);
    Integer insertFeedback(Feedback feedback);
}
