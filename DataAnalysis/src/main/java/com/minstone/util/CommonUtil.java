package com.minstone.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
