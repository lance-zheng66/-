package site.blbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.blbc.bean.AccountCheck;
import site.blbc.bean.UploadImage;

/**
 * 基于 Java 配置 Spring Bean
 */
@Configuration
public class UploadConfig {

    @Bean
    public UploadImage uploadImage() {
        UploadImage uploadImage = new UploadImage();
        // 注入依赖
        uploadImage.setAccountCheck(accountCheck());
        return uploadImage;
    }

    @Bean
    public AccountCheck accountCheck () {
        return  new AccountCheck();
    }
}
