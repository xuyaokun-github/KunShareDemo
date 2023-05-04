package cn.com.kun.foo.javacommon.collections.list;

import java.util.ArrayList;
import java.util.List;

public class TestListToArray {

    public static void main(String[] args) {

        List<String> stringList = new ArrayList<>();
        stringList.add("jjj");
        stringList.add("jjj2");

        //反例：它不会自动转成字符串类型，因为会有泛型擦除问题

        try {
            String[] targetStrArray = (String[]) stringList.toArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("end");
        //正例
        String[] targetStrArray = (String[]) stringList.toArray(new String[]{});
        System.out.println(targetStrArray.length);

    }

}
