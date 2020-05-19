package cn.com.kun.foo.powerMock.service.impl;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MyObjectServiceImpl {

    /**
     * 方法里有new对象的操作
     *
     * @param path
     * @return
     */
    public boolean makeFile(String path) {
        //new对象
        File file = new File(path);
        return file.exists();
    }

}
