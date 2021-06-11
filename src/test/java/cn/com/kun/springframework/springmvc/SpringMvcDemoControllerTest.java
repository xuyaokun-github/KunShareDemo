package cn.com.kun.springframework.springmvc;

import cn.com.kun.base.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringMvcDemoControllerTest extends BaseTest {

    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 上传文件的控制层单测写法
     * @throws Exception
     */
    @Test
    public void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        /**
         * multipart方法是5.0之后才有的，低版本的springboot单测没法用下面的方法
         */
        final MvcResult result = mvc.perform(
                MockMvcRequestBuilders
                        .multipart("/springmvcdemo/upload")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * 上传文件的控制层单测写法2（针对低版本的springboot）
     * @throws Exception
     */
    @Test
    public void uploadFile2() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        /**
         * multipart方法是5.0之后才有的，低版本的springboot只能用fileUpload方法
         */
        final MvcResult result = mvc.perform(
                MockMvcRequestBuilders
                        .fileUpload("/springmvcdemo/upload")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}