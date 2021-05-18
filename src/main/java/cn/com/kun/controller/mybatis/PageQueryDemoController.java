package cn.com.kun.controller.mybatis;

import cn.com.kun.common.entity.User;
import cn.com.kun.common.vo.PageParam;
import cn.com.kun.service.mybatis.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/pagequerydemo")
@RestController
public class PageQueryDemoController {

    @Autowired
    private UserService userService;

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

}
