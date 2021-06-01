package cn.com.kun.controller.mybatis;

import cn.com.kun.common.entity.User;
import cn.com.kun.common.vo.PageParam;
import cn.com.kun.common.vo.user.UserQueryParam;
import cn.com.kun.common.vo.user.UserQueryResVO;
import cn.com.kun.service.mybatis.UserErrorService;
import cn.com.kun.service.mybatis.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/pagequerydemo")
@RestController
public class PageQueryDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(PageQueryDemoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserErrorService userErrorService;

    /**
     * 分页查询
     *
     * @param pageParam 分页查询参数
     * @return 分页查询响应
     */
    @PostMapping(path = "page")
    public PageInfo<User> page(@RequestBody PageParam<UserQueryParam> pageParam) {
        return userService.page(pageParam);
    }

    @PostMapping(path = "pageNoCount")
    public PageInfo<User> pageNoCount(@RequestBody PageParam<UserQueryParam> pageParam) {
        return userService.pageNoCount(pageParam);
    }

    /**
     * 集合查询
     *
     * @param listParam 集合查询参数
     * @return 集合查询响应
     */
    @PostMapping(path = "list")
    public List<User> list(@RequestBody UserQueryParam listParam) {
        return userService.list(listParam);
    }

    /**
     * 分页查询
     *
     * @param pageParam 分页查询参数
     * @return 分页查询响应
     */
    @PostMapping(path = "/pageError")
    public PageInfo<UserQueryResVO> pageError(@RequestBody PageParam<UserQueryParam> pageParam) {

        //这里调用了分页方法，虽然这一行的返回泛型是UserQueryResVO，骗过了编译器，但实际PageInfo里的却不是UserQueryResVO对象
        PageInfo<UserQueryResVO> pageInfo = userErrorService.page(pageParam);
        List list = pageInfo.getList();
        for (Object obj : list){
            //输出的将是cn.com.kun.common.entity.User
            LOGGER.info(obj.getClass().getTypeName());
        }
        return pageInfo;
    }
}
