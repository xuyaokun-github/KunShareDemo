package cn.com.kun.foo.javacommon.datatype;

import cn.com.kun.common.vo.people.People;

public class TestDouble {

    public static void main(String[] args) {

        Double one = new Double(1000);
        System.out.println(one);
        changeDouble(one);
        System.out.println(one);

        System.out.println("----------------");

        People people = new People();
        people.setPhone("123");
        System.out.println(people);
        changePeople(people);
        System.out.println(people);
    }

    private static void changePeople(People people) {

        people = new People();
    }

    /**
     *
     * 这里是包装类型是值传递，注意不要在里面直接修改它的指针
     * @param one
     */
    private static void changeDouble(Double one) {

        one = new Double(2000);
        System.out.println("方法内修改后：" + one);
    }


}
