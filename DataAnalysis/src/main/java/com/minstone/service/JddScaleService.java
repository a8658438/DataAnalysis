package com.minstone.service;

import java.util.List;
import java.util.Map;

public interface JddScaleService {
	/**
	 * 批量插入各个数字的数据
	 * @param numberData
	 */
	void saveBatch(Map<Integer, Map<String, Integer>> numberData,String column);
	
	/**
	 * 按列查询每个数字的综合排名前十位
	 * @param column
	 * @return
	 */
	List<Map<String, Object>> queryColumnRank(String column);

	/**
	 * 计算可能出现的数字
	 * @param rank
	 * @param numberData
	 * @param numList
	 * @return
	 */
	List<Integer> getMaybeNumbers(List<Map<String, Object>> rank,
			Map<Integer, Map<String, Integer>> numberData, List<Integer> numList);

}
