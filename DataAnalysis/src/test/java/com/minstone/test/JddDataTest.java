package com.minstone.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.minstone.service.JddDataService;

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
		service.selectMaxAndMinByColumn(param);
		param.put("column", "one");
		param.put("countNum", 40);
//		service.selectCountByColumn(param);
//		service.countColumnProbability(param);
	}
	
	@Test
	public void test1() {
		JddDataService service = (JddDataService) context.getBean(JddDataService.class);
		service.getMaybeNumbers(154);
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
