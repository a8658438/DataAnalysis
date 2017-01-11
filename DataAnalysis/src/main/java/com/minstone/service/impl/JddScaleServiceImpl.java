package com.minstone.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minstone.mapper.JddScaleMapper;
import com.minstone.pojo.JddScale;
import com.minstone.service.JddScaleService;
import com.minstone.util.Constant;

@Service
@Transactional
public class JddScaleServiceImpl implements JddScaleService {
	@Autowired
	private JddScaleMapper mapper;

	@Override
	public void saveBatch(Map<Integer, Map<String, Integer>> numberData,String column) {
		//插入前先清空该列数据
		mapper.deleteColumnData(column);
		
		//循环插入数据
		for (Integer num : numberData.keySet()) {
			Map<String, Integer> numMap = numberData.get(num);
			
			JddScale s = new JddScale();
			s.setNum(num);
			s.setLateAllScale(numMap.get(Constant.LATE_ALL_SCALE));
			s.setOtherScale(numMap.get(Constant.OTHER_SCALE));
			s.setAllAvgLevel(numMap.get(Constant.ALL_AVG_LEVEL));
			s.setColumnAvgLevel(numMap.get(Constant.COLUMN_AVG_LEVEL));
			s.setColumnName(column);
			
			mapper.insert(s);
		}
	}

}
