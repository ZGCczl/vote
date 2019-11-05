package com.dcits.service;

public interface VoteUsersService {

    String findChoiceByUserIdAndVoteId(Integer userId, Integer voteId);

    Integer addVoteUsersByUsersIdAndVoteId(Integer userId, Integer voteId, String list);
}
