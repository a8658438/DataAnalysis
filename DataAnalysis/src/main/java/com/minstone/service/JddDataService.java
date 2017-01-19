package com.minstone.service;

import java.util.List;
import java.util.Map;

import com.minstone.pojo.JddData;
import com.minstone.util.Result;

public interface JddDataService {
	/**
	 * 获取当前期数前几期的数据总和
	 * @param param
	 * @return
	 */
	List<Map<String, Long>> selectSumByStage(Map<String , Object> param);
	
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
	 * 获取每个数字在各列中的数据
	 * @param num
	 * @return
	 */
	Map<Integer, Map<String, Integer>> getNumbersDataForColumn(Integer num,String column);

	/**
	 * 校验是否匹配（计算与前三十期的匹配率）
	 * @param numList
	 * @return 
	 */
	Map<String, Object> validate(List<Integer> numList, Integer id);

	/**
	 * 查询列表数据
	 * @param endId 
	 * @param startId 
	 * @param orderBy 
	 * @return
	 */
	List<JddData> selectAllData(Integer startId, Integer endId, String orderBy);
}
