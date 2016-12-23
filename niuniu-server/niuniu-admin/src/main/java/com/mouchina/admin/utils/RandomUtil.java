package com.mouchina.admin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
	/**
	 * 获取运营活动中奖人数
	 * 
	 * @param scopeBegin0
	 * @param scopeEnd0
	 * @return
	 */
	public static List<StringBuffer> getRandom(String scopeBegin0, String scopeEnd0, String[] numMax) {
		String[] strArray = new String[numMax.length];
		for (int i = 0; i < numMax.length; i++) {
			String scope = scopeBegin0 + "-" + scopeEnd0 + "," + numMax[i];
			strArray[i] = scope;
		}
		
		List<StringBuffer> list = new ArrayList<>();//存放所有区间中奖人数字符串
		List<Integer> checkList = new ArrayList<>();// 检查list
		if (strArray.length > 0) {
			// 循环每一条记录
			for (int i = 0; i < strArray.length; i++) {
				StringBuffer endBuffer = new StringBuffer();
				String currVo = strArray[i];
				String[] tempArr = currVo.split(",");
				String currPersons = tempArr[0];// 人数区间
				String currAwardNum = tempArr[1];// 出奖个数区间
				String[] numarr = currAwardNum.split("-");
				// 循环开奖个数的数组
				StringBuffer probility = new StringBuffer();// 2,1,1(555~666,1444,2888)
				for (int j = 0; j < 3; j++) {
					StringBuffer currNumProbility = new StringBuffer();// 555~666
					int currNum = Integer.parseInt(numarr[j]);// 当前开奖个数
					if (currNum > 0) {
						
						// 循环开奖个数生成随机数
						for (int k = 0; k < currNum; k++) {
							int gailvNum = 0;
							int[] sePersons = null;
							if (j == 0) {
								sePersons = new int[] { 0, Integer.parseInt(currPersons.split("-")[0]) };
								gailvNum = returnGailv(sePersons, checkList);
								checkList.add(gailvNum);
								if (k == currNum - 1) {
									currNumProbility.append(gailvNum); 
								}else{
									currNumProbility.append(gailvNum + "~");
								}
							} else if (j == 1) {
								sePersons = new int[] { Integer.parseInt(currPersons.split("-")[0]),
										Integer.parseInt(currPersons.split("-")[1]) };

								gailvNum = returnGailv(sePersons, checkList);
								checkList.add(gailvNum);
								if (k == currNum - 1) {
									currNumProbility.append(gailvNum); 
								}else{
									currNumProbility.append(gailvNum + "~");
								}
							}else if(j == 2){
								sePersons = new int[] { Integer.parseInt(currPersons.split("-")[1]),
										Integer.parseInt(currPersons.split("-")[1])+500 };

								gailvNum = returnGailv(sePersons, checkList);
								checkList.add(gailvNum);
								if (k == currNum - 1) {
									currNumProbility.append(gailvNum);
								}else{
									currNumProbility.append(gailvNum + "~");
								}
							}
						}
					} else if(currNum == 0){
						// 不会开出奖品
						currNumProbility.append(0);
					}
					if(j == numarr.length-1){
						probility.append(currNumProbility);
					}else{
						probility.append(currNumProbility + ",");
					}
				}
				endBuffer.append(probility);
				list.add(endBuffer);
			}
		}
		

		return list;
	}
	/**
	 * 产生随机数的方法
	 * @param per
	 * @param list
	 * @return
	 */
	public static int returnGailv(int[] per, List list) {
		int start = per[0];// 人数起始
		int end = per[1];// 人数结束

		int randomNum = new Random().nextInt(end - start)+start + 1;
		while (true) {
			if (!list.contains(randomNum)) {
				return randomNum;
			} else {
				randomNum = new Random().nextInt(end - start)+start + 1;
			}
		}
	}
}
