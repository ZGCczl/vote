package com.dcits.service;

import com.dcits.pojo.Vote;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;

public interface VoteService {
    String findNameById(Integer voteId);

    List<Vote> findVoteByTime();

    PageInfo<Vote> findVoteWithPage(int page, int pageSize, String name);

    int edit(int id, String name, String starttime, String endtime, Integer candidateCount, Integer voteCount,String explain);

    int add(String name, String starttime, String endtime, Integer candidateCount, Integer voteCount,String explain);

    int delete(int id);

    List<Vote> findVote();

    HashMap<String, Integer> findCount();

    Vote findVoteByVoteid(Integer voteId);

    List<Vote> findVoteOfNotStarted();

    List<Vote> findVoteOfFinish();
}
