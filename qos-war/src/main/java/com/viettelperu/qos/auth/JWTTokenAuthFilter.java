package com.viettelperu.qos.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class JWTTokenAuthFilter extends OncePerRequestFilter {
    private static List<String> authRoutes = new ArrayList<>();
//    private static List<String> NO_AUTH_ROUTES = new ArrayList<>();
    public static final String JWT_KEY = "JWT-TOKEN-SECRET";
    
//    static {
//        authRoutes.add("/api/history/create");
//        authRoutes.add("/api/history/getById");
//        authRoutes.add("/api/history/delete");
//        authRoutes.add("/api/history/getByUsername");
////        NO_AUTH_ROUTES.add("/api/user/authenticate");
////        NO_AUTH_ROUTES.add("/api/user/register");
////        NO_AUTH_ROUTES.add("/api/user/getMSISDN");
////        NO_AUTH_ROUTES.add("/api/user/checkSubExistsActive");
////        NO_AUTH_ROUTES.add("/api/file/upload");
////        NO_AUTH_ROUTES.add("/api/file/download");
//    }

    private Logger LOG = LoggerFactory.getLogger(JWTTokenAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("authorization");
        String authenticationHeader = request.getHeader("authentication");
        String route = request.getRequestURI();
        
        // no auth route matching
        boolean needsAuthentication = false;

//        for (Pattern p : AUTH_ROUTES) {
//            if (p.matcher(route).matches()) {
//                needsAuthentication = true;
//                break;
//            }
//        }

        for (String auth : authRoutes) {
			if (route.contains(auth)) {
				needsAuthentication = true;
				break;
			}
        }
        
//        if(route.startsWith(CONTEXT_PATH + "/api/")) {
//            needsAuthentication = true;
//        }

//        if (NO_AUTH_ROUTES.contains(route)) {
//            needsAuthentication = false;
//        }

        // Checking whether the current route needs to be authenticated
        if (needsAuthentication) {
            // Check for authorization header presence
            String authHeader = null;
            if (authorizationHeader == null || authorizationHeader.equalsIgnoreCase("")) {
                if (authenticationHeader == null || authenticationHeader.equalsIgnoreCase("")) {
                    authHeader = null;
                } else {
                    authHeader = authenticationHeader;
                    LOG.info("authentication: " + authenticationHeader);
                }
            } else {
                authHeader = authorizationHeader;
                LOG.info("authorization: " + authorizationHeader);
            }

            if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
                throw new AuthCredentialsMissingException("Missing or invalid Authorization header.");
            }

            final String token = authHeader.substring(7); // The part after "Bearer "
            try {
                final Claims claims = Jwts.parser().setSigningKey(JWT_KEY)
                        .parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);

                // Now since the authentication process if finished
                // move the request forward
                filterChain.doFilter(request, response);
            } catch (final Exception e) {
                throw new AuthenticationFailedException("Invalid token. Cause:"+e.getMessage());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

	public static List<String> getAuthRoutes() {
		return authRoutes;
	}

	public static void setAuthRoutes(List<String> authRoutes) {
		JWTTokenAuthFilter.authRoutes = authRoutes;
	}
}
