package com.stockup.StockUp.Manager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.exception.InvalidJwtAuthenticationException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class JwtTokenProvider {
	
	private final UserDetailsService userDetailsService;
	private final String issuer;
	private final long accessTokenValidity;
	private final long refreshTokenValidity;
	private final Algorithm algorithm;
	
	@Autowired
	public JwtTokenProvider(
		UserDetailsService userDetailsService,
		@Value("${security.jwt.token.secret-key}") String secretKey,
		@Value("${security.jwt.token.issuer:stockup}") String issuer,
		@Value("${security.jwt.token.access-expire-length:3600000}") long accessTokenValidity,
		@Value("${security.jwt.token.refresh-expire-length:2592000000}") long refreshTokenValidity) {
		
		this.userDetailsService = userDetailsService;
		this.issuer = issuer;
		this.accessTokenValidity = accessTokenValidity;
		this.refreshTokenValidity = refreshTokenValidity;
		
		if (secretKey == null || secretKey.length() < 32) {
			throw new IllegalArgumentException("Secret key must be at least 32 characters");
		}
		this.algorithm = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.isNotBlank(bearerToken)) {
			bearerToken = bearerToken.trim();
			if (bearerToken.startsWith("Bearer ")) {
				return bearerToken.substring(7);
			}
			return bearerToken;
		}
		return null;
	}
	
	public TokenDTO createAccessToken(String username, List<String> roles) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime accessValidity = now.plusSeconds(accessTokenValidity / 1000);
		String accessToken = buildToken(username, roles, now, accessValidity);
		LocalDateTime refreshValidity = now.plusSeconds(refreshTokenValidity / 1000);
		String refreshToken = buildToken(username, roles, now, refreshValidity);
		log.info("Generating JWT for user [{}] with roles: {}", username, roles);
		return new TokenDTO(username, true, now, accessValidity, accessToken, refreshToken);
	}
	
	public TokenDTO refreshToken(String refreshToken) {
		String token = extractToken(refreshToken);
		DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
		String username = decoded.getSubject();
		List<String> roleNames = decoded.getClaim("roles").asList(String.class);
		return createAccessToken(username, roleNames);
	}
	
	private String buildToken(String username, List<String> roleNames, LocalDateTime issuedAt, LocalDateTime expiresAt) {
		Instant issuedInstant = issuedAt.atZone(ZoneId.systemDefault()).toInstant();
		Instant expiresInstant = expiresAt.atZone(ZoneId.systemDefault()).toInstant();
		return JWT.create()
			.withSubject(username)
			.withClaim("roles", roleNames)
			.withIssuedAt(Date.from(issuedInstant))
			.withExpiresAt(Date.from(expiresInstant))
			.withIssuer(issuer)
			.sign(algorithm);
	}
	
	public Authentication getAuthentication(String token) {
		String username = getUsernameFromToken(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getUsernameFromToken(String token) {
		return decodedToken(extractToken(token)).getSubject();
	}
	
	private DecodedJWT decodedToken(String token) {
		return JWT.require(algorithm).build().verify(token);
	}
	
	private String extractToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return bearerToken;
	}
	
	public boolean validateToken(String token) {
		try {
			decodedToken(token);
			return true;
		} catch (JWTVerificationException e) {
			throw new InvalidJwtAuthenticationException("Invalid JWT token");
		}
	}
}