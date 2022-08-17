package cn.com.kun.springframework.core.resolvableType;

import cn.com.kun.bean.model.people.Dog;
import cn.com.kun.bean.model.people.People;

/**
 * author:xuyaokun_kzx
 * date:2021/5/27
 * desc:
*/
public class PeopleVaccineService implements CustomParameterizedTypeService<People, Dog> {


    @Override
    public void show(People data, Dog dog) {
        System.out.println(data.getClass().getTypeName());
    }
}
