package com.dcits.pojo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Users implements Serializable {
    private Integer id;

    /**
    * 姓名
    */
    private String name;

    /**
    * ITCODE
    */
    private String itcode;

    /**
    * 密码
    */
    private String password;

    /**
    * 部门
    */
    private String department;

    private static final long serialVersionUID = 1L;
}