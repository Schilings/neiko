package com.schilings.neiko.extend.mabatis.plus.method.insert;


import com.baomidou.mybatisplus.core.enums.SqlMethod;

/**
 * <pre>
 * <p>如果存在，则忽略当前新数据</p>
 * </pre>
 * @author Schilings
*/
public class InsertIgnoreBatch extends BaseInsertBatch{
    
    protected InsertIgnoreBatch() {
        super("insertIgnoreBatch");
    }

    /**
     * 方法名
     *
     * @param methodName
     */
    public InsertIgnoreBatch(String methodName) {
        super(methodName);
    }

    /**
     * <script>INSERT INTO %s %s VALUES %s</script>
     * @return
     */
    @Override
    protected String sql() {
        return SqlMethod.INSERT_ONE.getSql();
    }
    
}
