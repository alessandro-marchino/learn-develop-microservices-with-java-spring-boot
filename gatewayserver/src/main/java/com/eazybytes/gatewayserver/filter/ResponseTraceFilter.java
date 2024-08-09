package com.eazybytes.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ResponseTraceFilter {
	private final FilterUtility filterUtility;
	
	@Bean
	GlobalFilter postGlobalFilter() {
		return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
			HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
			String correlationId = filterUtility.getCorrelationId(requestHeaders);
			log.debug("Updated the correlation id to the response headers: {}",correlationId);
			exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
		}));
	}
}
