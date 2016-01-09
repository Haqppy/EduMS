package com.atguigu.ems.utils.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import com.atguigu.ems.orm2.PropertyFilter;
import com.atguigu.ems.utils.ReflectionUtils;


/**
 * Hibernate 针对 Web 应用的 Utils 函数集合
 * @author Administrator
 *
 */
public class HibernateWebUtils {
	
	private HibernateWebUtils(){}
	
	/**
	 * 根据按 PropertyFilter 命名规则的 Request 参数, 创建 PropertyFilter 列表
	 * PropertyFilter 命名规则为: Filter 属性前缀_标胶类型_属性名. 例如: filter_LIKE_name_OR_email
	 * @param request
	 * @param filterPrefix
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PropertyFilter> buildPropertyFilters(HttpServletRequest request, String filterPrefix){
		List<PropertyFilter> fillterList = new ArrayList<PropertyFilter>();
		
		//从 request 中获取含属性名前缀名的参数, 构造去除前缀名后的参数 Map.
		Map<String, Object> filterParamMap = WebUtils.getParametersStartingWith(request, filterPrefix);
		 
		//分析参数 Map, 构造 PropertyFilter 列表
		for(Map.Entry<String, Object> entity: filterParamMap.entrySet()){
			String filterName = entity.getKey();
			String value = (String) entity.getValue();
			
			//若 value 为空, 则忽略此 filter
			boolean omit = StringUtils.isBlank(value);
			
			if(!omit){
				PropertyFilter filter = new PropertyFilter(filterName, value);
				fillterList.add(filter);
			}
		}
		
		return fillterList;
	}
	
	/**
	 * 根据安 PropertyFilter 命名规则的 Request 参数, 创建 PropertyFilter 列表
	 * 默认 Filter 属性名前缀为 filter_
	 * @param request
	 * @return
	 */
	public static List<PropertyFilter> buildPropertyFilters(HttpServletRequest request){
		return buildPropertyFilters(request, "filter_");
	}
	
	/**
	 * 根据对象 ID 集合, 整理合并集合
	 * 
	 * 页面发送变更后的子对象 id 列表时, 删除原来的子对象集合再根据页面 id 列表创建一个全新的集合这种看似简单的做法是不行的.
	 * 因此需要采用如此的整合算法: 在源集合中删除 id 不再 ID 集合中的对象, 根据 ID 集合中的 id 创建对象并添加到源集合中.
	 * @param <T>
	 * @param <ID>
	 * @param srcObjects: 源对象集合
	 * @param checkIds: 目标 ID 集合
	 * @param clazz: 集合中对象的类型
	 * @param ids: 对象主键的名称
	 */
	public static <T, ID> void mergeByCheckedIds(Collection<T> srcObjects, Collection<ID> checkIds,
			Class<T> clazz, String idName){
		//参数校验
		Assert.notNull(srcObjects, "scrObjects不能为空");
		Assert.hasText(idName, "idName不能为空");
		Assert.notNull(clazz, "clazz不能为空");
		
		//目标 ID 集合为空, 删除源集合中所有对象后直接返回
		if(checkIds == null){
			srcObjects.clear();
			return;
		}
		
		//遍历源集合, 如果其 id 不再目标 id 集合中的对象, 进行删除
		//同时, 在目标 ID 集合中删除已在源集合中的 id, 使得目标 ID 集合中剩下的 id 均为源集合中没有的 ID。
		Iterator<T> srcIterator = srcObjects.iterator(); 
		
		try {
			while(srcIterator.hasNext()){
				T element = srcIterator.next();
				
				Object id = null;
				id = PropertyUtils.getProperty(element, idName);
				
				if(!checkIds.contains(id)){
					srcIterator.remove();
				}else{
					checkIds.remove(id);
				}
			}
			
			//ID 集合目前剩余的 id 均不在源集合中, 创建对象, 为 id 属性赋值并添加到源集合中.
			for(ID id: checkIds){
				T obj = clazz.newInstance();
				
				PropertyUtils.setProperty(obj, idName, id);
				srcObjects.add(obj);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw ReflectionUtils.convertToUncheckedException(e);
		}
	}
	
	/**
	 * 根据对象 ID 集合, 整理合并集合
	 * 默认对象主键的名称为 "id"
	 * @param <T>
	 * @param <ID>
	 * @param srcObjects
	 * @param checkedIds
	 * @param clazz
	 */
	public static<T, ID> void mergeByCheckedIds(Collection<T> srcObjects, Collection<ID> checkedIds, Class<T> clazz){
		mergeByCheckedIds(srcObjects, checkedIds, clazz, "id");
	}
}
