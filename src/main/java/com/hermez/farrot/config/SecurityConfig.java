package com.hermez.farrot.config;

import com.hermez.farrot.oauth.service.CustomOAuth2UserService;
import com.hermez.farrot.oauth.OAuth2SuccessHandler;
import com.hermez.farrot.member.security.JwtAuthenticationFilter;
import com.hermez.farrot.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final static int ONE_DAY = 24 * 60 * 60;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                )
                .requestMatchers("/error","/favicon.ico","https://firebasestorage.googleapis.com/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/admin/**").hasRole("admin")
                                .requestMatchers("/member/detail","/chat-room/**","/product/register-sell").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/member/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .permitAll()
                        .successForwardUrl("/")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/naver")
                        .userInfoEndpoint(userInfo->userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .logout(Customizer.withDefaults())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}