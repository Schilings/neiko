package com.schilings.neiko.samples.extend.mybatis.plus.mapper;

import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.GasStation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GasStationMapper extends ExtendMapper<GasStation> {

}
