package com.minstone.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author vic
 */
public class CommonUtil {
	/**
	 * 指定某个数据保留小数个数并返回
	 * @param num
	 * @param scaleNum
	 * @return
	 */
	public static Double getNumDecimal(Double num,int scaleNum){
		return new BigDecimal(num).setScale(scaleNum, RoundingMode.HALF_EVEN).doubleValue();
	}
	/**
	 * 获取1到49的数字集合
	 * @return
	 */
	public static List<Integer> getAllNumbers() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			list.add(i);
		}
		return list ;
	}
}
