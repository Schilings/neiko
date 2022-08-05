package com.schilings.neiko.extend.mabatis.plus.method.insert;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <pre>
 * <p>自定义批量插入方法的基类 {@link InsertBatchSomeColumn}</p>
 * </pre>
 * @author Schilings
*/
public abstract class BaseInsertBatch extends AbstractMethod {

    public static final String PARAMETER_NAME = "collection";

    public static final String KEY_PROPERTY_PREFIX = "collection.";

    /**
     * 方法名
     * @param methodName
     */
    public BaseInsertBatch(String methodName) {
        super(methodName);
    }

    /**
     * <p>{@link Insert#injectMappedStatement(Class, Class, TableInfo)}</p>
     * <p>{@link InsertBatchSomeColumn#injectMappedStatement(Class, Class, TableInfo))}</p>
     * @param mapperClass
     * @param modelClass
     * @param tableInfo
     * @return
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        
        // === mybatis 主键逻辑处理：主键生成策略，以及主键回填=======
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        //主键字段分析
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = getKeyProperty(tableInfo);
                keyColumn = tableInfo.getKeyColumn();
            } else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                keyProperty = getKeyProperty(tableInfo);
                keyColumn = tableInfo.getKeyColumn();
            }
        }
        //提取sql
        SqlSource sqlSource = languageDriver.createSqlSource(configuration,analyzedSql(tableInfo), modelClass);
        //添加sql statement
        return this.addInsertMappedStatement(mapperClass, modelClass,this.methodName, sqlSource, keyGenerator, keyProperty, keyColumn);
    }


    /**
     * 主键property前缀
     * @return
     */
    public String getKeyPropertyPrefix() {
        return KEY_PROPERTY_PREFIX;
    }

    /**
     * 主键字段
     * @param tableInfo
     * @return
     */
    protected String getKeyProperty(TableInfo tableInfo) {
        return getKeyPropertyPrefix() + tableInfo.getKeyProperty();
    }


    /**
     * 确定的sql
     * @param tableInfo
     * @return
     */
    protected String analyzedSql(TableInfo tableInfo) {
        //<script>INSERT INTO %s %s VALUES %<script>
        /**
         * <script>
         *     INSERT INTO table field1,field2
         *     VALUES 
         *     
         *     <foreach collection="collection" item="item" index="index" open="(" sparator="),(" close=")>">
         *          #{item.field1},#{item.field2}
         *     </foreach>
         * </script>
         */
        return String.format(sql(), tableInfo.getTableName(), prepareFieldSql(tableInfo), prepareValuesSqlForMysqlBatch(tableInfo));
    }
    
    /**
     * sql脚本
     * @return
     */
    protected abstract String sql();


    /**
     * 插入的表字段 field1,filed2,field3....
     * @param tableInfo
     * @return
     */
    protected String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        fieldSql.append(tableInfo.getKeyColumn()).append(COMMA);
        tableInfo.getFieldList().forEach(x -> fieldSql.append(x.getColumn()).append(COMMA));
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, LEFT_BRACKET);
        fieldSql.append(RIGHT_BRACKET);
        return fieldSql.toString();
    }


    /**
     * 插入值,默认只有foreach
     * @param tableInfo
     * @return
     */
    protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
        return prepareValuesBuildSqlForMysqlBatch(tableInfo).toString();
    }
    
    
    protected StringBuilder prepareValuesBuildSqlForMysqlBatch(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append(
                "<foreach collection=\"collection\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(field -> valueSql.append("#{item.").append(field.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql;
    }

    
    
    
}
