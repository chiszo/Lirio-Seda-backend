package pe.edu.lirio.Seda.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, FiltroJWTAutorizacion jwtFilter) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/auth/**", "/error").permitAll()
						.requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/roles/**", "/api/modelos/**", "/api/sedes/**", "/api/estados/**", "/api/motivos/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/roles/**", "/api/modelos/**", "/api/sedes/**", "/api/estados/**", "/api/motivos/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/roles/**", "/api/modelos/**", "/api/sedes/**", "/api/estados/**", "/api/motivos/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/productos-sedes/**", "/api/proveedores/**", "/api/entradas/**", "/api/salidas/**", "/api/pedidos/**", "/api/detalles-entrada/**", "/api/detalles-salida/**", "/api/detalles-pedido/**").authenticated()
						.requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/productos-sedes/**", "/api/proveedores/**", "/api/entradas/**", "/api/salidas/**", "/api/pedidos/**", "/api/detalles-entrada/**", "/api/detalles-salida/**", "/api/detalles-pedido/**").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/productos-sedes/**", "/api/proveedores/**", "/api/entradas/**", "/api/salidas/**", "/api/pedidos/**", "/api/detalles-entrada/**", "/api/detalles-salida/**", "/api/detalles-pedido/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
