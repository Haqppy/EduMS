package com.atguigu.ems.struts2.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.atguigu.ems.entities.Authority;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.exceptions.EmployeeNotExitException;
import com.atguigu.ems.exceptions.InvalidPageException;
import com.atguigu.ems.exceptions.NotMatchPasswordException;
import com.atguigu.ems.orm.Page;
import com.atguigu.ems.security.SecurityUser;
import com.atguigu.ems.service.DepartmentService;
import com.atguigu.ems.service.EmployeeService;
import com.atguigu.ems.service.RoleService;
import com.atguigu.ems.utils.Navigation;
import com.atguigu.ems.utils.UploadError;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Scope("prototype")
@Controller
public class EmployeeAction 
	extends ActionSupport 
	implements ModelDriven<Employee>, Preparable, SessionAware, RequestAware{

	private static final long serialVersionUID = 1L;
	
	private Employee model;
	
	@Override
	public Employee getModel() {
		return model;
	}
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DepartmentService departmentService;
	
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	private Page<Employee> page = new Page<Employee>();
	
	public void setPage(Page<Employee> page) {
		this.page = page;
	}
	
	public Page<Employee> getPage() {
		return page;
	}
	
	private Integer id;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	private String contentType = "text/html";
	
	public String getContentType() {
		return contentType;
	}
	
	private long contentLength = 1;
	
	public long getContentLength() {
		return contentLength;
	}
	
	private String contentDisposition = null;
	
	public String getContentDisposition() {
		return contentDisposition;
	}
	
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}
	
	//显示查询条件的页面
	public String criteriaInput(){
		request.put("departments", departmentService.getAll());
		return "criteria-input";
	}
	
	private List<Navigation> navigations;
	
	public List<Navigation> getNavigations() {
		return navigations;
	}
	
	public String navigate(){
		SecurityUser user = 
				(SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Employee employee = user.getEmployee();
		
		navigations = new ArrayList<>();
		
		Navigation navigation = new Navigation();
		navigation.setId("1001");
		navigation.setText("尚硅谷智能网络办公");
		navigations.add(navigation);
		
		List<Navigation> children = new ArrayList<>();
		navigation.setChildren(children);
		
		//存放每个具体权限的父权限对应的 Navigation 对象, 键为父权限对应的 Authority 对象, 值为 Navigation
		Map<Authority, Navigation> parentNavigations = new HashMap<Authority, Navigation>();
		
		for(Role role: employee.getRoles()){
			for(Authority authority: role.getAuthorities()){
				if(authority.getMainResource() == null)
					continue;
				
				String displayName = authority.getDisplayName();
				String id = authority.getId() + "";
				
				Navigation n = new Navigation();
				n.setId(id);
//				n.setState("closed");
				n.setText(displayName);
				String url = authority.getMainResource().getUrl();
				url = url.substring(0, url.length() - 1);
				url = ServletActionContext.getServletContext().getContextPath() + url;
				System.out.println(url);
				
				n.setUrl(url);
				
				Navigation pn = parentNavigations.get(authority.getParentAuthority());
				if(pn == null){
					Authority parentAuthority = authority.getParentAuthority();
					
					pn = new Navigation();
					pn.setState("closed");
					pn.setId(parentAuthority.getId() + "");
					pn.setText(parentAuthority.getDisplayName());
					pn.setChildren(new ArrayList<Navigation>());
					
					parentNavigations.put(parentAuthority, pn);
					children.add(pn);
				}
				
				pn.getChildren().add(n);
			}
		}
		
		Navigation logout = new Navigation();
		logout.setId(3000 + "");
		logout.setText("登出");
		logout.setUrl(ServletActionContext.getServletContext().getContextPath() + "/security-logout");
		children.add(logout);
		
		System.out.println("children:" + children);
		
		return "navigate-success";
	}
	
	public String upload() throws Exception{
		//1. 解析得到 Workbook 对象
		InputStream inp = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(inp);
		
		//2. 调用 Service 方法上传数据到数据库服务器
		List<UploadError> uploadErrors = employeeService.upload(wb);
		
		//3. 若验证没有通过: 上述方法有返回值. 返回值为 
		if(uploadErrors.size() > 0){
			//4. 获取 i18n 中的错误消息, 并把所有的错误消息都加到 actionErrors 中. 
			for(UploadError ue: uploadErrors){
				String fileName = ue.fieldName;
				int rowNumber = ue.rowNumber;
				
				String errorMesssage = 
						getText("errors.fileupload", new String[]{rowNumber + "", fileName});
				addActionError(errorMesssage);
			}
			
			//5. 返回 upload-input 页面
			return "upload-input";
		}
		
		return "upload-success";
	}
	
	public String uploadTemplateDownload() throws IOException{
		//1. 调用 Service 方法, 获取 Excel 文档对应的 Workbook 对象
		Workbook workbook = employeeService.buildUploadExcelWorkbook();
		//2. 先写到一个指定的位置
		String fileName = 
				ServletActionContext.getServletContext().getRealPath("/files/" + System.currentTimeMillis() + ".xls");
		workbook.write(new FileOutputStream(fileName));
		
		//3. 确定输入流
		inputStream = new FileInputStream(fileName);
		this.contentType = "application/vnd.ms-excel";
		this.contentLength = inputStream.available();
		this.contentDisposition = "attachment;filename=employees.xls";
		
		return "download-success";
	}
	
	//Excel 文件的下载. 
	public String downToExcel() throws IOException{
		//1. 调用 Service 方法, 获取 Excel 文档对应的 Workbook 对象
		Workbook workbook = employeeService.buildExcelWorkbook();
		
		//2. 先写到一个指定的位置
		String fileName = 
				ServletActionContext.getServletContext().getRealPath("/files/" + System.currentTimeMillis() + ".xls");
		workbook.write(new FileOutputStream(fileName));
		
		//3. 确定输入流
		inputStream = new FileInputStream(fileName);
		this.contentType = "application/vnd.ms-excel";
		this.contentLength = inputStream.available();
		this.contentDisposition = "attachment;filename=employees.xls";
		
		return "download-success";
	}
	
	//ajax 检验指定的 Employee 是否为 Manager
	public String ajaxValidateManager(){
		if(employeeService.isManager(id)){
			inputStream = new ByteArrayInputStream("1".getBytes());
		}else{
			delete();
			inputStream = new ByteArrayInputStream("0".getBytes());
		}
		
		return "ajax-success";
	}
	
	public void delete(){
		employeeService.delete(id);
	}
	
	public String list(){
		employeeService.getPage(page);
		return "list";
	}
	
	//ajax 检验 loginName 是否可用
	public String ajaxValidateLoginName(){
		String loginName = model.getLoginName();
		try {
			employeeService.login(loginName, "");
		} catch (EmployeeNotExitException e) {
			inputStream = new ByteArrayInputStream("1".getBytes());
		} catch(NotMatchPasswordException e){
			inputStream = new ByteArrayInputStream("0".getBytes());
		}
		
		return "ajax-success";
	}
	
	public void prepareAjaxValidateLoginName(){
		model = new Employee();
	}
	
	private String oldLoginName;
	
	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}
	
	public String validateLoginName(){
		
		if(oldLoginName.equals(model.getLoginName())){
			return "success";
		}
		
		//验证 loginName 是否可用
		String loginName = model.getLoginName();
		try {
			employeeService.login(loginName, "");
		} catch (EmployeeNotExitException e) {
			//用户名可用. 
			return "success";
		} catch(NotMatchPasswordException e){
			addFieldError("loginName", getText("errors.loginname.invalid"));
			return "save-input";
		}
		
		return null;
	}
	
	public String save(){
		String result = validateLoginName();
		
		employeeService.save(model); 
		return result;
	}
	
	public void prepareSave(){
		if(id == null)
			model = new Employee();
		else{
			model = employeeService.get(id);
			model.getRoles().clear();
		}
	}
	
	public String input(){
		this.request.put("departments", departmentService.getAll());
		this.request.put("roles", roleService.getAll());
		return "input";
	}
	
	public void prepareInput(){
		if(id != null){
			model = employeeService.get(id);
			if(model == null){
				throw new InvalidPageException("页面不存在");
			}
		}
	}
	
	public String login(){
		//Employee employee = employeeService.login(model.getLoginName(), model.getPassword());
		//从 SpringSecurity 中获取登录信息
		SecurityUser user = 
				(SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("employee: " + user.getEmployee());
		this.session.put("employee", user.getEmployee());
		return "success";
	}
	
//	public void prepareLogin(){
//		model = new Employee();
//	}

	@Override
	public void prepare() throws Exception {}

	private Map<String, Object> session;
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}
	
	private Map<String, Object> request;

	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

}
