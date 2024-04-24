package logs_analysis_tool.example.analysis_tool;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@EnableBatchProcessing
@Configuration
//@ComponentScan("LogsAnalysisTool.example.analysis_tool.*")
public class AnalysisToolApp {

	public static void main(String[] args) {
		SpringApplication.run(AnalysisToolApp.class, args);
	}

}
