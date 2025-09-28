package com.stockup.StockUp.Manager.audit;

import com.stockup.StockUp.Manager.util.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
	private static final Logger auditLogger = LoggerFactory.getLogger("audit");
	
	public static void log(String evento, String usuario, String status, String detalhes) {
		String logEntry = String.format(
			"time=%s | event=%s | user=%s | status=%s | details=%s | ip=%s",
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			evento, usuario, status, detalhes, WebClient.getClientIp()
		);
		
		auditLogger.info(logEntry);
	}
}