package cn.com.kun.controller;

import cn.com.kun.phoenix.NodeFlagHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private String old_node_string = "";
    private String new_node_string = "";

    //全局只需要一个
    private NodeFlagHolder nodeFlagHolder = new NodeFlagHolder();

    @RequestMapping("/hello")
    public String test1(){

        return "hello!!!";
    }

    @RequestMapping("/test2")
    public String test2(){

        nodeFlagHolder.init(System.currentTimeMillis() + "");
        System.out.println(String.format("当前NodeFlagHolder： 旧key: %s 新key: %s",
                nodeFlagHolder.getLastNodeKey(), nodeFlagHolder.getCurrentNodeKey()));
        return "test2!!!";
    }

}
