package com.fundooproject.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenUtil {

    static String tokenKey = "tzABx31ddQLAWEP";
	private static final long expiryTime = 3600*2000;
	
 	public String createToken(long id) {
		long timeNow = System.currentTimeMillis();
		Date issuDate = new Date(timeNow);
		Date expiry = new Date(issuDate.getTime()+expiryTime);
		JwtBuilder builder = Jwts.builder();
		builder.setSubject("tokenAccess");
		builder.setIssuedAt(issuDate);
		builder.setExpiration(expiry);
		builder.setIssuer(String.valueOf(id));
		builder.signWith(SignatureAlgorithm.HS256, tokenKey);
		String token = builder.compact();
		return token;
	}
 	
	public Long decodeToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody();
			claims.getExpiration();
			return Long.parseLong(claims.getIssuer());
		}catch(Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	
	public boolean checkTokenExpired(String token) {
		try {
			long timeNow = System.currentTimeMillis();
			Date currentTime = new Date(timeNow);
			Claims claims = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody();
			long DiffTime = Math.abs(currentTime.getTime() - claims.getExpiration().getTime());
			return expiryTime - DiffTime > 0 ? false : true;
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
	}
	 
  
}
