package com.example.milk;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
public class MilkApplicationTests {

    private static final long TIME = 1000 * 60 * 60 * 24;//一天
    private String signature = "admin";

    @Test
    public void jwt() {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", "tom")
                .claim("role", "admin")
                //主题
                .setSubject("admin-test")
                //有效期
                .setExpiration(new Date(System.currentTimeMillis() + TIME))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
        System.out.println(jwtToken);
    }

    @Test
    public void parse(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsInN1YiI6ImFkbWluLXRlc3QiLCJleHAiOjE2NTgyMDQzNDEsImp0aSI6IjY4MmRmMWUwLWQ5ZGQtNDRmYy04MDJmLWM3M2M2YzQ1MjBkYiJ9.19HJPXHPHlmhg3umNfZRDbfspYOPipC_ksa0IJOrWgg";
        JwtParser jwtParser = Jwts.parser();
        Claims claims = jwtParser.setSigningKey(signature).parseClaimsJws(token).getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());
    }

}
