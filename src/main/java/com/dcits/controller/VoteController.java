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

    /**
     * 请求投票活动信息页面
     * @return
     */
    @RequestMapping(value = "/voteList")
    public String toVoteList(){
        return "admin/voteList";
    }

    /**
     * 多条件、分页请求投票活动信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
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

    /**
     * 修改投票活动信息
     * @param id
     * @param name
     * @param starttime
     * @param endtime
     * @param candidateCount
     * @param voteCount
     * @param explain
     * @return
     */
    @RequestMapping(value = "editOne", method = RequestMethod.POST)
    @ResponseBody
    public String editOne(@RequestParam(value = "id") Integer id,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "starttime") String starttime,
                          @RequestParam(value = "endtime") String endtime,
                          @RequestParam(value = "candidateCount") Integer candidateCount,
                          @RequestParam(value = "voteCount") Integer voteCount,
                          @RequestParam(value = "explain") String explain) {
        int code = this.voteService.edit(id, name, starttime, endtime,candidateCount,voteCount,explain);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    /**
     * 添加投票活动信息
     * @param name
     * @param starttime
     * @param endtime
     * @param candidateCount
     * @param voteCount
     * @param explain
     * @return
     */
    @RequestMapping(value = "addOne", method = RequestMethod.POST)
    @ResponseBody
    public String addOne(@RequestParam(value = "name") String name,
                         @RequestParam(value = "starttime") String starttime,
                         @RequestParam(value = "endtime") String endtime,
                         @RequestParam(value = "candidateCount") Integer candidateCount,
                         @RequestParam(value = "voteCount") Integer voteCount,
                         @RequestParam(value = "explain") String explain) {
        int code = this.voteService.add(name, starttime, endtime,candidateCount,voteCount,explain);
        JSONObject jsonObject = new JSONObject();
        if (code == 1) {
            jsonObject.put("returnCode", 200);
        } else {
            jsonObject.put("returnCode", -1);
        }
        return jsonObject.toString();
    }

    /**
     * 删除投票活动信息
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteOne", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOne(@RequestParam(value = "id") Integer id) {
        int code = this.voteService.delete(id);
        String jsonString = CommomController.delete(code);
        return jsonString;
    }



}
