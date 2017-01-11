package com.minstone.pojo;

import java.util.ArrayList;
import java.util.List;

public class JddScaleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public JddScaleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return (Criteria) this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return (Criteria) this;
        }

        public Criteria andNumEqualTo(Integer value) {
            addCriterion("num =", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotEqualTo(Integer value) {
            addCriterion("num <>", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThan(Integer value) {
            addCriterion("num >", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("num >=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThan(Integer value) {
            addCriterion("num <", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThanOrEqualTo(Integer value) {
            addCriterion("num <=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumIn(List<Integer> values) {
            addCriterion("num in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotIn(List<Integer> values) {
            addCriterion("num not in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumBetween(Integer value1, Integer value2) {
            addCriterion("num between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotBetween(Integer value1, Integer value2) {
            addCriterion("num not between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleIsNull() {
            addCriterion("late_all_scale is null");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleIsNotNull() {
            addCriterion("late_all_scale is not null");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleEqualTo(Integer value) {
            addCriterion("late_all_scale =", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleNotEqualTo(Integer value) {
            addCriterion("late_all_scale <>", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleGreaterThan(Integer value) {
            addCriterion("late_all_scale >", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleGreaterThanOrEqualTo(Integer value) {
            addCriterion("late_all_scale >=", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleLessThan(Integer value) {
            addCriterion("late_all_scale <", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleLessThanOrEqualTo(Integer value) {
            addCriterion("late_all_scale <=", value, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleIn(List<Integer> values) {
            addCriterion("late_all_scale in", values, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleNotIn(List<Integer> values) {
            addCriterion("late_all_scale not in", values, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleBetween(Integer value1, Integer value2) {
            addCriterion("late_all_scale between", value1, value2, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andLateAllScaleNotBetween(Integer value1, Integer value2) {
            addCriterion("late_all_scale not between", value1, value2, "lateAllScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleIsNull() {
            addCriterion("other_scale is null");
            return (Criteria) this;
        }

        public Criteria andOtherScaleIsNotNull() {
            addCriterion("other_scale is not null");
            return (Criteria) this;
        }

        public Criteria andOtherScaleEqualTo(Integer value) {
            addCriterion("other_scale =", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleNotEqualTo(Integer value) {
            addCriterion("other_scale <>", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleGreaterThan(Integer value) {
            addCriterion("other_scale >", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleGreaterThanOrEqualTo(Integer value) {
            addCriterion("other_scale >=", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleLessThan(Integer value) {
            addCriterion("other_scale <", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleLessThanOrEqualTo(Integer value) {
            addCriterion("other_scale <=", value, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleIn(List<Integer> values) {
            addCriterion("other_scale in", values, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleNotIn(List<Integer> values) {
            addCriterion("other_scale not in", values, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleBetween(Integer value1, Integer value2) {
            addCriterion("other_scale between", value1, value2, "otherScale");
            return (Criteria) this;
        }

        public Criteria andOtherScaleNotBetween(Integer value1, Integer value2) {
            addCriterion("other_scale not between", value1, value2, "otherScale");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelIsNull() {
            addCriterion("column_avg_level is null");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelIsNotNull() {
            addCriterion("column_avg_level is not null");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelEqualTo(Integer value) {
            addCriterion("column_avg_level =", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelNotEqualTo(Integer value) {
            addCriterion("column_avg_level <>", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelGreaterThan(Integer value) {
            addCriterion("column_avg_level >", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("column_avg_level >=", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelLessThan(Integer value) {
            addCriterion("column_avg_level <", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelLessThanOrEqualTo(Integer value) {
            addCriterion("column_avg_level <=", value, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelIn(List<Integer> values) {
            addCriterion("column_avg_level in", values, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelNotIn(List<Integer> values) {
            addCriterion("column_avg_level not in", values, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelBetween(Integer value1, Integer value2) {
            addCriterion("column_avg_level between", value1, value2, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnAvgLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("column_avg_level not between", value1, value2, "columnAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelIsNull() {
            addCriterion("all_avg_level is null");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelIsNotNull() {
            addCriterion("all_avg_level is not null");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelEqualTo(Integer value) {
            addCriterion("all_avg_level =", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelNotEqualTo(Integer value) {
            addCriterion("all_avg_level <>", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelGreaterThan(Integer value) {
            addCriterion("all_avg_level >", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("all_avg_level >=", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelLessThan(Integer value) {
            addCriterion("all_avg_level <", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelLessThanOrEqualTo(Integer value) {
            addCriterion("all_avg_level <=", value, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelIn(List<Integer> values) {
            addCriterion("all_avg_level in", values, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelNotIn(List<Integer> values) {
            addCriterion("all_avg_level not in", values, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelBetween(Integer value1, Integer value2) {
            addCriterion("all_avg_level between", value1, value2, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andAllAvgLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("all_avg_level not between", value1, value2, "allAvgLevel");
            return (Criteria) this;
        }

        public Criteria andColumnNameIsNull() {
            addCriterion("column_name is null");
            return (Criteria) this;
        }

        public Criteria andColumnNameIsNotNull() {
            addCriterion("column_name is not null");
            return (Criteria) this;
        }

        public Criteria andColumnNameEqualTo(String value) {
            addCriterion("column_name =", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotEqualTo(String value) {
            addCriterion("column_name <>", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameGreaterThan(String value) {
            addCriterion("column_name >", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameGreaterThanOrEqualTo(String value) {
            addCriterion("column_name >=", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLessThan(String value) {
            addCriterion("column_name <", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLessThanOrEqualTo(String value) {
            addCriterion("column_name <=", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLike(String value) {
            addCriterion("column_name like", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotLike(String value) {
            addCriterion("column_name not like", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameIn(List<String> values) {
            addCriterion("column_name in", values, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotIn(List<String> values) {
            addCriterion("column_name not in", values, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameBetween(String value1, String value2) {
            addCriterion("column_name between", value1, value2, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotBetween(String value1, String value2) {
            addCriterion("column_name not between", value1, value2, "columnName");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}