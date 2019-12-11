package site.blbc;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import site.blbc.bean.UploadImage;
import site.blbc.config.UploadConfig;

public class TestUploadImage {

    @Test
    public void uploadTest() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-config-upload.xml");
        UploadImage uploadImage = context.getBean(UploadImage.class);
        uploadImage.upload("abd.jpg");

        uploadImage = (UploadImage) context.getBean("upload");
        uploadImage.upload("ccc.jpg");
    }

    @Test
    public void upload2Test() {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(UploadConfig.class);
        UploadImage uploadImage = applicationContext.getBean(UploadImage.class);
        uploadImage.upload("dddd.jpg");
    }
}
