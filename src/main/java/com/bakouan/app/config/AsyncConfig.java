package com.bakouan.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Nb. de threads à garder toujours disponibles
        executor.setCorePoolSize(4);

        // Nb. maximal de threads lorsqu’il y a beaucoup de tâches
        executor.setMaxPoolSize(10);

        // Taille de la file d’attente avant de créer de nouveaux threads
        executor.setQueueCapacity(100);

        // Préfixe pour identifier les threads dans les logs
        executor.setThreadNamePrefix("Async-Executor-");

        // Initialisation du ThreadPool
        executor.initialize();
        return executor;
    }
}
