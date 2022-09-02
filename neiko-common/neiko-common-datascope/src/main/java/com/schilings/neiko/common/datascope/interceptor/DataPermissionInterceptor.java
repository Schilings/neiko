package com.schilings.neiko.common.datascope.interceptor;


import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.datascope.core.DataPermissionHandler;
import com.schilings.neiko.common.datascope.holder.DataScopeMatchNumHolder;
import com.schilings.neiko.common.datascope.holder.MappedStatementIdsWithoutDataScope;
import com.schilings.neiko.common.datascope.sql.DataScopeSqlProcessor;
import com.schilings.neiko.common.datascope.util.PluginUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;

/**
 * 
 * <p>数据权限拦截器</p>
 * <p>{@link com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor}</p>
 * <p>在Mybatis层面对Sql进行拦截，数据权限控制</p> 
 * @author Schilings
*/
@RequiredArgsConstructor
@Intercepts({
        //在这里拦截的是StatementHandler的Statement prepare(Connection connection, Integer transactionTimeout);
        //Mybatis Plus的TenantLineInnerInterceptor也是在这里入手，
        //见com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor.beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout)
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataPermissionInterceptor implements Interceptor {

    private final DataScopeSqlProcessor dataScopeSqlProcessor;

    private final DataPermissionHandler dataPermissionHandler;
    
    //我们这里肯定要配合用户权限去做过滤的，但是方法参数中没办法传递参数，这里我们借助ThreadLocal去实现，注意使用完毕后要调用clear。
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        StatementHandler sh = (StatementHandler) target;
        //Copy From TenantLineInnerInterceptor
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();//Sql类型 INSERT UPDATE DELETE SELECT..
        PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();//实际 SQL 字符串
        String mappedStatementId = ms.getId();

        // 获取当前需要控制的 dataScope 集合
        List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(mappedStatementId);
        if (dataScopes == null || dataScopes.isEmpty()) {
            return invocation.proceed();
        }

        // 根据用户权限判断是否需要拦截，例如管理员可以查看所有，则直接放行
        if (dataPermissionHandler.ignorePermissionControl(dataScopes, mappedStatementId)) {
            return invocation.proceed();
        }

        // 创建 matchNumTreadLocal
        DataScopeMatchNumHolder.initMatchNum();
        try {
            //注意，这里以dataScope 集合为参数，传入到方法中，使其在Sql处理中发挥作用
            // 根据 DataScopes 进行数据权限的 sql 处理
            if (sct == SqlCommandType.SELECT) {
                mpBs.sql(dataScopeSqlProcessor.parserSingle(mpBs.sql(), dataScopes));
            }
            else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
                mpBs.sql(dataScopeSqlProcessor.parserMulti(mpBs.sql(), dataScopes));
            }
            // 如果解析后发现当前 mappedStatementId 对应的 sql，没有任何数据权限匹配，则记录下来，后续可以直接跳过不解析
            Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
            if (matchNum != null && matchNum == 0) {
                MappedStatementIdsWithoutDataScope.addToWithoutSet(dataScopes, mappedStatementId);
            }
        }
        finally {
            DataScopeMatchNumHolder.removeIfEmpty();
        }

        // 执行 sql
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
