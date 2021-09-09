package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RestController
@Api(tags = {"用户管理系统"})
@RequestMapping(value = "/v1", method = RequestMethod.POST)
public class UserController {

    //获取一个sql语句对象
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    //定义一个log
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @RequestMapping(value = "/login")
    public boolean login(HttpServletResponse response, @RequestBody User user) {
        //建立一个sql连接查询
        int result = sqlSessionTemplate.selectOne("login", user);
        //添加cookie
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        //判断结果
        if (result == 1) {
            //记日志
            logger.info("查询到的结果:" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户", httpMethod = "POST")
    @RequestMapping(value = "/addUser")
    public boolean addUser(HttpServletRequest request, @RequestBody User user) throws IOException {
        //验证cookies
        Boolean isCookiesTrue = verifyCookies(request);

        int result = 0;
        if (isCookiesTrue) {
            //建立一个sql查询连接
            result = sqlSessionTemplate.insert("addUser", user);
        }
        if (result > 0) {
            logger.info("添加用户数量：" + result);
            return true;
        }
        return false;
    }


    @ApiOperation(value = "更新/删除用户", httpMethod = "POST")
    @RequestMapping(value = "/updateUser")
    public Boolean updateUser(HttpServletRequest request, @RequestBody User user) {
        //验证cookies
        Boolean isCookiesTrue = verifyCookies(request);

        int result = 0;
        if (isCookiesTrue) {
            result = sqlSessionTemplate.update("updateUser", user);
        }
        if (result > 0) {
            logger.info("更新数据的条目数为:" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户信息", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo")
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user) {
        //验证cookies
        Boolean isCookiesTrue = verifyCookies(request);
        if (isCookiesTrue) {
            List<User> users = sqlSessionTemplate.selectList("getUserInfo", user);
            logger.info("获取用户数量：" + users.size());
            return users;
        }
        return null;
    }

    /**
     * 验证cookies
     */
    private Boolean verifyCookies(HttpServletRequest request) {
        //获取cookies
        Cookie[] cookies = request.getCookies();
        //判断cookies是否为空
        if (Objects.isNull(cookies)) {
            logger.info("cookies为空");
            return false;
        }
        //遍历比较
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                logger.info("cookies验证通过");
                return true;
            }
        }
        return false;
    }

}
