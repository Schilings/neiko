package com.schilings.neiko.samples.extend;


import com.schilings.neiko.extend.mabatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mabatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.extend.mabatis.plus.wrapper.join.NeikoQueryWrapper;
import com.schilings.neiko.samples.extend.mybatis.plus.Application;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.GasStation;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.Preorders;
import com.schilings.neiko.samples.extend.mybatis.plus.mapper.PreorderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {Application.class})
public class ApplicationTest {

    @Autowired
    protected PreorderMapper preorderMapper;

    @Test
    public void getOrderSimple() {
        List<Preorders> list = preorderMapper.selectJoinList(Preorders.class,
                new NeikoQueryWrapper<Preorders>().setAlias("pre")
                        .selectAll(Preorders.class)
                        .leftJoin("gas_station gs ON pre.gas_station_id = gs.id")
                        .eq("pre.deleted", "1")
        );
        list.forEach(System.out::println);
    }

    @Test
    public void test() {
        List<Preorders> list = preorderMapper.selectJoinList(Preorders.class,
                WrappersX.<Preorders>lambdaQueryJoin().selectAll(Preorders.class)
                        .leftJoin(GasStation.class, GasStation::getId, Preorders::getGasStationId)
                        .eq(Preorders::getDeleted, "1")
        );
        list.forEach(System.out::println);
    }

}
