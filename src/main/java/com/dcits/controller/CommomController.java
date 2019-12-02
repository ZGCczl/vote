package com.dcits.controller;


import cn.hutool.json.JSONObject;
import com.dcits.pojo.Candidate;
import com.dcits.pojo.Users;
import com.dcits.pojo.Vote;
import com.dcits.service.CandidateService;
import com.dcits.service.UsersService;
import com.dcits.service.VoteService;
import com.dcits.service.VoteUsersService;
import com.dcits.utils.DateUtils;
import com.dcits.utils.UploadUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;


@Controller
public class CommomController {

    @Autowired
    VoteService voteService;
    @Autowired
    UsersService usersService;
    @Autowired
    CandidateService candidateService;
    @Autowired
    VoteUsersService voteUsersService;

    /**
     * 本地访问系统方法
     * 初始化到登录页面
     * @return
     */
    @RequestMapping(value = "/")
    public String toLogin(Model model){
        //初始化当前时间可供投票的活动列表以供用户选择
        List<Vote> voteList= this.voteService.findVoteByTime();
        model.addAttribute("voteList",voteList);
        return "login";
    }

    public HashMap<String,Object> findCandidateAll(String itcode,Integer voteId, Integer userId){
        HashMap<String,Object> map= new HashMap<>();
        Vote vote= this.voteService.findVoteByVoteid(voteId);
        if(vote==null){
            return null;
        }
        String voteName=vote.getName();
        Integer voteCount= vote.getVoteCount();//必选个数儿
        Integer candidateCount= vote.getCandidateCount();
        List<Candidate> candidateList= UploadUtil.exchangeText(this.candidateService.findCandidateByVoteId(voteId));
        String explain= UploadUtil.toTextarea(vote.getExplain());
        //随机打乱候选人集合，保证每位候选人在前端位置都是随机的，起到公平作用
        Collections.shuffle(candidateList);
        String choice= this.voteUsersService.findChoiceByUserIdAndVoteId(userId,voteId);
        if(choice!=null){
            map.put("candidateList",setChoiceWithCandidate(candidateList,choice));
            map.put("choice",choice);
        }else{
            map.put("candidateList",candidateList);
        }
        map.put("voteCount",voteCount);
        map.put("voteName",voteName);
        map.put("explain",explain);
        map.put("candidateCount",candidateCount);
        return map;
    }

    /**
     * 第一版：直接请求当前活动页面，不经过投票活动分类页面
     * 内网服务器直接访问方法
     * @param model
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/")
    public String login(Model model,HttpServletRequest request){
        String username = request.getHeader("iv-user");  //获取request header中的用户名字段
        if (username != null ) {  //验证是否能够获取用户ITCODE
            //username = username.substring(username.indexOf("uid=") + 4, username.indexOf (",cn="));
            List<Vote> voteList= this.voteService.findVoteByTime();
            if(voteList==null || voteList.size()==0){
                return "error";
            }else{
                Integer voteid= voteList.get(0).getId();
                Users users= new Users();
                users= this.usersService.findUserByItcode(username);
                Integer userId= 0;
                if(users==null){//验证投票系统是否存在该用户，如果不存在则添加该用户
                    Users usersVo= new Users();
                    usersVo.setItcode(username);
                    Integer userCount= this.usersService.addUsers(usersVo);
                    userId= usersVo.getId();
                    if(userCount<=0 || userId==null || userId<=0){
                        return "404";
                    }
                }else{ //如果投票系统存在该用户，直接获取该用户的id
                    userId= users.getId();
                }
                HashMap<String,Object> map= new HashMap<>();
                map= findCandidateAll(username,voteid,userId); //展示投票信息前的信息处理方法
                if(map==null){
                    return "404";
                }
                String explain = ((String)map.get("explain")).replace(" ","");
                model.addAttribute("itcode",username);
                model.addAttribute("voteId",voteid);
                model.addAttribute("voteCount",map.get("voteCount"));
                model.addAttribute("voteName",map.get("voteName"));
                model.addAttribute("candidateCount",map.get("candidateCount"));
                model.addAttribute("candidateList",map.get("candidateList"));
                model.addAttribute("choice",map.get("choice"));
                model.addAttribute("explain",explain);
                return "index";
            }
        }else{
            return "404";
        }
    }*/

    /**
     * 最终版：请求投票活动分类页面，再进行选择进入投票活动
     * @param model
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/")
    public String login(Model model,HttpServletRequest request){
        String username = request.getHeader("iv-user");  //获取request header中的用户名字段
        if (username != null ) {  //验证是否能够获取用户ITCODE
            //username = username.substring(username.indexOf("uid=") + 4, username.indexOf (",cn="));
            Users users= new Users();
            users= this.usersService.findUserByItcode(username);
            Integer userId= 0;
            if(users==null){//验证投票系统是否存在该用户，如果不存在则添加该用户
                Users usersVo= new Users();
                usersVo.setItcode(username);
                Integer userCount= this.usersService.addUsers(usersVo);
                userId= usersVo.getId();
                if(userCount<=0 || userId==null || userId<=0){
                    return "404";
                }
            }else{ //如果投票系统存在该用户，直接获取该用户的id
                userId= users.getId();
            }
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
            model.addAttribute("itcode",username);
            return "synthesize";
        }else{
            return "404";
        }
    }*/

    /**
     * 最终版：手机端访问投票活动分类页面方法
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/phone")
    public String toIndexPhone(Model model,HttpServletRequest request){
        String username = request.getHeader("iv-user");  //获取request header中的用户名字段
        if (username != null ) {  //验证是否能够获取用户ITCODE
            //username = username.substring(username.indexOf("uid=") + 4, username.indexOf (",cn="));
            Users users= new Users();
            users= this.usersService.findUserByItcode(username);
            Integer userId= 0;
            if(users==null){//验证投票系统是否存在该用户，如果不存在则添加该用户
                Users usersVo= new Users();
                usersVo.setItcode(username);
                Integer userCount= this.usersService.addUsers(usersVo);
                userId= usersVo.getId();
                if(userCount<=0 || userId==null || userId<=0){
                    return "404";
                }
            }else{ //如果投票系统存在该用户，直接获取该用户的id
                userId= users.getId();
            }
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
            model.addAttribute("itcode",username);
            return "synthesize";
        }else{
            return "404";
        }
    }

    /**
     * 第一版：手机端、根据当前时间获得投票活动，直接请求投票活动页面。不经过投票活动分类页面
     * @return
     */
    /*@RequestMapping(value = "/phone")
    public String toIndexPhone(Model model,HttpServletRequest request){
        String username = request.getHeader("iv-user");  //获取request header中的用户名字段
        if (username != null ) {  //验证是否能够获取用户ITCODE
            //username = username.substring(username.indexOf("uid=") + 4, username.indexOf (",cn="));
            List<Vote> voteList= this.voteService.findVoteByTime();
            if(voteList==null || voteList.size()==0){
                return "error";
            }else{
                Integer voteid= voteList.get(0).getId();
                Users users= new Users();
                users= this.usersService.findUserByItcode(username);
                Integer userId= 0;
                if(users==null){//验证投票系统是否存在该用户，如果不存在则添加该用户
                    Users usersVo= new Users();
                    usersVo.setItcode(username);
                    Integer userCount= this.usersService.addUsers(usersVo);
                    userId= usersVo.getId();
                    if(userCount<=0 || userId==null || userId<=0){
                        return "404";
                    }
                }else{ //如果投票系统存在该用户，直接获取该用户的id
                    userId= users.getId();
                }
                HashMap<String,Object> map= new HashMap<>();
                map= findCandidateAll(username,voteid,userId); //展示投票信息前的信息处理方法
                if(map==null){
                    return "404";
                }
                String explain = (String)map.get("explain");
                model.addAttribute("itcode",username);
                model.addAttribute("voteId",voteid);
                model.addAttribute("voteCount",map.get("voteCount"));
                model.addAttribute("voteName",map.get("voteName"));
                model.addAttribute("candidateList",map.get("candidateList"));
                model.addAttribute("choice",map.get("choice"));
                model.addAttribute("explain",explain);
                return "index-phone";
            }
        }else{
            return "404";
        }
    }*/

    /**
     * 投票系统后台管理系统请求方法
     * @return
     */
    @RequestMapping(value = "/admin")
    public String toAdmin(){
        return "admin/index";
    }

    /**
     * 投票系统后台管理系统欢迎页面请求方法
     * @param model
     * @return
     */
    @RequestMapping(value = "/welcome")
    public String toWelcome(Model model){
        HashMap<String,Integer> map= new HashMap<>();
        map= this.voteService.findCount();
        model.addAttribute("map",map);
        return "admin/welcome";
    }

    /**
     * 设置投票选择结果显示
     * @param list
     * @param choice
     * @return
     */
    public List<Candidate> setChoiceWithCandidate(List<Candidate> list, String choice){
        String[] strings= null;
        strings= choice.split(",");
        for(int j=0;j<strings.length;j++){
            for (int i=0;i<list.size();i++){
                if(Integer.parseInt(strings[j])==list.get(i).getId()){
                    list.get(i).setShow(1);
                }
            }
        }
        return list;
    }

    /**
     * 公共删除方法
     * @param code
     * @return
     */
    public static String delete(int code) {
        JSONObject jsonObject = new JSONObject();
        if (code >= 1) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "删除成功");
        } else {
            jsonObject.put("code", -1);
            jsonObject.put("msg", "删除失败");
        }
        return jsonObject.toString();
    }

    /**
     * 公共上传文件方法
     * @param file
     * @return
     */
    public static String upload(MultipartFile file){
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, String> map = UploadUtil.uploadImage(file);
            if (map != null && map.size() != 0) {
                jsonObject.put("code", 0);
                jsonObject.put("data", map.get("name"));
            } else {
                jsonObject.put("code", 1);
            }
        } catch (Exception e) {
            return null;
        }
        return jsonObject.toString();
    }

    /**
     * 修改投票活动的时间格式：date类型->String
     * @param list
     * @return
     */
    public static List<Vote> changeTimetoString(List<Vote> list){
        if(list==null && list.size()==0){
            return null;
        }else{
            for (Vote vote : list) {
                Date starttime= vote.getStarttime();
                Date endtime= vote.getEndtime();
                vote.setStart(DateUtils.changeTimeOfDay(starttime));
                vote.setEnd(DateUtils.changeTimeOfDay(endtime));
            }
            return list;
        }
    }

}
