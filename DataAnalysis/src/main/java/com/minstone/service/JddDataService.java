package com.minstone.service;

import java.util.List;
import java.util.Map;

import com.minstone.util.Result;

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
	Map<String, Integer> selectMaxAndMinByColumn(Map<String , Object> param);

	/**
	 * 统计每一列各个值出现的次数
	 * @param param
	 * @return
	 */
	List<Map<String, Long>> selectCountByColumn(Map<String, Object> param);
	
	/**
	 * 统计某一列值的出现的区间概率,返回各数值出现的概率
	 * @param param
	 * @return 
	 */
	Map<Integer, Integer> countColumnProbability(Map<String, Object> param);

	/**
	 * 统计某一期的某一列值出现的区间概率（分别统计近5期、10期、20期、30期），并且返回各数字的值
	 * @param num
	 */
	Map<Integer, Map<String, Integer>> countColumnProbabilityAndRange(Map<String, Object> param);
	
	/**
	 * 获取某一期可能出现的6个数字
	 * @param num
	 * @return
	 */
	Result getMaybeNumbers(Integer num);
}
