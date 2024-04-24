package logs_analysis_tool.example.analysis_tool.batch_configuration;

import logs_analysis_tool.example.analysis_tool.models.LogsModel;
import logs_analysis_tool.example.analysis_tool.utils.Config;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class LogsBatchStepConfiguration {
    private StepBuilderFactory stepBuilderFactory;
    private static final Logger logger = Logger.getLogger(LogsBatchStepConfiguration.class);
    private ItemReader itemReader;

    private ItemProcessor itemProcessor;

    private ItemWriter itemWriter;
    @Qualifier("taskExecutor")
   private  TaskExecutor taskExecutor;
    @Value("${skip.limit:10}")
    private int skipLimit;

    @Value("${logs.chunk:500}")
    private int logsChunk;

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Autowired
    public void setItemReader(ItemReader itemReader) {
        this.itemReader = itemReader;
    }
    @Autowired
    public void setItemProcessor(ItemProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
    }
    @Autowired
    public void setItemWriter(ItemWriter itemWriter) {
        this.itemWriter = itemWriter;
    }
    @Autowired
    @Qualifier("taskExecutor")
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    @Bean("setupDirectoryStep")
    public Step setupDirectoryStep() {

            return stepBuilderFactory
                    .get("setupDirectoryStep")
                    .tasklet((contribution, chunkContext) -> {
                       logger.info("path:"+Config.getPassedDirectoryPath());
                        return RepeatStatus.FINISHED;
                    })
                    .build();
    }
    @Bean("parseAndInsertLogsStep")
    public Step parseAndInsertLogsStep() {
            return stepBuilderFactory
                    .get("parseAndInsertLogsStep")
                    .<LogsModel, LogsModel>chunk(logsChunk)
                    .reader(itemReader)
                    .processor(itemProcessor)
                    .writer(itemWriter)
                    .faultTolerant()
                    .skip(NonTransientResourceException.class)
                    .skip(FlatFileParseException.class)
//                    .skip(ReaderNotOpenException.class)
                    .skipLimit(skipLimit)
                    .taskExecutor(taskExecutor)
                    .build();
    }
}
