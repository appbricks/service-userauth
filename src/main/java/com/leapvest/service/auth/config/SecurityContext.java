package com.leapvest.service.auth.config;

import com.leapvest.model.user.User;
import com.leapvest.service.auth.service.UserLoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.inject.Inject;

/**
 * Spring security configuration context
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityContext
    extends WebSecurityConfigurerAdapter {

    @Inject
    private UserLoginService userLoginService;

    @Override
    public void configure(WebSecurity web) 
        throws Exception {
        
        web
            //Spring Security ignores request to static resources such as CSS or JS files.
            .ignoring()
            .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) 
        throws Exception {
        
        http
            //Configures form login
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/login?error=bad_credentials")
            //Configures the logout function
            .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            //Configures url based authorization
            .and()
                .authorizeRequests()
                    //Anyone can access the urls
                    .antMatchers(
                        "/home",
                        "/login",
                        "/auth/**",
                        "/signup/**").permitAll()
                    //The rest of the our application is protected.
                    .antMatchers("/**").hasRole("USER")
            //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
            .and()
                .apply(new SpringSocialConfigurer());
    }

    /**
     * Configures the authentication manager bean 
     * which processes authentication requests.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) 
        throws Exception {
        
        auth
            .userDetailsService(this.userLoginService)
            .passwordEncoder(User.PASSWORD_ENCODER);
    }
}
