package com.schilings.neiko.extend.mybatis.plus.method.join;


import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.schilings.neiko.extend.mybatis.plus.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <p>{@link AbstractMethod}</p>
 * </pre>
 * @author Schilings
*/
public abstract class JoinAbstractMethod extends AbstractMethod implements JoinBaseMethod {

    /**
     * EntityWrapper方式获取select where,连表操作不考虑entity查询和逻辑删除
     *<p>参考from {@link AbstractMethod}</p>
     * @param newLine 是否提到下一行
     * @param table   表信息
     * @return String
     */
    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        return joinSqlWhereEntityWrapper(newLine, table);
    }

    /**
     * SQL 查询所有表字段
     *<p>参考from {@link AbstractMethod}</p>
     * @param table        表信息
     * @param queryWrapper 是否为使用 queryWrapper 查询
     * @return sql 脚本
     */
    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = ASTERISK;
        if (table.getResultMap() == null || (table.getResultMap() != null)) {
            /* 未设置 resultMap 或者 resultMap 是自动构建的,视为属于mp的规则范围内 */
            //获取包含主键及字段的 select sql 片段
            selectColumns = table.getAllSqlSelect();
            String[] columns = selectColumns.split(StringPool.COMMA);
            List<String> selectColumnList = new ArrayList<>();
            for (String c : columns) {
                //给字段都加上前缀"t."
                selectColumnList.add(Constant.TABLE_ALIAS + StringPool.DOT + c);
            }
            selectColumns = String.join(StringPool.COMMA, selectColumnList);
        }
        //t.field1,t.field2,....
        
        //如果不是query，则可以返回了
        if (!queryWrapper) {
            return selectColumns;
        }
        /**
         * <script>
         *     <choose>
         *         <when test="ew != null and ew.sqlSelect != null>
         *             ${ew.sqlSelect}
         *         </when>
         *         <otherwize>t.field1,t.field2,....</otherwize>
         *     </choose>
         * </script>
         */
        //return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
        //        SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), otherwise);
        return convertChooseEwSelect(selectColumns);

    }

    /**
     * SQL 查询记录行数
     *<p>参考from {@link AbstractMethod}</p>
     * @return count sql 脚本
     */
    @Override
    protected String sqlCount() {
        /**
         * <script>
         *     <choose>
         *         <when test="ew != null and ew.sqlSelect != null and ew.sqlSelect != ''">
         *              ${ew.sqlSelect}
         *         </when>
         *         <otherwise>*</otherwise>
         *     </choose>
         * </script>
         */
        //相对于MP，多了ew.sqlSelect != ''
        return SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null and %s != ''", WRAPPER, Q_WRAPPER_SQL_SELECT, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), ASTERISK);
    }


    /**
     * SQL Alias
     * @return
     */
    protected String sqlAlias() {
        /**
         * <script>
         *     <if test="ew.alias != null and ew.alias != ''">${ew.alias}</if>
         * </script>
         */
        return SqlScriptUtils.convertIf("${ew.alias}", String.format("%s != null and %s != ''", "ew.alias", "ew.alias"), false);
    }

    /**
     * SQL From
     * @return
     */
    protected String sqlFrom() {
        /**
         * <script>
         *     <if test="ew.from != null and ew.from != ''">${ew.from}</if>
         * </script>
         */
        return SqlScriptUtils.convertIf("${ew.from}", String.format("%s != null and %s != ''", "ew.from", "ew.from"), false);
    }

    /**
     * SQL DISTINCT
     * @return
     */
    protected String sqlDistinct() {
        /**
         * <script>
         *     <if test="ew.selectDistinct">DISTINCT</if>
         * </script>
         */
        return SqlScriptUtils.convertIf("DISTINCT", "ew.selectDistinct", false);
    }

}
