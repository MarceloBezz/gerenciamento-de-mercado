package br.com.fatec.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Configuracoes {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(req -> {
                    req.anyRequest().permitAll(); // Permissão temporária de todas as rotas para não atrapalhar testes
                })
                .formLogin(form -> {
                    form
                            .defaultSuccessUrl("/home")
                            .loginPage("/login")
                            .failureUrl("/login?error")
                            .usernameParameter("email")
                            .passwordParameter("senha")
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .invalidateHttpSession(true)
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout");
                })
//                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
