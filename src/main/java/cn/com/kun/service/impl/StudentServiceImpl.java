package cn.com.kun.service.impl;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static cn.com.kun.common.constants.RedisCacheConstants.CACHE_CONFIGURATIONS_NAME_STUDENT;

/**
 * 学生服务层
 *
 * author:xuyaokun_kzx
 * date:2021/6/25
 * desc:
*/
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public ResultVo<Integer> add(StudentReqVO reqVO) {

        Student student = new Student();
        BeanUtils.copyProperties(reqVO, student);
        int res = studentMapper.insert(student);
        return ResultVo.valueOfSuccess(res);
    }


    /**
     * value表示的是使用哪个缓存配置，一般每个服务层都是自己的单独配置
     * 可以根据业务不同设置不同的过期时间等
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#id.toString()")
    @Override
    public ResultVo<StudentResVO> getStudentById(Long id) {

        Student student = studentMapper.getStudentById(id);
        StudentResVO resVO = new StudentResVO();
        BeanUtils.copyProperties(student, resVO);
        return ResultVo.valueOfSuccess(resVO);
    }

    /**
     * 在更新的时候应该更新缓存呢还是清空缓存呢？
     * 我推荐是清空缓存，lazy 计算的思想
     *
     * 清除操作默认是在对应方法成功执行之后触发的，即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。
     * 使用beforeInvocation可以改变触发清除操作的时间，当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素。
     *
     * 无论执行之前或者之后触发，都是用入参去生成key
     *
     * @param reqVO
     * @return
     */
//    @CacheEvict(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#reqVO.getId().toString()") //正例，可以用方法调用获取值
//    @CacheEvict(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#reqVO.id.toString()") //正例，可以用方法调用获取值
//    @CacheEvict(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#reqVO.id") //正例
    @CacheEvict(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#reqVO.id", beforeInvocation=true) //正例
    @Override
    public ResultVo<Integer> update(StudentReqVO reqVO) {

        /**
         * 执行顺序问题：执行下面的代码先还是先删缓存呢？
         *
         */
        Student student = new Student();
        BeanUtils.copyProperties(reqVO, student);
        int res = studentMapper.update(student);
        return ResultVo.valueOfSuccess(res);
    }

    @CacheEvict(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#id", beforeInvocation=true) //正例
    @Override
    public ResultVo<Integer> delete(Long id) {

        int res = studentMapper.delete(id);
        return ResultVo.valueOfSuccess(res);
    }
}
