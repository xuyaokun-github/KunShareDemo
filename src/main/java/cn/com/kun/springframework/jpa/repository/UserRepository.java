package cn.com.kun.springframework.jpa.repository;

import cn.com.kun.springframework.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 已经有的东西，不要重复造轮子，省下时间成本
 *
 * author:xuyaokun_kzx
 * date:2022/10/17
 * desc:
*/
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /*
        Long要和主键定义的类型一致，否则会报错
     */

    //单条件查询（查单条）
    @Query(value = "select * from tbl_jpa_user where phone =?1 ", nativeQuery = true)
    UserEntity findByPhone(String phone);

    //多条件查询（简单语法，查列表）
    @Query(value = "select * from tbl_jpa_user where phone =?1 || address = ?2", nativeQuery = true)
    List<UserEntity> findByPhoneAndAddress(String phone, String address);

    //复杂多条件查询（用到了in语法）
    @Query(value = "select * from tbl_jpa_user where phone =?1 and age = ?2 and name in (?3) ", nativeQuery = true)
    UserEntity findUsers(String phone, Integer age, List<String> name);

    /**
     * 更新
     * @return
     */
    @Modifying
    @Query(value = "update `tbl_jpa_user` set phone = :phone where address = :address", nativeQuery = true)
    int updateUserByAddress(@Param("phone")String phone, @Param("address")String address);

    /**
     * 删除
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "delete from `tbl_jpa_user` where id = :id", nativeQuery = true)
    int deleteUserByAddress(@Param("id")Long id);

}