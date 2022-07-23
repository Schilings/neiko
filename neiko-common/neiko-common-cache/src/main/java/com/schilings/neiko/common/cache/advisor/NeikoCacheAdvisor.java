package com.schilings.neiko.common.cache.advisor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;



/**
 * https://blog.csdn.net/f641385712/article/details/89303088?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-1-89303088-blog-97003179.pc_relevant_multi_platform_whitelistv2_ad_hc&spm=1001.2101.3001.4242.2&utm_relevant_index=4
 */

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class NeikoCacheAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    
    private final NeikoCachePointcut pointcut = new NeikoCachePointcut();
    
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    
    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }
}
