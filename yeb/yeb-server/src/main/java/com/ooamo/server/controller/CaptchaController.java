package com.ooamo.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码controller
 */
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha",produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        //设置response输出类型为image/jpeg类型
        response.setDateHeader("Expires",0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).

        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.

        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //1、获取验证码文本内容
        String text = defaultKaptcha.createText();
        System.out.println("验证码："+text);

        //2、将验证码放入session中
        request.getSession().setAttribute("captcha",text);

        //3、根据文本内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);

        //4、输出流输出照片
        ServletOutputStream outputStream = null;
        try{
            outputStream = response.getOutputStream();
            ImageIO.write(image,"jpeg",outputStream);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try{
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
