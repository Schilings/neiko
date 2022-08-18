package com.schilings.neiko.common.event.handler;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class EventHandlerCachHolder {

    private static Map<Object, List<EventHandler>> CACHE = new ConcurrentHashMap<>();
    
    public static List<EventHandler> get(Object o) {
        return CACHE.get(o);
    }

    public static void put(Object o, List<EventHandler> list) {
        CACHE.put(o, list);
    }
    
    
}
