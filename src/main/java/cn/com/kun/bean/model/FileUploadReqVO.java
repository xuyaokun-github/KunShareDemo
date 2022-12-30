package cn.com.kun.bean.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 验证文件流和普通属性放在一起，能否成功接收？
 *
 * author:xuyaokun_kzx
 * date:2022/12/27
 * desc:
*/
public class FileUploadReqVO {

    private String name;

    /**
     * 假如要传多个文件咋办？
     * 难道要定义多个属性吗？file1、file2、file3之类的？
     */
    private MultipartFile file;

    /**
     * 一次性传多个文件
     */
    private List<MultipartFile> files;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
