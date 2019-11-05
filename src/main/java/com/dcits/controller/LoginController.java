package com.dcits.controller;

import cn.hutool.json.JSONObject;
import com.dcits.pojo.Users;
import com.dcits.service.UsersService;
import com.dcits.utils.DcitsLdapAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private UsersService usersService;

    /** 
    * @Description: 通过LDAP的验证形式，验证用户输入的ITCODE PASSWORD。公司内网统一用户验证，并且判断系统内是否有该用户，如果没有就创建该用户。
    * @Param: [itcode, password] 
    * @return: java.lang.String 
    * @Author: chenlls
    * @Date: 2019/10/14 
    */
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(String itcode,String password){
        JSONObject jsonObject= new JSONObject();
        DcitsLdapAuth auth = new DcitsLdapAuth();
            if (auth.ldapAuthentication(auth.TDS_LDAP_URL, "cn=" + itcode , password)) {
            Users userVo=  this.usersService.findUserByItcode(itcode);
            if(userVo==null){
                Users users= new Users();
                users.setName(itcode);
                users.setItcode(itcode);
                users.setPassword(password);
              Integer code= this.usersService.addUsers(users);
              if(code<=0){
                  jsonObject.put("code",-2);
                  jsonObject.put("msg","网络异常，请稍后重试");
              }
            }
            jsonObject.put("code",200);
            jsonObject.put("msg","登录成功");
        } else {
            jsonObject.put("code",-1);
            jsonObject.put("msg","用户名或密码错误");
        }
        return jsonObject.toString();
    }
}
