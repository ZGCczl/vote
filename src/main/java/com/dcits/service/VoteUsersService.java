package com.dcits.service;

import com.dcits.pojo.VoteUsers;

import java.util.List;

public interface VoteUsersService {

    String findChoiceByUserIdAndVoteId(Integer userId, Integer voteId);

    Integer addVoteUsersByUsersIdAndVoteId(Integer userId, Integer voteId, String list);

    List<VoteUsers> findVoteUsersByUsersIdAndVoteId(Integer userId, Integer voteId);
}
