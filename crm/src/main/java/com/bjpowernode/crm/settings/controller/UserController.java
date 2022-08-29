package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.commons.constant.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.util.DateUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.fasterxml.jackson.databind.util.ObjectIdMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }
    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String password, String isRemPwd, HttpServletRequest request, HttpServletResponse response){
        Map map=new HashMap<String, Object>();
        ReturnObject returnObject = new ReturnObject();
        map.put("loginAct",loginAct);
        map.put("password",password);
        User user=userService.findUserByNameAndPassword(map);
        String ip = request.getRemoteAddr();
        if(user==null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("请重新输入，登录用户名或密码错误!");
        }
        else{
            if(DateUtils.formateDate(new Date()).compareTo(user.getExpireTime())>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("该用户已过期");
            }else if(user.getLockState().equals("0")){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("该用户已被锁定");
            }else if(!user.getAllowIps().contains(ip)){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("该用户ip受限");
            }else{
                if(isRemPwd.equals("true")){
                    Cookie nameCookie = new Cookie("loginAct",user.getLoginAct());
                    Cookie pdCookie=new Cookie("password",user.getLoginPwd());
                    nameCookie.setMaxAge(60*60*24*10);
                    pdCookie.setMaxAge(60*60*24*10);
                    System.out.println(request.getContextPath());
                    nameCookie.setPath(request.getContextPath());
                    pdCookie.setPath(request.getContextPath());
                    response.addCookie(pdCookie);
                    response.addCookie(nameCookie);
                }else {
                    Cookie nameCookie = new Cookie("loginAct",null);
                    Cookie pdCookie=new Cookie("password",null);
                    nameCookie.setMaxAge(0);
                    pdCookie.setMaxAge(0);
                    response.addCookie(pdCookie);
                    response.addCookie(nameCookie);
                }
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                request.getSession().setAttribute(Contants.SESSION_USER,user);
            }
        }
        return returnObject;

    }
    @RequestMapping("/setting/qx/user/loginOut.do")
    public String loginOut(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Cookie nameCookie = new Cookie("loginAct",null);
        Cookie pdCookie=new Cookie("password",null);
        nameCookie.setMaxAge(0);
        pdCookie.setMaxAge(0);
        nameCookie.setPath(request.getContextPath());
        pdCookie.setPath(request.getContextPath());
        response.addCookie(pdCookie);
        response.addCookie(nameCookie);
        session.invalidate();
        return "redirect:/";
    }
}
