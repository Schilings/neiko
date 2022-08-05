package com.schilings.neiko.samples.extend.mybatis.plus.mapper;

import com.schilings.neiko.extend.mabatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.Preorders;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PreorderMapper extends ExtendMapper<Preorders> {
    
}
