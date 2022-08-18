package com.schilings.neiko.common.util.json;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * <p>copy from Jackson TypeReference</p>
 * <p>为了解决List<Config>这样的泛型,提取出通用类</p>
 * @author Schilings
*/
public abstract class TypeReference<T> implements Comparable<TypeReference<T>>{

    protected final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            this.type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
