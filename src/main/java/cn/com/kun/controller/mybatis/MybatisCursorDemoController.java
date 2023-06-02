package cn.com.kun.controller.mybatis;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.mybatis.MybatisCursorDemoService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mybatis-cursor-demo")
@RestController
public class MybatisCursorDemoController {

    private final static Logger logger = LoggerFactory.getLogger(MybatisCursorDemoController.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private MybatisCursorDemoService mybatisCursorDemoService;

    /**
     * 游标查询
     * @return
     */
    @GetMapping("/testCursor")
    public ResultVo testCursor(){

        new Thread(()->{
            mybatisCursorDemoService.testRealCursor();
        }).start();

        return ResultVo.valueOfSuccess();
    }

    /**
     * 验证流式查询 长事务问题
     *
     * @return
     */
    @GetMapping("/testCursorTrx")
    public ResultVo testCursorTrx(){

        return mybatisCursorDemoService.testCursorTrx();
    }


    /**
     * 游标查询
     * @return
     */
    @GetMapping("/testFailCursor")
    public ResultVo testFailCursor(){


        return mybatisCursorDemoService.testFakeCursor();
    }


    /**
     * 游标查询
     * @return
     */
    @GetMapping("/testCursorBreak")
    public ResultVo testCursorBreak(){
        return mybatisCursorDemoService.testCursorBreak();
    }

    /**
     * 游标查询
     * @return
     */
    @GetMapping("/testCursorWithDelete")
    public ResultVo testCursorWithDelete(){
        return mybatisCursorDemoService.testCursorWithDelete();
    }

}
