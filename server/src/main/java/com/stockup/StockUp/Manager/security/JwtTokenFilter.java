package com.stockup.StockUp.Manager.security;

import com.stockup.StockUp.Manager.model.User;
import com.stockup.StockUp.Manager.repository.UserRepository;
import com.stockup.StockUp.Manager.security.jwt.JwtTokenProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
	
	@Autowired
	private final JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
		throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String token = tokenProvider.resolveToken(httpRequest);
		
		if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
			Authentication auth = tokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			User user = userRepository.findByUsername(auth.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			
			if (user.getLastActivity() != null &&
				user.getLastActivity().isBefore(LocalDateTime.now().minusMinutes(30))) {
				SecurityContextHolder.clearContext();
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired due to inactivity");
				return; // encerra o filtro
			}
			
			if (user.getLastActivity() == null ||
				user.getLastActivity().isBefore(LocalDateTime.now().minusMinutes(5))) {
				user.setLastActivity(LocalDateTime.now());
				userRepository.save(user);
			}
		}
		
		filter.doFilter(request, response);
	}
}