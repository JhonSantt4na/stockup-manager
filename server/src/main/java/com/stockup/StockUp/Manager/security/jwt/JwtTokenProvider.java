package com.stockup.StockUp.Manager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import com.stockup.StockUp.Manager.exception.InvalidJwtAuthenticationException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
	
	private final UserDetailsService userDetailsService;
	private final String issuer;
	private final long accessTokenValidity;
	private final long refreshTokenValidity;
	private final Algorithm algorithm;
	
	public JwtTokenProvider(
		UserDetailsService userDetailsService,
		@Value("${security.jwt.token.secret-key}") String secretKey,
		@Value("${security.jwt.token.issuer:health-check}") String issuer,
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
	
	public TokenDTO createAccessToken(String username, List<String> roles) {
		Date now = new Date();
		Date accessValidity = new Date(now.getTime() + accessTokenValidity);
		String accessToken = buildToken(username, roles, now, accessValidity);
		String refreshToken = buildRefreshToken(username, roles, now);
		return new TokenDTO(username, true, now, accessValidity, accessToken, refreshToken);
	}
	
	public TokenDTO refreshToken(String refreshToken) {
		if (StringUtils.isBlank(refreshToken)) {
			throw new InvalidJwtAuthenticationException("Refresh token is missing");
		}
		
		String token = extractToken(refreshToken);
		DecodedJWT decodedJWT = decodedToken(token);
		
		String username = decodedJWT.getSubject();
		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
		return createAccessToken(username, roles);
	}
	
	private String buildToken(String username, List<String> roles, Date now, Date validity) {
		return JWT.create()
			.withClaim("roles", roles)
			.withIssuedAt(now)
			.withExpiresAt(validity)
			.withSubject(username)
			.withIssuer(issuer)
			.sign(algorithm);
	}
	
	private String buildRefreshToken(String username, List<String> roles, Date now) {
		Date validity = new Date(now.getTime() + refreshTokenValidity);
		return buildToken(username, roles, now, validity);
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	private DecodedJWT decodedToken(String token) {
		try {
			return JWT.require(algorithm).build().verify(token);
		} catch (JWTDecodeException e) {
			throw new InvalidJwtAuthenticationException("Invalid JWT token: " + e.getMessage());
		}
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		return extractToken(bearerToken);
	}
	
	private String extractToken(String bearerToken) {
		if (StringUtils.isNotBlank(bearerToken)){
			bearerToken = bearerToken.trim();
			if (bearerToken.startsWith("Bearer ")) {
				return bearerToken.substring(7);
			}
			return bearerToken;
		}
		return null;
	}
	
	public boolean validateToken(String token) {
		try {
			decodedToken(token); // Valida assinatura e expiração
			return true;
		} catch (TokenExpiredException e) {
			throw new InvalidJwtAuthenticationException("Expired JWT token");
		} catch (JWTVerificationException e) {
			throw new InvalidJwtAuthenticationException("Invalid JWT token: " + e.getMessage());
		}
	}
}
