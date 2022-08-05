package com.schilings.neiko.extend.mabatis.plus.injector;


import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.schilings.neiko.extend.mabatis.plus.method.insert.InsertBatchSomeColumnByCollection;
import com.schilings.neiko.extend.mabatis.plus.method.join.SelectJoinList;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <p>拓展MP的SqlInjector,用来注入自定义方法</p>
 * </pre>
 * @author Schilings
*/

@Order(Integer.MIN_VALUE)
@ConditionalOnMissingBean({DefaultSqlInjector.class, AbstractSqlInjector.class, ISqlInjector.class})
public class ExtendSqlInjector extends DefaultSqlInjector {
    

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.addAll(getJoinMethod());
        methodList.add(new InsertBatchSomeColumnByCollection());
        return methodList;
    }

    private List<AbstractMethod> getJoinMethod() {
        List<AbstractMethod> list = new ArrayList<>();
        list.add(new SelectJoinList());
        return list;
    }
}
