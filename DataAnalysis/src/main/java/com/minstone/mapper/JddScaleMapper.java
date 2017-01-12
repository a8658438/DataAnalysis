package com.minstone.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.minstone.pojo.JddScale;
import com.minstone.pojo.JddScaleExample;

public interface JddScaleMapper {
    int countByExample(JddScaleExample example);

    int deleteByExample(JddScaleExample example);

    int insert(JddScale record);

    int insertSelective(JddScale record);

    List<JddScale> selectByExample(JddScaleExample example);

    int updateByExampleSelective(@Param("record") JddScale record, @Param("example") JddScaleExample example);

    int updateByExample(@Param("record") JddScale record, @Param("example") JddScaleExample example);

	void deleteColumnData(String column);
	List<Map<String, Object>> queryColumnRank(String column);
}