package com.dcits.pojo;

import lombok.Data;

@Data
public class DistributionOfPoll {
    //票数分布情况
    private Integer id;//参选者id

    private String name;//参选者名称

    private Integer jcSBU;//集成SBU票数

    private Integer jrSBU;//金融SBU票数

    private Integer fwSBU;//服务SBU票数

    private Integer zfSBU;//政府SBU票数

    private Integer nybk;//农业板块票数

    private Integer gcy;//工程院

    private Integer jxSBU;//金信SBU

    private Integer rlzyjxzb;//人力资源及行政部

    private Integer cwb;//财务部

    private Integer zcs;//总裁室

    private Integer yxglzx;//营销管理中心

    private Integer fwcpzx;//服务产品中心

    private Integer dgb;//党工办(对外关系)

    private Integer dqzhglb;//东区综合管理部

    private Integer xqzhglb;//西区综合管理部

    private Integer nqzhglb;//南区综合管理部

    private Integer jryjy;//金融研究院

    private Integer jxfwjtlywdy;//金信服务及铁路业务单元

    private Integer ppscb;//品牌市场部

    private Integer rxkj;//锐行快捷

    private Integer szbb;//神州邦邦

    private Integer szgxlz;//神州国信量子

    private Integer tzglb;//投资管理部

    private Integer sjb;//审计部

    private Integer xmglb;//项目管理部

    private Integer zbzqb;//资本证券部

    private Integer xxhglb;//信息化管理部

    private Integer ambiguity;//不明确部门的票数


}
