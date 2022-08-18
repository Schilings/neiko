package com.schilings.neiko.samples.extend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.common.util.json.JsonToolSouce;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoQueryWrapper;
import com.schilings.neiko.samples.extend.mybatis.plus.Application;
import com.schilings.neiko.samples.extend.mybatis.plus.dto.EntirePreorders;
import com.schilings.neiko.samples.extend.mybatis.plus.dto.PreordersGasStationDto;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.GasStation;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.PodRefOutord;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.PodRefOutordProps;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.Preorders;
import com.schilings.neiko.samples.extend.mybatis.plus.mapper.PreorderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = { Application.class })
public class ApplicationTest {

	@Autowired
	protected PreorderMapper preorderMapper;

	@Test
	public void getOrderSimple() {
		List<Preorders> list = preorderMapper.selectJoinList(Preorders.class, "",
				new NeikoQueryWrapper<Preorders>().setAlias("pre").selectAll(Preorders.class)
						.leftJoin("gas_station gs ON pre.gas_station_id = gs.id").eq("pre.deleted", "1"));
		list.forEach(System.out::println);
	}

	@Test
	public void test() {
		List<PreordersGasStationDto> list = preorderMapper.selectJoinList(PreordersGasStationDto.class, "",
				WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class).select(GasStation::getBrandCode)
						.leftJoin(GasStation.class, GasStation::getId, Preorders::getGasStationId)
						.eqIfPresent(Preorders::getDeleted, "1"));
		list.forEach(System.out::println);
	}

	@Test
	public void test2() {
		List<Preorders> list1 = preorderMapper.selectJoinList(Preorders.class, "", // EntirePreorders
				WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class)
						.select(PodRefOutord::getOutOrderType, PodRefOutord::getOutOrderCode)
						// .selectAs(PodRefOutord::getPodRefId,"podRefId_PRO")
						// .selectAs(PodRefOutord::getPreorderId, "preorderId_PRO")
						// .selectAs(PodRefOutord::getStatus,"status_PRO")
						.selectAs(PodRefOutord::getPodRefId, EntirePreorders::getPodRefId_PRO)
						.selectAs(PodRefOutord::getPreorderId, EntirePreorders::getPreorderId_PRO)
						.selectAs(PodRefOutord::getStatus, EntirePreorders::getStatus_PRO)
						.selectAll(PodRefOutordProps.class)
						.leftJoin(PodRefOutord.class, PodRefOutord::getPreorderId, Preorders::getPreorderId)
						.leftJoin(PodRefOutordProps.class, PodRefOutordProps::getPodRefId, PodRefOutord::getPodRefId)
						.eqIfPresent(Preorders::getDeleted, ""));

		List<EntirePreorders> list2 = preorderMapper.selectJoinList(EntirePreorders.class, "EntirePreorders",
				WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class)
						.select(PodRefOutord::getOutOrderType, PodRefOutord::getOutOrderCode)
						// .selectAs(PodRefOutord::getPodRefId,"podRefId_PRO")
						// .selectAs(PodRefOutord::getPreorderId, "preorderId_PRO")
						// .selectAs(PodRefOutord::getStatus,"status_PRO")
						.selectAs(PodRefOutord::getPodRefId, EntirePreorders::getPodRefId_PRO)
						.selectAs(PodRefOutord::getPreorderId, EntirePreorders::getPreorderId_PRO)
						.selectAs(PodRefOutord::getStatus, EntirePreorders::getStatus_PRO)
						.selectAll(PodRefOutordProps.class)
						.leftJoin(PodRefOutord.class, PodRefOutord::getPreorderId, Preorders::getPreorderId)
						.leftJoin(PodRefOutordProps.class, PodRefOutordProps::getPodRefId, PodRefOutord::getPodRefId)
						.eqIfPresent(Preorders::getDeleted, ""));

		System.out.println(list1.toString() + list2.toString());

	}

	@Test
	public void test3() {

		Page<EntirePreorders> page = new Page<>(1, 5);
		IPage<EntirePreorders> list2 = preorderMapper.selectJoinPage(page, EntirePreorders.class, "EntirePreorders",
				WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class)
						.select(PodRefOutord::getOutOrderType, PodRefOutord::getOutOrderCode)
						.selectAs(PodRefOutord::getPodRefId, EntirePreorders::getPodRefId_PRO)
						.selectAs(PodRefOutord::getPreorderId, EntirePreorders::getPreorderId_PRO)
						.selectAs(PodRefOutord::getStatus, EntirePreorders::getStatus_PRO)
						.selectAll(PodRefOutordProps.class)
						.leftJoin(PodRefOutord.class, PodRefOutord::getPreorderId, Preorders::getPreorderId)
						.leftJoin(PodRefOutordProps.class, PodRefOutordProps::getPodRefId, PodRefOutord::getPodRefId)
						.eqIfPresent(Preorders::getDeleted, "0")

		);

		System.out.println(list2.getRecords());
		System.out.println(list2.getCurrent());
		System.out.println(list2.getSize());
		System.out.println(list2.getPages());
		System.out.println(list2.getTotal());

	}

	@Test
	public void test4() {
		List<Map<String, Object>> maps = preorderMapper.selectJoinMapsList(WrappersX.<Preorders>lambdaQueryJoin()
				.selectAll(Preorders.class).select(PodRefOutord::getOutOrderType, PodRefOutord::getOutOrderCode)
				.selectAs(PodRefOutord::getPodRefId, EntirePreorders::getPodRefId_PRO)
				.selectAs(PodRefOutord::getPreorderId, EntirePreorders::getPreorderId_PRO)
				.selectAs(PodRefOutord::getStatus, EntirePreorders::getStatus_PRO).selectAll(PodRefOutordProps.class)
				.leftJoin(PodRefOutord.class, PodRefOutord::getPreorderId, Preorders::getPreorderId)
				.leftJoin(PodRefOutordProps.class, PodRefOutordProps::getPodRefId, PodRefOutord::getPodRefId)
				.eqIfPresent(Preorders::getDeleted, "0"));
		System.out.println(maps);

	}

	@Test
	public void test5() {

		Page<EntirePreorders> page = new Page<>(1, 5);
		IPage<Map<String, Object>> iPage = preorderMapper.selectJoinMapsPage(page,
				WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class)
						.select(PodRefOutord::getOutOrderType, PodRefOutord::getOutOrderCode)
						.selectAs(PodRefOutord::getPodRefId, EntirePreorders::getPodRefId_PRO)
						.selectAs(PodRefOutord::getPreorderId, EntirePreorders::getPreorderId_PRO)
						.selectAs(PodRefOutord::getStatus, EntirePreorders::getStatus_PRO)
						.selectAll(PodRefOutordProps.class)
						.leftJoin(PodRefOutord.class, PodRefOutord::getPreorderId, Preorders::getPreorderId)
						.leftJoin(PodRefOutordProps.class, PodRefOutordProps::getPodRefId, PodRefOutord::getPodRefId)
						.eqIfPresent(Preorders::getDeleted, "0").likeIfPresent(Preorders::getDriverName, "司机"));

		System.out.println(iPage.getRecords());
		System.out.println(iPage.getCurrent());
		System.out.println(iPage.getSize());
		System.out.println(iPage.getPages());
		System.out.println(iPage.getTotal());

	}

	@Test
	public void test6() {
		Page<PreordersGasStationDto> page = new Page<>(1, 20);
		IPage<PreordersGasStationDto> iPage = preorderMapper.selectJoinPage(page, PreordersGasStationDto.class, "",
				WrappersX.lambdaQueryJoin().selectAll(Preorders.class).select(GasStation::getBrandCode)
						.selectAs(GasStation::getName, PreordersGasStationDto::getDriverName)
						.leftJoin(GasStation.class, GasStation::getId, Preorders::getGasStationId)
						.eqIfPresent(Preorders::getDeleted, "0").likeIfPresent(GasStation::getName, "hel")

		);
		List<PreordersGasStationDto> list = iPage.getRecords();
		list.forEach(System.out::println);
	}

	@Test
	public void test7() {
		ArrayList<Preorders> list = new ArrayList<>();
		list.add(new Preorders());
		list.add(new Preorders());
		list.add(new Preorders());
		list.add(new Preorders());
		preorderMapper.insertBatchSomeColumn(list);
	}

}
