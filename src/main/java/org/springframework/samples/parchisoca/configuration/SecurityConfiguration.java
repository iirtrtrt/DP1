package org.springframework.samples.parchisoca.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.samples.parchisoca.user.FacebookSignupService;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userService;


    @Autowired
    private FacebookSignupService facebookConnectionSignup;

    @Value("${spring.social.facebook.appSecret}")
    String appSecret;

    @Value("${spring.social.facebook.appId}")
    String appId;




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
            .antMatchers(HttpMethod.GET, "/","/oups").permitAll()
            .antMatchers("/register/**").permitAll()
            .antMatchers("/admin/**").hasAnyAuthority("admin")
            .antMatchers("/owners/**").hasAnyAuthority("player","admin")
            .antMatchers("/invite/**").hasAnyAuthority("player")
            .antMatchers("/game/**").hasAnyAuthority("player","admin")
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .successHandler(myAuthenticationSuccessHandler())
            .failureUrl("/login-error")

            .and()
            .logout()
            .logoutSuccessUrl("/");
        // Configuración para que funcione la consola de administración
        // de la BD H2 (deshabilitar las cabeceras de protección contra
        // ataques de tipo csrf y habilitar los framesets si su contenido
        // se sirve desde esta misma página.
        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(
                "select username,password,enabled "
                    + "from users "
                    + "where username = ?")
            .authoritiesByUsernameQuery(
                "select username, authority "
                    + "from authorities "
                    + "where username = ?")
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
        return encoder;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new UrlAuthenticationSuccessHandler();
    }


    @Bean
    public
    ProviderSignInController providerSignInController() {
        ConnectionFactoryLocator connectionFactoryLocator =
            connectionFactoryLocator();
        UsersConnectionRepository usersConnectionRepository =
            getUsersConnectionRepository(connectionFactoryLocator);
        ((InMemoryUsersConnectionRepository) usersConnectionRepository)
            .setConnectionSignUp(facebookConnectionSignup);
        return new ProviderSignInController(connectionFactoryLocator,
            usersConnectionRepository, new FacebookSignInAdapter());
    }

    private ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(appId, appSecret));
        return registry;
    }

    private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator
                                                                       connectionFactoryLocator) {
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
    }

}


