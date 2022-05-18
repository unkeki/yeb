package com.ooamo.server.exception;

import com.ooamo.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespBean mySQLException(SQLException e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.fail("该数据库有关数据操作失败！");
        }
        return RespBean.fail("该数据库异常，操作失败！");
    }

}
