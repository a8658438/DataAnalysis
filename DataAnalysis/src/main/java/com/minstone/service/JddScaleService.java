package com.minstone.service;

import java.util.Map;

public interface JddScaleService {
	/**
	 * 批量插入各个数字的数据
	 * @param numberData
	 */
	void saveBatch(Map<Integer, Map<String, Integer>> numberData,String column);
}
