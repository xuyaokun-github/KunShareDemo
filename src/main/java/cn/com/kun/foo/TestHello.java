package cn.com.kun.foo;

import cn.com.kun.KunShareDemoApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;

public class TestHello {

    private static final String aaa = "aaa";

   public static void main(String[] args) {

       Annotation annotation = AnnotationUtils.findAnnotation(KunShareDemoApplication.class, SpringBootApplication.class);
       System.out.println(annotation);
   }

}
