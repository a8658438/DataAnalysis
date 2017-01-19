package com.minstone.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minstone.pojo.JddData;
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
	
	/**
	 * 校验程序的匹配率
	 * @param id 开始的期数
	 * @param nums 统计的期数
	 * @return
	 */
	@RequestMapping("/validate1/{id}")
	@ResponseBody
	public Result validate1(@PathVariable Integer id,Integer nums) {
		List<Object> list = new ArrayList<Object>();
		int count = 0;//计算匹配的次数
		//校验近20期的匹配率
		for (int id1 = id; id1 > id - nums; id1--) {
			System.out.println("当前计算第："+id1+"期");
			List<Integer> numList = new ArrayList<Integer>();
			
			List<JddData> list1 = service.selectAllData(1,id - 1,"id desc");
			List<Integer> numbers = new ArrayList<Integer>();
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			for (int y = 0; y < 6; y++) {
				for (int n = 1; n < 16; n++) {//上几期的几
					System.out.println("上"+n+"期：");
					for (int shu = 1; shu <= 9; shu++) {//加减的数
						int count1 = 0;
						for (int i = 0; i < list1.size()-n; i++) {
							JddData obj1 =list1.get(i);//1.序号,2.特码
							JddData obj2 =list1.get(i + n);//1.序号,2.特码
							int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
							int sq2 = y==0?obj2.getOne():y==1?obj2.getTwo():y==2?obj2.getThree():y==3?obj2.getFour():y==4?obj2.getFive():obj2.getSix();
							
							int x = 0;
							if(sq2 >= 6){
								x = sq2 - shu;
							}else{
								x = sq2 + shu;
							}
							if(sq1 == x){
								count1++ ;
							}
							
						}
						if(count1 == 0 || count1 == 1){
							map.put(n+"-"+shu, count1);
							System.out.println("成----" + shu + ":" + count1);
						}
					}
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
				
				//计算每个数字与前面对应期数的加减值
				for (int i = 1; i < 50; i++) {
					for (int n = 0; n < 15; n++) {//上几期的几
						for (int shu = 1; shu <= 9; shu++) {//加减的数
							int count1 = 0;
							JddData obj1 =list1.get(n);//1.序号,2.特码
							int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
							
							int x = 0;
							if(sq1 >= 6){
								x = sq1 - shu;
							}else{
								x = sq1 + shu;
							}
							if(i == x){
								count1++ ;
								Integer num = map.get(n+"-"+shu);
								if (num != null) {
									numbers.add(i);
									System.out.println("数字："+i+"与上"+n+"期排除");
								}
							}
						}
					}
				}
			}
			
				String column = Constant.ONE;
				for (int i = 0; i < 6; i++) {
					column = i==0?Constant.ONE:i==1?Constant.TWO:i==2?Constant.THREE:i==3?Constant.FOUR:i==4?Constant.FIVE:Constant.SIX;
					
					Map<Integer, Map<String, Integer>> nunberData1 = new HashMap<Integer, Map<String,Integer>>();
					//获取每列的数字信息
					Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(id,column);
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
			
			Map<String, Object> map2 = service.validate(numList,id1);
			map2.put("staticNumbers", numList);//保存计算出的数字
			list.add(map2);
			if ((Boolean) map2.get("mate")) {
				count ++;
			}
		}
		Double d = CommonUtil.getNumDecimal(Double.parseDouble(count+"") / Double.parseDouble(nums+""), 2);
		list.add(0, d);
		return Result.ok(list);
	}
	
	
	/**
	 * 校验程序的匹配率（加入更多过滤）
	 * @param id 开始的期数
	 * @param nums 统计的期数
	 * @return
	 */
	@RequestMapping("/validate2/{id}")
	@ResponseBody
	public Result validate2(@PathVariable Integer id,Integer nums) {
		List<Object> list = new ArrayList<Object>();
		int count = 0;//计算匹配的次数
		//校验近20期的匹配率
		for (int id1 = id; id1 > id - nums; id1--) {
			System.out.println("当前计算第："+id1+"期");
			List<Integer> numList = new ArrayList<Integer>();
			
			List<JddData> list1 = service.selectAllData(1,id - 1,"id desc");
			//查询column列最大最小值
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("startNum", 1);
			param.put("endNum", id -1);
			Map<String, Integer> maxAndMin = service.selectMaxAndMinByColumn(param);

			//分开统计各列数据
			for (int y = 0; y < 6; y++) {
				String column = y==0?Constant.ONE:y==1?Constant.TWO:y==2?Constant.THREE:y==3?Constant.FOUR:y==4?Constant.FIVE:Constant.SIX;
				
				//记录排除的数字
				List<Integer> numbers = new ArrayList<Integer>();
				Map<String, Integer> map = new HashMap<String, Integer>();
				
				for (int n = 1; n < 16; n++) {//上几期的几
					System.out.println("上"+n+"期：");
					for (int shu = 1; shu <= 9; shu++) {//加减的数
						int count1 = 0;
						for (int i = 0; i < list.size()-n; i++) {
							JddData obj1 =list1.get(i);//1.序号,2.特码
							JddData obj2 =list1.get(i + n);//1.序号,2.特码
							int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
							int sq2 = y==0?obj2.getOne():y==1?obj2.getTwo():y==2?obj2.getThree():y==3?obj2.getFour():y==4?obj2.getFive():obj2.getSix();
							
							int x = 0;
							if(sq2 >= 6){
								x = sq2 - shu;
							}else{
								x = sq2 + shu;
							}
							if(sq1 == x){
								count1++ ;
							}
							
						}
						if(count1 == 0 || count1 == 1){
							map.put(n+"-"+shu, count1);
							System.out.println("成----" + shu + ":" + count1);
						}
					}
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
				
				//计算每个数字与前面对应期数的加减值
				for (int i = 1; i < 50; i++) {
					for (int n = 0; n < 15; n++) {//上几期的几
						for (int shu = 1; shu <= 9; shu++) {//加减的数
							int count1 = 0;
							JddData obj1 =list1.get(n);//1.序号,2.特码
							int sq1 = y==0?obj1.getOne():y==1?obj1.getTwo():y==2?obj1.getThree():y==3?obj1.getFour():y==4?obj1.getFive():obj1.getSix();
							
							int x = 0;
							if(sq1 >= 6){
								x = sq1 - shu;
							}else{
								x = sq1 + shu;
							}
							if(i == x){
								count1++ ;
								Integer num = map.get(n+"-"+shu);
								if (num != null) {
									if(!numbers.contains(i)) numbers.add(i);
									System.out.println("数字："+i+"与上"+n+"期排除");
								}
							}
						}
					}
					//通过列取值区间排除
					if (i < maxAndMin.get("min_"+column) || i > maxAndMin.get("max_"+column)) {
						System.out.println("数字："+i+"超范围排除");
						if(!numbers.contains(i)) numbers.add(i);
					}
					//通过出现频率极低的排除
					List<Map<String, Long>> countNumbersList = service.countNumberShows(column);
					for (Map<String, Long> map2 : countNumbersList) {
						if (Integer.parseInt(map2.get("count_num")+"") ==2 || Integer.parseInt(map2.get("count_num")+"") == 1) {
							System.out.println("数字："+map2.get(column)+"出现概率低被排除");
							Integer a = Integer.parseInt(map2.get(column)+"");
							if(!numbers.contains(a)) numbers.add(a);
						}
					}
				}
				
				Map<Integer, Map<String, Integer>> nunberData1 = new HashMap<Integer, Map<String,Integer>>();
				//获取每列的数字信息
				Map<Integer, Map<String, Integer>> numberData = service.getNumbersDataForColumn(id,column);
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
			
			Map<String, Object> map2 = service.validate(numList,id1);
			map2.put("staticNumbers", numList);//保存计算出的数字
			list.add(map2);
			if ((Boolean) map2.get("mate")) {
				count ++;
			}
		}
		Double d = CommonUtil.getNumDecimal(Double.parseDouble(count+"") / Double.parseDouble(nums+""), 2);
		list.add(0, d);
		return Result.ok(list);
	}
	@RequestMapping("/showJddData")
	public String showJddData(Model model) {
		//查询列表数据
		List<JddData> list = service.selectAllData(null,null,"id desc");
		model.addAttribute("data", list);
		model.addAttribute("numbers", CommonUtil.getAllNumbers());
		return "data_sum";
	}
}
