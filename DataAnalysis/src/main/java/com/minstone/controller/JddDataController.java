package com.minstone.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minstone.service.JddDataService;
import com.minstone.service.JddScaleService;
import com.minstone.util.Constant;
import com.minstone.util.Result;

@Controller
@RequestMapping("/jddData")
public class JddDataController {
	@Autowired
	private JddDataService service;
	@Autowired
	private JddScaleService scaleService;

	@RequestMapping("/{num}")
	@ResponseBody
	public Result query(@PathVariable Integer num) {
		List<Integer> numList = new ArrayList<Integer>();
		
		String column = Constant.ONE;
		for (int i = 0; i < 6; i++) {
			column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
			
			//获取每列的数字信息
			Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(num,column);
			//保存进数据库
			scaleService.saveBatch(numberData, column);
			List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
			//获取综合排名第一的数字
			numList = scaleService.getMaybeNumbers(rank,numberData,numList);
		}
		return Result.ok(numList);
	}

	@RequestMapping("/validate")
	@ResponseBody
	public Result validate() {
		//校验近20期的匹配率
		for (int num = 153; num > 133; num--) {
			
			List<Integer> numList = new ArrayList<Integer>();
			
			String column = Constant.ONE;
			for (int i = 0; i < 6; i++) {
				column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
				
				//获取每列的数字信息
				Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(num,column);
				//保存进数据库
				scaleService.saveBatch(numberData, column);
				List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
				//获取综合排名第一的数字
				numList = scaleService.getMaybeNumbers(rank,numberData,numList);
			}
			
			scaleService.validate(numList);
		}
		return Result.ok();
	}
}
