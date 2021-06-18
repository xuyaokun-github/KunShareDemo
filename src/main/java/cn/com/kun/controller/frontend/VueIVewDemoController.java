package cn.com.kun.controller.frontend;

import cn.com.kun.bean.model.PointCountResVO;
import cn.com.kun.common.vo.ResultVo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@CrossOrigin(value = "http://localhost:8081") //这是正常的
@RequestMapping("/vue-iview-demo")
@RestController
public class VueIVewDemoController {

    /**
     * 折线图例子-球星的得分趋势图
     *
     * @param request
     * @return
     */
    @RequestMapping("/point-count")
    public ResultVo<List<PointCountResVO>> pointCount(HttpServletRequest request){

        List<PointCountResVO> resVOList = new ArrayList<>();
        String[] names = new String[]{"麦迪", "姚明", "库里", "詹姆斯", "卡特", "乔丹", "邓肯",};
        for (int i = 0; i < 7; i++) {//遍历七个球星
            PointCountResVO resVO = new PointCountResVO();
            resVO.setName(names[i]);
            List<Integer> dataList = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                //只统计七天的数据
                dataList.add(new Integer(ThreadLocalRandom.current().nextInt(20, 100)));
            }
            resVO.setData(dataList);
            resVOList.add(resVO);
        }
        return ResultVo.valueOfSuccess(resVOList);
    }






}
