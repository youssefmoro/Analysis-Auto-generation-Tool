package logs_analysis_tool.example.analysis_tool.service;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import logs_analysis_tool.example.analysis_tool.models.LogsModel;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.RegexLineTokenizer;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static logs_analysis_tool.example.analysis_tool.utils.Constants.J_SESSION_TOKEN_BEGINNING;


@Service
@Component
public class LogsService {

    private LogsRepo logsRepo;
    private static final org.apache.log4j.Logger logger = Logger.getLogger(LogsService.class);
    @Value("${logs.parse.path:D://task2 AnalysisTool 3-5-2024//logs//platformAudit_cleared.log1//platformAudit_cleared.log1//platformAudit_cleared.log1}")
    private String logsPath;
    @Value("${regex:.*operationUrl='([^']+)',.*?requestIdentifier='(.*?)',.*?username='(.*?)',.*?sessionId='(.*?)',.*?ip='(.*?)',.*?device='(.*?)',.*?operationTime='(.*?)',.*?operationExecutionTime=(\\\\d+),.*currentPortalId='([^']+)',.*currentOrganizationId='([^']+)',.*currentSiteId='([^']+)',.*requestHeaders='([^']+)',.*responseHeaders='([^']+)}")
    private String regex;
    @Value("${logs.chunk:500}")
    private int logsChunk;
    @Value("${logs.directory.path:D:\\task2 AnalysisTool 3-5-2024\\logs\\platformAudit_cleared.log1 - TEST}")
    private String directoryPath;

    @Value("${names:operationUrl,requestIdentifier,username,sessionId,ip,device,operationTime,operationExecutionTime_milliSec,currentPortalId,currentOrganizationId,currentSiteId,requestHeaders,responseHeaders}")
    private String[] NAMES;


    @Autowired
    public void setLogsRepo(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }

    // item reader here to parse our configured file path and return a reader that will
    //be used to get each line inside the file
    @Bean
    @StepScope
    public SynchronizedItemStreamReader<LogsModel> multiResourceItemReaderSynch(@Value("#{jobParameters['directory']}") String directory) {
        SynchronizedItemStreamReader<LogsModel> reader = new SynchronizedItemStreamReader<>();
        reader.setDelegate(multiResourceItemReader(directory));
        return reader;
    }
    public MultiResourceItemReader<LogsModel> multiResourceItemReader(@Value("#{jobParameters['directory']}") String directory)
    {

        MultiResourceItemReader<LogsModel> reader = new MultiResourceItemReader<>();
        List<Path> filePaths=new ArrayList<>();
        filePaths=filePathsListing(directory);
            List<Resource> resources = filePaths.stream()
                    .map(path -> new FileSystemResource(path))
                    .collect(Collectors.toList());
            for(int i =0;i<resources.size();i++)
//            System.out.println(resources.get(0).isReadable());
            reader.setResources(resources.toArray(new Resource[0]));
            reader.setDelegate(logsItemReader());
            reader.setStrict(true);
            return reader;
    }
    public FlatFileItemReader<LogsModel> logsItemReader() {
            FlatFileItemReader<LogsModel> reader = new FlatFileItemReader<>();
            reader.setEncoding("UTF-8");
            reader.setLineMapper(createLogsLineMapper());
            return reader;
    }
     protected static List<Path> filePathsListing (String directoryPath)
    {

        List<Path> pathsToBeReturned=new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
//                    here we will get the absolute path as a string
//                    String filePath = file.getAbsolutePath();
                    Path path = file.toPath();
                    pathsToBeReturned.add(path);
                }
            }
        }
        return pathsToBeReturned;
    }
    //here's our line mapper that take each required token mapped to our model
    // will do so using the methods line inside the log tokenizer to cut all the tokens
    //and the logs field set mapper will take each token and map it to the right attribute inside the model
    private @NotNull LineMapper<LogsModel> createLogsLineMapper() {
        DefaultLineMapper<LogsModel> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(createLogsLineTokenizer());
        lineMapper.setFieldSetMapper(createLogsFieldSetMapper());
        return lineMapper;
    }

    private @NotNull LineTokenizer createLogsLineTokenizer() {
        //regular record
        RegexLineTokenizer lineTokenizer = new RegexLineTokenizer();
//        lineTokenizer.setRegex(".*operationUrl='([^']+)',.*?requestIdentifier='(.*?)',.*?username='(.*?)',.*?sessionId='(.*?)',.*?ip='(.*?)',.*?device='(.*?)',.*?operationTime='(.*?)',.*?operationExecutionTime=(\d+),.*currentPortalId='([^']+)',.*currentOrganizationId='([^']+)',.*currentSiteId='([^']+)',.*requestHeaders='([^']+)',.*responseHeaders='([^']+)'");
//        lineTokenizer.setNames("operationUrl","requestIdentifier","username","sessionId","ip","device", "operationTime", "operationExecutionTime_milliSec","currentPortalId","currentOrganizationId","currentSiteId","requestHeaders","responseHeaders");
          lineTokenizer.setRegex(regex);
        lineTokenizer.setNames(NAMES);
        return lineTokenizer;
    }

    @Contract(pure = true)
    private @NotNull FieldSetMapper<LogsModel> createLogsFieldSetMapper() {
        return fieldSet -> {
            LogsModel log = new LogsModel();
            //operation url capturing
                log.setOperationUrl(fieldSet.readString("operationUrl"));
           //requestIdentifier capturing
                log.setRequestIdentifier(fieldSet.readString("requestIdentifier"));
          //username capturing
                log.setUsername(fieldSet.readString("username"));
           //sessionId capturing
                log.setSessionId(fieldSet.readString("sessionId"));
            //ip capturing
                log.setIp(fieldSet.readString("ip"));
            //device capturing
                String devicePlaceHolder = fieldSet.readString("device");
                    if (devicePlaceHolder.toLowerCase().contains("mobile")) {
                        log.setDevice("MOBILE");
                    }else {
                     log.setDevice("WEB");
                    }
                //device trace
                log.setDeviceTrace(devicePlaceHolder);
            //operationTime capturing to capture date and time
            //Wed Jan 31 21:14:24 UTC 2024
                String timeDateInString = fieldSet.readString("operationTime");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                    LocalDateTime dateTime = LocalDateTime.parse(timeDateInString, formatter);
                    LocalDate date = dateTime.toLocalDate();
                    LocalTime time = dateTime.toLocalTime();
                    log.setLocalDate(date);
                    log.setLocalTime(time);
            //operationExecutionTime capturing
            log.setOperationExecutionTime_milliSec(fieldSet.readLong("operationExecutionTime_milliSec"));
            //currentPortalId capturing
                log.setCurrentPortalId(fieldSet.readString("currentPortalId"));
            //currentOrganizationId capturing
                log.setCurrentOrganizationId(fieldSet.readString("currentOrganizationId"));
            //currentSiteId capturing
                log.setCurrentSiteId(fieldSet.readString("currentSiteId"));
            //requestHeaders capturing to consequently capture status
                log.setRequestHeaders(fieldSet.readString("requestHeaders"));
                if(fieldSet.readString("requestHeaders").contains("error"))
                {
                    log.setStatus(false);
                }else log.setStatus(true);
                //JSESSIONID capturing (note that it doesn't exist in some requestHeaders,thus at these records=NOT_FOUND)
            if (fieldSet.readString("requestHeaders").contains("JSESSIONID"))
            {
                int startingIndex=fieldSet.readString("requestHeaders").indexOf("JSESSIONID");
                String token=fieldSet.readString("requestHeaders").substring(startingIndex);
                if(token.contains(";"))
                {
                    String[] tokens=token.split(";");
                    // J_SESSION_TOKEN_BEGINNING=11th character for now,please change it if you changed it
                    log.setJ_sessionId(tokens[0].substring(J_SESSION_TOKEN_BEGINNING));
                } else log.setJ_sessionId(token.substring(J_SESSION_TOKEN_BEGINNING,token.indexOf(",")));
            } else log.setJ_sessionId("NOT_FOUND");
            //responseHeaders capturing
                log.setResponseHeaders(fieldSet.readString("responseHeaders"));
            return log;
        };
    }

    @Bean
    public ItemProcessor<LogsModel, LogsModel> logsItemProcessor() {
        logger.info("IAM AT PROCESSOR");
        ItemProcessor x = new ItemProcessor() {
            @Override
            public Object process(Object o) throws Exception {
                return o;
            }
        };
        return x;
    }

    // here is our itemwrite to save the read then processed logs into our h2-database
    @Bean
    public ItemWriter<LogsModel> logsItemWriter() {
        logger.info("IAM AT WRITER");
        return logs -> {
            try {
                logsRepo.saveAll(logs);
            } catch (Exception e) {
                logger.error("error at inserting into dataBase",e);
                // Add specific handling based on the exception type
            }
        };
    }
}


