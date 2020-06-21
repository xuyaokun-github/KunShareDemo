package cn.com.kun.dataSet;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * 自定义的数据加载器
 *
 * Created by xuyaokun On 2020/6/21 14:06
 * @desc:
 */
public class MyXmlDataSetLoader extends AbstractDataSetLoader {

    protected IDataSet createDataSet(Resource resource) throws Exception {
        InputStream inputStream = resource.getInputStream();
        try {
            //FlatXmlDataSet是spring的类
//            return new FlatXmlDataSet(inputStream);
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            return builder.build(inputStream);
        } finally {
            inputStream.close();
        }
    }
}
