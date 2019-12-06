package com.dcits.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CountPollOfSection implements Serializable {

    private Integer cId;//候选者ID

    private Integer sId;//部门id

    private Integer count;//该部门对该选手的投票数
}
