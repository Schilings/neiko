package com.schilings.neiko.common.cache.selector;



import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.configuration.NeikoCachingConfiguration;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

import java.util.function.Predicate;


/**
 * <pre>
 * <p>注解驱动，注入相关配置类</p>
 * <p>{@link CachingConfigurationSelector}</p>
 * </pre>
 * @author Schilings
*/
public class NeikoCachingConfigurationSelector extends AdviceModeImportSelector<EnableNeikoCaching> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{NeikoCachingConfiguration.class.getName()};
    }
    
    @Override
    public Predicate<String> getExclusionFilter() {
        return super.getExclusionFilter();
    }
}
