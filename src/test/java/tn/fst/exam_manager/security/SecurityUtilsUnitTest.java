package tn.fst.exam_manager.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test class for the {@link SecurityUtils} utility class.
 */
class SecurityUtilsUnitTest {

    @BeforeEach
    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUserCin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin"));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin"));
        SecurityContextHolder.setContext(securityContext);
        Optional<String> cin = SecurityUtils.getCurrentUserLogin();
        assertThat(cin).contains("12345678");
    }

    @Test
    void testGetCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "token"));
        SecurityContextHolder.setContext(securityContext);
        Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
        assertThat(jwt).contains("token");
    }

    @Test
    void testIsAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin"));
        SecurityContextHolder.setContext(securityContext);
        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    void testHasCurrentUserThisAuthority() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.SUPER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.SUPER)).isTrue();
        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.INSTITUTE)).isFalse();
        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.DEPARTMENT)).isFalse();
        assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PROFESSOR)).isFalse();
    }

    @Test
    void testHasCurrentUserAnyOfAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.SUPER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.SUPER, AuthoritiesConstants.SUPER)).isTrue();
        assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.INSTITUTE, AuthoritiesConstants.INSTITUTE)).isFalse();
        assertThat(
            SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.DEPARTMENT, AuthoritiesConstants.DEPARTMENT)
        ).isFalse();
        assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.PROFESSOR, AuthoritiesConstants.PROFESSOR)).isFalse();
    }

    @Test
    void testHasCurrentUserNoneOfAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.SUPER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("12345678", "super-admin", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.SUPER, AuthoritiesConstants.INSTITUTE)).isFalse();
        assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.SUPER, AuthoritiesConstants.DEPARTMENT)).isFalse();
        assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities("ROLE_USER", "ROLE_ADMIN")).isFalse();
        assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities("ROLE_ANONYMOUS", "ROLE_ADMIN")).isTrue();
    }
}
