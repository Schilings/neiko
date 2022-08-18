package com.schilings.neiko.cloud.gateway.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


/**
 *修改请求body的代码，既RewriteFunction的实现类，代码很简单
 * ，将原始的请求body解析成Map对象，取出user-id字段，生成user-name字段放回map，apply方法返回的是个Mono：
 */
@Slf4j
public class RequestBodyRewrite implements RewriteFunction<String, String> {

    private ObjectMapper objectMapper;

    public RequestBodyRewrite(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 根据用户ID获取用户名称的方法，可以按实际情况来内部实现，例如查库或缓存，或者远程调用
     * @param userId
     * @return
     */
    private  String mockUserName(int userId) {
        return "user-" + userId;
    }

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        try {
            Map<String, Object> map = objectMapper.readValue(body, Map.class);


            //如果请求body不含user-id参数就返回Mono.error，入参是ResponseStatusException异常，设置了返回码为400，message为一段中文描述
            if (!map.containsKey("user-id")) {
                /**
                 * 第一种错误异常返回解决方案
                 */
                //return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "请求参数必须包含user-id字段"));
                /**
                 * 第二种
                 */
                //return Mono.error(new MyGatewayException());
                /**
                 * 第三种
                 */
                ServiceException customizeInfoException = new ServiceException(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
//                // 这里按照业务需要自行设置code
//                customizeInfoException.setCode(010020003);
//                // 这里按照业务需要自行设置返回的message
//                customizeInfoException.setMessage("请确保请求参数中的user-id字段是有效的");
                return Mono.error(customizeInfoException);

                
            }

            // 取得id
            int userId = (Integer)map.get("user-id");

            // 得到nanme后写入map
            map.put("user-name", mockUserName(userId));

            // 添加一个key/value
            map.put("gateway-request-tag", userId + "-" + System.currentTimeMillis());

            return Mono.just(objectMapper.writeValueAsString(map));
        } catch (Exception ex) {
            log.error("1. json process fail", ex);
            // json操作出现异常时的处理
            return Mono.error(new Exception("1. json process fail", ex));
        }
    }
}

