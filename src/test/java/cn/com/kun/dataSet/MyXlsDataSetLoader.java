package cn.com.kun.dataSet;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * 自定义的数据加载器
 *
 * Created by xuyaokun On 2020/6/21 14:06
 * @desc:
 */
public class MyXlsDataSetLoader extends AbstractDataSetLoader {

    protected IDataSet createDataSet(Resource resource) throws Exception {
        InputStream inputStream = resource.getInputStream();
        try {
            //XlsDataSet是spring的类
            return new XlsDataSet(inputStream);
        } finally {
            inputStream.close();
        }
    }
}
