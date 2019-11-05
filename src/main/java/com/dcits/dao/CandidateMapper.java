package com.dcits.dao;

import com.dcits.pojo.Candidate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CandidateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Candidate record);

    int insertSelective(Candidate record);

    Candidate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Candidate record);

    int updateByPrimaryKey(Candidate record);

    List<Candidate> findCandidateByVoteId(Integer voteId);

    Integer updatePollById(Integer id);

    List<Candidate> findWithVoteIdAndName(@Param("voteid") Integer voteid, @Param("name") String name);

    Integer countPoll();
}