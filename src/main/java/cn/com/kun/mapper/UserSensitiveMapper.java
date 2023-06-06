package cn.com.kun.mapper;

import cn.com.kun.bean.entity.UserSensitiveDO;
import cn.com.kun.bean.model.user.UserQueryParam;
import cn.com.kun.service.mybatis.sensitiveDemo.EncryptDecryptField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserSensitiveMapper {

    int insert(UserSensitiveDO user);

    List<UserSensitiveDO> list(UserQueryParam userQueryParam);

    UserSensitiveDO getUserByEmail(@EncryptDecryptField @Param("email") String email);

}
