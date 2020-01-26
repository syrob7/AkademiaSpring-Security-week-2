package pl.akademiaspring.week2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        User admin = new User("admin",
                getPasswordEncoder().encode("admin-123"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

        User user = new User("user",
                getPasswordEncoder().encode("user-123"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        auth.inMemoryAuthentication().withUser(admin);
        auth.inMemoryAuthentication().withUser(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/helloAdmin").hasRole("ADMIN")
                .antMatchers("/helloUser").hasAnyRole( "ADMIN", "USER")
                .antMatchers("/helloAnonymous").hasAnyRole("ANONYMOUS", "ADMIN", "USER")
                .antMatchers("/bye").hasAnyRole("ANONYMOUS", "ADMIN", "USER")
                .and()
                .formLogin().permitAll()
                .and()
                .logout().logoutSuccessUrl("/bye");

    }
}
