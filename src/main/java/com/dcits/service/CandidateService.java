package com.dcits.service;

import com.dcits.pojo.Candidate;
import com.dcits.pojo.Vote;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CandidateService {

    List<Candidate> findCandidateByVoteId(Integer voteId);

    int insertCandidateOne(Candidate candidate);

    Integer updateCandidate(String itcode,Integer voteId,String list);

    PageInfo<Candidate> findCandidateWithPage(int page, int pageSize, Integer voteid, String name);

    int edit(int id,int voteid, String name, String department, String filePath, String text);

    int add(int voteid, String name, String department, String filePath, String text);

    int delete(Integer id);

    Integer countPoll();
}
