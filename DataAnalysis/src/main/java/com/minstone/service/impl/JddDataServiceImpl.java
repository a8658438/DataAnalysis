package com.minstone.service.impl;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;

import com.minstone.mapper.JddDataMapper;
import com.minstone.service.JddDataService;
import com.minstone.util.CommonUtil;
import com.minstone.util.Constant;

@Service
public class JddDataServiceImpl implements JddDataService{
	@Autowired
	private JddDataMapper mapper;

	@Override
	public List<Map<String, Long>> selectSumByStage(Map<String , Object> param) {
		List<Map<String,Long>> list = mapper.selectSumByStage(param);
//		for (Map<String, Long> map : list) {
//			System.out.println("期数："+map.get("id")+">>总和："+map.get("sum_num"));
//		}
		return list;
	}

	@Override
	public List<Map<String, Long>> selectMaxAndMinByColumn(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectMaxAndMinByColumn(param);
		return list;
	}
	
	@Override
	public List<Map<String, Long>> selectCountByColumn(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectCountByColumn(param);
		return list;
	}

	@Override
	public void countColumnProbability(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectCountByColumn(param);
		int min = 49,max = 0,percent=0;
		for (Map<String, Long> map : list) {
			int currentNum = Integer.parseInt(map.get(param.get("column"))+"");//当前数字
			Double divi = Double.parseDouble(param.get("countNum").toString());//期数
			//统计各个数值出现的概率
			Integer  d = (int) (Double.parseDouble(map.get("count_num").toString()) / divi *100);
			System.out.println("数字："+currentNum+"==>比例："+d);
			
			//数字概率根据小于5期要大于30，小于10期要大于20，小于20期要大于10，小于30期要大于5的方式的计算入区间
			if (d >= (divi <= Constant.NUM_5?Constant.NUM_30:divi <= Constant.NUM_10?Constant.NUM_20:divi<=20?Constant.NUM_10:Constant.NUM_5)) {
				min = currentNum < min ? currentNum : min;
				max = currentNum > max ? currentNum : max;
				percent += d;
			}
		}
		System.out.println("区间:"+min+"至"+max+"==>概率："+percent);
	}
	
	@Override
	public void countColumnProbability(Integer num,String column) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("endNum", num - 1);
		param.put("column", column);

		for (int i = 0; i < 4; i++) {
			Integer stageNum = i == 0?Constant.NUM_5:i==1?Constant.NUM_10:i==2?Constant.NUM_20:i==3?Constant.NUM_30:0;
			//统计5期
			param.put("startNum", num - stageNum);
			param.put("countNum", stageNum);
			this.countColumnProbability(param);
		}
	}
	
	public static void main(String[] args) {
		double a = 10.2,b=2.56;
		System.out.println(new BigDecimal(a/b).setScale(2, RoundingMode.HALF_EVEN));
	}
}
