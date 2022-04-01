package cn.com.kun.foo.javacommon.queue;

import java.util.LinkedList;

public class TestQueue {

    public static void main(String[] args) {

        LinkedList<String> linkedList = new LinkedList<>();
        /**
         * push是表示放到栈头，入栈
         */
        linkedList.push("11");
        linkedList.push("22");
        linkedList.push("33");

        /**
         * 执行完上面三句之后，就是
         * 33 22 11
         */

        //所以pollLast输出时，先是11 再到22 最后33
//        System.out.println(linkedList.pollLast());//11
//        System.out.println(linkedList.pollLast());//22
//        System.out.println(linkedList.pollLast());//33
//        System.out.println(linkedList.size());//0

        /**
         * pollFirst是拿到栈顶
         */
        System.out.println(linkedList.pollFirst());//33
        System.out.println(linkedList.pollFirst());//22
        System.out.println(linkedList.pollFirst());//11
        System.out.println(linkedList.size());//0


        linkedList.clear();
        System.out.println(linkedList.size());
        System.out.println("---------------------------------");
        linkedList.add("kunghsu");
        System.out.println(linkedList.peek());
        System.out.println(linkedList.peek());
        System.out.println(linkedList.peek());
        System.out.println(linkedList.peek());


    }
}
