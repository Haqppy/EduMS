package com.atguigu.ems.orm2;

import java.sql.Date;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 与具体 ORM 实现无关的属性过滤条件封装类 主要记录页面中 "简单" 的搜索过滤条件
 */
public class PropertyFilter {

	/**
	 * 多个属性间 OR 关系分隔符
	 */
	public static final String OR_SEPERATOR = "_OR_";

	/**
	 * 属性比较类型
	 */
	public enum MatchType {
		EQ, LIKE, LT, GT, LE, GE, ISNULL;
	}

	/**
	 * 属性数据类型
	 */
	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), F(Float.class), D(
				Date.class), B(Boolean.class), O(Object.class);

		private Class<?> clazz;

		PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	// 需要比对的属性名的数组
	private String[] propertyNames; // [name, email]
	// 属性的类型
	private Class<?> propertyType; // String.class
	// 比较的属性值
	private Object propertyValue; // a
	// 比较类型
	private MatchType matchType = MatchType.EQ; // MatchType.LIKE

	public PropertyFilter() {
	}

	/**
	 * @param filterName: 比较属性字符串, 含待比较的比较类型, 属性值类型及属性列表. 比如: 
	 *                    new PropertyFilter("LIKES_email_OR_loginName", "om");
	 *                    
	 *                    email LIKE '%om%' OR loginName LIKE '%om%'
	 *                    
	 * @param value: 待比较的值
	 */
	public PropertyFilter(String filterName, Object value) {
		String matchTypeStr = StringUtils.substringBefore(filterName, "_"); // LIKES
		String matchTypeCode = StringUtils.substring(matchTypeStr, 0,
				matchTypeStr.length() - 1); // LIKE
		String propertyTypeCode = StringUtils.substring(matchTypeStr,
				matchTypeStr.length() - 1, matchTypeStr.length()); // S

		try {
			matchType = Enum.valueOf(MatchType.class, matchTypeCode); // LIKE
		} catch (RuntimeException e) {
			// e.printStackTrace();
			throw new IllegalArgumentException("filter 名称" + filterName
					+ "没有按规则编写, 无法得到属性比较类型", e);
		}

		try {
			propertyType = Enum.valueOf(PropertyType.class, propertyTypeCode)
					.getValue(); // String.class
		} catch (RuntimeException e) {
			// e.printStackTrace();
			throw new IllegalArgumentException("filter 名称" + filterName
					+ "没有按规则编写, 无法得到属性值类型", e);
		}

		String propertyNameStr = StringUtils.substringAfter(filterName, "_"); // name_OR_loginName
		propertyNames = StringUtils.split(propertyNameStr,
				PropertyFilter.OR_SEPERATOR); // name, loginName

		Assert.isTrue(propertyNames.length > 0, "filter名称" + filterName
				+ "没有按规则编写, 无法得到属性名称");

		this.propertyValue = value; // om
	}

	/**
	 * 是否有多个属性
	 * 
	 * @return
	 */
	public boolean isMultiProperty() {
		return propertyNames.length > 1;
	}

	/**
	 * 返回比较属性列表
	 * 
	 * @return
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * 获取唯一的属性名称
	 * 
	 * @return
	 */
	public String getPropertyName() {
		if (propertyNames.length > 1)
			throw new IllegalArgumentException(
					"There are not only one property");
		return propertyNames[0];
	}

	/**
	 * 获取比较值
	 * 
	 * @return
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}

	/**
	 * 获取比较值的类型
	 * 
	 * @return
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * 获取比较类型
	 * 
	 * @return
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	@Override
	public String toString() {
		return "PropertyFilter [matchType=" + matchType + ", propertyNames="
				+ Arrays.toString(propertyNames) + ", propertyType="
				+ propertyType + ", propertyValue=" + propertyValue + "]";
	}

}