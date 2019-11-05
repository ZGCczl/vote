package com.dcits.service.Impl;

import com.dcits.dao.VoteMapper;
import com.dcits.pojo.Vote;
import com.dcits.service.CandidateService;
import com.dcits.service.UsersService;
import com.dcits.service.VoteService;
import com.dcits.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    VoteMapper voteMapper;
    @Autowired
    UsersService usersService;
    @Autowired
    CandidateService candidateService;
    /**
     * 根据id查找用户姓名
     * @param voteId
     * @return
     */
    @Override
    public String findNameById(Integer voteId) {
        return this.voteMapper.findNameById(voteId);
    }

    /**
     * 根据时间查找投票活动
     * @return
     */
    @Override
    public List<Vote> findVoteByTime() {
        return this.voteMapper.findVoteByTime();
    }

    @Override
    public PageInfo<Vote> findVoteWithPage(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(this.voteMapper.findOrderByStarttime(name));
    }

    @Override
    public int edit(int id, String name, String starttime, String endtime, Integer candidateCount, Integer voteCount) {
        Date startTime= DateUtils.convert(starttime);
        Date endTime= DateUtils.convert(endtime);
        if(startTime==null || endTime==null){
            return -1;
        }else{
            Vote vote= new Vote();
            vote.setId(id);
            vote.setName(name);
            vote.setStarttime(startTime);
            vote.setEndtime(endTime);
            vote.setCandidateCount(candidateCount);
            vote.setVoteCount(voteCount);
            return this.voteMapper.updateByPrimaryKeySelective(vote);
        }
    }

    @Override
    public int add(String name, String starttime, String endtime, Integer candidateCount, Integer voteCount) {
        Date startTime= DateUtils.convert(starttime);
        Date endTime= DateUtils.convert(endtime);
        if(startTime==null || endTime==null){
            return -1;
        }else{
            Vote vote= new Vote();
            vote.setName(name);
            vote.setStarttime(startTime);
            vote.setEndtime(endTime);
            vote.setCandidateCount(candidateCount);
            vote.setVoteCount(voteCount);
            return this.voteMapper.insertSelective(vote);
        }
    }

    @Override
    public int delete(int id) {
        return this.voteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Vote> findVote() {
        return this.voteMapper.findAll();
    }

    @Override
    public HashMap<String, Integer> findCount() {
        HashMap<String,Integer> map= new HashMap<>();
        Integer voteTotal= this.voteMapper.countTotal();//活动总数
        Integer voteCurrent= this.voteMapper.countCurrent();//当前活动数
        Integer voteUpcoming= this.voteMapper.countUpcoming();//未开始的活动数
        Integer voteFinished= this.voteMapper.countFinished();//已结束的活动数
        Integer countUsers= this.usersService.countUsers();//用户数
        Integer countPoll= this.candidateService.countPoll();//投票总数
        map.put("voteTotal",voteTotal);
        map.put("voteCurrent",voteCurrent);
        map.put("voteUpcoming",voteUpcoming);
        map.put("voteFinished",voteFinished);
        map.put("countUsers",countUsers);
        map.put("countPoll",countPoll);
        return map;
    }

    @Override
    public Vote findVoteByVoteid(Integer voteId) {
        return this.voteMapper.selectByPrimaryKey(voteId);
    }
}
