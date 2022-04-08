package cn.com.kun.springframework.actuator;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决JVM信息缺失问题（这个类根本是没必要的）
 * author:xuyaokun_kzx
 * date:2022/4/8
 * desc:
*/
//@Configuration
public class PrometheusActuatorMetricsConfig {

    //方法1
//    @Bean
    InitializingBean forcePrometheusPostProcessor(BeanPostProcessor meterRegistryPostProcessor, PrometheusMeterRegistry registry) {
        //这里实际触发的是org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryPostProcessor#postProcessAfterInitialization
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }

    //方法2(不奏效)
    @Configuration
    @ConditionalOnProperty(value = "management.metrics.binders.jvm.enabled" , matchIfMissing = true)
    static class JvmMeterBindersConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public JvmGcMetrics jvmGcMetrics(){
            return new JvmGcMetrics();
        }

        @Bean
        @ConditionalOnMissingBean
        public JvmMemoryMetrics jvmMemoryMetrics(){
            return new JvmMemoryMetrics();
        }

        @Bean
        @ConditionalOnMissingBean
        public JvmThreadMetrics jvmThreadMetricss(){
            return new JvmThreadMetrics();
        }

        @Bean
        @ConditionalOnMissingBean
        public ClassLoaderMetrics classLoaderMetrics(){
            return new ClassLoaderMetrics();
        }

    }
}