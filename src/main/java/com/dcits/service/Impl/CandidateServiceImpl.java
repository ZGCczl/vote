package com.dcits.service.Impl;

import com.dcits.dao.CandidateMapper;
import com.dcits.pojo.Candidate;
import com.dcits.pojo.Vote;
import com.dcits.pojo.VoteUsers;
import com.dcits.service.CandidateService;
import com.dcits.service.UsersService;
import com.dcits.service.VoteService;
import com.dcits.service.VoteUsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    CandidateMapper candidateMapper;

    @Autowired
    UsersService usersService;
    @Autowired
    VoteUsersService voteUsersService;
    @Autowired
    VoteService voteService;

    /**
     * 根据投票活动Id查找所属候选人
     * @param voteId
     * @return
     */
    @Override
    public List<Candidate> findCandidateByVoteId(Integer voteId) {
        return this.candidateMapper.findCandidateByVoteId(voteId);
    }

    /**
     * 添加候选者信息
     * @param candidate
     * @return
     */
    @Override
    public int insertCandidateOne(Candidate candidate) {
        return this.candidateMapper.insertSelective(candidate);
    }

    /**
     * 用户提交完投票选项后，添加投票活动和用户关联信息，和改变候选者的票数
     * @param itcode
     * @param voteId
     * @param list
     * @return
     */
    @Override
    public Integer updateCandidate(String itcode,Integer voteId,String list) {
        //获取用户id
        Integer userId= this.usersService.findUserIdByItcode(itcode);
        //添加用户和投票活动关联信息（用户投票信息）
        List<VoteUsers> voteUsers= this.voteUsersService.findVoteUsersByUsersIdAndVoteId(userId,voteId);
        if(voteUsers.size()!=0){
            return -1;
        }else{
            Integer codeUser= this.voteUsersService.addVoteUsersByUsersIdAndVoteId(userId,voteId,list);
            if(codeUser<=0){
                return -1;
            }else{
                //同步代码块 防止用户并发操作导致投票数据错误或丢失
                synchronized (this){
                    String[] strings= null;
                    strings= list.split(",");
                    for(int i=0;i<strings.length;i++){
                        Integer codeCandidate= this.candidateMapper.updatePollById(Integer.parseInt(strings[i]));
                        if(codeCandidate<=0){
                            return -1;
                        }
                    }
                    return 1;
                }
            }
        }
    }

    @Override
    public PageInfo<Candidate> findCandidateWithPage(int page, int pageSize, Integer voteid, String name) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(this.candidateMapper.findWithVoteIdAndName(voteid,name));
    }

    @Override
    public int edit(int id,int voteid, String name, String department, String filePath, String text) {
        Candidate candidate= new Candidate();
        candidate.setId(id);
        candidate.setName(name);
        candidate.setVoteid(voteid);
        candidate.setDepartment(department);
        candidate.setImage(filePath);
        candidate.setText(text);
        return this.candidateMapper.updateByPrimaryKeySelective(candidate);
    }

    @Override
    public int add(int voteid, String name, String department, String filePath, String text) {
        Candidate candidate= new Candidate();
        candidate.setName(name);
        candidate.setVoteid(voteid);
        candidate.setDepartment(department);
        candidate.setImage(filePath);
        candidate.setText(text);
        return this.candidateMapper.insertSelective(candidate);
    }

    @Override
    public int delete(Integer id) {
        return this.candidateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer countPoll() {
        return this.candidateMapper.countPoll();
    }

}
