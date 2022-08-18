package com.schilings.neiko.extend.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.schilings.neiko.extend.mybatis.plus.method.insert.InsertBatchSomeColumnByCollection;
import com.schilings.neiko.extend.mybatis.plus.method.join.*;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <p>拓展MP的SqlInjector,用来注入自定义方法</p>
 * </pre>
 *
 * @author Schilings
 */

@Order(Integer.MIN_VALUE)
public class ExtendSqlInjector extends DefaultSqlInjector {

	private List<JoinAbstractMethod> getJoinMethods() {
		ArrayList<JoinAbstractMethod> list = new ArrayList<>();
		list.add(new SelectJoinList());
		list.add(new SelectJoinPage());
		list.add(new SelectJoinMapsList());
		list.add(new SelectJoinMapsPage());
		return list;
	}

	private List<AbstractMethod> getInsertMethods() {
		ArrayList<AbstractMethod> list = new ArrayList<>();
		list.add(new InsertBatchSomeColumnByCollection());
		return list;
	}

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
		methodList.addAll(getJoinMethods());
		methodList.addAll(getInsertMethods());
		return methodList;
	}

}
