package logs_analysis_tool.example.analysis_tool.controller;

import logs_analysis_tool.example.analysis_tool.pdf.GeneratePDF;
import logs_analysis_tool.example.analysis_tool.service.LogsService;
import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParsingAndInsertionJobExecutionListener implements JobExecutionListener {
    private GeneratePDF generatePDF;
    private static final Logger logger = Logger.getLogger(LogsService.class);
    @Autowired
    public void setGeneratePDF(GeneratePDF generatePDF) {
        this.generatePDF = generatePDF;
    }
    @Override
    public void beforeJob(JobExecution jobExecution) {
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution != null) {
            // Call your desired method within this class
            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    generatePDF.generatePDF();
            }
        } else {
            logger.info("JobExecution object is null in afterJob");
            // here we handle the case where jobExecution is null (optional done for testing)("JobExecution object is null in afterJob");
        }
    }
}
