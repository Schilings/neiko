package com.schilings.neiko.admin.upms;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@MapperScan({
        "com.gitee.sunchenbin.mybatis.actable.dao.*",//自动建表
        "com.schilings.neiko.**.mapper"
})
@ComponentScan({
        "com.gitee.sunchenbin.mybatis.actable.manager.*",//自动建表
        "com.schilings.neiko.admin.upms",
        "com.schilings.neiko.system",
        "com.schilings.neiko.log"
})
@EnableAsync
@AutoConfiguration
public class UpmsAutoConfiguration {
}
