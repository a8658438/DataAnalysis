package com.minstone.service;

import java.util.List;
import java.util.Map;

public interface JddDataService {
	/**
	 * 获取当前期数前几期的数据总和
	 * @param param
	 * @return
	 */
	List<Map<String, Long>> selectSumByStage(Map<String , Object> param);
	
	/**
	 * 获取各列的最大值和最小值范围
	 * @param param
	 * @return
	 */
	List<Map<String, Long>> selectMaxAndMinByColumn(Map<String , Object> param);

	/**
	 * 统计每一列各个值出现的次数
	 * @param param
	 * @return
	 */
	List<Map<String, Long>> selectCountByColumn(Map<String, Object> param);
	
	/**
	 * 统计某一列值的出现的区间概率
	 * @param param
	 */
	void countColumnProbability(Map<String, Object> param);

	/**
	 * 统计某一期的某一列值出现的区间概率（分别统计近5期、10期、20期、30期）
	 * @param num
	 */
	void countColumnProbability(Integer num,String column);
}
