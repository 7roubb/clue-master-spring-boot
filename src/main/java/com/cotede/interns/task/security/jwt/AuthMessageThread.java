package com.cotede.interns.task.security.jwt;
import org.springframework.stereotype.Component;
@Component
public class AuthMessageThread {
    private static final ThreadLocal<String> authError = new ThreadLocal<>();
    public static String getAuthError() {
        return authError.get();
    }
    public static void setAuthError(String authError){
        AuthMessageThread.authError.set(authError);
    }
}