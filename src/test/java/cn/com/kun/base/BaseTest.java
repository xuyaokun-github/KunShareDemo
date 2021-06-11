package cn.com.kun.base;

import cn.com.kun.KunShareDemoApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 所有单测类的基类
 * author:xuyaokun_kzx
 * date:2021/6/11
 * desc:
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = KunShareDemoApplication.class)
public abstract class BaseTest {


}
