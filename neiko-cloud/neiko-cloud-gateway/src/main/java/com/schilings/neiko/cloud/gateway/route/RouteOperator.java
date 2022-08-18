package com.schilings.neiko.cloud.gateway.route;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.util.json.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RouteOperator {

    //路由配置的处理主要是RouteDefinitionWriter类型的bean完成的
    private final RouteDefinitionWriter routeDefinitionWriter;

    //为了让配置立即生效，还要用applicationEventPublisher发布进程内消息
    private final ApplicationEventPublisher applicationEventPublisher;

    //存放路由
    private static final List<String> routeList = new ArrayList<>();

    /**
     * 清理集合中的所有路由，并清空集合
     */
    private void clear() {
        //全部调用API清理掉
        routeList.stream().forEach(id -> routeDefinitionWriter.delete(Mono.just(id)).subscribe());
        //清空集合
        routeList.clear();
    }

    /**
     * 新增路由
     */
    private void add(List<RouteDefinition> routeDefinitions) {
        try {
            routeDefinitions.stream().forEach(routeDefinition -> {
                routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
                routeList.add(routeDefinition.getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布进程内通知，更新路由
     */
    private void publish() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
    }

    /**
     * 更新所有路由信息
     * @param configStr
     */
    public void refreshAll(String configStr) {
        log.info("start refreshAll : {}", configStr);
        // 无效字符串不处理
        if (!StringUtils.hasText(configStr)) {
            log.error("invalid string for route config");
            return;
        }

        // 用Jackson反序列化
        List<RouteDefinition> routeDefinitions = null;

        try {
            routeDefinitions = JsonUtils.toObj(configStr, new TypeReference<List<RouteDefinition>>(){});
        } catch (Exception e) {
            log.error("get route definition from nacos string error", e);
        }

        // 如果等于null，表示反序列化失败，立即返回
        if (null==routeDefinitions) {
            return;
        }

        // 清理掉当前所有路由
        clear();

        // 添加最新路由
        add(routeDefinitions);

        // 通过应用内消息的方式发布
        publish();

        log.info("finish refreshAll");
    }


}
