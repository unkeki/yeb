package com.ooamo.server.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.ooamo.server.pojo.*;
import com.ooamo.server.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private INationService nationService;

    @Autowired
    private IPoliticsStatusService politicsStatusService;

    @Autowired
    private IJoblevelService joblevelService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "通过分页获取员工")
    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          Employee employee, LocalDate[] beginDateScope){
        return employeeService.getAllEmployeeByPage(currentPage,size,employee, beginDateScope);

    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean insertEmployee(Employee employee){
        return employeeService.insertEmployee(employee);
    }

    @ApiOperation(value = "获取民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation(value = "获取政治面貌")
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取职称")
    @GetMapping("/jobLevel")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取职位")
    @GetMapping("/position")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取部门")
    @GetMapping("/dep")
    public List<Department> getAllDepartment(){
        return departmentService.list();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/workId")
    public RespBean getMaxWorkId(){
        return employeeService.maxWorkId();
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmployee(@PathVariable Integer id){
        if(employeeService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.fail("删除失败！");
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmployee(Employee employee){
        if(employeeService.updateById(employee)){
            return RespBean.success("更新成功");
        }
        return RespBean.fail("更新失败！");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> list = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook book = ExcelExportUtil.exportExcel(params, Employee.class, list);
        ServletOutputStream out = null;
        try{
            response.setHeader("content-type","application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename=" +
                    URLEncoder.encode("员工表.xls","UTF-8"));
            out = response.getOutputStream();
            book.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.flush();
                    out.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    @ApiOperation(value = "导入员工数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "file",value = "上传文件",dataType = "MultipartFile")})
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        List<Department> departmentList = departmentService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                employee.setNationId(nationList.get(
                        nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setPoliticId(politicsStatusList.get(
                        politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setPoliticId(joblevelList.get(
                        joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setPoliticId(positionList.get(
                        positionList.indexOf(new Position(employee.getPosition().getName()))).getId());
                employee.setPoliticId(departmentList.get(
                        departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId());
            });
            if(employeeService.saveBatch(list)){
                return RespBean.success("导入成功！");
            }
            return RespBean.fail("导入失败！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.fail("导入失败！");
    }
}
