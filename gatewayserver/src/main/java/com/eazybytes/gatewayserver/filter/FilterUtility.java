package com.eazybytes.gatewayserver.filter;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtility {
	
	public static final String CORRELATION_ID = "eazybank-correlation-id";

	public String getCorrelationId(HttpHeaders requestHeaders) {
		List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
		if(requestHeaderList == null) {
			return null;
		}
		return requestHeaderList.getFirst();
	}
	
	public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
		return setRequestHeader(exchange, CORRELATION_ID, correlationId);
	}

	public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
		return exchange.mutate()
				.request(exchange.getRequest().mutate().header(name, value).build())
				.build();
	}

}
