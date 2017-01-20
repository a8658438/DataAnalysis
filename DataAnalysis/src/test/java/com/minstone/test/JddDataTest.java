package com.minstone.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.minstone.pojo.JddData;
import com.minstone.service.JddDataService;
import com.minstone.service.JddExcludeService;
import com.minstone.service.JddScaleService;
import com.minstone.util.Constant;

public class JddDataTest {
	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
	/**
	 * 测试单个版redis
	 */
	@Test
	public void testSpringSingle() {
		JddDataService service = (JddDataService) context.getBean(JddDataService.class);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startNum", 114);
		param.put("endNum", 153);
//		service.selectSumByStage(param);
//		service.selectMaxAndMinByColumn(param);
		param.put("column", "one");
		param.put("countNum", 40);
//		service.selectCountByColumn(param);
//		service.countColumnProbability(param);
	}
	
	@Test
	public void test1() {
		JddDataService service = context.getBean(JddDataService.class);
		JddScaleService scaleService = context.getBean(JddScaleService.class);
		
		List<Integer> numList = new ArrayList<Integer>();
		
		String column = Constant.SIX;
		for (int i = 0; i < 6; i++) {
			column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
			
			//获取每列的数字信息
			Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(153,column);
			//保存进数据库
			scaleService.saveBatch(numberData, column);
			List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
			//获取综合排名第一的数字
			numList = scaleService.getMaybeNumbers(rank,numberData,numList);
		}
		for (Integer num : numList) {
			System.out.println(num);
		}
	}
	
	@Test
	public void test2() {
		JddDataService service = context.getBean(JddDataService.class);
		JddScaleService scaleService = context.getBean(JddScaleService.class);
		
		Integer stageNum = 153;//计算的期数
		
		//查询column列最大最小值
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startNum", 1);
		param.put("endNum", stageNum -1);
		Map<String, Integer> maxAndMin = service.selectMaxAndMinByColumn(param);

		//查询所有数据
		List<JddData> list = service.selectAllData(1,stageNum - 1,"id desc");
		
		List<Integer> numList = new ArrayList<Integer>();
		//分开统计各列数据
		for (int y = 0; y < 6; y++) {
			String column = y==0?Constant.ONE:y==1?Constant.TWO:y==2?Constant.THREE:y==3?Constant.FOUR:y==4?Constant.FIVE:Constant.SIX;
			
			//记录排除的数字
			List<Integer> numbers = new ArrayList<Integer>();
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			for (int n = 1; n < 16; n++) {//上几期的几
				System.out.println("上"+n+"期：");
				for (int shu = 1; shu <= 9; shu++) {//加减的数
					int count = 0;
					for (int i = 0; i < list.size()-n; i++) {
						JddData obj1 =list.get(i);//1.序号,2.特码
						JddData obj2 =list.get(i + n);//1.序号,2.特码
						int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
						int sq2 = y==0?obj2.getOne():y==1?obj2.getTwo():y==2?obj2.getThree():y==3?obj2.getFour():y==4?obj2.getFive():obj2.getSix();
						
						int x = 0;
						if(sq2 >= 6){
							x = sq2 - shu;
						}else{
							x = sq2 + shu;
						}
						if(sq1 == x){
							count++ ;
						}
						
					}
					if(count == 0 || count == 1){
						map.put(n+"-"+shu, count);
						System.out.println("成----" + shu + ":" + count);
					}
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
			
			//计算每个数字与前面对应期数的加减值
			for (int i = 1; i < 50; i++) {
				for (int n = 0; n < 15; n++) {//上几期的几
					for (int shu = 1; shu <= 9; shu++) {//加减的数
						int count = 0;
						JddData obj1 =list.get(n);//1.序号,2.特码
						int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
						
						int x = 0;
						if(sq1 >= 6){
							x = sq1 - shu;
						}else{
							x = sq1 + shu;
						}
						if(i == x){
							count++ ;
							Integer num = map.get(n+"-"+shu);
							if (num != null) {
								numbers.add(i);
								System.out.println("数字："+i+"与上"+n+"期排除");
							}
						}
					}
				}
				//通过列取值区间排除
				if (i < maxAndMin.get("min_"+column) || i > maxAndMin.get("max_"+column)) {
					System.out.println("数字："+i+"超范围排除");
					numbers.add(i);
				}
				//通过出现频率极低的排除
				List<Map<String, Long>> countNumbersList = service.countNumberShows(column);
				for (Map<String, Long> map2 : countNumbersList) {
					if (Integer.parseInt(map2.get("count_num")+"") ==2 || Integer.parseInt(map2.get("count_num")+"") == 1) {
						System.out.println("数字："+map2.get(column)+"出现概率低被排除");
						numbers.add(Integer.parseInt(map2.get(column)+""));
					}
				}
			}
			
			Map<Integer, Map<String, Integer>> nunberData1 = new HashMap<Integer, Map<String,Integer>>();
			//获取每列的数字信息
			Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(stageNum,column);
			for (Integer key : numberData.keySet()) {
				if (!numbers.contains(key)) {
					nunberData1.put(key, numberData.get(key));
				}
			}
			//保存进数据库
			scaleService.saveBatch(nunberData1, column);
			List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
			//获取综合排名第一的数字
			numList = scaleService.getMaybeNumbers(rank,nunberData1,numList);
		}
		for (Integer integer : numList) {
			System.out.println(integer);
		}
	}
	
	@Test
	public void test3() {
		JddDataService service = context.getBean(JddDataService.class);
		JddScaleService scaleService = context.getBean(JddScaleService.class);
		JddExcludeService excludeService = context.getBean(JddExcludeService.class);
		int id = 154;
		
		Map<Integer, List<Integer>> map = excludeService.queryStageExcludeArea(1, id-1, "id desc");
		Integer[] keySet = map.keySet().toArray(new Integer[map.size()]);
		for (int n = 1; n < 16; n++) {//上几期的几
			System.out.println("上"+n+"期：");
			for (int shu = 1; shu <= 9; shu++) {//加减的数
				int count = 0;
				for (int i = keySet.length -1; i >= n ; i--) {
					Integer sq1 = keySet[i];
					Integer sq2 = keySet[i - n];
					//获取这两期未出的区域
					List<Integer> list1 = map.get(sq1);
					List<Integer> list2 = map.get(sq2);
					for (Integer area1 : list1) {
						for (Integer area2 : list2) {
							int x = 0;
							if (area2 >= 6) {
								x = area2 - shu;
							}else {
								x = area2 + shu;
							}
							if (area1 == x) {
								count ++;
							}
						}
					}
				}
				System.out.println("加减数："+shu+"==>次数："+count);
				if (count == 0 || count == 1) {
				}
				System.out.println("=================================》");
			}
		}
		
	}
	
	@Test
	public void test4() {
		int id = 153;
		JddDataService service = context.getBean(JddDataService.class);
		List<JddData> list = service.selectAllData(1 , id-1, "id desc");
		for (JddData jddData : list) {
			System.out.println("期数："+jddData.getId() + "==>平均数："+(jddData.sum()/6));
		}
	}
	
	@Test
	public void test5() {
		JddExcludeService excludeService = context.getBean(JddExcludeService.class);
		int id = 150;
		
		Map<Integer, List<Integer>> map = excludeService.queryStageExcludeArea(1, id-1, "id desc");
		Integer[] keySet = map.keySet().toArray(new Integer[map.size()]);
		for (int n = 1; n < 16; n++) {//上几期的几
			System.out.println("上"+n+"期：");
			for (int shu = 1; shu <= 6; shu++) {//加减的数
				int count = 0;
				for (int i = keySet.length -1; i >= n ; i--) {
					Integer sq1 = keySet[i];
					Integer sq2 = keySet[i - n];
					//获取这两期未出的区域
					List<Integer> list1 = map.get(sq1);
					List<Integer> list2 = map.get(sq2);
						for (Integer area2 : list2) {
							int x = 0;
							if (area2 >= 3) {
								x = area2 - shu;
							}else {
								x = area2 + shu;
							}
							for (Integer area1 : list1) {
								if (area1 == x) {
									count ++;
								}
							}
					}
				}
				if (count <= 50) {
					System.out.println("加减数："+shu+"==>次数："+count);
				}
			}
			System.out.println("=================================》");
		}
		
		//测试为加减数6的时候的概率
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		for (int n = keySet.length - 1; n >= keySet.length - 16; n--) {//上几期的几
			System.out.println("上"+n+"期：");
				Integer sq1 = 0;
				Integer sq2 = keySet[n];
				//获取上期未出的区域
				List<Integer> list2 = map.get(sq2);
					for (Integer area2 : list2) {
						int x = 0;
						if (area2 >= 3) {
							x = area2 - 6;
						}else {
							x = area2 + 6;
						}
						countMap.put(x, countMap.get(x)==null ? 1 : countMap.get(x)+1);
				}
		}
		for (Integer integer : countMap.keySet()) {
			System.out.println("区间："+integer+"---次数："+countMap.get(integer));
		}
	}
	
	/**
	 * 尝试计算对于全期的
	 */
	@Test
	public void test6() {
		JddDataService service = context.getBean(JddDataService.class);
		JddScaleService scaleService = context.getBean(JddScaleService.class);
		
		Integer stageNum = 147;//计算的期数
		List<JddData> list = service.selectAllData(1,stageNum - 1,"id desc");
		List<Integer> numbers = new ArrayList<Integer>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
			for (int n = 1; n < 16; n++) {//上几期的几
				System.out.println("上"+n+"期：");
				for (int shu = 1; shu <= 9; shu++) {//加减的数
					int count = 0;
					for (int i = 0; i < list.size()-n; i++) {
						JddData obj1 =list.get(i);//1.序号,2.特码
						JddData obj2 =list.get(i + n);//1.序号,2.特码
						
						int sq1 = 0;
						for (int y = 0; y < 6; y++) {
							sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
							for (int j = 0; j < 6; j++) {
								int sq2 = j==0?obj2.getOne():j==1?obj2.getTwo():j==2?obj2.getThree():j==3?obj2.getFour():j==4?obj2.getFive():obj2.getSix();

								int x = 0;
								if(sq2 >= 6){
									x = sq2 - shu;
								}else{
									x = sq2 + shu;
								}
								if(sq1 == x){
									count++ ;
								}
							}
						}
						
						
					}
					System.out.println("成----" + shu + ":" + count);
					if(count == 0 || count == 1){
						map.put(n+"-"+shu, count);
					}
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
			
			//计算每个数字与前面对应期数的加减值
			for (int i = 1; i < 50; i++) {
				for (int n = 0; n < 15; n++) {//上几期的几
					for (int shu = 1; shu <= 9; shu++) {//加减的数
						int count = 0;
						JddData obj1 =list.get(n);//1.序号,2.特码
//						int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
						int sq1 = 0;
						int x = 0;
						if(sq1 >= 6){
							x = sq1 - shu;
						}else{
							x = sq1 + shu;
						}
						if(i == x){
							count++ ;
							Integer num = map.get(n+"-"+shu);
							if (num != null) {
								numbers.add(i);
								System.out.println("数字："+i+"与上"+n+"期排除");
							}
						}
					}
				}
			}
		
//			List<Integer> numList = new ArrayList<Integer>();
//			String column = Constant.ONE;
//			for (int i = 0; i < 6; i++) {
//				column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
//				
//				Map<Integer, Map<String, Integer>> nunberData1 = new HashMap<Integer, Map<String,Integer>>();
//				//获取每列的数字信息
//				Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(stageNum,column);
//				for (Integer key : numberData.keySet()) {
//					if (!numbers.contains(key)) {
//						nunberData1.put(key, numberData.get(key));
//					}
//				}
//				//保存进数据库
//				scaleService.saveBatch(nunberData1, column);
//				List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
//				//获取综合排名第一的数字
//				numList = scaleService.getMaybeNumbers(rank,nunberData1,numList);
//			}
//			for (Integer integer : numList) {
//				System.out.println(integer);
//			}
	}
	
	@Test
	public void test7() {
		JddDataService service = context.getBean(JddDataService.class);
		JddScaleService scaleService = context.getBean(JddScaleService.class);
		int id = 154;
		List<JddData> list = service.selectAllData(1, id-1, "id desc");
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		//分开统计各列数据
		int count = 0;
		int sum = 0;
		for (int n = 1; n < 16; n++) {//上几期的几
			System.out.println("上"+n+"期：");
			for (int i = 0; i < list.size()-n; i++) {
				JddData jdd1 = list.get(i);
				JddData jdd2 = list.get(i + n);
				
				int sq1 = jdd1.getSix();
				int sq2 = jdd2.getSix();
				
				int x = sq2 > sq1 ? sq2 - sq1 : sq1 - sq2;
				sum += x;
				count ++;
				map.put(x+"-"+n, map.get(x+"-"+n) == null ? 1 : map.get(x+"-"+n)+1);
			}
			System.out.println("=================================》");
		}
		for (String key : map.keySet()) {
			System.out.println("差值和期数："+key+"==》出现的次数："+map.get(key));
		}
		System.out.println(sum/count);
		System.out.println(sum%count);
		//差值从0到23 ，那是否将循环从1到9改为0到23呢？中间的数值又要取多少好呢？
		
	}
	public static void main(String[] args) {
		System.out.println(22);
	}
}
