package com.schilings.neiko.extend.mybatis.plus.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;

import java.util.List;

public abstract class PageUtil {

	private PageUtil() {
	}

	/**
	 * 根据 PageParam 生成一个 IPage 实例
	 * @param pageParam 分页参数
	 * @param <V> 返回的 Record 对象
	 * @return IPage<V>
	 */
	public static <V> IPage<V> prodPage(PageParam pageParam) {
		Page<V> page = new Page<>(pageParam.getPage(), pageParam.getSize());
		List<PageParam.Sort> sorts = pageParam.getSort();
		for (PageParam.Sort sort : sorts) {
			OrderItem orderItem = sort.isAsc() ? OrderItem.asc(sort.getField()) : OrderItem.desc(sort.getField());
			page.addOrder(orderItem);
		}
		return page;
	}

	/**
	 * 根据 IPage 生成一个 PageResult 实例
	 * @param iPage
	 * @return PageResult
	 */
	public static <V> PageResult<V> prodPageResult(IPage<V> iPage) {
		return new PageResult<V>().setData(iPage.getRecords()).setPage(iPage.getCurrent()).setPages(iPage.getPages())
				.setTotal(iPage.getTotal()).setSize(iPage.getSize());
	}

}
