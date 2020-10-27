package cn.com.kun.web.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;

/**
 * Created by xuyaokun On 2020/10/26 23:13
 * @desc: 
 */
public final class JwtUtil {

    /**
     * 这个秘钥是防止JWT被篡改的关键，决不能泄露
     */
    private final static String secretKey = "kunghsu-jwt-secretkey";

    public final static String AES_KEY = "kunghsu-aes-secretkey";


    /**
     * 过期时间目前设置成2天，这个配置随业务需求而定
     */
    private final static Duration expiration = Duration.ofHours(2);

    /**
     * 生成JWT(默认两小时过期)
     * @param subject 需要存放的内容
     * @return JWT
     */
    public static String generate(String subject) {
        // 过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        return generate(subject, expiryDate);
    }


    public static String generate(String subject, Date expiryDate) {

        return Jwts.builder()
                .setSubject(subject) // 将userName放进JWT，这里也可以直接把一个json串放进去，传递更多必要信息给客户端
                .setIssuedAt(new Date()) // 设置JWT签发时间
                .setExpiration(expiryDate)  // 设置过期时间
                //该方法是在JWT中加入值为vaule的key字段
                .claim("key", "vaule")
                .signWith(SignatureAlgorithm.HS512, secretKey) // 设置加密算法和秘钥
                .compact();
    }

    /**
     * 解析JWT
     * @param token JWT字符串
     * @return 解析成功返回Claims对象，解析失败返回null
     */
    public static Claims parse(String token) {
        // 如果是空字符串直接返回null
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Claims claims = null;
        // 解析失败了会抛出异常，所以我们要捕捉一下。token过期、token非法都会导致解析失败
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            e.printStackTrace();
            System.err.println("解析失败！");
        }
        return claims;
    }
}
