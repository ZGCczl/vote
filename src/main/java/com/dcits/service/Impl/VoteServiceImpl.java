package com.dcits.service.Impl;

import com.dcits.dao.VoteMapper;
import com.dcits.pojo.Candidate;
import com.dcits.pojo.CountPollOfSection;
import com.dcits.pojo.DistributionOfPoll;
import com.dcits.pojo.Vote;
import com.dcits.service.CandidateService;
import com.dcits.service.UsersService;
import com.dcits.service.VoteService;
import com.dcits.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public int edit(int id, String name, String starttime, String endtime, Integer candidateCount, Integer voteCount,String explain) {
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
            vote.setExplain(explain);
            return this.voteMapper.updateByPrimaryKeySelective(vote);
        }
    }

    @Override
    public int add(String name, String starttime, String endtime, Integer candidateCount, Integer voteCount,String explain) {
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
            vote.setExplain(explain);
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

    @Override
    public List<Vote> findVoteOfNotStarted() {
        return this.voteMapper.findVoteOfNotStarted();
    }

    @Override
    public List<Vote> findVoteOfFinish() {
        return this.voteMapper.findVoteOfFinish();
    }

    @Override
    public List<DistributionOfPoll> findItcodeExportWithPage(Integer voteid) {
        List<DistributionOfPoll> list= new ArrayList<>();
        //根据voteid查询出所有参选者ID并正序排列
        List<Candidate> candidateIdList= this.candidateService.findCandidateIdByVoteID(voteid);
        //如果投票活动不存在，或者投票活动没有候选者，则直接返回null
        if(candidateIdList==null || candidateIdList.size()==0){
            return null;
        }else{
            //根据参选者ID去vote_users表中查询出choice包含该ID的userid链接查询并计算每个部门的投票数
            for(int i=0;i<candidateIdList.size();i++){
                Integer cId= candidateIdList.get(i).getId();
                String name= candidateIdList.get(i).getName();
                List<CountPollOfSection> stringList= this.voteMapper.findItcodeByVoteidAndCandidateId(voteid, cId,cId+"");
                DistributionOfPoll distributionOfPoll= setDistributionOfPollParm(cId,name,stringList);
                list.add(distributionOfPoll);
            }
            return list;
        }
    }

    public DistributionOfPoll setDistributionOfPollParm(Integer cId,String name, List<CountPollOfSection> list){
        DistributionOfPoll distributionOfPoll= new DistributionOfPoll();
        distributionOfPoll.setId(cId);
        distributionOfPoll.setName(name);
        if(list==null || list.size()==0){
            distributionOfPoll.setCwb(0);
            distributionOfPoll.setDgb(0);
            distributionOfPoll.setDqzhglb(0);
            distributionOfPoll.setFwcpzx(0);
            distributionOfPoll.setFwSBU(0);
            distributionOfPoll.setGcy(0);
            distributionOfPoll.setJcSBU(0);
            distributionOfPoll.setJrSBU(0);
            distributionOfPoll.setJryjy(0);
            distributionOfPoll.setJxfwjtlywdy(0);
            distributionOfPoll.setJxSBU(0);
            distributionOfPoll.setNqzhglb(0);
            distributionOfPoll.setNybk(0);
            distributionOfPoll.setPpscb(0);
            distributionOfPoll.setRlzyjxzb(0);
            distributionOfPoll.setRxkj(0);
            distributionOfPoll.setSjb(0);
            distributionOfPoll.setSzbb(0);
            distributionOfPoll.setSzgxlz(0);
            distributionOfPoll.setTzglb(0);
            distributionOfPoll.setXmglb(0);
            distributionOfPoll.setXqzhglb(0);
            distributionOfPoll.setXxhglb(0);
            distributionOfPoll.setYxglzx(0);
            distributionOfPoll.setZbzqb(0);
            distributionOfPoll.setZcs(0);
            distributionOfPoll.setZfSBU(0);
            distributionOfPoll.setAmbiguity(0);
        }else{
             for (int i=0;i<list.size();i++){
                 Integer sid= list.get(i).getSId();
                 Integer count= list.get(i).getCount();
                 if(sid==null || ("").equals(sid)){
                     distributionOfPoll.setAmbiguity(count);
                 }else{
                     switch (sid){
                         case 1:
                             distributionOfPoll.setCwb(count);
                             break;
                         case 4:
                             distributionOfPoll.setZcs(count);
                             break;
                         case 5:
                             distributionOfPoll.setJrSBU(count);
                             break;
                         case 6:
                             distributionOfPoll.setFwSBU(count);
                             break;
                         case 7:
                             distributionOfPoll.setJcSBU(count);
                             break;
                         case 8:
                             distributionOfPoll.setGcy(count);
                             break;
                         case 9:
                             distributionOfPoll.setYxglzx(count);
                             break;
                         case 10:
                             distributionOfPoll.setFwcpzx(count);
                             break;
                         case 11:
                             distributionOfPoll.setDgb(count);
                             break;
                         case 12:
                             distributionOfPoll.setDqzhglb(count);
                             break;
                         case 13:
                             distributionOfPoll.setJryjy(count);
                             break;
                         case 14:
                             distributionOfPoll.setJxSBU(count);
                             break;
                         case 15:
                             distributionOfPoll.setJxfwjtlywdy(count);
                             break;
                         case 16:
                             distributionOfPoll.setNqzhglb(count);
                             break;
                         case 17:
                             distributionOfPoll.setNybk(count);
                             break;
                         case 18:
                             distributionOfPoll.setPpscb(count);
                             break;
                         case 19:
                             distributionOfPoll.setRlzyjxzb(count);
                             break;
                         case 20:
                             distributionOfPoll.setRxkj(count);
                             break;
                         case 21:
                             distributionOfPoll.setSzbb(count);
                             break;
                         case 22:
                             distributionOfPoll.setSzgxlz(count);
                             break;
                         case 23:
                             distributionOfPoll.setTzglb(count);
                             break;
                         case 24:
                             distributionOfPoll.setSjb(count);
                             break;
                         case 25:
                             distributionOfPoll.setXmglb(count);
                             break;
                         case 26:
                             distributionOfPoll.setXqzhglb(count);
                             break;
                         case 27:
                             distributionOfPoll.setZbzqb(count);
                             break;
                         case 28:
                             distributionOfPoll.setXxhglb(count);
                             break;
                         case 29:
                             distributionOfPoll.setZfSBU(count);
                             break;
                     }
                 }
             }
        }
        return distributionOfPoll;
    }
}
