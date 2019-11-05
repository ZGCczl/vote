package com.dcits.service.Impl;

import com.dcits.dao.VoteUsersMapper;
import com.dcits.pojo.VoteUsers;
import com.dcits.service.VoteUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteUsersServiceImpl implements VoteUsersService {

    @Autowired
    VoteUsersMapper voteUsersMapper;

    @Override
    public String findChoiceByUserIdAndVoteId(Integer userId, Integer voteId) {
        return this.voteUsersMapper.findChoiceByUserIdAndVoteId(userId,voteId);
    }

    /**
     * 当用户完成投票操作时，添加一条用户和投票活动的信息
     * @param userId
     * @param voteId
     * @param list
     * @return
     */
    @Override
    public Integer addVoteUsersByUsersIdAndVoteId(Integer userId, Integer voteId, String list) {
        VoteUsers voteUsers= new VoteUsers();
        voteUsers.setUserId(userId);
        voteUsers.setVoteId(voteId);
        voteUsers.setChoice(list);
        return this.voteUsersMapper.insertSelective(voteUsers);
    }
}
