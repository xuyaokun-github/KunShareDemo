package cn.com.kun.service.impl;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

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
//    @Cacheable(value = CACHE_CONFIGURATIONS_NAME_STUDENT, key = "#id.toString()")
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

    /**
     * 验证一个经典的事务可见问题
     * 唯一索引插入提示重复后，立刻做一次查询，是否能立刻查到新数据？
     * 默认情况下是不能。
     *
     * @return
     */
    @Transactional
    @Override
    public Student saveIfNotExist() {

        Map<String, Object> map = new HashMap<>();
        map.put("idCard", "10086");
        //基于普通唯一索引进行查询 idCard列建了普通唯一索引
        List<Student> studentList = studentMapper.query(map);

        Student student = null;
        if (studentList.size() > 0){
            LOGGER.info("student已存在，不作插入");
        }else {
            student = new Student();
            student.setIdCard("10086");
            student.setAddress(UUID.randomUUID().toString());
            student.setStudentName("kunghsu");
            student.setCreateTime(new Date());
            try {
                studentMapper.insert(student);
                LOGGER.info("保存student成功");
            }catch (Exception e){
                if (e instanceof DuplicateKeyException){
                    LOGGER.info("保存student重复");
                    //反例代码（）
                    while (true){
                        //再次基于相同的条件做查询（快照读）
                        studentList = studentMapper.query(map);
                        if (studentList.size() > 0){
                            student = studentList.get(0);
                            LOGGER.info("再次查询得到student：{}", JacksonUtils.toJSONString(student));
                            break;
                        }else {
                            //前后两次查询是一致的，没有读取到最新的数据（不存在幻读问题）
                            LOGGER.info("再次查询student，数据不存在");
                            ThreadUtils.sleep(1000);
                        }
                    }

                    //正例代码(重新起一个事务，就能解决幻读问题)
//                    while (true){
//                        studentList = studentService.query(map);
//                        if (studentList.size() > 0){
//                            student = studentList.get(0);
//                            LOGGER.info("再次查询得到student：{}", JacksonUtils.toJSONString(student));
//                            break;
//                        }else {
//                            LOGGER.info("再次查询student，数据不存在");
//                            ThreadUtils.sleep(1000);
//                        }
//                    }
                }else {
                    LOGGER.error("保存student异常", e);
                }
            }
        }

        return student;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Student> query(Map<String, Object> map) {

        return studentMapper.query(map);
    }


    /**
     * 验证一个经典的事务可见问题
     * 唯一索引插入提示重复后，立刻做一次查询，是否能立刻查到新数据？
     * 默认情况下是不能。
     *
     * @return
     */
    @Transactional
    @Override
    public Student saveIfNotExist2() {

        //找到最大的主键ID
        long maxId = studentMapper.findMaxId();
        long newMaxId = maxId + 1;

        Map<String, Object> map = new HashMap<>();
        map.put("id", newMaxId);
        //基于主键做查询
        List<Student> studentList = studentMapper.query(map);

        if(studentList.size() > 0){

        }

        Student student = null;
        student = new Student();
        student.setId(newMaxId);
        student.setIdCard(UUID.randomUUID().toString());
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        try {
            studentMapper.insertWithId(student);
            LOGGER.info("保存student成功");
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                LOGGER.info("保存student重复");
                //（基于主键也没有拿到最新数据，因为基于主键查询也是快照读，和普通唯一索引是一个道理）
                while (true){
                    studentList = studentMapper.query(map);
                    if (studentList.size() > 0){
                        //基于主键查询，能查出数据
                        student = studentList.get(0);
                        LOGGER.info("再次查询得到student：{}", JacksonUtils.toJSONString(student));
                        break;
                    }else {
                        LOGGER.info("再次查询student，数据不存在");
                        ThreadUtils.sleep(1000);
                    }
                }

            }else {
                LOGGER.error("保存student异常", e);
            }
        }

        return student;
    }

}
