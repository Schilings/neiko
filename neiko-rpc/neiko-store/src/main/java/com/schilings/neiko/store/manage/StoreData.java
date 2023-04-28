package com.schilings.neiko.store.manage;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class StoreData implements Serializable {

    private static final long serialVersionUID = 8445773977080406428L;

    private byte[] body;

    public StoreData(byte[] body) {
        this.body = body;
    }

    private Map<String, String> properties;

    void clearProperty(final String name) {
        if (null != this.properties) {
            this.properties.remove(name);
        }
    }

    void putProperty(final String name, final String value) {
        if (null == this.properties) {
            this.properties = new HashMap<String, String>();
        }

        this.properties.put(name, value);
    }

    public String getProperty(final String name) {
        if (null == this.properties) {
            this.properties = new HashMap<String, String>();
        }

        return this.properties.get(name);
    }

    public void putUserProperty(final String name, final String value) {
        if (DataConst.STRING_HASH_SET.contains(name)) {
            throw new RuntimeException(String.format(
                    "The Property<%s> is used by system, input another please", name));
        }

        if (value == null || value.trim().isEmpty()
                || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "The name or value of property can not be null or blank string!"
            );
        }

        this.putProperty(name, value);
    }

    public String getUserProperty(final String name) {
        return this.getProperty(name);
    }

    public void setKeys(String keys) {
        this.putProperty(DataConst.PROPERTY_KEYS, keys);
    }
    public String getKeys() {
        return this.getProperty(DataConst.PROPERTY_KEYS);
    }

    public void setKeys(Collection<String> keys) {
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            sb.append(k);
            sb.append(DataConst.KEY_SEPARATOR);
        }

        this.setKeys(sb.toString().trim());
    }

    public int getDelayTimeLevel() {
        String t = this.getProperty(DataConst.PROPERTY_DELAY_TIME_LEVEL);
        if (t != null) {
            return Integer.parseInt(t);
        }

        return 0;
    }

    public void setDelayTimeLevel(int level) {
        this.putProperty(DataConst.PROPERTY_DELAY_TIME_LEVEL, String.valueOf(level));
    }

    public boolean isWaitStoreMsgOK() {
        String result = this.getProperty(DataConst.PROPERTY_WAIT_STORE_MSG_OK);
        if (null == result) {
            return true;
        }

        return Boolean.parseBoolean(result);
    }

    public void setWaitStoreMsgOK(boolean waitStoreMsgOK) {
        this.putProperty(DataConst.PROPERTY_WAIT_STORE_MSG_OK, Boolean.toString(waitStoreMsgOK));
    }


}
