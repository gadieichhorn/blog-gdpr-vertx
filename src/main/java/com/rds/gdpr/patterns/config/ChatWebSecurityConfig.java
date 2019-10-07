//package com.rds.gdpr.patterns.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//
//@EnableWebFluxSecurity
//public class ChatWebSecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.csrf().disable()
//                .authorizeExchange()
//                .pathMatchers(HttpMethod.POST, "/chat").hasRole("ADMIN")
//                .pathMatchers("/**")
//                .permitAll()
//                .and()
//                .httpBasic();
//        return http.build();
//    }
//
//}