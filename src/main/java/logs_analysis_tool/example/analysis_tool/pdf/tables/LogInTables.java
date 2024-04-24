package logs_analysis_tool.example.analysis_tool.pdf.tables;


import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogInTables implements Tables {

    private final Logger LOG = LoggerFactory.getLogger(LogInTables.class);

    private final LogsRepo logsRepo;

    @Value("${logInTablePage:2}")
    private int logInPage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public LogInTables(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }

    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {

        List<Long> logInPerHour = logsRepo.logInPerHour(month);

        PDPage requestPage = document.getPage(logInPage);

        logInPerHour = logInPerHour
                .stream()
                .map(z -> z / 30)
                .collect(Collectors.toList());
        try(PDPageContentStream contentStream = new PDPageContentStream(document, requestPage, PDPageContentStream.AppendMode.APPEND, false)) {
            createLogInTableAM(contentStream,logInPerHour,font);
            createLogInTablePM(contentStream,logInPerHour,font);
        }
    }

    private void createLogInTableAM(PDPageContentStream contentStream,List<Long> logInPerHour, PDType0Font font) throws IOException {
        LOG.info("DRAWING LOGIN TABLE");


        float x = 150;
        float y = 389;
        float differenceOfTwoValues = 31;


        for(int i = 0 ; i < 12 ; i++)
        {
            contentStream.beginText();
            contentStream.setFont(font, 14);

            contentStream.newLineAtOffset(x, y - (i * differenceOfTwoValues) );
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(String.valueOf(logInPerHour.get(i)));
            contentStream.endText();
        }
    }

    private void createLogInTablePM(PDPageContentStream contentStream,List<Long> logInPerHour, PDType0Font font) throws IOException {

        float x = 437;
        float y = 389;
        float differenceOfTwoValues = 31;

        for(int i = 0 ; i < 12 ; i++)
        {
            contentStream.beginText();
            contentStream.setFont(font, 14);

            contentStream.newLineAtOffset(x, y - (i * differenceOfTwoValues) );
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(String.valueOf(logInPerHour.get(i+12)));
            contentStream.endText();
        }
        LOG.info("LOGIN TABLE COMPLETED");
    }
}
