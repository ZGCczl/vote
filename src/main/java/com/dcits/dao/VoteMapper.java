package com.dcits.dao;

import com.dcits.pojo.CountPollOfSection;
import com.dcits.pojo.Vote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);

    String findNameById(Integer voteId);

    List<Vote> findVoteByTime();

    List<Vote> findOrderByStarttime(@Param("name") String name);

    List<Vote> findAll();

    Integer countTotal();

    Integer countCurrent();

    Integer countUpcoming();

    Integer countFinished();

    List<Vote> findVoteOfNotStarted();

    List<Vote> findVoteOfFinish();

    List<CountPollOfSection> findItcodeByVoteidAndCandidateId(@Param("voteId") Integer voteId, @Param("cId") Integer cId, @Param("candidateId") String candidateId);
}