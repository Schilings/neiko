package com.schilings.neiko.extend.mabatis.plus.wrapper.join;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.schilings.neiko.extend.mabatis.plus.constants.Constant;

/**
 * <pre>
 * <p>{@link Query}</p>
 * </pre>
 * @author Schilings
*/
public interface NeikoJoin<Children> extends NeikoBaseJoin{

    /**
     * LEFT JOIN
     * @param joinSql
     * @return
     */
    default Children leftJoin(String joinSql) {
        return leftJoin(true, joinSql);
    }
    default Children leftJoin(boolean condition, String joinSql) {
        return join(Constant.LEFT_JOIN, condition, joinSql);
    }

    /**
     * RIGHT JOIN
     * @param joinSql
     * @return
     */
    default Children rightJoin(String joinSql) {
        return rightJoin(true, joinSql);
    }
    default Children rightJoin(boolean condition, String joinSql) {
        return join(Constant.RIGHT_JOIN, condition, joinSql);
    }

    /**
     * INNER JOIN
     * @param joinSql
     * @return
     */
    default Children innerJoin(String joinSql) {
        return innerJoin(true, joinSql);
    }
    default Children innerJoin(boolean condition, String joinSql) {
        return join(Constant.INNER_JOIN, condition, joinSql);
    }

    /**
     * 
     * @param keyWord
     * @param condition
     * @param joinSql
     * @return
     */
    Children join(String keyWord, boolean condition, String joinSql);
}
