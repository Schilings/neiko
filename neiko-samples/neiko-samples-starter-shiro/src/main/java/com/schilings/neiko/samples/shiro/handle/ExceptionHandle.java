package com.schilings.neiko.samples.shiro.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/11 1:29
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

//    @ExceptionHandler(value =Throwable.class)
//    public String exceptionHandler(HttpServletRequest req, Throwable e){
//        System.out.println("未知异常！原因是:"+e.toString());
//        return e.toString();
//    }
}
