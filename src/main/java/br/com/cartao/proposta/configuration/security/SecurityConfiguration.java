package br.com.cartao.proposta.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Profile(value = {"dev","prod"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                        .antMatchers(HttpMethod.GET,"/actuator/prometheus/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                        .antMatchers(HttpMethod.GET,  "/v3/api-docs/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/swagger-ui/**", "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated()
                .and()
                        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                        .csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                        .httpBasic().disable().formLogin().disable();

    }

}
