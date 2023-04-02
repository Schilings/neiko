package com.schilings.neiko.samples.application.web;


import com.schilings.neiko.log.remote.AccessLogRemote;
import com.schilings.neiko.log.remote.LoginLogRemote;
import com.schilings.neiko.log.remote.OperationLogRemote;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogRemoteController {

    private final AccessLogRemote accessLogRemote;
    private final OperationLogRemote operationLogRemote;
    private final LoginLogRemote loginLogRemote;


    @GetMapping("/login")
    public Object login() throws IOException {
        byte[] bytes = loginLogRemote.exportAccessLogList(null);
        return bytes;

       
    }
    
}
