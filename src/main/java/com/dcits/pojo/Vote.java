package com.dcits.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Vote implements Serializable {
    /**
    * 主键
    */
    private Integer id;

    /**
    * 投票名称
    */
    private String name;

    /**
    * 开始时间
    */
    private Date starttime;

    /**
    * 结束时间
    */
    private Date endtime;

    private Integer candidateCount;

    private Integer voteCount;

    private static final long serialVersionUID = 1L;
}