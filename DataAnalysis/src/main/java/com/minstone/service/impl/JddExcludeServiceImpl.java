package com.minstone.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minstone.pojo.JddData;
import com.minstone.service.JddDataService;
import com.minstone.service.JddExcludeService;

@Service
public class JddExcludeServiceImpl implements JddExcludeService {
	@Autowired
	private JddDataService dataService;
	
	@Override
	public Map<Integer, List<Integer>> queryStageExcludeArea(
			Integer startId, Integer endId, String orderBy) {
		List<JddData> list = dataService.selectAllData(startId, endId,orderBy);
		Map<Integer, List<Integer>> map = new TreeMap<Integer, List<Integer>>();
		for (JddData jddData : list) {//循环获取每期不由出数字的区间
			List<Integer> areas = new ArrayList<Integer>();
			for (int i = 1; i < 8; i++) {
				int min=(i-1)*7+1,max=i*7;//定义区间范围
				if ((min <= jddData.getOne() && jddData.getOne() <= max)
						|| (min <= jddData.getTwo() && jddData.getTwo() <= max)
						|| (min <= jddData.getThree() && jddData.getThree() <= max)
						|| (min <= jddData.getFour() && jddData.getFour() <= max)
						|| (min <= jddData.getFive() && jddData.getFive() <= max)
						|| (min <= jddData.getSix() && jddData.getSix() <= max)) {
					
				}else{//没有出的区间
					areas.add(i);
				}
			}
			map.put(jddData.getId(), areas);
		}
		return map;
	}

}
