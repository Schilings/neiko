package com.schilings.neiko.samples.cloud.provider.controller;


import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private String dateStr() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }

    /**
     * 返回字符串类型
     *
     * @return
     */
    @GetMapping("/str")
    public String getStr() {
        return dateStr();
    }

    /**
     * 返回字符串类型
     *
     * @return
     */
    @PostMapping("/str")
    public String postStr() {
        return dateStr();
    }

}
