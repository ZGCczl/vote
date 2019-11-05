package com.dcits.controller;


import cn.hutool.json.JSONObject;
import com.dcits.pojo.Candidate;
import com.dcits.pojo.Vote;
import com.dcits.service.VoteService;
import com.dcits.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CommomController {

    @Autowired
    VoteService voteService;



    /**
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

    @RequestMapping(value = "/admin")
    public String toAdmin(){
        return "admin/index";
    }

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

}
