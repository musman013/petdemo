package com.fastcode.demopet.emailbuilder;

import java.io.IOException;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFilesystemStores(basePackages = {"org.springframework.content.elasticsearch"})
@Import(org.springframework.content.rest.config.RestConfiguration.class)
public class ContentConfig {

    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() throws IOException {
        return new FileSystemResourceLoader(new java.io.File("C:\\tmp\\uploadedFiles").getAbsolutePath());
    }
}
