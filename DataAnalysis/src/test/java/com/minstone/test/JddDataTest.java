package com.minstone.test;

import java.util.HashMap;
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
		//获取每列的数字信息
		Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(151,Constant.ONE);
		//保存进数据库
		scaleService.saveBatch(numberData, Constant.ONE);
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
