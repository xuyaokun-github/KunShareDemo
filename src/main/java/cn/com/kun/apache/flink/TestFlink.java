package cn.com.kun.apache.flink;

import cn.com.kun.common.vo.people.People;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟一个OOM
 * author:xuyaokun_kzx
 * date:2021/9/9
 * desc:
*/
public class TestFlink {

    public static List<People> list = new ArrayList<>();

    public static void main(String[] args) {
        while (true){
            People people = new People();
            people.setFirstname("000000000000000");
            list.add(people);
        }
    }
}
