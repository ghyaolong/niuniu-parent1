package com.mouchina.moumou_server_dubbo.provider.income;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.exception.BusinessException;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.dao.income.UserIncomeMapper;
import com.mouchina.moumou_server.dao.income.UserIncomeSumMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.pay.UserCashFlowMapper;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.vo.user.particular.UserIncomeDo;
import com.mouchina.moumou_server_interface.income.UserIncomeService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.IncomeResult;
import com.mouchina.moumou_server_interface.view.UserResult;

public class UserIncomeServiceSupport implements UserIncomeService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserIncomeAwardServiceSupport.class);

	private static final String USER_INCOME_GET_LOCK = "USER_INCOME_GET_LOCK_";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

	@Resource
	private UserIncomeMapper userIncomeMapper;
	@Resource
	private UserIncomeSumMapper userIncomeSumMapper;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private RedisLockHandler redisLockHandler;
	@Resource
	private UserCashFlowMapper userCashFlowMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPartMapper userPartMapper;

	@Override
	public IncomeResult<UserIncome> selectUserIncome(Long UserIncomeId) {
		IncomeResult<UserIncome> IncomeResult = new IncomeResult<UserIncome>();
		UserIncome UserIncome = userIncomeMapper.selectByPrimaryKey(UserIncomeId);
		if (UserIncome != null) {
			IncomeResult.setContent(UserIncome);
			IncomeResult.setState(1);
		}
		return IncomeResult;
	}

	@Override
	public IncomeResult<UserIncome> selectUserIncome(Map<String, Object> map) {
		IncomeResult<UserIncome> UserIncomeResult = new IncomeResult<UserIncome>();
		UserIncome UserIncome = userIncomeMapper.selectModel(map);
		if (UserIncome != null) {
			UserIncomeResult.setContent(UserIncome);
			UserIncomeResult.setState(1);
		}
		return UserIncomeResult;
	}

	@Override
	public IncomeResult<UserIncome> addUserIncome(UserIncome UserIncome) {
		IncomeResult<UserIncome> UserIncomeResult = new IncomeResult<UserIncome>();
		UserIncome.setCreateTime(new Date());
		int result = userIncomeMapper.insertSelective(UserIncome);
		UserIncomeResult.setContent(UserIncome);
		UserIncomeResult.setState(result);

		return UserIncomeResult;
	}

	private int selectListUserIncomeCount(Map<String, Object> map) {
		return userIncomeMapper.selectCount(map);
	}

	private List<UserIncome> selectPageListUserIncome(Map<String, Object> map) {
		return userIncomeMapper.selectPageList(map);
	}

	private int selectListUserIncomeCountCal(Map<String, Object> map) {
		return userIncomeMapper.selectIncomeCount(map);
	}

	private List<UserIncome> selectPageListUserIncomeCal(Map<String, Object> map) {
		return userIncomeMapper.selectIncomePageList(map);
	}

	@Override
	public IncomeResult<ListRange<UserIncome>> selectListUserIncomePage(Map<String, Object> map) {
		IncomeResult<ListRange<UserIncome>> UserIncomeResult = new IncomeResult<ListRange<UserIncome>>();
		ListRange<UserIncome> listRange = new ListRange<UserIncome>();
		int count = selectListUserIncomeCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListUserIncome(map));
		UserIncomeResult.setContent(listRange);
		UserIncomeResult.setState(1);
		return UserIncomeResult;
	}

	@Override
	public IncomeResult<Integer> deleteUserIncome(Long UserIncomeId) {
		IncomeResult<Integer> UserIncomeResult = new IncomeResult<Integer>();
		int result = userIncomeMapper.deleteByPrimaryKey(UserIncomeId);
		UserIncomeResult.setState(result);
		return UserIncomeResult;
	}

	@Override
	public IncomeResult<Integer> updateUserIncome(UserIncome UserIncome) {
		IncomeResult<Integer> UserIncomeResult = new IncomeResult<Integer>();
		int result = userIncomeMapper.updateByPrimaryKeySelective(UserIncome);
		UserIncomeResult.setState(result);
		return UserIncomeResult;
	}

	@Override
	public IncomeResult<List<UserIncome>> selectListUserIncome(Map<String, Object> map) {
		IncomeResult<List<UserIncome>> IncomeResult = new IncomeResult<List<UserIncome>>();
		List<UserIncome> list = selectPageListUserIncome(map);
		IncomeResult.setContent(list);
		return IncomeResult;
	}

	@Override
	public int insertCalUserIncomeByDay(Date date) {
		int result = 0;
		Map<String, Object> checkMap = new HashMap<String, Object>();
		checkMap.put("state", 1);
		checkMap.put("incomeDate", date);
		int pageSize = 100;
		int maxPage = 10;
		int sumCount = selectListUserIncomeCountCal(checkMap);

		int pageTatal = sumCount / pageSize;
		if ((sumCount + pageSize) % pageSize != 0) {
			pageTatal++;
		}

		if (pageTatal > maxPage) {
			pageTatal = maxPage;

		}
		for (int i = 0; i < pageTatal; i++) {
			Map<String, Object> selectMap = new HashMap<String, Object>();
			Page page = new Page(0, pageSize);
			page.setBegin(i * pageSize);
			selectMap.put("state", 1);
			selectMap.put("group", "user_id");
			selectMap.put("incomeDate", date);
			List<UserIncome> incomeList = selectPageListUserIncomeCal(selectMap);
			if (incomeList != null) {
				for (UserIncome userIncome : incomeList) {

					int r = insertUserIncomeByDay(userIncome);
					if (r == 1) {
						result++;
					}

				}
			}
		}

		return result;
	}

	@Override
	public int insertUserIncomeByDay(UserIncome userIncome) {
		int result = 0;
		List<String> userIncomeLocks = new ArrayList<String>();
		String userIncomeLock = USER_INCOME_GET_LOCK + userIncome.getUserId() + "_"
				+ DateUtils.getDateString(userIncome.getIncomeDate());
		userIncomeLocks.add(userIncomeLock);
		try {
			if (redisLockHandler.tryLock(userIncomeLock)) {
				Map<String, Object> checkMap = new HashMap<String, Object>();
				checkMap.put("userId", userIncome.getUserId());
				checkMap.put("incomeDate", userIncome.getIncomeDate());
				IncomeResult<List<UserIncomeSum>> listIncomeResult = selectListUserIncomeSum(checkMap);
				if (listIncomeResult.getContent().size() == 0) {
					UserIncomeSum userIncomeSum = new UserIncomeSum();
					userIncomeSum.setAmount(userIncome.getAmout());
					userIncomeSum.setSourceAmout(userIncome.getSourceAmount());
					userIncomeSum.setUserId(userIncome.getUserId());
					userIncomeSum.setDescb(userIncome.getEnventName());
					userIncomeSum.setIncomeSize(userIncome.getEventUserId().intValue());
					userIncomeSum.setAloneUserIncomeSize(userIncome.getEventUserId().intValue());
					userIncomeSum.setIncomeDate(userIncome.getIncomeDate());
					userIncomeSum.setDescb("邀请好友每日收益");
					IncomeResult<UserIncomeSum> incomeResult = addUserIncomeSum(userIncomeSum);
					if (incomeResult.getState() == 1) {
						userIncomeSum = incomeResult.getContent();
						UserResult<User> userResult = userVerifyService.selectUserByUserId(userIncome.getUserId());
						UserAssets userAssets = userVerifyService.getUserAssetsByUser(userResult.getContent());
						userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + userIncome.getAmout());
						userAssets.setTableNum(userVerifyService.userPartTableNum(userResult.getContent().getPhone()));

						userVerifyService.updateUserAssets(userAssets);

						// UserCashFlow userCashFlow = new UserCashFlow();
						//
						// userCashFlow.setCashFlowTitle(DateUtils.getDateString(userIncome.getIncomeDate())+"邀请好友收益");
						//
						// userCashFlow.setPrice(userIncome.getAmout());
						// String tableNum = DateUtils.getDateStringYYYY(new
						// Date());
						// userCashFlow.setTableNum(tableNum);
						// userCashFlow.setBalance(userAssets.getCashBalance());
						// userCashFlow.setUserId(userIncome.getUserId());
						// userCashFlow.setTriggerNo(userIncomeSum.getId() +
						// "");
						// userCashFlowMapper.insertSelective(userCashFlow);
					}

				}

				Map<String, Object> updateMap = new HashMap<String, Object>();
				updateMap.put("userId", userIncome.getUserId());
				updateMap.put("state", 1);
				updateMap.put("afterState", 3);
				updateMap.put("incomeDate", userIncome.getIncomeDate());
				userIncomeMapper.updateByUserIdAndIncomeDateSelective(updateMap);

			}
		} catch (Exception e) {

		} finally {
			for (String lock : userIncomeLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return result;
	}

	@Override
	public int insertCalUserInvertAdvertPushlishedByDay(Date date) {
		return 0;
	}

	@Override
	public IncomeResult<UserIncomeSum> selectUserIncomeSum(Long UserIncomeSumId) {
		IncomeResult<UserIncomeSum> IncomeResult = new IncomeResult<UserIncomeSum>();
		UserIncomeSum UserIncomeSum = userIncomeSumMapper.selectByPrimaryKey(UserIncomeSumId);
		if (UserIncomeSum != null) {
			IncomeResult.setContent(UserIncomeSum);
			IncomeResult.setState(1);
		}

		return IncomeResult;
	}

	@Override
	public IncomeResult<UserIncomeSum> selectUserIncomeSum(Map<String, Object> map) {
		IncomeResult<UserIncomeSum> UserIncomeSumResult = new IncomeResult<UserIncomeSum>();

		UserIncomeSum UserIncomeSum = userIncomeSumMapper.selectModel(map);
		if (UserIncomeSum != null) {
			UserIncomeSumResult.setContent(UserIncomeSum);
			UserIncomeSumResult.setState(1);
		}

		return UserIncomeSumResult;
	}

	@Override
	public IncomeResult<UserIncomeSum> addUserIncomeSum(UserIncomeSum userIncomeSum) {
		IncomeResult<UserIncomeSum> UserIncomeSumResult = new IncomeResult<UserIncomeSum>();
		userIncomeSum.setCreateTime(new Date());
		int result = userIncomeSumMapper.insertSelective(userIncomeSum);
		UserIncomeSumResult.setContent(userIncomeSum);
		UserIncomeSumResult.setState(result);
		return UserIncomeSumResult;
	}

	private int selectListUserIncomeSumCount(Map<String, Object> map) {
		return userIncomeSumMapper.selectCount(map);
	}

	private List<UserIncomeSum> selectPageListUserIncomeSum(Map<String, Object> map) {
		return userIncomeSumMapper.selectPageList(map);
	}

	@Override
	public IncomeResult<ListRange<UserIncomeSum>> selectListUserIncomeSumPage(Map<String, Object> map) {
		IncomeResult<ListRange<UserIncomeSum>> userIncomeSumResult = new IncomeResult<ListRange<UserIncomeSum>>();
		ListRange<UserIncomeSum> listRange = new ListRange<UserIncomeSum>();
		int count = selectListUserIncomeSumCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListUserIncomeSum(map));
		userIncomeSumResult.setContent(listRange);
		userIncomeSumResult.setState(1);
		return userIncomeSumResult;
	}

	@Override
	public IncomeResult<Integer> deleteUserIncomeSum(Long userIncomeSumId) {
		IncomeResult<Integer> userIncomeSumResult = new IncomeResult<Integer>();
		int result = userIncomeSumMapper.deleteByPrimaryKey(userIncomeSumId);
		userIncomeSumResult.setState(result);
		return userIncomeSumResult;
	}

	@Override
	public IncomeResult<Integer> updateUserIncomeSum(UserIncomeSum userIncomeSum) {
		IncomeResult<Integer> userIncomeSumResult = new IncomeResult<Integer>();
		int result = userIncomeSumMapper.updateByPrimaryKeySelective(userIncomeSum);
		userIncomeSumResult.setState(result);
		return userIncomeSumResult;
	}

	@Override
	public IncomeResult<List<UserIncomeSum>> selectListUserIncomeSum(Map<String, Object> map) {
		IncomeResult<List<UserIncomeSum>> incomeResult = new IncomeResult<List<UserIncomeSum>>();
		List<UserIncomeSum> list = selectPageListUserIncomeSum(map);
		incomeResult.setContent(list);
		return incomeResult;
	}

	@Override
	public int selectUserIncomeSumCount(Map<String, Object> map) {
		return userIncomeSumMapper.selectUserIncomeSumCount(map);
	}

	@Override
	public List<UserIncomeSum> selectIncomeList3(Map<String,Object> map) {
		List<UserIncomeSum> userIncomeSums = userIncomeSumMapper.selectIncomeList3(map);
		Integer length = userIncomeSums.size();
		//如果只有一条数据，就不用再次比较
		if(length <= 1) return userIncomeSums;
		
		for (int i = 0; i < length; i++) {
			//只有在加载第一页数据的时候才会在顶部加上月份
			if((Integer)map.get("offset") <= 1 && i == 0 ) {
				//给首位加入月份
				String firstTime = sdf.format(userIncomeSums.get(0).getIncomeDate());
				UserIncomeSum uis1 = new UserIncomeSum();
				uis1.setMonth(firstTime.split("-")[1] + "月");
				userIncomeSums.add(0, uis1);
				continue;
			}
			
			//如果最后一位，就不用再循环
			if(i+1 >= length) return userIncomeSums;
			
			UserIncomeSum userIncomeSumPrev = userIncomeSums.get(i);
			UserIncomeSum userIncomeSumAfter = userIncomeSums.get(i + 1);
			String datePrev = sdf.format(userIncomeSumPrev.getIncomeDate());
			String dateAfter = sdf.format(userIncomeSumAfter.getIncomeDate());
			
			// 如果月份不是一个，那么就出现了跨月份，需要插入月份
			if (!datePrev.equals(dateAfter)) {
				UserIncomeSum uis = new UserIncomeSum();
				uis.setMonth(dateAfter.split("-")[1] + "月");
				userIncomeSums.add(i + 1, uis);
				return userIncomeSums;
			}
		}
		return userIncomeSums;
	}
	

	@Override
	public Integer selectIncomeCount(Long userId) {
		return userIncomeSumMapper.selectIncomeCount(userId);
	}

	@Override
	public List<UserIncomeDo> userIncomeDetailList3(Map<String, Object> map) {
		List<UserIncomeDo> userIncomeDos = userIncomeMapper.userIncomeDetailList3(map);
		if( userIncomeDos == null || userIncomeDos.size() <= 0) {
			logger.error("用户没有收益明细!");
			return null;
		}
		
		for (UserIncomeDo userIncomeDo : userIncomeDos) {
			User user = this.getUser(userIncomeDo.getEventUserId());
			userIncomeDo.setUserNickName(user.getNickName());
		}
		return userIncomeDos;
	}

	/**
	 * 根据userID查询一个用户
	 * 
	 * @param userId
	 * @return
	 */
	private User getUser(Long userId) {
		Map<String, String> userPartMap = new HashMap<String, String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();

		// 查询用户的财富
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("id", userId);
		userMap.put("tableNum", tableIndex);
		return userMapper.selectModel(userMap);
	}

}
