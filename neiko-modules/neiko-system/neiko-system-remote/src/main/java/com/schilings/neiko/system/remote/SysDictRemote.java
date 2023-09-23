package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.entity.SysDict;
import com.schilings.neiko.system.model.entity.SysDictItem;
import com.schilings.neiko.system.model.qo.SysDictQO;
import com.schilings.neiko.system.model.vo.DictDataVO;
import com.schilings.neiko.system.model.vo.SysDictItemPageVO;
import com.schilings.neiko.system.model.vo.SysDictPageVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 * 字典表
 * </p>
 *
 * @author Schilings
 */

@HttpExchange("/system/dict")
public interface SysDictRemote {

	/**
	 * 通过字典标识查找对应字典项
	 * @param dictCodes 字典标识列表
	 * @return 同类型字典
	 */
	@GetExchange("/data")
	R<List<DictDataVO>> getDictData(@RequestParam("dictCodes") String[] dictCodes);

	/**
	 * 通过字典标识查找对应字典项
	 * @param dictHashCode 字典标识
	 * @return 同类型字典
	 */
	@PostExchange("/invalidHash")
	R<List<String>> invalidDictHash(@RequestBody Map<String, String> dictHashCode);

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysDictQO 字典查询参数
	 * @return R<PageResult<SysDictVO>>
	 */
	@GetExchange("/page")
	R<PageResult<SysDictPageVO>> getSysDictPage(PageParam pageParam, @RequestParameterObject SysDictQO sysDictQO);

	/**
	 * 新增字典表
	 * @param sysDict 字典表
	 * @return R
	 */
	@PostExchange
	R<Void> save(@RequestBody SysDict sysDict);

	/**
	 * 修改字典表
	 * @param sysDict 字典表
	 * @return R
	 */
	@PutExchange
	R<Void> updateById(@RequestBody SysDict sysDict);

	/**
	 * 通过id删除字典表
	 * @param id id
	 * @return R
	 */
	@DeleteExchange("/{id}")
	R<Void> removeById(@PathVariable("id") Long id);

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param dictCode 字典标识
	 * @return R
	 */
	@GetExchange("/item/page")
	R<PageResult<SysDictItemPageVO>> getSysDictItemPage(PageParam pageParam, @RequestParam("dictCode") String dictCode);

	/**
	 * 新增字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@PostExchange("item")
	R<Void> saveItem(@RequestBody SysDictItem sysDictItem);

	/**
	 * 修改字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@PutExchange("item")
	R<Void> updateItemById(@RequestBody SysDictItem sysDictItem);

	/**
	 * 通过id删除字典项
	 * @param id id
	 * @return R
	 */
	@DeleteExchange("/item/{id}")
	R<Void> removeItemById(@PathVariable("id") Integer id);

	/**
	 * 通过id修改字典项状态
	 * @param id id
	 * @return R
	 */
	@PatchMapping("/item/{id}")
	R<Void> updateDictItemStatusById(@PathVariable("id") Long id, @RequestParam("status") Integer status);

}
