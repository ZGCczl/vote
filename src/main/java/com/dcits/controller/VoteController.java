package com.dcits.controller;

import cn.hutool.json.JSONObject;
import com.dcits.pojo.Vote;
import com.dcits.service.VoteService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "vote")
public class VoteController {

    @Autowired
    VoteService voteService;

    @RequestMapping(value = "/voteList")
    public String toVoteList(){
        return "admin/voteList";
    }

    @ResponseBody
    @RequestMapping(value = "/findWithPage")
    public String findWithPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                               @RequestParam(value = "name", required = false) String name){
        JSONObject jsonObject= new JSONObject();
        PageInfo<Vote> pageInfo= this.voteService.findVoteWithPage(pageNum,pageSize,name);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", pageInfo.getTotal());
        jsonObject.put("data", pageInfo.getList());
        return jsonObject.toString();
    }

    @RequestMapping(value = "editOne", method = RequestMethod.POST)
    @ResponseBody
    public String editOne(@RequestParam(value = "id") Integer id,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "starttime") String starttime,
                          @RequestParam(value = "endtime") String endtime,
                          @RequestParam(value = "candidateCount") Integer candidateCount,
                          @RequestParam(value = "voteCount") Integer voteCount) {
        int code = this.voteService.edit(id, name, starttime, endtime,candidateCount,voteCount);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "addOne", method = RequestMethod.POST)
    @ResponseBody
    public String addOne(@RequestParam(value = "name") String name,
                         @RequestParam(value = "starttime") String starttime,
                         @RequestParam(value = "endtime") String endtime,
                         @RequestParam(value = "candidateCount") Integer candidateCount,
                         @RequestParam(value = "voteCount") Integer voteCount) {
        int code = this.voteService.add(name, starttime, endtime,candidateCount,voteCount);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "deleteOne", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOne(@RequestParam(value = "id") Integer id) {
        int code = this.voteService.delete(id);
        String jsonString = CommomController.delete(code);
        return jsonString;
    }



}
