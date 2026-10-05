package pe.edu.lirio.Seda.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, FiltroJWTAutorizacion jwtFilter) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/auth/**", "/error").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
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
	CorsConfigurationSource corsConfigurationSource(
			@Value("${app.cors.allowed-origin:http://localhost:4200}") String allowedOrigin) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(allowedOrigin));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration);
		return source;
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
