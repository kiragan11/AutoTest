package com.course.code;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(tags = {"这是我全部的get方法"})
public class MyGetMethod {

    /**
     * 这是一个返回cookies的get请求
     */
    @RequestMapping(value = "/getcookies",method= RequestMethod.GET)
    @ApiOperation(value = "通过这个方法可以获取cookies信息",httpMethod ="GET")
    public String getCookies(HttpServletResponse response){

        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "恭喜你获得cookies信息成功";
    }

    /**
     * 这是一个携带cookies信息才能访问的get请求
     */
    @GetMapping("/getwithcookies")
    @ApiOperation(value = "这是一个带cookies的get请求",httpMethod ="GET")
    public String getWithCookies(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            return "必须携带cookies才能访问！！！";
        }

        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login")&&cookie.getValue().equals("true")){
                return "恭喜你访问成功";
            }
        }

        return "这是一个带cookies的get请求";
    }

    /**
     * 这是一个携带参数的get请求
     * 第一种方式uri ip:port/getwithparam?key=value&&key=value
     */
    @GetMapping("/getwithparam")
    @ApiOperation(value = "这是一个携带参数的get请求",httpMethod ="GET")
    public Map<String,Integer> MyList(@RequestParam Integer start,
                                      @RequestParam Integer end){
        Map<String,Integer> map = new HashMap<String,Integer>();

        map.put("电扇",50);
        map.put("水壶",5);
        map.put("手机",100);

        return map;
    }


    /**
     * 这是一个携带参数的get请求
     * 第二种方式uri ip:port/getwithparam/value/value
     */

    @RequestMapping(value = "/getwithparam/{start}/{end}")
    @ApiOperation(value = "这是另一个携带参数的get请求",httpMethod ="GET")

    public Map<String,Integer> MyList2(@PathVariable Integer start,
                                       @PathVariable Integer end ){
        Map<String,Integer> map = new HashMap<String,Integer>();

        map.put("电扇",50);
        map.put("水壶",5);
        map.put("手机",100);

        return map;
    }
}
