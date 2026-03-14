package br.com.fatec.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
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
                    req.requestMatchers(HttpMethod.GET, "/home").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/funcionarios", "/balanco-financeiro", "/vendas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/funcionarios/cadastrar").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/estoque").hasRole("ESTOQUISTA");
                    req.requestMatchers(HttpMethod.GET, "/caixa").hasRole("CAIXA");
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

    @Bean
    RoleHierarchy roleHierarchy() {
        String hierarquia = "ROLE_ADMIN > ROLE_ESTOQUISTA\n" +
                "ROLE_ADMIN > ROLE_CAIXA";
        return RoleHierarchyImpl.fromHierarchy(hierarquia);
    }
}
