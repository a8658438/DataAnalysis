package com.minstone.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.minstone.pojo.JddData;
import com.minstone.pojo.JddDataExample;

public interface JddDataMapper {
    int countByExample(JddDataExample example);

    int deleteByExample(JddDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(JddData record);

    int insertSelective(JddData record);

    List<JddData> selectByExample(JddDataExample example);

    JddData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JddData record, @Param("example") JddDataExample example);

    int updateByExample(@Param("record") JddData record, @Param("example") JddDataExample example);

    int updateByPrimaryKeySelective(JddData record);

    int updateByPrimaryKey(JddData record);
    
    List<Map<String, Long>> selectSumByStage(Map<String, Object> param);
    Map<String, Integer> selectMaxAndMinByColumn(Map<String, Object> param);
    List<Map<String, Long>> selectCountByColumn(Map<String, Object> param);
    List<Map<String, Integer>> selectByColumn(Map<String, Object> param);
    Map<String, Integer> selectLateId(Map<String, Object> param);
    List<Map<String, Integer>> selectIsShowId(Map<String, Object> param);

	Map<String, Integer> countNumberShows(String column);
}