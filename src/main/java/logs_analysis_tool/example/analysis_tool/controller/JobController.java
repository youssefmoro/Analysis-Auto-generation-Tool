package logs_analysis_tool.example.analysis_tool.controller;

import logs_analysis_tool.example.analysis_tool.models.dtos.PathDto;
import logs_analysis_tool.example.analysis_tool.models.dtos.ResponseDto;
import logs_analysis_tool.example.analysis_tool.utils.Config;
import logs_analysis_tool.example.analysis_tool.vaildation.PathsValidation;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;

@RestController
@RequestMapping("/parseInsertLogsJob")
public class JobController {
    private JobLauncher jobLauncher;
    private static final Logger logger = Logger.getLogger(JobController.class);
    private Job parseLogsJob;

    private Boolean busyFlag = false;

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setParseLogsJob(Job parseLogsJob) {
        this.parseLogsJob = parseLogsJob;
    }

    @PostMapping("/startJobForDirectoryPath")
    public ResponseEntity<ResponseDto> startJobForThisPath(@RequestBody @NotNull PathDto path) {
        ResponseDto responseDto = new ResponseDto();
        if (PathsValidation.isValidDirectoryPath(path.getPath())) {
            Config.setPassedDirectoryPath(path.getPath());
            if (busyFlag == false) {
                busyFlag = true;
                Paths.get(path.getPath());
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .addString("directory", path.getPath())
                        .toJobParameters();
                try {
                    jobLauncher.run(parseLogsJob, jobParameters);
                } catch (Exception e) {
                    logger.info("error launching the job from the launcher",e);
                }
                busyFlag = false;
                responseDto.setResponse("job started... ");
                return ResponseEntity.ok(responseDto);
            } else {
                responseDto.setResponse("busy doing another job, please wait for seconds");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
            }
        } else {
            responseDto.setResponse("invalid input path");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

    }
}

