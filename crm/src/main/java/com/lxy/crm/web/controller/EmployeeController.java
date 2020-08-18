package com.lxy.crm.web.controller;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.domain.Menu;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.EmployeeQueryObject;
import com.lxy.crm.service.IEmployeeService;
import com.lxy.crm.service.IMenuService;
import com.lxy.crm.service.IPermissionService;
import com.lxy.crm.util.AjaxResult;
import com.lxy.crm.util.ExcelUtil;
import com.lxy.crm.util.PermissionUtil;
import com.lxy.crm.util.UserContext;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/10- 22:36
 */
@Controller
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final IPermissionService permissionService;
    private final IMenuService menuService;

    @Autowired
    public EmployeeController(IEmployeeService employeeService, IPermissionService permissionService, IMenuService menuService) {
        this.employeeService = employeeService;
        this.permissionService = permissionService;
        this.menuService = menuService;
    }


    /**
     * 请求员工转到employee.html页面
     * @return 页面地址
     */
    @RequestMapping("/employee")
    public String index() {
        return "employee";
    }

    /**
     * 登录请求
     * 如果登录失败,返回ajaxresult
     * 前台解析为json对象,给出失败信息
     *
     * @param username 用户名
     * @param password 密码
     * @param request 当前线程request
     * @return Json对象
     */
    @RequestMapping("/login")
    @ResponseBody
    public AjaxResult login(String username, String password, HttpServletRequest request) {
        /*----------Aop日志相关:给当前线程传入request-----*/
        UserContext.set(request);

        //初始化返回的json串
        AjaxResult result;
        //调用service层的getEmployeeForLogin方法返回当前用户对象
        Employee currentUser = employeeService.getEmployeeForLogin(username, password);
        if (currentUser != null) {
            /*---------判断是否是二次登录----------------*/
            if (currentUser.getOnline()) {
                //返回失败信息
                result = new AjaxResult("不要重复登录,很费服务器资源的", false);
                //清楚二次登录线程的request
                UserContext.remove();
                return result;
            }
            /*-----------登录相关*---------------*/
            //修改后台用户online的状态为在线
            employeeService.updateOnline(currentUser);
            /*-------------修改用户的online属性为在线------------*/
            currentUser.setOnline(true);
            //保存Employee对象到session域
            request.getSession().setAttribute(UserContext.USER_IN_SESSION, currentUser);

            /*为EQO静态成员变量赋值为当前用户的admin*/
            EmployeeQueryObject.setAdmin(currentUser.getAdmin());

            /*-----------------------URL权限验证-------------------*/
            //根据当前用户id查询所拥有的的权限
            List<String> currentUserPermission = permissionService.queryPermissionByEid(currentUser.getId());
            //通过ThreadLocal保存在session中
            request.getSession().setAttribute(UserContext.PERMISSION_IN_SESSION, currentUserPermission);

            /*----------------菜单权限验证-------------------*/
            /*
             * 把用户能访问的菜单存入session
             *   1.先查出系统所有菜单
             *   2.根据当前用户拥有的权限，筛选出用户专属菜单
             * */
            //得到所有菜单集合
            List<Menu> menus = menuService.queryForMenu();
            //检查该用户是否拥有该菜单项权限
            PermissionUtil.checkMenuPermission(menus);
            //将已拥有的菜单项保存到Seesion域中
            request.getSession().setAttribute(UserContext.MENU_IN_SESSION, menus);
            //移除当前线程的request
            UserContext.remove();
            result = new AjaxResult("登录成功", true);
        } else {
            //返回失败信息
            result = new AjaxResult("账号或密码错误", false);
        }
        return result;
    }

    /**
     * 退出登录(销毁session)
     * @return 返回Json
     */
    @RequestMapping("/exit")
    @ResponseBody
    public AjaxResult exit() {
        try {
            //得到session中的user
            Employee employee = (Employee) UserContext.get().getSession().getAttribute(UserContext.USER_IN_SESSION);
            //退出时更改用户为离线状态
            employeeService.updateOnline(employee);
            //得到当前线程的requeset对象,进而得到session然后销毁
            UserContext.get().getSession().invalidate();
            return new AjaxResult("注销成功,返回登录页面", true);
        } catch (Exception e) {
            return new AjaxResult("注销失败", false);
        }

    }

    /**
     * 分页展示所有员工
     *
     * @param qo (两个属性:page->当前页,rows->当前页要显示的行数)
     * @return PageResult(两个属性 : total - 查询到的总记录数 >
     *rows - > 查询到所有员工的List集合)
     */
    @RequestMapping("/employee_list")
    @ResponseBody
    public PageResult list(EmployeeQueryObject qo) {
        PageResult pageResult;
        pageResult = employeeService.queryForPage(qo);
        return pageResult;
    }


    /**
     * 新增员工保存方法
     *
     * @ return 返回自定义的json字符串,
     * 判断新增员工是否成功保存
     */
    @RequestMapping("/employee_save")
    @ResponseBody
    public AjaxResult save(Employee employee) {
        AjaxResult result;
        try {
            //前端只输入部分字段,剩余字段password,state,admin,online后端设置
            employee.setPassword("123");
            employee.setState(true);
            employee.setAdmin(false);
            employee.setOnline(false);
            //账号相同的不能新增,离职账号可以重新新增
            List<Employee> employees = employeeService.selectAllForExcel();
            for (Employee all:employees){
                if (all.getUsername().equals(employee.getUsername())){
                    return new AjaxResult("保存失败,账号不能有相同",false);
                }
            }
            employeeService.insert(employee);
            //保存成功信息到自定义对象result
            result = new AjaxResult("保存成功", true);
        } catch (Exception e) {
            result = new AjaxResult("保存失败,请联系管理员", false);
        }
        return result;
    }

    /**
     * 编辑员工并保存方法
     *
     * @ return 返回自定义的json字符串,
     * 判断新增员工是否成功保存
     */
    @RequestMapping("/employee_update")
    @ResponseBody
    public AjaxResult update(Employee employee) {
        AjaxResult result;
        try {
            //调用更新方法
            employeeService.updateByPrimaryKey(employee);
            //保存成功信息到自定义对象result
            result = new AjaxResult("更新成功", true);
        } catch (Exception e) {
            result = new AjaxResult("更新失败,请联系管理员", false);
        }

        return result;
    }

    /**
     * 发送ajax同步请求
     * 根据每个员工的id查询对应所拥有的角色id集合
     * @param eid 每个员工的id
     * @return List<Long>返回每个角色的id
     */
    @RequestMapping("/role_queryByEid")
    @ResponseBody
    public List<Long> queryByEid(long eid) {
        return employeeService.queryByEid(eid);
    }

    /**
     * 假删除员工改变员工state设置为false
     *
     * @ return 返回自定义的json字符串,
     * 判断新增员工是否成功保存
     */
    @RequestMapping("/employee_delete")
    @ResponseBody
    public AjaxResult delete(@RequestParam(value = "id") Long id) {
        AjaxResult result;
        try {
            //调用更新方法
            employeeService.deleteByPrimaryKey(id);
            //保存成功信息到自定义对象result
            result = new AjaxResult("离职成功", true);
        } catch (Exception e) {
            result = new AjaxResult("离职失败,请联系管理员", false);
        }
        return result;
    }

    /**
     * 导出在职员工为Excel只有超级管理员有这个权限
     * @return 响应体里面有excel文件
     */
    @RequestMapping("/exportEmployeeForExcel")
    @ResponseBody
    public ResponseEntity<byte[]> exportEmployee() {
        //查询所有的在职员工
        List<Employee> employees=employeeService.selectAllForExcel();
        try {
            //这个路径被我写死了,可以改
            String path="G://crm//employee.xls";
            //创建excel文件
            WritableWorkbook wb = Workbook.createWorkbook(new File(path));
            // 创建sheet
            WritableSheet sheet = wb.createSheet("employee", 0);
            //设置行高
            for (int x=0;x<employees.size()+1;x++){
                sheet.setRowView(0, 300);
            }

            for (int i=0;i<5;i++){
                // 设置列宽
                sheet.setColumnView(i, 25);
            }
            //设置Excel的第一行(索引为0)
            ExcelUtil.setLabel(sheet);
            //接下来就是一行一行在职的员工了
            //设置新的标签
            Label cell;
            //设置格式为居中显示
            WritableCellFormat format=new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            for (int i=0;i<employees.size();i++){
                Employee employee = employees.get(i);
                cell=new Label(0,i+1,employee.getRealname(),format);
                sheet.addCell(cell);
                cell=new Label(1,i+1,employee.getTel(),format);
                sheet.addCell(cell);
                cell=new Label(2,i+1,employee.getEmail(),format);
                sheet.addCell(cell);
                cell=new Label(3,i+1,employee.getDept().getName(),format);
                sheet.addCell(cell);

                //将数据库的时间转换为字符串
                Date date = employee.getInputtime();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String inputtime = simpleDateFormat.format(date);
                cell=new Label(4,i+1,inputtime,format);
                sheet.addCell(cell);
            }
            // 将内容写入Excel文件
            wb.write();
            // 关闭
            wb.close();
            //将文件放在响应体中返回给客户端
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            byte[] btm=new byte[inputStream.available()];
            IOUtils.read(inputStream,btm);
            inputStream.close();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Disposition","attachment;filename="+"employee.xls");
            return new ResponseEntity<>(btm,httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}