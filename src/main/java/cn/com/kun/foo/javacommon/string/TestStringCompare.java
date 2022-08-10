package cn.com.kun.foo.javacommon.string;

import cn.com.kun.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestStringCompare {

    public static void main(String[] args) {

        String pattern = "yyyy-MM-dd HH:mm:ss.S";
//        String pattern = "yyyy-MM-dd HH:mm:ss.SS";
//        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";

        int length = pattern.replaceAll("-", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll(" ", "").length();

        List<String> sourceList = new ArrayList<>();

        for (int i = 0; i < 5000; i++) {

            String dateString = DateUtils.toStr(new Date(), pattern);
//            String dateString = DateUtils.toStr(new Date(), );
//            String dateString = DateUtils.toStr(new Date(), );
            sourceList.add(dateString);
        }

        //排序
        List<String> newList = sourceList.stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //升序
                return o1.compareTo(o2);
            }
        }).collect(Collectors.toList());

//        sourceList
        String last = null;
        for(String str : newList){
            if (last != null){
                //比较
                String one = last.replaceAll("-", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll(" ", "");
                String two = str.replaceAll("-", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll(" ", "");
                if (one.length() < length){
                    one = one.substring(14) + "0" + one.substring(14, length-1);//补零
                }
                if (two.length() < length){
                    two = two.substring(14) + "0" + two.substring(14, length-1);//补零
                }
                if (Long.parseLong(one) > Long.parseLong(two)){
                    System.out.println("存在前比后大" + " 前：" + last + " 后:" + str);
                }
            }

            last = str;
        }

    }


}
