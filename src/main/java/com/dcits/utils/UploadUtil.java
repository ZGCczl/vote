package com.dcits.utils;

import com.dcits.pojo.Candidate;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadUtil {

    private static final String imagePath = "/images/";


    private static String imagepath = "/home/dc/voteapp/static/images";

    public static Map<String, String> uploadImage(MultipartFile file) {
        Map<String, String> map = null;
        try {
            String name = file.getOriginalFilename();
            String suffixName = name.substring(name.lastIndexOf("."));
            String filename = name.substring(0, name.lastIndexOf("."));
            Date date = new Date();
            String hash = new SimpleDateFormat("yyyyMMddhhmmss").format(date);//给照片自定义一个名字，用时间做名称，不会重复
            String fileName = hash + suffixName;
            File tempFile = new File(imagepath, fileName);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            tempFile.createNewFile();
            file.transferTo(tempFile);
            map = new HashMap<String, String>();
            String pathName= imageAppendPath(tempFile.getName());
            map.put("name", pathName);
            map.put("filename", filename);
        } catch (IOException e) {
            return null;
        }
        return map;
    }

    public static String imageAppendPath(String image) {
        StringBuilder stringBuilder = new StringBuilder();
        String filePath = imagePath;
        stringBuilder.append(filePath);
        stringBuilder.append(image);
        return stringBuilder.toString();
    }

    public static List<Candidate> exchangeText(List<Candidate> list){
        if(list==null||list.size()==0){
            return null;
        }
        for (Candidate candidate : list) {
            String text= candidate.getText();
            if(text!=null&&text!="") {
                candidate.setText(toTextarea(text));
            }
        }
        return list;
    }

    public static String toTextarea(String str){
        String text= str.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t");
        return text;
    }

    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }


}
