package com.atguigu.ems.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.DepartmentDao;
import com.atguigu.ems.dao.EmployeeDao;
import com.atguigu.ems.dao.RoleDao;
import com.atguigu.ems.entities.Department;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.exceptions.EmployeeNotExitException;
import com.atguigu.ems.exceptions.NotMatchPasswordException;
import com.atguigu.ems.orm.Page;
import com.atguigu.ems.utils.UploadError;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private RoleDao roleDao;
	
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	
	public Employee get(Integer id) {
		return employeeDao.getByRef(id);
	}
	
	public Workbook buildUploadExcelWorkbook() {
		Workbook wb = null;
		
		//1. 创建 Workbook 对象
		wb = new HSSFWorkbook();
		
		//2. 创建一个工作表 Sheet
		Sheet sheet = wb.createSheet("employees");
		
		//3. 创建标头对应的行: 序号 登录名 姓名	性别	登录许可	部门	职位	E-mail 角色
		fillHeaders(sheet);
		
		//4. 设置样式: 单元格的宽度, 高度和边框. 
		setStyles(sheet, wb);
		
		return wb;
	}
	
	public Workbook buildExcelWorkbook(){
		Workbook wb = null;
		
		//1. 创建 Workbook 对象
		wb = new HSSFWorkbook();
		
		//2. 创建一个工作表 Sheet
		Sheet sheet = wb.createSheet("employees");
		
		//3. 创建标头对应的行: 序号 登录名 姓名	性别	登录许可	部门	职位	E-mail 角色
		fillHeaders(sheet);
		
		//4. 填写其他的 Employee 信息
		fillData(sheet);
		
		//5. 设置样式: 单元格的宽度, 高度和边框. 
		setStyles(sheet, wb);
		
		return wb;
	}
	
	private void setStyles(Sheet sheet, Workbook wb) {
		//1. 设置行高
		for(int i = 0; i <= sheet.getLastRowNum(); i++){
			Row row = sheet.getRow(i);
			row.setHeightInPoints(30);
		}
		
		//2. 设置列宽
		for(int i = 0; i < HEADERS.length; i++){
			sheet.autoSizeColumn((short)i);
			sheet.setColumnWidth(i, (int)(sheet.getColumnWidth(i) * 1.3));
		}
		
		//3. 设置边框
		CellStyle style = wb.createCellStyle();
	    style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    
	    for(int i = 0; i <= sheet.getLastRowNum(); i++){
	    	Row row = sheet.getRow(i);
	    	for(int j = 0; j < HEADERS.length; j++){
	    		Cell cell = row.getCell(j);
	    		cell.setCellStyle(style);
	    	}
	    }
	}

	private void fillData(Sheet sheet) {
		List<Employee> employees = employeeDao.getAll();
		
		for(int i = 0; i < employees.size(); i++){
			Employee emp = employees.get(i);
			
			Row row = sheet.createRow((short) (i + 1));
			
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(emp.getLoginName());
			row.createCell(2).setCellValue(emp.getEmployeeName());
			
			row.createCell(3).setCellValue(emp.getGender());
			row.createCell(4).setCellValue(emp.getEnabled());
			row.createCell(5).setCellValue(emp.getDepartment().getDepartmentName());

			row.createCell(6).setCellValue(emp.getEmail());
			row.createCell(7).setCellValue(emp.getRoleNames());
		}
	}

	public static final String [] HEADERS = 
			new String[]{"序号","登录名","姓名","性别","登录许可","部门","E-mail", "角色"};
	
	private void fillHeaders(Sheet sheet) {
		Row row = sheet.createRow((short) 0);
		
		for(int i = 0; i < HEADERS.length; i++){
			row.createCell(i).setCellValue(HEADERS[i]); 
		}
	}
	
	public Employee getEmployeeByLoginName(String loginName){
		return employeeDao.getByLoginName(loginName);
	}

	public void delete(Integer id) {
		Employee employee = employeeDao.get(id);
		employee.setIsDeleted(1);
	}
	
	public boolean isManager(Integer id){
		return departmentDao.isManager(id);
	}
	
	public void getPage(Page<Employee> page){
		employeeDao.getPage(page);
	}
	
	public void save(Employee employee){
		if(employee.getEmployeeId() == null){
			employee.setVisitedTimes(0);
			
			employee.setPassword(passwordEncoder.encodePassword("123456", employee.getLoginName())); 
		}
		
		employeeDao.save(employee);
	}
	
	//登录操作
	public Employee login(String loginName, String password){
		Employee employee = employeeDao.getByLoginName(loginName);
		
		if(employee == null){
			throw new EmployeeNotExitException("用户名不存在");
		}
		
		if(!password.equals(employee.getPassword())){
			throw new NotMatchPasswordException("用户名和密码不匹配");
		}
		
		//使访问次数 + 1
		employee.setVisitedTimes(employee.getVisitedTimes() + 1);
		
		return employee;
	}

	/**
	 * 把 Workbook 对象的数据上传到数据库服务器
	 * @param wb
	 * @return 
	 */
	public List<UploadError> upload(Workbook wb) {
		List<UploadError> errors = new ArrayList<>();
		
		//1. 解析 Excel 文档, 得到一个 List<Map<String, Object>>.
		List<Map<String, Object>> data = parseWorkbook(wb);
		
		//2. 验证. 若验证通过, 则把一个 Map 转为一个 Employee 对象
		List<Employee> emps = transToEmployees(data, errors);
		
		//3. 若验证不通过, 则把错误消息封装为一个 UploadError 对象, 并加入到 errors 中
		//4. 若没验证 50 条没有错误, 则完成一次上传.
		//5. 若验证的错误超过 10 行, 则停止验证, 给用户提示信息. 
		if(errors.size() == 0){
			employeeDao.save(emps);
		}
		
		return errors;
	}
	
	private List<Employee> transToEmployees(List<Map<String, Object>> data,
			List<UploadError> errors) {
		List<Employee> emps = new ArrayList<>();
		
		for(Map<String, Object> map: data){
			int rowNumber = (int)Double.parseDouble((String)map.get(NUM));
			
			String loginName = (String) map.get(LAST_NAME);
			if(loginName.length() < 6){
				errors.add(new UploadError(rowNumber, "loginName"));
			}
			
			String empName = (String) map.get(EMPLOYEE_NAME);
			//验证
			
			String gender = (String) map.get(GENDER);
			//验证

			String enabled = (String) map.get(ENABLED);
			//验证
			
			String deptName = (String) map.get(DEPT);
			//验证
			Department dept = departmentDao.getDepartmentByName(deptName);
			
			String email = (String) map.get(EMAIL);
			//验证
			
			String roleNames = (String) map.get(ROLES);
			//验证
			Set<Role> roles = new HashSet<>(roleDao.getRoleByNames(roleNames.split(",")));
			
			Employee e = new Employee();
			
			e.setDepartment(dept);
			e.setEmail(email);
			e.setEmployeeName(empName);
			
			e.setEnabled((int)Double.parseDouble(enabled));
			e.setGender((int)Double.parseDouble(gender) + "");
			e.setLoginName(loginName);
			
			e.setPassword("123456");
			e.setRoles(roles);
			e.setVisitedTimes(0);
			
			emps.add(e);
		}
		
		return emps;
	}

	public static final String NUM = "num";
	public static final String LAST_NAME = "lastname";
	public static final String EMPLOYEE_NAME = "employeename";
	
	public static final String GENDER = "gender";
	public static final String ENABLED = "enabled";
	public static final String DEPT = "dept";
	
	public static final String EMAIL = "email";
	public static final String ROLES = "roles";

	private List<Map<String, Object>> parseWorkbook(Workbook wb) {
		List<Map<String, Object>> data = new ArrayList<>();
		
		Sheet sheet = wb.getSheet("employees");
		System.out.println("lastRowNum: " + sheet.getLastRowNum());
		
		for(int i = 1; i <= sheet.getLastRowNum(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			Row row = sheet.getRow(i);
			
			if(row.getCell(0) == null){
				break;
			}
			
			map.put(NUM, row.getCell(0).getNumericCellValue() + "");
			map.put(LAST_NAME, row.getCell(1).getStringCellValue());
			map.put(EMPLOYEE_NAME, row.getCell(2).getStringCellValue());
			
			map.put(GENDER, row.getCell(3).getNumericCellValue() + "");
			map.put(ENABLED, row.getCell(4).getNumericCellValue() + "");
			map.put(DEPT, row.getCell(5).getStringCellValue());
			
			map.put(EMAIL, row.getCell(6).getStringCellValue());
			map.put(ROLES, row.getCell(7).getStringCellValue());
			
			System.out.println(map); 
			data.add(map);
		}
		
		return data;
	}


}
