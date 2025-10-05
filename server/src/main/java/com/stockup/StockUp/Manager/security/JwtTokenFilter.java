package com.stockup.StockUp.Manager.security;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
@Component
public class JwtTokenFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	@Autowired
	private final JwtTokenProvider tokenProvider;
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final UserService service;
	
	public JwtTokenFilter(JwtTokenProvider tokenProvider, UserRepository userRepository, UserService service) {
		this.tokenProvider = tokenProvider;
		this.userRepository = userRepository;
		this.service = service;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String path = httpRequest.getRequestURI();
		if (path.startsWith("/auth/login") || path.startsWith("/auth/refresh")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String token = tokenProvider.resolveToken(httpRequest);
			logger.debug("JwtTokenFilter - Path: {} - Token: {}", httpRequest.getRequestURI(), token);
			if (token != null && tokenProvider.validateToken(token)) {
				Authentication auth = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				if (auth != null) {
					AuditLogger.log("ACCESS", auth.getName(), "SUCCESS", httpRequest.getRequestURI());
					logger.info("Authentication set for user {} with roles {}", auth.getName(), auth.getAuthorities());
					
					userRepository.findByUsername(auth.getName()).ifPresent(user -> {
						LocalDateTime now = LocalDateTime.now();
						
						// Etapa 4: Expiração por inatividade (30 minutos)
						if (user.getLastActivity() != null &&
							user.getLastActivity().isBefore(now.minusMinutes(30))) {
							SecurityContextHolder.clearContext();
							try {
								httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
									"Session expired due to inactivity");
							} catch (IOException e) {
								logger.error("Error sending unauthorized response", e);
							}
							return; // interrompe o filtro para usuários inativos
						}
						
						// Etapa 5: Atualiza lastActivity se não expirou
						if (user.getLastActivity() == null ||
							user.getLastActivity().isBefore(now.minusMinutes(5))) {
							user.setLastActivity(now);
							userRepository.save(user);
							logger.debug("Updated lastActivity for user {}", user.getUsername());
						}
					});
				}
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}");
			SecurityContextHolder.clearContext();
		}
		
		filterChain.doFilter(request, response);
	}
	
}

//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
//		throws IOException, ServletException {
//
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//		String token = tokenProvider.resolveToken(httpRequest);
//		String path = httpRequest.getRequestURI();
//
//		System.out.println("JwtTokenFilter - Path: " + httpRequest.getRequestURI() + " - Token: " + token);
//
//		if (path.startsWith("/auth/login") || path.startsWith("/auth/refresh")) {
//			filter.doFilter(request, response);
//			return;
//		}
//
//		if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
//			Authentication auth = tokenProvider.getAuthentication(token);
//			SecurityContextHolder.getContext().setAuthentication(auth);
//
//			if (auth != null) {
//				System.out.println("Authentication set for: " + auth.getName() + " with roles: " + auth.getAuthorities());
//			}
//
//			User user = userRepository.findByUsername(auth.getName())
//				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//			if (user.getLastActivity() != null &&
//				user.getLastActivity().isBefore(LocalDateTime.now().minusMinutes(30))) {
//				SecurityContextHolder.clearContext();
//				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired due to inactivity");
//				return;
//			}
//
//			if (user.getLastActivity() == null ||
//				user.getLastActivity().isBefore(LocalDateTime.now().minusMinutes(5))) {
//				user.setLastActivity(LocalDateTime.now());
//				userRepository.save(user);
//			}
//
//			log.debug("Authenticated user: {}, Authorities: {}", auth.getName(), auth.getAuthorities());
//		}
//
//		filter.doFilter(request, response);
//	}
//}