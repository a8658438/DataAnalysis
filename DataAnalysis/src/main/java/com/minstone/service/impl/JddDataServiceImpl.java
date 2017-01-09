package com.minstone.service.impl;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;

import com.minstone.mapper.JddDataMapper;
import com.minstone.pojo.JddData;
import com.minstone.service.JddDataService;
import com.minstone.util.CommonUtil;
import com.minstone.util.Constant;
import com.minstone.util.Result;

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
	public Map<String, Integer> selectMaxAndMinByColumn(Map<String, Object> param) {
		Map<String, Integer> maxAndMin = mapper.selectMaxAndMinByColumn(param);
		return maxAndMin;
	}
	
	@Override
	public List<Map<String, Long>> selectCountByColumn(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectCountByColumn(param);
		return list;
	}

	@Override
	public Map<Integer, Integer> countColumnProbability(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectCountByColumn(param);
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (Map<String, Long> map : list) {
			int currentNum = Integer.parseInt(map.get(param.get("column"))+"");//当前数字
			Double divi = Double.parseDouble(param.get("countNum").toString());//期数
			//统计各个数值出现的概率
			Integer  d = (int) (Double.parseDouble(map.get("count_num").toString()) / divi *100);
			System.out.println("数字："+currentNum+"==>比例："+d);
			
			result.put(currentNum, d);
		}
		
		return result;
	}
	
	/**
	 * 计算区间的百分比
	 * @param list
	 * @param param
	 * @return 
	 */
	public Map<String, Integer> countRangeScale(Map<Integer, Integer> map,Map<String, Object> param){
		Map<String, Integer> result = new HashMap<String, Integer>();
		int min = 49,max = 0,percent=0;
		//数字概率根据小于5期要大于30，小于10期要大于20，小于20期要大于10，小于30期要大于5的方式的计算入区间
		for (Integer num : map.keySet()) {
			Double divi = Double.parseDouble(param.get("countNum").toString());//期数
			//统计各个数值出现的概率
			Integer  d = map.get(num);
			if (d >= (divi <= Constant.NUM_5?Constant.NUM_30:divi <= Constant.NUM_10?Constant.NUM_20:divi<=20?Constant.NUM_10:Constant.NUM_5)) {
				min = num < min ? num : min;
				max = num > max ? num : max;
			}
		}
		if (min > max) min = 0;
		//循环拼接区间概率
		for (int i = min; max!=0 && i <= max; i++) {
			percent += map.get(i) != null ? map.get(i) : 0;
		}
		
		System.out.println("区间:"+min+"至"+max+"==>概率："+percent);
		result.put("min", min);
		result.put("max", max);
		result.put("percent", percent);
		return result;
	}
	
	@Override
	public Map<Integer, Map<String, Integer>> countColumnProbabilityAndRange(Map<String, Object> param) {
		Map<Integer, Map<String, Integer>> result = new HashMap<Integer, Map<String,Integer>>();
		
		String column = (String) param.get("column");//查询的列
		//查询每列最大值和最小值
		Map<String,Integer> maxAndMin = mapper.selectMaxAndMinByColumn(param);
		
		for (int i = 0; i < 4; i++) {
			Integer stageNum = i == 0?Constant.NUM_5:i==1?Constant.NUM_10:i==2?Constant.NUM_20:i==3?Constant.NUM_30:0;
			//统计5期
			param.put("startNum", (Integer)param.get("endNum") - stageNum + 1);
			param.put("countNum", stageNum);
			//查询每列各值出现的概率
			Map<Integer, Integer> map = this.countColumnProbability(param);
			//查询最频繁区间和概率
			Map<String, Integer> rangeScale = this.countRangeScale(map, param);
			
			//寻找符合的数字
			for (int j = 1; j <= 49; j++) {
				Map<String, Integer> numResu = i==0 ? new HashMap<String, Integer>():result.get(j);
				
				numResu.put("late_"+stageNum, map.get(j) == null ? 0:map.get(j));
				//判断数字是否属于热门区间
				numResu.put("late_range_"+stageNum, rangeScale.get("min") <= j && j <= rangeScale.get("max") ? rangeScale.get("percent") : 0);
				//最大最小区间
				numResu.put(Constant.MAX_MIN, maxAndMin.get("max_"+column) >= j && j >= maxAndMin.get("min_"+column) ? 1 : 0);
				result.put(j, numResu);
				
				if (rangeScale.get("min") <= j && j <= rangeScale.get("max")) 
					System.out.println("数字："+j+"==>属于近"+stageNum+"期热门区间:"+rangeScale.get("min")+"-"+rangeScale.get("max")+"==>热门率："+rangeScale.get("percent"));
				
				if (maxAndMin.get("max_"+column) >= j && j >= maxAndMin.get("min_"+column))
					System.out.println("数字："+j+"属于该列取值范围区间，区间是："+maxAndMin.get("min_"+column)+"-"+maxAndMin.get("max_"+column));
			}
		}
		return result;
	}
	

	@Override
	public Result getMaybeNumbers(Integer num) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("endNum", num - 1);
		param.put("column", Constant.ONE);
		param.put("id", num);
		Map<Integer, Map<String, Integer>> map = countColumnProbabilityAndRange(param);
		map = compareAvg(map, param);//比对出现频率
		differenceColumn(param,map);//计算行与行之间的差值
		sumColumn(param,map);//计算行与行之间的和
		return Result.ok();
	}
	
	/**
	 * 计算两行之间的和值
	 * @param param
	 * @param map
	 */
	private void sumColumn(Map<String, Object> param,
			Map<Integer, Map<String, Integer>> map) {
		Integer countNum = 25;//设置统计期数
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		String column = (String) param.get("column");//获取查询的列
		List<Map<String, Integer>> list = mapper.selectByColumn(param);
		
		//计算每行之间的差值
		int min = 0,max = 0;
		for (int i = list.size()-1; i >= 1 ; i--) {
			Map<String, Integer> map1 = list.get(i);
			Map<String, Integer> map2 = list.get(i-1);
			Integer and = map1.get(column) + map2.get(column);
			if (and < min) min = and;
			if (and > max) max = and; 
			System.out.println(map1.get("id")+"-"+map2.get("id")+"和值为:"+and);
		}
		
		//判断差值是否在范围之内
		for (Integer num : map.keySet()) {
			Map<String, Integer> numMap = map.get(num);
			Map<String, Integer> preMap = list.get(list.size()-1);
			Integer sub = num + preMap.get(column);
			
			numMap.put(Constant.COLUMN_AND, sub >= min && sub <= max ? Constant.MATE_TRUE:Constant.MATE_FALSE);
		}
		
	}

	/**
	 * 计算每两行之间的差值
	 * @param param
	 * @param map 
	 */
	private void differenceColumn(Map<String, Object> param, Map<Integer, Map<String, Integer>> map) {
		Integer countNum = 25;//设置统计期数
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		String column = (String) param.get("column");//获取查询的列
		List<Map<String, Integer>> list = mapper.selectByColumn(param);
		
		//计算每行之间的差值
		int min = 0,max = 0;
		for (int i = list.size()-1; i >= 1 ; i--) {
			Map<String, Integer> map1 = list.get(i);
			Map<String, Integer> map2 = list.get(i-1);
			Integer sub = map1.get(column) - map2.get(column);
			if (sub < min) min = sub;
			if (sub > max) max = sub; 
		}
		
		//判断差值是否在范围之内
		for (Integer num : map.keySet()) {
			Map<String, Integer> numMap = map.get(num);
			Map<String, Integer> preMap = list.get(list.size()-1);
			Integer sub = num - preMap.get(column);
			
			numMap.put(Constant.COLUMN_SUB, sub >= min && sub <= max ? Constant.MATE_TRUE:Constant.MATE_FALSE);
		}
	}

	/**
	 * 比对每个数字与平均出现频率
	 * @param map
	 * @param param
	 * @return
	 */
	private Map<Integer, Map<String, Integer>> compareAvg(Map<Integer, Map<String, Integer>> map ,Map<String, Object> param){
		Map<String, Object> avgMap = calculateNumShowAvg(param);
		for (Integer num : map.keySet()) {
			Map<String, Integer> numMap = map.get(num);
			//查询该数据最近出现的一次
			param.put("num", num);
			Map<String, Integer> lateMap = mapper.selectLateByColumn(param);
			
			//比较判断该数值
			numMap.put(Constant.COLUMN_AVG, Constant.MATE_FALSE);
			if (lateMap != null && lateMap.get("id") != null) {
				Integer sub = (Integer)param.get("id") - lateMap.get("id");
				if (((Boolean)avgMap.get("maxMore") && sub > (Double)avgMap.get("avg"))
						|| (!(Boolean)avgMap.get("maxMore") && sub < (Double)avgMap.get("avg"))) {
					numMap.put(Constant.COLUMN_AVG, Constant.MATE_TRUE);
				}
			}
		}
		return map;
	}
	/**
	 * 计算近20期该列的数据平均多少期出现一次
	 * @param param
	 * @return 
	 */
	private Map<String, Object> calculateNumShowAvg(Map<String, Object> param) {
		Integer countNum = 20;//设置统计期数
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		String column = (String) param.get("column");//获取查询的列
		List<Map<String, Integer>> list = mapper.selectByColumn(param);
		
		//查询每期的数字最近出现的期数
		Map<String, Object> subParam = new HashMap<String, Object>();
		Integer sum = 0;
		Integer count = 0;//统计只出现了一次的数字
		List<Integer> numList = new ArrayList<Integer>();
		for (Map<String, Integer> map : list) {
			subParam.put("column", column);
			subParam.put("id", map.get("id"));
			subParam.put("num", map.get(column));
			Map<String, Integer> lateMap = mapper.selectLateByColumn(subParam);
			
			if (lateMap == null || lateMap.get("id") == null) {
				count ++;
			}else {
				Integer a = map.get("id") - lateMap.get("id");
				sum += a;
				numList.add(a);
			}
		}
		countNum -= count;
		
		Double avgNum = CommonUtil.getNumDecimal(Double.parseDouble(sum.toString())/Double.parseDouble(countNum.toString()), 2);
		System.out.println("平均每："+avgNum+"期出现一次");
		
		//统计大于平均数的数量
		int maxAvg = 0,minAvg = 0;
		for (Integer i : numList) {
			if (i>avgNum) {
				maxAvg++;
			}else {
				minAvg++;
			}
		}
		subParam.clear();
		subParam.put("maxMore", maxAvg >= minAvg);
		subParam.put("avg", avgNum);
		
		return subParam;
	}
}
