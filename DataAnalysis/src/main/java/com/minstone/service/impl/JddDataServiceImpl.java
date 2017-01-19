package com.minstone.service.impl;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;

import com.minstone.mapper.JddDataMapper;
import com.minstone.pojo.JddData;
import com.minstone.pojo.JddDataExample;
import com.minstone.pojo.JddDataExample.Criteria;
import com.minstone.service.JddDataService;
import com.minstone.util.CommonUtil;
import com.minstone.util.Constant;
import com.minstone.util.Result;

@Service
public class JddDataServiceImpl implements JddDataService{
	@Autowired
	private JddDataMapper mapper;

	@Override
	public Map<Integer, Map<String, Integer>> getNumbersDataForColumn(Integer num,String column) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("endNum", num - 1);
		param.put("column", column);
		param.put("id", num);
		Map<Integer, Map<String, Integer>> map = countColumnProbabilityAndRange(param);
		map = compareAvg(map, param);//比对列出现频率
		sumAndDifferenceColumn(param, map);//计算列的和与差之值
		
		//比对每期的出现频率
		compareAllAvg(map, param);
		//统计每个数字N期出现一次的概率
		countAllProbability(param,map);
		
		//分析每个数字的概率
		analysisNumChance(map);
		return map;
	}
	
	@Override
	public List<Map<String, Long>> selectSumByStage(Map<String , Object> param) {
		List<Map<String,Long>> list = mapper.selectSumByStage(param);
//		for (Map<String, Long> map : list) {
//			System.out.println("期数："+map.get("id")+">>总和："+map.get("sum_num"));
//		}
		return list;
	}

	@Override
	public Map<Integer, Integer> countColumnProbability(Map<String, Object> param) {
		List<Map<String,Long>> list = mapper.selectCountByColumn(param);
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (Map<String, Long> map : list) {
			int currentNum = Integer.parseInt(map.get(param.get("column"))+"");//当前数字
			Double divi = Double.parseDouble(param.get("countNum").toString());//统计了N期
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
				
				//这里打印数据只是为了方便查看，与真正的统计无关
				if (rangeScale.get("min") <= j && j <= rangeScale.get("max")) 
					System.out.println("数字："+j+"==>属于近"+stageNum+"期热门区间:"+rangeScale.get("min")+"-"+rangeScale.get("max")+"==>热门率："+rangeScale.get("percent"));
				
				if (maxAndMin.get("max_"+column) >= j && j >= maxAndMin.get("min_"+column))
					System.out.println("数字："+j+"属于该列取值范围区间，区间是："+maxAndMin.get("min_"+column)+"-"+maxAndMin.get("max_"+column));
			}
		}
		return result;
	}
	

	/**
	 * 分析每个数字的概率
	 * @param map
	 */
	private void analysisNumChance(Map<Integer, Map<String, Integer>> map) {
		for (Integer number : map.keySet()) {
			Map<String, Integer> numMap = map.get(number);
			//分摊 late_all占比35%，其他占比55%
			Double lateAllScale = CommonUtil.getNumDecimal(statisLateAllScale(numMap), 4) * 10000;//计算lateAll所占比例
			Double otherScale = CommonUtil.getNumDecimal(statisOtherScale(numMap), 4) * 10000;//计算其他数值的占比
			
			numMap.put(Constant.LATE_ALL_SCALE, lateAllScale.intValue());
			numMap.put(Constant.OTHER_SCALE, otherScale.intValue());
//			Double scale = CommonUtil.getNumDecimal(lateAllScale+otherScale,2);
//			System.out.println("数字："+number +"==>概率："+scale+"(其中lateAll占比："+lateAllScale+";other占比："+otherScale+")");
			
		}
	}

	/**
	 * 计算其他数值的占比
	 * @param numMap
	 * @return 
	 */
	private Double statisOtherScale(Map<String, Integer> numMap) {
		Double lateAllScale = 55d;//占比60
		//计算符合的条件个数
		Double count = 0d;
		count += numMap.get(Constant.COLUMN_SUB);//列差值是否符合
		count += numMap.get(Constant.COLUMN_AND);//列和值是否符合
		count += numMap.get(Constant.ALL_AVG);//全表平均频率出现值是否符合
		count += numMap.get(Constant.MAX_MIN);//取值范围是否符合
//		count += numMap.get(Constant.COLUMN_AVG);//列平均频率出现值是否符合
		//如果属于近5期的热门区间+1
		count += numMap.get(Constant.LATE_RANGE_5) != 0 ? 1 : 0;
		//如果属于近10期热门期间+1
		count += numMap.get(Constant.LATE_RANGE_10) != 0 ? 1 : 0;
		//如果属于近20期热门期间+1
		count += numMap.get(Constant.LATE_RANGE_20) != 0 ? 1 : 0;
		//如果属于近30期热门期间+1
		count += numMap.get(Constant.LATE_RANGE_30) != 0 ? 1 : 0;
		
		//近5期出现过的将下次出现的概率+1
		count += numMap.get(Constant.LATE_5) != 0 ? 1 : 0;
		count += numMap.get(Constant.LATE_10) != 0 ? 1 : 0;
		count += numMap.get(Constant.LATE_20) != 0 ? 1 : 0;
		count += numMap.get(Constant.LATE_30) != 0 ? 1 : 0;
		
		//计算比例
//		Double scale = count / numMap.keySet().size() * lateAllScale;
		Double scale = count / 13;
		return scale;
	}

	/**
	 * 计算lateAll所占比例
	 * @param numMap
	 * @return
	 */
	private Double statisLateAllScale(Map<String, Integer> numMap) {
		Double lateAllScale = 35d;
		//关于late_all介绍：当前数字在越小的期内出现过时，则取最小期的概率做统计
		Integer lateAll1 = numMap.get(Constant.LATE_ALL_1),lateAll5 = numMap.get(Constant.LATE_ALL_5),
				lateAll10 = numMap.get(Constant.LATE_ALL_10),lateAll20 = numMap.get(Constant.LATE_ALL_20);
		//该数字在该期可能会出现的比例
		Integer maybeShowScale = lateAll1 != 0 ? lateAll1 : lateAll5 != 0 ? lateAll5 : lateAll10!=0 ? lateAll10 : lateAll20;
		//该比例占比为
//		lateAllScale = lateAllScale * Double.parseDouble(maybeShowScale.toString()) / 100;
		lateAllScale = Double.parseDouble(maybeShowScale.toString()) / 100;
		
		numMap.remove(Constant.LATE_ALL_1);
		numMap.remove(Constant.LATE_ALL_5);
		numMap.remove(Constant.LATE_ALL_10);
		numMap.remove(Constant.LATE_ALL_20);
		
		return lateAllScale;
	}

	/**
	 * 取25行数据做范本，统计每个数字连续期数出现的概率，5期出现一次的概率，10期出现一次的概率，20期出现一次的概率
	 * @param param
	 * @param map 
	 * @param map
	 */
	private void countAllProbability(Map<String, Object> param, Map<Integer, Map<String, Integer>> map) {
		Integer countNum = 25;//设置统计期数范本数据
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		List<Map<String, Integer>> list = mapper.selectByColumn(param);//查询多行数据
		Integer quantum = list.size() * 6;//统计的数字数量
	
		//查询每一行的每个数字的的概率
		Map<String, Object> subParam = new HashMap<String, Object>();
		for (int i = 0; i < 4; i++) {//循环2期、5期、10期、20期
			Integer stageNum = i == 0?Constant.NUM_1:i==1?Constant.NUM_5:i==2?Constant.NUM_10:Constant.NUM_20;
			//查询有多少个数字是符合条件了的 
			Integer hasNum = queryNumFrequency(list, subParam, stageNum);
			//计算N期内会出现的概率
			Double divi = Double.parseDouble(hasNum.toString()) / Double.parseDouble(quantum.toString()) * 100;
			divi = CommonUtil.getNumDecimal(divi, 2);
			System.out.println("在"+stageNum+"期内，数字出现的概率为："+divi);
			
			//统计指定期数
			subParam.put("startNum", (Integer)param.get("endNum") - stageNum + 1);
			subParam.put("endNum", (Integer)param.get("endNum"));
			for (Integer num : map.keySet()) {
				subParam.put("num", num);
				Map<String, Integer> numMap = map.get(num);
				List<Map<String, Integer>> hasList = mapper.selectIsShowId(subParam);
				
				//如果前N期出过了，那么本期可能出现的概率则为该期的概率
				numMap.put("late_all_"+stageNum, Constant.MATE_FALSE);
				if (hasList != null && hasList.size() != 0) 
					numMap.put("late_all_"+stageNum,divi.intValue());
					
			}
		}
	}

	/**
	 * 查询指定期数有多少个数字是出现了的
	 * @param list
	 * @param subParam
	 * @param stageNum
	 * @return 
	 */
	private Integer queryNumFrequency(List<Map<String, Integer>> list,Map<String, Object> subParam, Integer stageNum) {
		Integer hasNum = 0;//存在的次数
		for (Map<String, Integer> map2 : list) {//循环每一行查询
			subParam.put("endNum", map2.get("id")-1);//查询前一期
			subParam.put("startNum", map2.get("id") - stageNum);//设置起始数据
			
			for (String key : map2.keySet()) {//循环每个数字查询
				subParam.put("num", map2.get(key));
				List<Map<String, Integer>> hasList = mapper.selectIsShowId(subParam);
				//如果有数据表示存在
				if (hasList != null && hasList.size() != 0) hasNum++;
			}
		}
		return hasNum;
	}
	/**
	 * 比对每期的出现频率
	 * @param map
	 * @param param
	 */
	private void compareAllAvg(Map<Integer, Map<String, Integer>> map,Map<String, Object> param) {
		//计算该列的数字平均多少期出现一次
		Map<String, Object> avgMap = calculateNumShowAvgForAll(param);
		
		Map<String, Object> subParam = new HashMap<String, Object>();
		subParam.put("id", param.get("id"));
		for (Integer num : map.keySet()) {
			Map<String, Integer> numMap = map.get(num);
			//查询该数据最近出现的一次
			subParam.put("num", num);
			Map<String, Integer> lateMap = mapper.selectLateId(subParam);
			//计算上次出现和这次出现相差多少期
			Integer lateNum = (Integer)param.get("id") - (lateMap !=null && lateMap.get("id") !=null?lateMap.get("id"):9999);
			
			//获取频率等级
			numMap.put(Constant.ALL_AVG_LEVEL, (Integer) (avgMap.get(lateNum.toString()) == null ? 0 : avgMap.get(lateNum.toString())));
			//比较判断该数值
			numMap.put(Constant.ALL_AVG, Constant.MATE_FALSE);
			if (lateMap != null && lateMap.get("id") != null) {
				Integer sub = (Integer)param.get("id") - lateMap.get("id");
				if (((Boolean)avgMap.get("maxMore") && sub > (Double)avgMap.get("avg"))
						|| (!(Boolean)avgMap.get("maxMore") && sub < (Double)avgMap.get("avg"))) {
					numMap.put(Constant.ALL_AVG, Constant.MATE_TRUE);
				}
			}
		}
	}

	/**
	 * 计算近30期该行的每个数据平均多少期出现一次
	 * @param param
	 * @return 
	 */
	private Map<String, Object> calculateNumShowAvgForAll(Map<String, Object> param) {
		Integer countNum = 30;//设置统计期数
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		List<Map<String, Integer>> list = mapper.selectByColumn(param);
		
		//查询每期的数字最近出现的期数
		Map<String, Object> subParam = new HashMap<String, Object>();
		Integer sum = 0;
		Integer count = 0;//统计只出现了一次的数字
		List<Integer> numList = new ArrayList<Integer>();
		for (Map<String, Integer> map : list) {
			subParam.put("id", map.get("id"));
			//计算每一个数值相隔期数
			for (String column : map.keySet()) {
				if ("id".equals(column)) continue;
				subParam.put("num", map.get(column));
				Map<String, Integer> lateMap = mapper.selectLateId(subParam);
				
				if (lateMap == null || lateMap.get("id") == null) {
					count ++;
				}else {
					Integer a = map.get("id") - lateMap.get("id");
					sum += a;
					numList.add(a);
					subParam.put(a.toString(), subParam.get(a.toString()) == null ? 1 : (Integer)subParam.get(a.toString())+1);
				}
			}
		}
		countNum -= count;
		
		Double avgNum = CommonUtil.getNumDecimal(Double.parseDouble(sum.toString())/Double.parseDouble(numList.size()+""), 2);
		System.out.println("全表==>平均每："+avgNum+"期出现一次");
		
		//统计大于平均数的数量
		int maxAvg = 0,minAvg = 0;
		for (Integer i : numList) {
			if (i>avgNum) {
				maxAvg++;
			}else {
				minAvg++;
			}
		}
		subParam.put("maxMore", maxAvg >= minAvg);
		subParam.put("avg", avgNum);
		return subParam;
	}

	/**
	 * 计算和还有差的值
	 * @param param
	 * @param map
	 */
	private void sumAndDifferenceColumn(Map<String, Object> param,
			Map<Integer, Map<String, Integer>> map) {
		sumOrDifferenceColumn(param,map,false);//计算行与行之间的差值
		sumOrDifferenceColumn(param,map,true);//计算行与行之间的和
	}
	
	/**
	 * 计算每两行之间的差值或和值
	 * @param param
	 * @param map 
	 */
	private void sumOrDifferenceColumn(Map<String, Object> param, Map<Integer, Map<String, Integer>> map,boolean isSum) {
		Integer countNum = 25;//设置统计期数
		param.put("startNum", (Integer)param.get("endNum") - countNum + 1);//设置查询的起始数据
		String column = (String) param.get("column");//获取查询的列
		List<Map<String, Integer>> list = mapper.selectByColumn(param);
		
		//计算每行之间的差值
		int min = 0,max = 0;
		for (int i = list.size()-1; i >= 1 ; i--) {
			Map<String, Integer> map1 = list.get(i);
			Map<String, Integer> map2 = list.get(i-1);
			Integer val = isSum ? map1.get(column) + map2.get(column):map1.get(column) - map2.get(column);
			if (val < min) min = val;
			if (val > max) max = val; 
		}
		
		//判断差值或和值是否在范围之内
		for (Integer num : map.keySet()) {
			Map<String, Integer> numMap = map.get(num);
			Map<String, Integer> preMap = list.get(list.size()-1);
			Integer val = isSum ? num + preMap.get(column) : num - preMap.get(column);
			
			numMap.put(isSum ? Constant.COLUMN_AND : Constant.COLUMN_SUB, val >= min && val <= max ? Constant.MATE_TRUE:Constant.MATE_FALSE);
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
			Map<String, Integer> lateMap = mapper.selectLateId(param);
			Integer lateNum = (Integer)param.get("id") - (lateMap !=null && lateMap.get("id") !=null?lateMap.get("id"):9999);
			//设置该数字等级
			numMap.put(Constant.COLUMN_AVG_LEVEL, (Integer) (avgMap.get(lateNum.toString()) == null ? 0 : avgMap.get(lateNum.toString())));
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
	 * 计算近30期该列的数据相对于列平均多少期出现一次
	 * @param param
	 * @return 
	 */
	private Map<String, Object> calculateNumShowAvg(Map<String, Object> param) {
		Integer countNum = 30;//设置统计期数
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
			Map<String, Integer> lateMap = mapper.selectLateId(subParam);
			
			if (lateMap == null || lateMap.get("id") == null) {
				count ++;
			}else {
				Integer a = map.get("id") - lateMap.get("id");
				sum += a;
				numList.add(a);
				
				//统计各个数字出现的次数
				subParam.put(a.toString(), subParam.get(a.toString()) == null ? 1 : (Integer)subParam.get(a.toString())+1);
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
		subParam.put("maxMore", maxAvg >= minAvg);
		subParam.put("avg", avgNum);
		
		return subParam;
	}
	
	@Override
	public Map<String, Object> validate(List<Integer> numList,Integer id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startNum", id);
		param.put("endNum", id);
		//查询近30期的数据
		List<Map<String,Integer>> list = mapper.selectByColumn(param);
		param.clear();
		
		param.put("stageNumber", id);//期数
		String nums = "";
		boolean flag = false;
		//循环匹配统计
		for (Map<String, Integer> map : list) {
			for (String key : map.keySet()) {
				if (numList.contains(map.get(key))) {//循环进行数字匹配
					nums += "、"+map.get(key);
					flag = true;
				}
			}
		}
		param.put("mateNumbers",StringUtils.isBlank(nums) ? "" : nums.substring(1));//匹配的数字
		param.put("mate", flag);
		return param;
	}

	@Override
	public List<JddData> selectAllData(Integer startId, Integer endId,String orderBy) {
		JddDataExample example = new JddDataExample();
		example.setOrderByClause(orderBy);
		if (startId != null && endId != null) {
			example.createCriteria().andIdBetween(startId, endId);
		}
		return mapper.selectByExample(example);
	}
}
