package com.dcits.pojo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Candidate implements Serializable {
    private Integer id;

    /**
    * 姓名
    */
    private String name;

    /**
    * 部门
    */
    private String department;

    /**
    * 头像
    */
    private String image;

    /**
    * 个人简述
    */
    private String text;

    /**
    * 票数
    */
    private Integer poll;

    /**
    * 投票项目
    */
    private Integer voteid;

    /**
    * 版本号
    */
    private Integer version;

    private Integer show;

    private String voteName;

    private static final long serialVersionUID = 1L;
}