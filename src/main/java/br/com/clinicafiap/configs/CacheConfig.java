package br.com.clinicafiap.configs;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	private final String CACHE_USUARIOS_POR_PERFIL = "usuarios_por_perfil";
	private final String CACHE_USUARIO = "usuario";
		
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(CACHE_USUARIOS_POR_PERFIL), new ConcurrentMapCache(CACHE_USUARIO)));

		return cacheManager;
	}
}