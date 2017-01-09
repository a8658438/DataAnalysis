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
//		service.selectMaxAndMinByColumn(param);
		param.put("column", "one");
		param.put("countNum", 40);
//		service.selectCountByColumn(param);
		service.countColumnProbability(param);
	}
	
	@Test
	public void test1() {
		JddDataService service = (JddDataService) context.getBean(JddDataService.class);
		service.countColumnProbability(154,"two");
	}
	
	public static void main(String[] args) {
		System.out.println(22);
	}
}
