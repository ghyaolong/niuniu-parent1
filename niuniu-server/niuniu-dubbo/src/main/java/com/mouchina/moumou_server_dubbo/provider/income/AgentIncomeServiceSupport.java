package com.mouchina.moumou_server_dubbo.provider.income;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.dao.income.AgentIncomeDetailMapper;
import com.mouchina.moumou_server.dao.income.AgentIncomeMapper;
import com.mouchina.moumou_server.dao.income.AgentIncomeStatisticsMapper;
import com.mouchina.moumou_server.dao.income.AgentIncomeSumMapper;
import com.mouchina.moumou_server.dao.member.UserAgentMapper;
import com.mouchina.moumou_server.dao.social.UserInviteMapper;
import com.mouchina.moumou_server.dao.system.SystemGlobalConfigMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeDetail;
import com.mouchina.moumou_server.entity.income.AgentIncomeParameter;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatistics;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatisticsEnum;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server.entity.member.AreaBusiness;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.vo.AgentIncomeStatisticsVo;
import com.mouchina.moumou_server.entity.vo.AgentStatisticsVo;
import com.mouchina.moumou_server_interface.income.AgentIncomeService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.IncomeResult;

public class AgentIncomeServiceSupport implements AgentIncomeService {
	
	private Logger logger = Logger.getLogger(AgentIncomeServiceSupport.class);

	public static String AGENT_INCOME_GET_LOCK = "AGENT_INCOME_GET_LOCK_";

	public static String AGENT_INCOME_DETAIL_GET_LOCK = "AGENT_INCOME_DETAIL_GET_LOCK_";

	@Resource
	AgentIncomeMapper agentIncomeMapper;
	@Resource
	AgentIncomeSumMapper agentIncomeSumMapper;
	@Resource
	AgentIncomeDetailMapper agentIncomeDetailMapper;
	@Resource
	UserInviteMapper userInviteMapper;
	@Resource
	UserAgentMapper userAgentMapper;
	@Resource
	SystemGlobalConfigMapper systemGlobalConfigMapper;
	@Resource
	AgentIncomeStatisticsMapper agentIncomeStatisticsMapper;

	@Resource
	UserAgentService userAgentService;
	@Resource
	UserVerifyService userVerifyService;
	@Resource
	RedisLockHandler redisLockHandler;
	
	private Map map = new HashMap();

	@Override
	public IncomeResult<AgentIncome> selectAgentIncome(Long agentIncomeId) {
		// TODO Auto-generated method stub

		IncomeResult<AgentIncome> IncomeResult = new IncomeResult<AgentIncome>();

		AgentIncome AgentIncome = agentIncomeMapper.selectByPrimaryKey(agentIncomeId);
		if (AgentIncome != null) {
			IncomeResult.setContent(AgentIncome);
			IncomeResult.setState(1);
		}

		return IncomeResult;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IncomeResult<AgentIncome> selectAgentIncome(Map map) {
		IncomeResult<AgentIncome> AgentIncomeResult = new IncomeResult<AgentIncome>();

		AgentIncome AgentIncome = agentIncomeMapper.selectModel(map);
		if (AgentIncome != null) {
			AgentIncomeResult.setContent(AgentIncome);
			AgentIncomeResult.setState(1);
		}

		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<AgentIncome> addAgentIncome(AgentIncome agentIncome) {
		// TODO Auto-generated method stub
		IncomeResult<AgentIncome> AgentIncomeResult = new IncomeResult<AgentIncome>();
		agentIncome.setCreateTime(new Date());
		int result = agentIncomeMapper.insertSelective(agentIncome);
		AgentIncomeResult.setContent(agentIncome);
		AgentIncomeResult.setState(result);

		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<ListRange<AgentIncome>> selectListAgentIncomePage(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<ListRange<AgentIncome>> AgentIncomeResult = new IncomeResult<ListRange<AgentIncome>>();
		ListRange<AgentIncome> listRange = new ListRange<AgentIncome>();
		int count = selectListAgentIncomeCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListAgentIncome(map));
		AgentIncomeResult.setContent(listRange);
		AgentIncomeResult.setState(1);
		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<ListRange<AgentIncome>> selectListAgentDayIncomePage(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<ListRange<AgentIncome>> AgentIncomeResult = new IncomeResult<ListRange<AgentIncome>>();
		ListRange<AgentIncome> listRange = new ListRange<AgentIncome>();
		int count = selectListAgentIncomeCountCal(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListAgentIncomeCal(map));
		AgentIncomeResult.setContent(listRange);
		AgentIncomeResult.setState(1);
		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<Integer> deleteAgentIncome(Long AgentIncomeId) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeResult = new IncomeResult<Integer>();

		int result = agentIncomeMapper.deleteByPrimaryKey(AgentIncomeId);
		AgentIncomeResult.setState(result);

		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<Integer> updateAgentIncome(AgentIncome agentIncome) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeResult = new IncomeResult<Integer>();
		int result = agentIncomeMapper.updateByPrimaryKeySelective(agentIncome);
		AgentIncomeResult.setState(result);

		return AgentIncomeResult;
	}

	@Override
	public IncomeResult<List<AgentIncome>> selectListAgentIncome(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<List<AgentIncome>> IncomeResult = new IncomeResult<List<AgentIncome>>();

		List<AgentIncome> list = selectPageListAgentIncome(map);
		IncomeResult.setContent(list);

		return IncomeResult;
	}

	private int selectListAgentIncomeCount(Map map) {

		return agentIncomeMapper.selectCount(map);
	}

	private List<AgentIncome> selectPageListAgentIncome(Map map) {

		return agentIncomeMapper.selectPageList(map);
	}

	private int selectListAgentIncomeCountCal(Map map) {

		return agentIncomeMapper.selectMonthIncomeCount(map);
	}

	@Override
	public int insertCalAgentIncomeByMonth(Date date) throws Exception {
		int result = 0;
		Map checkMap = new HashMap();
		checkMap.put("state", 1);
		String[] preDate = null;
		preDate = DateUtils.getNowPreDate(DateUtils.dateFormat(date));
		Date startMonthDate = DateUtils.parseDate(preDate[2]);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date endMonthDate = null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(startMonthDate);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		endMonthDate = ca.getTime();
		checkMap.put("incomeDategt", startMonthDate);
		checkMap.put("incomeDatelt", endMonthDate);
		int pageSize = 100;
		int maxPage = 10;
		int sumCount = selectListAgentIncomeCountCal(checkMap);

		int pageTatal = sumCount / pageSize;
		if ((sumCount + pageSize) % pageSize != 0) {
			pageTatal++;
		}

		if (pageTatal > maxPage) {
			pageTatal = maxPage;

		}
		for (int i = 0; i < pageTatal; i++) {
			Map selectMap = new HashMap();
			selectMap.put("state", 1);
			Page page = new Page(0, pageSize);
			page.setBegin(i * pageSize);
			selectMap.put("state", 1);
			selectMap.put("group", "user_id");
			selectMap.put("incomeDategt", startMonthDate);
			selectMap.put("incomeDatelt", endMonthDate);
			List<AgentIncome> incomeList = selectPageListMonthAgentIncomeCal(selectMap);
			if (incomeList != null) {
				for (AgentIncome agentIncome : incomeList) {

					int r = insertAgentIncomeByMonth(agentIncome, startMonthDate, endMonthDate);
					if (r == 1) {
						result++;
					}

				}
			}
		}

		return result;
	}

	private List<AgentIncome> selectPageListAgentIncomeCal(Map map) {

		return agentIncomeMapper.selectIncomePageList(map);
	}

	private List<AgentIncome> selectPageListMonthAgentIncomeCal(Map map) {

		return agentIncomeMapper.selectMonthIncomePageList(map);
	}

	@Override
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertAgentIncomeByMonth(AgentIncome agentIncome, Date startDay, Date endDay) {
		int result = 0;
		List<String> AgentIncomeDetailLocks = new ArrayList<String>();
		String AgentIncomeDetailLock = AGENT_INCOME_GET_LOCK + agentIncome.getUserId() + "_"
				+ DateUtils.getDateString(agentIncome.getIncomeDate());
		AgentIncomeDetailLocks.add(AgentIncomeDetailLock);
		try {
			if (redisLockHandler.tryLock(AgentIncomeDetailLock)) {
				Map checkMap = new HashMap();
				checkMap.put("userId", agentIncome.getUserId());

				checkMap.put("incomeDate", agentIncome.getIncomeDate());
				IncomeResult<List<AgentIncomeSum>> listIncomeResult = selectListAgentIncomeSum(checkMap);
				if (listIncomeResult.getContent().size() == 0) {
					AgentIncomeSum agentIncomeSum = new AgentIncomeSum();
					agentIncomeSum.setAmount(agentIncome.getAmout());
					agentIncomeSum.setSourceAmout(agentIncome.getSourceAmount());
					agentIncomeSum.setUserId(agentIncome.getUserId());
					agentIncomeSum.setDescb(agentIncome.getEnventName());
					agentIncomeSum.setAloneUserIncomeSize(0);
					agentIncomeSum.setIncomeSize(agentIncome.getEventUserId().intValue());

					agentIncomeSum.setIncomeDate(agentIncome.getIncomeDate());
					agentIncomeSum.setDescb("代理商每日收益");
					addAgentIncomeSum(agentIncomeSum);

				}

				Map updateMap = new HashMap();
				updateMap.put("userId", agentIncome.getUserId());
				updateMap.put("state", 1);
				updateMap.put("afterState", 3);
				updateMap.put("incomeDategt", startDay);
				updateMap.put("incomeDatelt", endDay);
				agentIncomeMapper.updateByUserIdAndIncomeDateSelective(updateMap);

			}
		} catch (Exception e) {

		} finally {
			for (String lock : AgentIncomeDetailLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return result;
	}

	@Override
	public IncomeResult<AgentIncomeSum> selectAgentIncomeSum(Long AgentIncomeSumId) {
		// TODO Auto-generated method stub

		IncomeResult<AgentIncomeSum> IncomeResult = new IncomeResult<AgentIncomeSum>();

		AgentIncomeSum AgentIncomeSum = agentIncomeSumMapper.selectByPrimaryKey(AgentIncomeSumId);
		if (AgentIncomeSum != null) {
			IncomeResult.setContent(AgentIncomeSum);
			IncomeResult.setState(1);
		}

		return IncomeResult;
	}

	@Override
	public IncomeResult<AgentIncomeSum> selectAgentIncomeSum(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<AgentIncomeSum> AgentIncomeSumResult = new IncomeResult<AgentIncomeSum>();

		AgentIncomeSum AgentIncomeSum = agentIncomeSumMapper.selectModel(map);
		if (AgentIncomeSum != null) {
			AgentIncomeSumResult.setContent(AgentIncomeSum);
			AgentIncomeSumResult.setState(1);
		}

		return AgentIncomeSumResult;
	}

	@Override
	public IncomeResult<AgentIncomeSum> addAgentIncomeSum(AgentIncomeSum AgentIncomeSum) {
		// TODO Auto-generated method stub
		IncomeResult<AgentIncomeSum> AgentIncomeSumResult = new IncomeResult<AgentIncomeSum>();
		AgentIncomeSum.setCreateTime(new Date());
		int result = agentIncomeSumMapper.insertSelective(AgentIncomeSum);
		AgentIncomeSumResult.setContent(AgentIncomeSum);
		AgentIncomeSumResult.setState(result);

		return AgentIncomeSumResult;
	}

	private int selectListAgentIncomeSumCount(Map map) {

		return agentIncomeSumMapper.selectCount(map);
	}

	private List<AgentIncomeSum> selectPageListAgentIncomeSum(Map map) {

		return agentIncomeSumMapper.selectPageList(map);
	}

	@Override
	public IncomeResult<ListRange<AgentIncomeSum>> selectListAgentIncomeSumPage(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<ListRange<AgentIncomeSum>> AgentIncomeSumResult = new IncomeResult<ListRange<AgentIncomeSum>>();
		ListRange<AgentIncomeSum> listRange = new ListRange<AgentIncomeSum>();
		int count = selectListAgentIncomeSumCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListAgentIncomeSum(map));
		AgentIncomeSumResult.setContent(listRange);
		AgentIncomeSumResult.setState(1);
		return AgentIncomeSumResult;
	}

	@Override
	public IncomeResult<Integer> deleteAgentIncomeSum(Long AgentIncomeSumId) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeSumResult = new IncomeResult<Integer>();

		int result = agentIncomeSumMapper.deleteByPrimaryKey(AgentIncomeSumId);
		AgentIncomeSumResult.setState(result);

		return AgentIncomeSumResult;
	}

	@Override
	public IncomeResult<Integer> updateAgentIncomeSum(AgentIncomeSum AgentIncomeSum) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeSumResult = new IncomeResult<Integer>();
		int result = agentIncomeSumMapper.updateByPrimaryKeySelective(AgentIncomeSum);
		AgentIncomeSumResult.setState(result);

		return AgentIncomeSumResult;
	}

	@Override
	public IncomeResult<List<AgentIncomeSum>> selectListAgentIncomeSum(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<List<AgentIncomeSum>> incomeResult = new IncomeResult<List<AgentIncomeSum>>();

		List<AgentIncomeSum> list = selectPageListAgentIncomeSum(map);
		incomeResult.setContent(list);

		return incomeResult;
	}

	@Override
	public IncomeResult<AgentIncomeDetail> selectAgentIncomeDetail(Long agentIncomeDetailId) {
		// TODO Auto-generated method stub

		IncomeResult<AgentIncomeDetail> incomeResult = new IncomeResult<AgentIncomeDetail>();

		AgentIncomeDetail agentIncomeDetail = agentIncomeDetailMapper.selectByPrimaryKey(agentIncomeDetailId);
		if (agentIncomeDetail != null) {
			incomeResult.setContent(agentIncomeDetail);
			incomeResult.setState(1);
		}

		return incomeResult;
	}

	@Override
	public IncomeResult<AgentIncomeDetail> selectAgentIncomeDetail(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<AgentIncomeDetail> agentIncomeDetailResult = new IncomeResult<AgentIncomeDetail>();

		AgentIncomeDetail agentIncomeDetail = agentIncomeDetailMapper.selectModel(map);
		if (agentIncomeDetail != null) {
			agentIncomeDetailResult.setContent(agentIncomeDetail);
			agentIncomeDetailResult.setState(1);
		}

		return agentIncomeDetailResult;
	}

	@Override
	public IncomeResult<AgentIncomeDetail> addAgentIncomeDetail(AgentIncomeDetail agentIncomeDetail) {
		// TODO Auto-generated method stub
		IncomeResult<AgentIncomeDetail> agentIncomeDetailResult = new IncomeResult<AgentIncomeDetail>();
		agentIncomeDetail.setCreateTime(new Date());
		int result = agentIncomeDetailMapper.insertSelective(agentIncomeDetail);
		agentIncomeDetailResult.setContent(agentIncomeDetail);
		agentIncomeDetailResult.setState(result);

		return agentIncomeDetailResult;
	}

	private int selectListAgentIncomeDetailCount(Map map) {

		return agentIncomeDetailMapper.selectCount(map);
	}

	private int selectListAgentIncomeDetailCountCal(Map map) {

		return agentIncomeDetailMapper.selectIncomeCount(map);
	}

	private List<AgentIncomeDetail> selectPageListAgentIncomeDetailCal(Map map) {

		return agentIncomeDetailMapper.selectIncomePageList(map);
	}

	@Override
	public IncomeResult<ListRange<AgentIncomeDetail>> selectListAgentIncomeDetailPage(Map map) {
		IncomeResult<ListRange<AgentIncomeDetail>> AgentIncomeDetailResult = new IncomeResult<ListRange<AgentIncomeDetail>>();
		ListRange<AgentIncomeDetail> listRange = new ListRange<AgentIncomeDetail>();
		int count = selectListAgentIncomeDetailCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListAgentIncomeDetail(map));
		AgentIncomeDetailResult.setContent(listRange);
		AgentIncomeDetailResult.setState(1);
		return AgentIncomeDetailResult;
	}

	@Override
	public IncomeResult<Integer> deleteAgentIncomeDetail(Long AgentIncomeDetailId) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeDetailResult = new IncomeResult<Integer>();

		int result = agentIncomeDetailMapper.deleteByPrimaryKey(AgentIncomeDetailId);
		AgentIncomeDetailResult.setState(result);

		return AgentIncomeDetailResult;
	}

	@Override
	public IncomeResult<Integer> updateAgentIncomeDetail(AgentIncomeDetail AgentIncomeDetail) {
		// TODO Auto-generated method stub
		IncomeResult<Integer> AgentIncomeDetailResult = new IncomeResult<Integer>();
		int result = agentIncomeDetailMapper.updateByPrimaryKeySelective(AgentIncomeDetail);
		AgentIncomeDetailResult.setState(result);

		return AgentIncomeDetailResult;
	}

	private List<AgentIncomeDetail> selectPageListAgentIncomeDetail(Map map) {

		return agentIncomeDetailMapper.selectPageList(map);
	}

	@Override
	public IncomeResult<List<AgentIncomeDetail>> selectListAgentIncomeDetail(Map map) {
		// TODO Auto-generated method stub
		IncomeResult<List<AgentIncomeDetail>> IncomeResult = new IncomeResult<List<AgentIncomeDetail>>();

		List<AgentIncomeDetail> list = selectPageListAgentIncomeDetail(map);
		IncomeResult.setContent(list);

		return IncomeResult;
	}

	@Override
	public int insertCalAgentIncomeDetailByDay(Date date) {
		// TODO Auto-generated method stub

		int result = 0;
		Map checkMap = new HashMap();
		checkMap.put("state", 1);
		checkMap.put("incomeDate", date);
		int pageSize = 100;
		int maxPage = 10;
		int sumCount = selectListAgentIncomeDetailCountCal(checkMap);

		int pageTatal = sumCount / pageSize;
		if ((sumCount + pageSize) % pageSize != 0) {
			pageTatal++;
		}

		if (pageTatal > maxPage) {
			pageTatal = maxPage;

		}
		for (int i = 0; i < pageTatal; i++) {
			Map selectMap = new HashMap();
			selectMap.put("state", 1);
			Page page = new Page(0, pageSize);
			page.setBegin(i * pageSize);
			selectMap.put("state", 1);
			selectMap.put("group", "user_id");
			checkMap.put("incomeDate", date);
			List<AgentIncomeDetail> incomeList = selectPageListAgentIncomeDetailCal(selectMap);
			if (incomeList != null) {
				for (AgentIncomeDetail AgentIncomeDetail : incomeList) {

					int r = insertAgentIncomeDetailByDay(AgentIncomeDetail);
					if (r == 1) {
						result++;
					}

				}
			}
		}

		return result;
	}

	@Override
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertAgentIncomeDetailByDay(AgentIncomeDetail agentIncomeDetail) {
		int result = 0;
		List<String> agentIncomeDetailLocks = new ArrayList<String>();
		String agentIncomeDetailLock = AGENT_INCOME_DETAIL_GET_LOCK + agentIncomeDetail.getUserId() + "_"
				+ DateUtils.getDateString(agentIncomeDetail.getIncomeDate());
		agentIncomeDetailLocks.add(agentIncomeDetailLock);
		try {
			if (redisLockHandler.tryLock(agentIncomeDetailLock)) {
				Map checkMap = new HashMap();
				checkMap.put("userId", agentIncomeDetail.getUserId());
				checkMap.put("incomeDate", agentIncomeDetail.getIncomeDate());
				IncomeResult<List<AgentIncome>> listIncomeResult = selectListAgentIncome(checkMap);
				if (listIncomeResult.getContent().size() == 0) {
					AgentIncome agentIncome = new AgentIncome();
					agentIncome.setSourceAmount(agentIncomeDetail.getSourceAmount());
					agentIncome.setUserId(agentIncomeDetail.getUserId());
					agentIncome.setEnventName(agentIncomeDetail.getEnventName());
					agentIncome.setEnventId(agentIncomeDetail.getId());
					agentIncome.setAmout(agentIncomeDetail.getAmout());

					agentIncome.setIncomeDate(agentIncomeDetail.getIncomeDate());
					IncomeResult<AgentIncome> incomeResult = addAgentIncome(agentIncome);

				}

				Map updateMap = new HashMap();
				updateMap.put("userId", agentIncomeDetail.getUserId());
				updateMap.put("state", 1);
				updateMap.put("afterState", 3);
				updateMap.put("incomeDate", agentIncomeDetail.getIncomeDate());
				agentIncomeDetailMapper.updateByUserIdAndIncomeDateSelective(updateMap);

			}
		} catch (Exception e) {

		} finally {
			for (String lock : agentIncomeDetailLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return result;
	}

	private int statisticsListCount(Map<Object, Object> map) {
		return agentIncomeMapper.selectStatisticsListCount(map);
	}

	private List<AgentStatisticsVo> selectStatisticsListPageList(Map<Object, Object> map) {
		return agentIncomeMapper.selectStatisticsAgent(map);
	}

	public IncomeResult<ListRange<AgentStatisticsVo>> selectStatisticsPageList(Map<Object, Object> map) {
		IncomeResult<ListRange<AgentStatisticsVo>> AgentIncomeDetailResult = new IncomeResult<ListRange<AgentStatisticsVo>>();
		ListRange<AgentStatisticsVo> list = new ListRange<AgentStatisticsVo>();
		List<String> ids = new ArrayList<String>();
		Map<Object, Object> param = new HashMap<Object, Object>();
		String id = map.get("userId").toString();
		param.put("userId", id);
		String subAgentids = agentIncomeMapper.selectSubAgent(param);
		param.clear();
		if (subAgentids.length() > 0) {
			String[] split = subAgentids.split(",");
			for (String string : split) {
				ids.add(string);
			}
			Page page = (Page) map.get("page");
			param.put("ids", ids);
			param.put("page", page);
			int count = statisticsListCount(param);
			page.setCount(count);
			list.setPage(page);
			list.setData(selectStatisticsListPageList(param));
			AgentIncomeDetailResult.setContent(list);
			AgentIncomeDetailResult.setState(1);

		}
		return AgentIncomeDetailResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processComputeAgentIncome(Advert advert) {
		Map map = new HashMap();
		/*查询收益配置参数*/
		AgentIncomeParameter parameter = queryParams();
		BigDecimal baseBigDecimal = new BigDecimal(0.01);
		BigDecimal baseAmount = new BigDecimal(advert.getRedEnvelopeAmount());
		/*计算好友(前提条件:好友为代理商)三级收益、推广收益*/
		Long userId = advert.getUserId();
		int i = 0;
		while (i < 3) {
			map.clear();
			map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
			map.put("newUserId", userId);
			UserInvite userInvite = userInviteMapper.selectModel(map);
			if(userInvite != null){
				userId = userInvite.getUserId();
				map.clear();
				map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
				map.put("userId", userId);
				UserAgent userAgent = userAgentMapper.selectModel(map);
				if(userAgent != null){
					AgentIncomeStatistics agentIncomeStatistics = getAis(advert, userAgent, i);
					int level = userAgent.getAgentLevel();
					switch (level) {
					case 2:
						BigDecimal countyBigDecimal = new BigDecimal(parameter.getPublishAdvertIncomeCounty());
						BigDecimal countyAmount = baseAmount.multiply(countyBigDecimal).multiply(baseBigDecimal);
						agentIncomeStatistics.setIncomeAmount(countyAmount);
						break;
					case 3:
						BigDecimal centreBigDecimal = new BigDecimal(parameter.getPublishAdvertIncomeCentre());
						BigDecimal centreAmount = baseAmount.multiply(centreBigDecimal).multiply(baseBigDecimal);
						agentIncomeStatistics.setIncomeAmount(centreAmount);
						map.clear();
						map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
						map.put("newUserId", userAgent.getUserId());
						UserInvite centreUserInvite = userInviteMapper.selectModel(map);
						if(centreUserInvite != null){
							map.clear();
							map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
							map.put("userId", centreUserInvite.getUserId());
							UserAgent agent = userAgentMapper.selectModel(map);
							if(agent != null && agent.getAgentLevel() == AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker()){
								AgentIncomeStatistics incomeStatistics = getAis(advert, agent, 5);
								BigDecimal countyToCentreBigDecimal = new BigDecimal(parameter.getPromotionCommissionAdvertIncomeCountyToCentre());
								BigDecimal countyToCentreAmount = centreAmount.multiply(countyToCentreBigDecimal).multiply(baseBigDecimal);
								incomeStatistics.setIncomeAmount(countyToCentreAmount);
								agentIncomeStatisticsMapper.insertSelective(incomeStatistics);
							}
						}
						break;
					case 4:
						BigDecimal starlevelBigDecimal = new BigDecimal(parameter.getPublishAdvertIncomeStarlevel());
						BigDecimal starlevelAmount = baseAmount.multiply(starlevelBigDecimal).multiply(baseBigDecimal);
						agentIncomeStatistics.setIncomeAmount(starlevelAmount);
						map.clear();
						map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
						map.put("newUserId", userAgent.getUserId());
						UserInvite starUserInvite = userInviteMapper.selectModel(map);
						if(starUserInvite != null){
							map.clear();
							map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
							map.put("userId", starUserInvite.getUserId());
							UserAgent starAgent = userAgentMapper.selectModel(map);
							if(starAgent != null && starAgent.getAgentLevel() != AgentIncomeStatisticsEnum.AGENT_LEVEL_4.getMarker()){
								int starLevel = starAgent.getAgentLevel();
								switch (starLevel) {
								case 2:
									AgentIncomeStatistics incomeStatistics = getAis(advert, starAgent, 6);
									BigDecimal countyToStarBigDecimal = new BigDecimal(parameter.getPromotionCommissionAdvertIncomeCountyToStarlevel());
									BigDecimal countyToStarAmount = starlevelAmount.multiply(countyToStarBigDecimal).multiply(baseBigDecimal);
									incomeStatistics.setIncomeAmount(countyToStarAmount);
									agentIncomeStatisticsMapper.insertSelective(incomeStatistics);
									break;
								case 3:
									AgentIncomeStatistics centreToStarIncomeStatistics = getAis(advert, starAgent, 6);
									BigDecimal centreToStarBigDecimal = new BigDecimal(parameter.getPromotionCommissionAdvertIncomeCentreToStarlevel());
									BigDecimal centreToStarAmount = starlevelAmount.multiply(centreToStarBigDecimal).multiply(baseBigDecimal);
									centreToStarIncomeStatistics.setIncomeAmount(centreToStarAmount);
									agentIncomeStatisticsMapper.insertSelective(centreToStarIncomeStatistics);
									break;
								default:
									break;
								}
							}
						}
						break;
					default:
						agentIncomeStatistics.setIncomeAmount(new BigDecimal(0));
						break;
					}
					agentIncomeStatisticsMapper.insertSelective(agentIncomeStatistics);
				}
			}else{
				break;
			}
			i++;
		}
		/*计算区域收益、推荐收益*/
		String areaCode = advert.getAreaCode();
		if(areaCode != null && !areaCode.isEmpty()){
			//区域商家数统计
			map.clear();
			try {
				map.put("statDate", DateUtils.formatDate(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("userId", advert.getUserId());
			map.put("countyCode", areaCode);
			AreaBusiness areaBusiness = userAgentMapper.selectAreaBusiness(map);
			if(areaBusiness == null){
				AreaBusiness insertAreaBusiness = new AreaBusiness();
				insertAreaBusiness.setStatDate(new Date());
				insertAreaBusiness.setUserId(advert.getUserId());
				insertAreaBusiness.setAdvertCount(1);
				insertAreaBusiness.setCountyCode(areaCode);
				userAgentMapper.insertAreaBusiness(insertAreaBusiness);
			}else{
				map.clear();
				map.put("id", areaBusiness.getId());
				map.put("advertCount", areaBusiness.getAdvertCount() + 1);
				userAgentMapper.updateAreaBusinessByMap(map);
			}
			//区域收益
			map.clear();
			map.put("countyCode", areaCode);
			map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
			UserAgent userAgent = userAgentMapper.selectModel(map);
			if(userAgent != null && userAgent.getAgentLevel() == AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker()){
				AgentIncomeStatistics incomeStatistics = getAis(advert, userAgent, 3);
				BigDecimal areaBigDecimal = new BigDecimal(parameter.getAreaAdvertIncomeCounty());
				BigDecimal areaAmount = baseAmount.multiply(areaBigDecimal).multiply(baseBigDecimal);
				incomeStatistics.setIncomeAmount(areaAmount);
				agentIncomeStatisticsMapper.insertSelective(incomeStatistics);

				//推荐收益
				map.clear();
				map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
				map.put("newUserId", userAgent.getUserId());
				UserInvite userInvite = userInviteMapper.selectModel(map);
				if(userInvite != null){
					map.clear();
					map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
					map.put("userId", userInvite.getUserId());
					UserAgent agent = userAgentMapper.selectModel(map);
					if(agent != null){
						AgentIncomeStatistics statistics = getAis(advert, agent, 4);
						int level = agent.getAgentLevel();
						statistics.setAgentLevel(level);
						switch (level) {
						case 2:
							BigDecimal countyBigDecimal = new BigDecimal(parameter.getRecommendAdvertIncomeCounty());
							BigDecimal countyAmount = baseAmount.multiply(countyBigDecimal).multiply(baseBigDecimal);
							statistics.setIncomeAmount(countyAmount);
							break;
						case 3:
							BigDecimal centreBigDecimal = new BigDecimal(parameter.getRecommendAdvertIncomeCentre());
							BigDecimal centreAmount = baseAmount.multiply(centreBigDecimal).multiply(baseBigDecimal);
							statistics.setIncomeAmount(centreAmount);
							break;
						case 4:
							BigDecimal starlevelBigDecimal = new BigDecimal(parameter.getRecommendAdvertIncomeStarlevel());
							BigDecimal starlevelAmount = baseAmount.multiply(starlevelBigDecimal).multiply(baseBigDecimal);
							statistics.setIncomeAmount(starlevelAmount);
							break;
						default:
							statistics.setIncomeAmount(new BigDecimal(0));
							break;
						}
						agentIncomeStatisticsMapper.insertSelective(statistics);
					}
				}
			}
		}
		
	}
	
	
	//查询代理商收益参数
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AgentIncomeParameter queryParams(){
		SystemGlobalConfig sgc;
		Map map = new HashMap();
		AgentIncomeParameter parameter = new AgentIncomeParameter();
		 	
		map.clear();
		map.put("configKey", "proc.publish.advert.income.county");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPublishAdvertIncomeCounty(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.publish.advert.income.centre");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPublishAdvertIncomeCentre(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.publish.advert.income.starlevel");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPublishAdvertIncomeStarlevel(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.area.advert.income.county");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setAreaAdvertIncomeCounty(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.promotion.commission.advert.income.county-to-centre");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPromotionCommissionAdvertIncomeCountyToCentre(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.promotion.commission.advert.income.county-to-starlevel");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPromotionCommissionAdvertIncomeCountyToStarlevel(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.promotion.commission.advert.income.centre-to-starlevel");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPromotionCommissionAdvertIncomeCentreToStarlevel(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.recommend.advert.income.county");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setRecommendAdvertIncomeCounty(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.recommend.advert.income.centre");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setRecommendAdvertIncomeCentre(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "proc.recommend.advert.income.starlevel");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setRecommendAdvertIncomeStarlevel(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		map.clear();
		map.put("configKey", "system.publish.advert.deduct");
		map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
		sgc = systemGlobalConfigMapper.selectModel(map);
		parameter.setPublishAdvertDeduct(Double.valueOf(sgc != null ? sgc.getConfigContent() : "0"));
		
		return parameter;
	}
	
	//构造收益实体
	private AgentIncomeStatistics getAis(Advert advert, UserAgent userAgent, int i){
		AgentIncomeStatistics incomeStatistics = new AgentIncomeStatistics();
		incomeStatistics.setUserId(advert.getUserId());
		incomeStatistics.setIncomeUserId(userAgent.getUserId());
		int level = userAgent.getAgentLevel();
		incomeStatistics.setAgentLevel(level);
		switch (i) {
		case 0:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_1.getMarker());
			break;
		case 1:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_2.getMarker());					
			break;
		case 2:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_3.getMarker());
			break;
		case 3:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_4.getMarker());
			break;
		case 4:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_5.getMarker());
			break;
		case 5:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_6.getMarker());
			break;
		case 6:
			incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_7.getMarker());
			break;
		default:
			break;
		}
		incomeStatistics.setIncomeDate(new Date());
		incomeStatistics.setCreateTime(new Date());
		return incomeStatistics;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ListRange<AgentIncomeStatisticsVo> selectAgentIncomeStatisticsPageList(Map map) {
		ListRange<AgentIncomeStatisticsVo> listRange = new ListRange<AgentIncomeStatisticsVo>();
		int count = agentIncomeStatisticsMapper.selectAgentIncomeStatisticsCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<AgentIncomeStatisticsVo> agentIncomeStatisticsVos = agentIncomeStatisticsMapper.selectAgentIncomeStatisticsPageList(map);
		if(agentIncomeStatisticsVos != null && agentIncomeStatisticsVos.size() > 0){
			for(AgentIncomeStatisticsVo statisticsVo : agentIncomeStatisticsVos){
				map.clear();
				map.put("incomeUserId", statisticsVo.getIncomeUserId());
				map.put("incomeDate", statisticsVo.getIncomeDate());
				statisticsVo.setBusinessTotal(agentIncomeStatisticsMapper.selectBusinessCount(map));
				if(statisticsVo.getAgentLevel().equals(String.valueOf(AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker()))){
					map.clear();
					map.put("userId", statisticsVo.getIncomeUserId());
					UserAgent userAgent = userAgentMapper.selectModel(map);
					if(userAgent != null && userAgent.getAgentLevel() == AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker()){
						map.clear();
						map.put("incomeDate", statisticsVo.getIncomeDate());
						map.put("countyCode", userAgent.getCountyCode());
						statisticsVo.setAreaBusinessTotal(agentIncomeStatisticsMapper.selectAreaAdvertCount(map));
					}
				}
				map.clear();
				map.put("incomeUserId", statisticsVo.getIncomeUserId());
				map.put("incomeDate", statisticsVo.getIncomeDate());
				statisticsVo.setPromotionAgentTotal(agentIncomeStatisticsMapper.selectExpandAgentCount(map));
			}
		}
		listRange.setData(agentIncomeStatisticsVos);
		return listRange;
	}
	
}
