package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.config.AutoConfiguration;
import com.antzuhl.zeus.config.InterceptorConfig;
import com.antzuhl.zeus.config.ServerConfig;
import com.antzuhl.zeus.limit.RateLimitFilter;
import com.antzuhl.zeus.properties.PropertySourcesPlaceholderConfigurerBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({AutoConfiguration.class,
        RateLimitFilter.class,
        ServerConfig.class,
        AnnotationImport.class,
        InterceptorConfig.class})
@Documented
public @interface RateLimit {
    int num() default Integer.MAX_VALUE;
}
