package com.stockup.StockUp.Manager.security;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		String path = request.getRequestURI();
		
		if (path.startsWith("/auth/login") || path.startsWith("/auth/refresh") || path.contains("/error")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		AtomicBoolean isActive = new AtomicBoolean(true);
		
		try {
			String token = tokenProvider.resolveToken(request);
			logger.debug("JwtTokenFilter - Path: {} - Token presente: {}", path, token != null);
			
			if (token != null && tokenProvider.validateToken(token)) {
				Authentication auth = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				if (auth != null) {
					AuditLogger.log("ACCESS", auth.getName(), "SUCCESS", path);
					logger.info("Authentication set for user {} with roles {}", auth.getName(), auth.getAuthorities());
					
					userRepository.findByUsername(auth.getName()).ifPresent(user -> {
						LocalDateTime now = LocalDateTime.now();
						LocalDateTime lastAct = user.getLastActivity();
						
						if (lastAct != null && lastAct.isBefore(now.minusMinutes(30))) {
							SecurityContextHolder.clearContext();
							isActive.set(false);
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json");
							try {
								response.getWriter().write("{\"error\": \"Sessão expirada por inatividade\"}");
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							logger.warn("User {} session expired due to inactivity (última: {})", user.getUsername(), lastAct);
						} else {
							if (lastAct == null || lastAct.isBefore(now.minusMinutes(5))) {
								user.setLastActivity(now);
								userRepository.save(user);
								logger.debug("Updated lastActivity for user {}", user.getUsername());
							}
						}
					});
				}
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e.getMessage(), e);
			SecurityContextHolder.clearContext();
		}
		
		if (isActive.get()) {
			filterChain.doFilter(request, response);
		}
	}
}