package com.ooamo.server.pojo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 统一返回结果
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespBean {

    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "提示信息")
    private String message;
    @ApiModelProperty("对象")
    private Object obj;

    /**
     * 返回成功结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200, message, null);
    }

    /**
     * 返回成功结果
     * @param message
     * @param obj
     * @return
     */
    public static RespBean success(String message,Object obj){
        return new RespBean(200, message, obj);
    }

    /**
     * 返回失败结果
     * @param message
     * @return
     */
    public static RespBean fail(String message){
        return new RespBean(500,message,null);
    }

    /**
     * 返回失败结果
     * @param message
     * @param obj
     * @return
     */
    public static RespBean fail(String message,Object obj){
        return new RespBean(500,message,obj);
    }

}
