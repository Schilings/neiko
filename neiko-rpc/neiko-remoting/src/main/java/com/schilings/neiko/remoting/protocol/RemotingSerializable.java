package com.schilings.neiko.remoting.protocol;


import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 
 * <p>可序列化，默认提供Json序列化</p>
 * 
 * @author Schilings
*/
public abstract class RemotingSerializable {
    //public final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public final static Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /* 静态方法 */
    public static byte[] encode(final Object obj) {
        final String json = toJson(obj, false);
        if (json != null) {
            return json.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }

    public static String toJson(final Object obj, boolean prettyFormat) {
        return JSON.toJSONString(obj, prettyFormat);
    }

    public static <T> T decode(final byte[] data, Class<T> classOfT) {
        final String json = new String(data, CHARSET_UTF8);
        return fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

    /* 实例方法 */
    public byte[] encode() {
        final String json = this.toJson();
        if (json != null) {
            return json.getBytes(CHARSET_UTF8);
        }
        return null;
    }

    public String toJson() {
        return toJson(false);
    }

    public String toJson(final boolean prettyFormat) {
        return toJson(this, prettyFormat);
    }


}
