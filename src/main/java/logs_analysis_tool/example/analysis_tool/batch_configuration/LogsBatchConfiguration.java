package logs_analysis_tool.example.analysis_tool.batch_configuration;

import logs_analysis_tool.example.analysis_tool.controller.ParsingAndInsertionJobExecutionListener;
import logs_analysis_tool.example.analysis_tool.service.LogsService;
import lombok.Getter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;


@Configuration
@EnableBatchProcessing
@Getter
public class LogsBatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;

    private LogsService logsService;

    private ParsingAndInsertionJobExecutionListener parsingAndInsertionJobExecutionListner;

    @Value("${logs.directory.path:D:\\task2 AnalysisTool 3-5-2024\\logs\\platformAudit_cleared.log1 - TEST}")
    private String directoryPath;
    @Qualifier("parseAndInsertLogsStep")
    private Step step2;

    @Qualifier("setupDirectoryStep")
    private Step step1;

    @Autowired
    @Qualifier("parseAndInsertLogsStep")
    public void setStep2(Step step2) {
        this.step2 = step2;
    }

    @Autowired
    @Qualifier("setupDirectoryStep")
    public void setStep1(Step step1) {
        this.step1 = step1;
    }
    @Autowired
    public void setParsingAndInsertionJobExecutionListener(ParsingAndInsertionJobExecutionListener parsingAndInsertionJobExecutionListner) {
        this.parsingAndInsertionJobExecutionListner = parsingAndInsertionJobExecutionListner;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setLogsService(LogsService logsService) {
        this.logsService = logsService;
    }


    @Bean
    public Job parseAndInsertLogsJob() {

            return jobBuilderFactory
                    .get("parseAndInsertLogsJob")
                    .start(step1)
                    .next(step2)
                    .listener(parsingAndInsertionJobExecutionListner)
                    .build();
    }
}



