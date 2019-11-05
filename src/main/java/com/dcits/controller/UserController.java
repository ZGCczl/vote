package com.dcits.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "vote")
public class UserController {

    @RequestMapping("userList")
    public String toUserList(){
        return "admin/userList";
    }
}
