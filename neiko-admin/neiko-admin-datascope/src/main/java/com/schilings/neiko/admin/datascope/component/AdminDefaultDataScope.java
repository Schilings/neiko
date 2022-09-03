package com.schilings.neiko.admin.datascope.component;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.security.constant.UserAttributeNameConstants;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.*;
import java.util.stream.Collectors;

public class AdminDefaultDataScope implements DataScope {
    
    private static final String USER_ID = "user_id";

    private static final String ORGANIZATION_ID = "organization_id";

    /**
     * 拥有 organization_id 字段的表名集合
     */
    private static final Set<String> ORGANIZATION_ID_TABLE_NAMES = CollectionUtil.newHashSet("nk_sys_user");

    @Override
    public String getResource() {
        return "UserData";
    }

    @Override
    public Collection<String> getTableNames() {
        Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        tableNames.addAll(Collections.singletonList("nk_sys_user"));
        return tableNames;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        // 获取当前登录用户
        UserDetails user = RBACAuthorityHolder.getUserDetails();
        if (user == null) {
            return null;
        }

        UserDataScope userDataScope = getUserDataScope(user);

        // 如果数据权限是全部，直接放行
        if (userDataScope.isAllScope()) {
            return null;
        }

        // 如果数据权限是仅自己
        if (userDataScope.isOnlySelf()) {
            // 数据权限规则，where user_id = xx
            return userIdEqualsToExpression(tableAlias, Long.valueOf(user.getUserId()));
        }

        // 如果当前表有组织id字段，则优先使用组织id字段控制范围
        if (ORGANIZATION_ID_TABLE_NAMES.contains(tableName)) {
            // 数据权限规则，where (user_id =xx or organization_id in ("x"，"y"))
            EqualsTo equalsTo = userIdEqualsToExpression(tableAlias, Long.valueOf(user.getUserId()));
            Expression inExpression = getInExpression(tableAlias, ORGANIZATION_ID, userDataScope.getScopeDeptIds());
            // 这里一定要加括号，否则如果有其他查询条件，or 会出问题
            return new Parenthesis(new OrExpression(equalsTo, inExpression));
        }
        else {
            // 数据权限规则，where user_id in ("x"，"y")
            return getInExpression(tableAlias, USER_ID, userDataScope.getScopeUserIds());
        }
    }

    private UserDataScope getUserDataScope(UserDetails user) {
        Map<String, Object> attributes = user.getAttributes();
        Object o = attributes.get(UserAttributeNameConstants.USER_DATA_SCOPE);
        if (o instanceof UserDataScope) {
            return (UserDataScope) o;
        }
        else {
            return BeanUtil.toBean(o, UserDataScope.class);
        }
    }

    private EqualsTo userIdEqualsToExpression(Alias tableAlias, Long userId) {
        Column column = new Column(tableAlias == null ? USER_ID : tableAlias.getName() + "." + USER_ID);
        return new EqualsTo(column, new LongValue(userId));
    }

    private Expression getInExpression(Alias tableAlias, String columnName, Set<Long> scopeUserIds) {
        Column column = new Column(tableAlias == null ? columnName : tableAlias.getName() + "." + columnName);
        ExpressionList expressionList = new ExpressionList();
        List<Expression> list = scopeUserIds.stream().map(LongValue::new).collect(Collectors.toList());
        expressionList.setExpressions(list);
        return new InExpression(column, expressionList);
    }

}
