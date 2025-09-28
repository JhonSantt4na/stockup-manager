package com.stockup.StockUp.Manager.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebClient {
	
	public static String getClientIp() {
		try {
			HttpServletRequest request = getCurrentRequest();
			if (request == null) return "unknown";
			
			String ip = request.getHeader("X-Forwarded-For");
			if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
				return ip.split(",")[0].trim(); // Pega primeiro IP da lista
			}
			
			return request.getRemoteAddr();
			
		} catch (Exception e) {
			return "unknown";
		}
	}
	
	public static String getCurrentUser() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated() &&
				!(authentication instanceof AnonymousAuthenticationToken)) {
				return authentication.getName();
			}
			return "system";
		} catch (Exception e) {
			return "unknown";
		}
	}
	
	private static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)
			RequestContextHolder.getRequestAttributes();
		return attributes != null ? attributes.getRequest() : null;
	}
}