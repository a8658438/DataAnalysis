package com.minstone.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minstone.service.JddDataService;
import com.minstone.util.Result;

@Controller
@RequestMapping("/jddData")
public class JddDataController {
	@Autowired
	private JddDataService service;

	@RequestMapping("/{num}")
	@ResponseBody
	public Result query(@PathVariable Integer num) {
		System.out.println(num);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startNum", num - 10);
		param.put("endNum", num - 1);
		service.selectSumByStage(param);
		return Result.ok();
	}

}
