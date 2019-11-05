package com.dcits.dao;

import com.dcits.pojo.VoteUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VoteUsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoteUsers record);

    int insertSelective(VoteUsers record);

    VoteUsers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoteUsers record);

    int updateByPrimaryKey(VoteUsers record);

    String findChoiceByUserIdAndVoteId(@Param("userId") Integer userId, @Param("voteId") Integer voteId);
}