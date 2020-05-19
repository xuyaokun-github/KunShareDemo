package cn.com.kun.controller;

import cn.com.kun.common.vo.EasyUIPageInfo;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.common.vo.User;
import cn.com.kun.mapper.UserMapper;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/v1")
@RestController
public class EasyUIDemoController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 直接返回com.github.pagehelper.PageInfo，EasyUI前端拿不到数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/users2")
    public PageInfo users2(HttpServletRequest request){

        getPageInfo(request);
        List<User> userList = userMapper.query(null);
        PageInfo pageInfo = new PageInfo(userList);
        return pageInfo;
    }

    /**
     * 用自定义的VO继承com.github.pagehelper.PageInfo，解决上面的问题
     * @param request
     * @return
     */
    @RequestMapping("/users3")
    public EasyUIPageInfo users3(HttpServletRequest request){

        getPageInfo(request);
        List<User> userList = userMapper.query(null);
        EasyUIPageInfo pageInfo = new EasyUIPageInfo(userList);
        return pageInfo;
    }


    @RequestMapping("/save_user")
    public ResultVo save_user(HttpServletRequest request){

        User user = getObjectParam(request, User.class, "bean");
        System.out.println(JSON.toJSONString(user));
        userMapper.insert(user);
        ResultVo resultVo = ResultVo.valueOfSuccess();
        resultVo.setMessage("保存成功");
        return resultVo;
    }

    @RequestMapping("/update_user")
    public ResultVo update_user(HttpServletRequest request){
        User user = getObjectParam(request, User.class, "bean");
        userMapper.update(user);
        ResultVo resultVo = ResultVo.valueOfSuccess();
        resultVo.setMessage("修改成功");
        return resultVo;
    }

    @RequestMapping("/remove_user/{firstname}")
    public ResultVo remove_user(@PathVariable String firstname){
        userMapper.deleteByFirstname(firstname);
        ResultVo resultVo = ResultVo.valueOfSuccess();
        resultVo.setMessage("删除成功");
        return ResultVo.valueOfSuccess();
    }

    /**
     * 获取分页参数
     *
     * @param request
     * @return
     */
    protected PageInfo getPageInfo(HttpServletRequest request) {

        try {
            int page = Integer.parseInt(request.getParameter("page"));
            int rows = Integer.parseInt(request.getParameter("rows"));
            //分页
            //传参，页码，每页条数
            //其实还可以传排序方式等
            PageHelper.startPage(page, rows);

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取json请求数据转换后Java对象
     *
     * @param request
     * @param clazz
     * @param name
     * @param <T>
     * @return
     */
    private <T> T getObjectParam(HttpServletRequest request, Class<T> clazz, String name) {

        //其实推荐在解析参数时，就判断是否分页请求
        String s = request.getParameter(name);
        if (s == null){
            return null;
        }
        return JSON.parseObject(s, clazz);
    }
}
