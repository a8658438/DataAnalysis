package com.minstone.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.minstone.service.JddDataService;
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
		JddDataService service = (JddDataService) context.getBean(JddDataService.class);
		for (int i = 1; i <= 49; i++) {
		}
//		service.countColumnProbabilityAndRange(154,"one");
	}
	
	public static void main(String[] args) {
		System.out.println(22);
	}
}
