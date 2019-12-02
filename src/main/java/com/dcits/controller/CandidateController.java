package com.dcits.controller;

import cn.hutool.json.JSONObject;
import com.dcits.pojo.Candidate;
import com.dcits.pojo.Vote;
import com.dcits.service.CandidateService;
import com.dcits.service.UsersService;
import com.dcits.service.VoteService;
import com.dcits.service.VoteUsersService;
import com.dcits.utils.UploadUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    UsersService usersService;
    @Autowired
    CommomController commomController;
    @Autowired
    VoteUsersService voteUsersService;
    @Autowired
    VoteService voteService;

    /**
     * 根据活动ID和用户ITCODE请求投票活动页面
     * @param model
     * @param itcode
     * @param voteId
     * @return
     */
    @RequestMapping(value = "/index")
    public String findCandidateAll(Model model,String itcode,Integer voteId){
        Vote vote= this.voteService.findVoteByVoteid(voteId);
        if(vote==null){
            return "404";
        }
        String voteName=vote.getName();
        Integer voteCount= vote.getVoteCount();
        Integer candidateCount= vote.getCandidateCount();
        List<Candidate> candidateList= UploadUtil.exchangeText(this.candidateService.findCandidateByVoteId(voteId));
        String explain = UploadUtil.toTextarea(vote.getExplain());
        explain = explain.replace(" ","");
        //随机打乱候选人集合，保证每位候选人在前端位置都是随机的，起到公平作用
        Collections.shuffle(candidateList);
        Integer userId= this.usersService.findUserIdByItcode(itcode);
        String choice= this.voteUsersService.findChoiceByUserIdAndVoteId(userId,voteId);
        if(choice!=null){
            model.addAttribute("candidateList",this.commomController.setChoiceWithCandidate(candidateList,choice));
            model.addAttribute("choice",choice);
        }else{
            model.addAttribute("candidateList",candidateList);
        }
        model.addAttribute("itcode",itcode);
        model.addAttribute("voteId",voteId);
        model.addAttribute("voteCount",voteCount);
        model.addAttribute("voteName",voteName);
        model.addAttribute("explain",explain);
        model.addAttribute("candidateCount",candidateCount);
        return "index";
    }

    /**
     * 根据用户itcode请求投票活动分类页面
     * @param model
     * @param itcode
     * @return
     */
    @RequestMapping(value = "/synthesize")
    public String findCandidateAll(Model model,String itcode){
        List<Vote> underwayList= new ArrayList<>();
        List<Vote> notStartedList= new ArrayList<>();
        List<Vote> finishList= new ArrayList<>();
        //投票活动的开始时间和结束时间转为字符串类型
        underwayList= CommomController.changeTimetoString(this.voteService.findVoteByTime());
        notStartedList= CommomController.changeTimetoString(this.voteService.findVoteOfNotStarted());
        finishList= CommomController.changeTimetoString(this.voteService.findVoteOfFinish());
        model.addAttribute("underwayList",underwayList);
        model.addAttribute("notStartedList",notStartedList);
        model.addAttribute("finishList",finishList);
        model.addAttribute("itcode",itcode);
        return "synthesize";
    }

    /**
     * 手机端请求投票活动详情页面
     * @param model
     * @param itcode
     * @param voteId
     * @return
     */
    @RequestMapping(value = "/indexPhone")
    public String findCandidateAllPhone(Model model,String itcode,Integer voteId){
        Vote vote= this.voteService.findVoteByVoteid(voteId);
        if(vote==null){
            return "404";
        }
        String voteName=vote.getName();
        Integer voteCount= vote.getVoteCount();
        List<Candidate> candidateList= UploadUtil.exchangeText(this.candidateService.findCandidateByVoteId(voteId));
        String explain = UploadUtil.toTextarea(vote.getExplain());
        //随机打乱候选人集合，保证每位候选人在前端位置都是随机的，起到公平作用
        Collections.shuffle(candidateList);
        Integer userId= this.usersService.findUserIdByItcode(itcode);
        String choice= this.voteUsersService.findChoiceByUserIdAndVoteId(userId,voteId);
        if(choice!=null){
            model.addAttribute("candidateList",this.commomController.setChoiceWithCandidate(candidateList,choice));
            model.addAttribute("choice",choice);
        }else{
            model.addAttribute("candidateList",candidateList);
        }
        model.addAttribute("itcode",itcode);
        model.addAttribute("voteId",voteId);
        model.addAttribute("voteCount",voteCount);
        model.addAttribute("voteName",voteName);
        model.addAttribute("explain",explain);
        return "index-phone";
    }

    /**
    * @Description:  用户提交投票，变更数据库，并响应投票结果
    * @Param: [itcode, list]
    * @return: java.lang.String
    * @Author: chenlls
    * @Date: 2019/10/21
    */
    @ResponseBody
    @RequestMapping(value = "/vote")
    public synchronized String vote(String itcode,Integer voteId,String list){
        JSONObject jsonObject= new JSONObject();
        if(list==null){
            jsonObject.put("code",-1);
            jsonObject.put("msg","网络波动，请重试！");
            return jsonObject.toString();
        }else{
            Integer candidateCode= this.candidateService.updateCandidate(itcode,voteId,list);
            if(candidateCode<=0){
                    jsonObject.put("code",-1);
                    jsonObject.put("msg","投票失败，请稍后重试");
                    return jsonObject.toString();
            }
        }
        jsonObject.put("code",1);
        jsonObject.put("msg","投票成功");
        return jsonObject.toString();
    }

    /**
     * 请求参选者列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/vote/candidateList")
    public String toCandidateList(Model model){
        List<Vote> votes= this.voteService.findVote();
        model.addAttribute("votes",votes);
        return "admin/candidateList";
    }

    /**
     * 多条件、分页请求参选者信息
     * @param pageNum
     * @param pageSize
     * @param voteid
     * @param name
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/candidate/findWithPage")
    public String findWithPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                               @RequestParam(value = "voteid", defaultValue = "0") Integer voteid,
                               @RequestParam(value = "name", required = false) String name){
        JSONObject jsonObject= new JSONObject();
        PageInfo<Candidate> pageInfo= this.candidateService.findCandidateWithPage(pageNum,pageSize,voteid,name);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", pageInfo.getTotal());
        jsonObject.put("data", pageInfo.getList());
        return jsonObject.toString();
    }

    /**
     * @Description: 参选者引导图上传
     * @Param: [file]
     * @return: java.lang.String
     * @Author: chenlls
     * @Date: 2019/8/29
     */
    @RequestMapping(value = "/candidate/upload")
    @ResponseBody
    public String update(@RequestParam(value = "file") MultipartFile file) {
        String imageName = "solutionImage";
        String jsonString = commomController.upload(file);
        return jsonString;
    }

    /**
     * 修改参选者信息
     * @param id
     * @param voteid
     * @param name
     * @param department
     * @param filePath
     * @param text
     * @return
     */
    @RequestMapping(value = "/candidate/editOne", method = RequestMethod.POST)
    @ResponseBody
    public String editOne(@RequestParam(value = "id") int id,
                          @RequestParam(value = "voteid") int voteid,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "department") String department,
                          @RequestParam(value = "filePath") String filePath,
                          @RequestParam(value = "text") String text) {
        int code = this.candidateService.edit(id,voteid, name,department, filePath, text);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    /**
     * 添加参选者信息
     * @param voteid
     * @param name
     * @param department
     * @param filePath
     * @param text
     * @return
     */
    @RequestMapping(value = "/candidate/addOne", method = RequestMethod.POST)
    @ResponseBody
    public String addOne(@RequestParam(value = "voteid") int voteid,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "department") String department,
                         @RequestParam(value = "filePath") String filePath,
                         @RequestParam(value = "text") String text) {
        int code = this.candidateService.add(voteid,name,department, filePath, text);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    /**
     * 删除参选者信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/candidate/deleteOne", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOne(@RequestParam(value = "id") Integer id) {
        int code = this.candidateService.delete(id);
        String jsonString = CommomController.delete(code);
        return jsonString;
    }

}
