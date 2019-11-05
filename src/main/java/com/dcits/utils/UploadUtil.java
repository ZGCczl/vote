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

public class UploadUtil {

    private static final String imagePath = "/vote/images/";


    private static String imagepath = "D:\\Tools\\vote\\images";

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


}
