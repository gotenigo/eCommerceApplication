package com.example.demo.security;



//This class contains the literal constants that are used in the JWTAuthenticationFilter class.
public class SecurityConstants {

	public static final String SECRET = "oursecretkey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer "; // prefix to be added to the JWT
    public static final String HEADER_STRING = "Authorization"; // Header String to provide  along with the JAT. Authorization= TOKEN_PREFIX + JWT Code
    public static final String SIGN_UP_URL = "/api/user/create";
}
