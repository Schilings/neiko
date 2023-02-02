package com.schilings.neiko.authorization.remote;

import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.Map;

@HttpExchange(url = "/authorization/registeredClient")
public interface OAuth2RegisteredClientRemote {

    @GetExchange("/registeredClientPage")
    R<PageResult<OAuth2RegisteredClientPageVO>> getRegisteredClientPage(@RequestParam(name = "map") Map<String, Object> map);
    
    
    /**
     * 分页查询
     */
    @GetExchange("/registeredClientPage")
    R<PageResult<OAuth2RegisteredClientPageVO>> getRegisteredClientPage(PageParam pageParam, OAuth2RegisteredClientQO qo);

    /**
     * 获取指定的客户端基本信息
     */
    @GetExchange("/{id}")
    R<OAuth2RegisteredClientInfo> getClientInfo(@PathVariable("id") Long id);

    /**
     * 新增客户端基本信息
     */
    @PostExchange
    R<Void> addClient(@RequestBody OAuth2RegisteredClientDTO dto);

    /**
     * 新增客户端基本信息
     */
    @PutExchange
    R<Void> updateClient(@RequestBody OAuth2RegisteredClientDTO dto);

    /**
     * 删除指定的客户端基本信息
     */
    @DeleteExchange("/{id}")
    R<Void> deleteClientInfo(@PathVariable("id") Long id);


}
