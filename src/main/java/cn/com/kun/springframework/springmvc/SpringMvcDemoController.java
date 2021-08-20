package cn.com.kun.springframework.springmvc;

import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.ResultVo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE;

/**
 */
@RequestMapping("/springmvcdemo")
@RestController
public class SpringMvcDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(SpringMvcDemoController.class);

    //@CrossOrigin(value = "*")
    @CrossOrigin(value = "http://localhost:8081") //这是正常的
//    @CrossOrigin(value = "http://localhost:8081/") //这是会失败的
    @PostMapping("/upload")
    public ResultVo upload(@RequestParam MultipartFile file) throws IllegalStateException, IOException {
        // 判断是否为空文件
        if (file.isEmpty()) {
            LOGGER.info("上传文件不能为空");
            return ResultVo.valueOfError("上传文件不能为空");
        }
        // 文件类型
        String contentType = file.getContentType();
        // springmvc处理后的文件名
        String fileName = file.getName();
        // 原文件名即上传的文件名
        String origFileName = file.getOriginalFilename();
        LOGGER.info("服务器接收到文件名：{} 源文件名：{}", fileName, origFileName);
        // 文件大小
        Long fileSize = file.getSize();

        //假如不需要落地，可以直接解析
        //假如先调transferTo，再调getInputStream就会抛异常

        try (InputStream inputStream = file.getInputStream();){
            List<String> lines = readLines(inputStream);
            if (lines != null && lines.size() > 0){
                for (String line : lines){
                    LOGGER.info("文件行内容：{}", line);
                }
            }
        }catch (Exception e){
            LOGGER.error("", e);
        }

        //getInputStream调完之后，仍可继续调用transferTo方法，读完流之后依然可以另存为
        //假如把流关了，也依然能调transferTo方法
//        file.getInputStream().close();

        // 假如需要落地
        // 保存文件 可以使用二进制流直接保存
        // 这里直接使用transferTo，直接另存为成另一个文件
        file.transferTo(new File("d://home/upload/" + origFileName));

        LOGGER.info(String.format(file.getClass().getName() + "方式文件上传成功！\n文件名:%s,文件类型:%s,文件大小:%s", origFileName, contentType,fileSize));
        return ResultVo.valueOfSuccess("上传成功");
    }

    /**
     * 读取文件的所有行
     * @param inputStream
     * @return
     * @throws IOException
     */
    private List<String> readLines(InputStream inputStream) throws IOException {

        InputStream in = inputStream;
        return IOUtils.readLines(in, "UTF-8");
    }



    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/testGetControllerNameFromRequest")
    public ResultVo testGetControllerNameFromRequest(HttpServletRequest request){

        Object handlerMethod = request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handlerMethod != null && handlerMethod instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod) handlerMethod;
            //获取实际调用的方法
            Method classMethod = method.getMethod();
            Class clazz = method.getBeanType();
            String clazzName = clazz.getSimpleName();
            LOGGER.info(clazzName);

            //即使这个controller被代理了，这里的bean还是拿到一个字符串，即beanName
            //同理，即使这个controller被代理，className也还是原类型
            Object bean = method.getBean();
            if (bean instanceof String){
                //有时候bean不一定是字符串类型
                LOGGER.info((String) bean);
            }

            //但假如用这个bean名去bean工厂拿实例时，获取的引用是代理对象的引用
            Object beanObj = SpringContextUtil.getBean((String) bean);
            //输出的将是类似这种：cn.com.kun.springframework.springmvc.SpringMvcDemoController$$EnhancerBySpringCGLIB$$6a2feee9
            LOGGER.info(beanObj.getClass().getTypeName());
        }
        return ResultVo.valueOfSuccess();
    }

}
