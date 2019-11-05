package com.dcits.pojo;

import java.io.Serializable;
import lombok.Data;

@Data
public class VoteUsers implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer voteId;

    private String choice;

    private static final long serialVersionUID = 1L;
}