package com.example.demo.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

/** 新增自定义度量指标
 * @author bing
 *
 */
@Component
public class ApplicationContextMetrics implements MeterBinder {

	@Autowired
	public ApplicationContext context;

	@Override
	public void bindTo(MeterRegistry registry) {
		Gauge.builder("applicationcontextbeans", context, context -> context.getBeanNamesForType(Object.class).length)
				.register(registry);

	}

}
