package com.minstone.pojo;

public class JddScale {
    private Integer num;

    private Integer lateAllScale;

    private Integer otherScale;

    private Integer columnAvgLevel;

    private Integer allAvgLevel;

    private String columnName;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getLateAllScale() {
        return lateAllScale;
    }

    public void setLateAllScale(Integer lateAllScale) {
        this.lateAllScale = lateAllScale;
    }

    public Integer getOtherScale() {
        return otherScale;
    }

    public void setOtherScale(Integer otherScale) {
        this.otherScale = otherScale;
    }

    public Integer getColumnAvgLevel() {
        return columnAvgLevel;
    }

    public void setColumnAvgLevel(Integer columnAvgLevel) {
        this.columnAvgLevel = columnAvgLevel;
    }

    public Integer getAllAvgLevel() {
        return allAvgLevel;
    }

    public void setAllAvgLevel(Integer allAvgLevel) {
        this.allAvgLevel = allAvgLevel;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }
}