package com.mouchina.base.utils.redpacket;

import java.util.Random;
/**
 * 红包算法
 * @author 侯斌飞
 *
 */
public final class UtilsRedPacket {
	/**
	 * 获取随机红包
	 * <pre>
	 * 单位：分
	 * </pre>
	 * @param totalMoney 红包总额
	 * @param balanceMoney 未领红包总额
	 * @param totalNumber 红包总数量
	 * @param balanceNumber 未领红包个数
	 * @return
	 */
	public static int randomRedPacket(int totalMoney,int balanceMoney,int totalNumber,int balanceNumber){
		if(balanceNumber == 1){
			return balanceMoney;
		}
		int mean = totalMoney/totalNumber;
		int maxMoney = mean * 2;
		int minMoney = 20;
		
		if(mean <= minMoney){
			return mean;
		}
		
		Random random = new Random(System.nanoTime());
		while (true) {
			int temp = random.nextInt(maxMoney-minMoney) + minMoney;
			
			if(temp > maxMoney || temp < minMoney || temp >= balanceMoney - minMoney * (balanceNumber -1)){
				// 获取随机数 不能大于 最大值，不能小于最小值，不能大于等于 剩余未领红包总额最小值(未领红包个数*最小领取金额)
				// 否则重新获取
				continue;
			}
			// 返回随机金额
			return temp;
		}	
	}
	
	/**
	 * 获取等额红包金额
	 * <pre>
	 * 单位：分
	 * </pre>
	 * @param totalMoney
	 * @param totalNumber
	 */
	public static int meanRedPacket(int totalMoney,int totalNumber){
		return totalMoney/totalNumber;
	}
	
	public static void main(String[] args) {
		int totalMoney = 100,balanceMoney = 100 ,totalNumber = 5;
		int temp = 0;
		for (int i = 0; i < totalNumber; i++) {
			temp = UtilsRedPacket.randomRedPacket(totalMoney, balanceMoney, totalNumber, totalNumber - i);
			balanceMoney -= temp;
			System.out.println(String.format("第 %s 次, 获取红包金额：%s，剩余金额：%s", new Object[]{
					i+1,temp,balanceMoney
			}));
		}
	}
}
