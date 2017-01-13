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
import com.minstone.util.CommonUtil;
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

	/**
	 * 校验程序的匹配率
	 * @param id 开始的期数
	 * @param nums 统计的期数
	 * @return
	 */
	@RequestMapping("/validate/{id}")
	@ResponseBody
	public Result validate(@PathVariable Integer id,Integer nums) {
		List<Object> list = new ArrayList<Object>();
		int count = 0;//计算匹配的次数
		//校验近20期的匹配率
		for (int id1 = id; id1 > id - nums; id1--) {
			System.out.println("当前计算第："+id1+"期");
			List<Integer> numList = new ArrayList<Integer>();
			
			String column = Constant.ONE;
			for (int i = 0; i < 6; i++) {
				column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
				
				//获取每列的数字信息
				Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(id1,column);
				//保存进数据库
				scaleService.saveBatch(numberData, column);
				List<Map<String, Object>> rank = scaleService.queryColumnRank(column);
				//获取综合排名第一的数字
				numList = scaleService.getMaybeNumbers(rank,numberData,numList);
			}
			
			Map<String, Object> map = service.validate(numList,id1);
			map.put("staticNumbers", numList);//保存计算出的数字
			list.add(map);
			if ((Boolean) map.get("mate")) {
				count ++;
			}
		}
		Double d = CommonUtil.getNumDecimal(Double.parseDouble(count+"") / Double.parseDouble(nums+""), 2);
		list.add(0, d);
		return Result.ok(list);
	}
}
