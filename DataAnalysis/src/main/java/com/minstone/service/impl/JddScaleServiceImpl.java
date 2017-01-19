package com.minstone.service.impl;

import java.util.List;
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

	@Override
	public List<Map<String, Object>> queryColumnRank(String column) {
		return mapper.queryColumnRank(column);
	}

	@Override
	public List<Integer> getMaybeNumbers(List<Map<String, Object>> rank,
			Map<Integer, Map<String, Integer>> numberData, List<Integer> numList) {
		if (rank == null || rank.size() == 0) return numList;
		
		
		//循环数据库统计出的前十位数字
		Integer rownum = (int) (Double.parseDouble(rank.get(0).get("rownum").toString()));
		Integer num = (Integer) rank.get(0).get("num");
		Integer realNum = sameRankCompare(1, rank, rownum, num, numList, numberData);
		numList.add(realNum);
			
		return numList;
	}

	
	/**
	 * 当第一跟第二个数字排名相同时，通过细节进行比较
	 * @param index
	 * @param rank
	 * @param rownum
	 * @param num
	 * @param numList
	 * @param numberData
	 * @return
	 */
	private Integer sameRankCompare(Integer index,List<Map<String, Object>> rank,Integer rownum,Integer num,List<Integer> numList,Map<Integer, Map<String, Integer>> numberData){
		if (index==rank.size()) {
			return num;
		}
		Integer rownum2 = (int) (Double.parseDouble(rank.get(index).get("rownum").toString()));
		Integer num2 = (Integer) rank.get(index).get("num");
		if (rownum == rownum2) {//如果两数字综合排名一致
			if (!numList.contains(num) && !numList.contains(num2)) {//两个数字都不包含
				Map<String, Integer> numMap1 = numberData.get(num);
				Map<String, Integer> numMap2 = numberData.get(num2);
				//比较是否符合取值的范围区间
				if (numMap1.get(Constant.MAX_MIN) == numMap2.get(Constant.MAX_MIN)) {
					//该条件相等再比较下一个条件,判断近5期是否出现过
					if (numMap1.get(Constant.LATE_5) == numMap2.get(Constant.LATE_5)) {
						//判断近10期
						if (numMap1.get(Constant.LATE_ALL_SCALE) == numMap2.get(Constant.LATE_ALL_SCALE)) {
							return sameRankCompare(index+1, rank, rownum, num, numList, numberData);
						}else {
							Integer realNum = numMap1.get(Constant.LATE_ALL_SCALE) > numMap2.get(Constant.LATE_ALL_SCALE)? num : num2;
							Integer realRowNum = numMap1.get(Constant.LATE_ALL_SCALE) > numMap2.get(Constant.LATE_ALL_SCALE)? rownum : rownum2;
							//与第三个数字再比较一次
							return sameRankCompare(index+1, rank, realRowNum, realNum, numList, numberData);
						}
						
					}else {
						Integer realNum = numMap1.get(Constant.LATE_5) > numMap2.get(Constant.LATE_5)? num2 : num;
						Integer realRowNum = numMap1.get(Constant.LATE_5) > numMap2.get(Constant.LATE_5)? rownum2 : rownum;
						//与第三个数字再比较一次
						return sameRankCompare(index+1, rank, realRowNum, realNum, numList, numberData);
					}
					
				}else {
					Integer realNum = numMap1.get(Constant.MAX_MIN) > numMap2.get(Constant.MAX_MIN)? num : num2;
					Integer realRowNum = numMap1.get(Constant.MAX_MIN) > numMap2.get(Constant.MAX_MIN)? rownum : rownum2;
					//与第三个数字再比较一次
					return sameRankCompare(index+1, rank, realRowNum, realNum, numList, numberData);
				}
			}else {//其中一个包含
				Integer realNum = numList.contains(num)? num2 : num;
				Integer realRowNum = numList.contains(num)? rownum2 : rownum;
				//与第三个数字再比较一次
				return sameRankCompare(index+1, rank, realRowNum, realNum, numList, numberData);
			}
		}else {
			//集合中不包含
			if (!numList.contains(num)) {
				return num;
			}else {
				//与第三个数字再比较一次
				return sameRankCompare(index+1, rank, rownum2, num2, numList, numberData);
			}
		}
	}

}
