package com.atguigu.ems.orm;

import java.util.List;

public class Page<T> {

	//排序相关的两个字段
	private static final String DESC = "desc";
	private static final String ASC = "asc";
	
	//从前端传入的分页字段
	private int pageSize = 5;
	private int pageNo = 1;
	
	//从服务端需要获取的两个字段
	private long totalItemNumbers;
	private List<T> content = null;
	
	public void setPageNo(int pageNo) {
		if(pageNo < 1){
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}
	
	//根据总页数校验当前页码. 后面使用的 pageNo 统一使用 getPageNo 方法, 而不是使用 pageNo 字段. 
	public int getPageNo() {
		if(this.pageNo > getTotalPages()){
			this.pageNo = getTotalPages();
		}
		
		return this.pageNo;
	}
	
	public void setPageSize(int pageSize) {
		if(pageSize < 1){
			pageSize = 5;
		}
		this.pageSize = pageSize;
	}
	
	public int getPageSize() {
		
		return pageSize;
	}
	
	//服务端需要设置的. 到服务端最先设置该属性. 
	public void setTotalItemNumbers(long totalItemNumbers) {
		this.totalItemNumbers = totalItemNumbers;
	}
	
	public long getTotalItemNumbers() {
		return totalItemNumbers;
	}
	
	public void setContent(List<T> content) {
		this.content = content;
	}
	
	public List<T> getContent() {
		return content;
	}
	
	//返回总的页数. 由计算得到. 该方法需要在 setTotalItemNumbers 方法后被调用 
	public int getTotalPages(){
		int totalPages = 0;
		
		totalPages = (int)this.totalItemNumbers / this.pageSize;
		if(this.totalItemNumbers % this.pageSize != 0){
			totalPages++;
		}
		
		return totalPages;
	}
	
	//是否存在上一页
	public boolean isHasPrevPage(){
		if(this.getPageNo() != 1){
			return true;
		}
		
		return false;
	}
	
	//返回上一页的页码
	public int getPrevPage(){
		if(isHasPrevPage()){
			return this.getPageNo() - 1;
		}
		
		return this.getPageNo();
	}
	
	//是否存在下一页
	public boolean isHasNextPage(){
		if(getPageNo() < getTotalPages()){
			return true;
		}
		
		return false;
	}
	
	//返回下一页
	public int getNextPage(){
		if(isHasNextPage()){
			return this.getPageNo() + 1;
		}
		
		return this.getPageNo();
	}
	
	//从 0 开始
	public int getFirstResult(){
		return (getPageNo() - 1) * this.pageSize;
	}
	
	public int getMaxResults(){
		return this.pageSize;
	}
	
	//和排序相关的. 若按多个字段排序. 则传入的 order 和 orderBy 应该是匹配的. 
	//例如传入的 order 为 ASC,DESC,ASC, 对应的 orderBy 应为 email,salary,age
	private String order;
	private String orderBy;
	
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
}
