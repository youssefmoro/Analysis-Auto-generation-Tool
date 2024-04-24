package logs_analysis_tool.example.analysis_tool.batch_configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class LogsExecutorBeanConfiguration {
    @Value("${connection.pool.size:4}")
    private int poolSize;
    @Value("${connection.pool.max.size:8}")
    private int maxPoolSize;
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize); // Adjust as needed from app.properties
        executor.setMaxPoolSize(maxPoolSize); // Adjust as needed from app.properties
        executor.setThreadNamePrefix("logs-processor-thread-");
        executor.initialize();
        return executor;
        //....................................................
//        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
//        executor.setConcurrencyLimit(Runtime.getRuntime().availableProcessors());
//        return executor;
    }
}
