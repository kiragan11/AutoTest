package com.course.code;

import com.course.bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = {"这是我全部的post方法"})
@RequestMapping("/v1")
public class MyPostMethod {
    /**
     * 这是一个返回cookies的post请求
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录成功后，获取cookies",httpMethod ="POST")
    public String login(HttpServletResponse response,
                        @ApiParam("输入用户名") @RequestParam("username") String username,
                        @RequestParam("password") String password){

        if (username.equals("kiragan")&&password.equals("123456")){
            Cookie cookie = new Cookie("login","true");
            response.addCookie(cookie);
            return "恭喜你登录成功！";
        }
        return "用户名或密码错误";
    }

    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @ApiOperation(value = "登录成功后获取用户列表",httpMethod = "POST")
    public String getUserList(HttpServletRequest request,
                              @RequestBody User reqUser){
       Cookie[] cookies =request.getCookies();
       for (Cookie cookie:cookies){
           if (cookie.getName().equals("login")
                   &&cookie.getValue().equals("true")
                   &&reqUser.getUsername().equals("kiragan")
                   &&reqUser.getPassword().equals("123456")
           ){
               User resUser= new User();
               resUser.setName("干甜");
               resUser.setAge("33");
               resUser.setSex("女");
               String result="";
               try {
                   ObjectMapper o = new ObjectMapper();
                   result = o.writeValueAsString(resUser);
               }catch (Exception e){
                   e.printStackTrace();
               }

               return result;
           }
       }
        return "参数不合法";
    }

}
