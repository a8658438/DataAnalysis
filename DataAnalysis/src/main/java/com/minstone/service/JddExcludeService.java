package com.minstone.service;

import java.util.List;
import java.util.Map;

public interface JddExcludeService {
	/**
	 * 获取每期不出的区间
	 * @param startId
	 * @param endId
	 * @param orderBy
	 * @return
	 */
	public Map<Integer, List<Integer>> queryStageExcludeArea(Integer startId,Integer endId,String orderBy);
}
