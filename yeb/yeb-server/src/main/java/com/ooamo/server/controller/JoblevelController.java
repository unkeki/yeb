package com.ooamo.server.controller;

import com.ooamo.server.pojo.Joblevel;
import com.ooamo.server.pojo.RespBean;
import com.ooamo.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService jobLevelService;

    @ApiOperation(value = "获得所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJobLevel(){
        return jobLevelService.list();
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel) {
        joblevel.setCreateDate(LocalDateTime.now());
        if (jobLevelService.save(joblevel)) {
            return RespBean.success("添加成功！");
        }
        return RespBean.fail("添加失败！");
    }

    @ApiOperation(value = "更新职称信息")
    @PutMapping("/")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel){
        if(jobLevelService.updateById(joblevel)){
            return RespBean.success("更新成功！");
        }
        return RespBean.fail("更新失败！");
    }

    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id){
        if(jobLevelService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.fail("删除失败！");
    }

    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJobLevelByIds(Integer[] ids){
        if(jobLevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.fail("删除失败！");
    }
}
