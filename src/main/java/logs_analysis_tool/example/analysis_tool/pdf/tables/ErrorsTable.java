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
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
public class ErrorsTable implements Tables{

    private final Logger LOG = LoggerFactory.getLogger(ErrorsTable.class);

    @Value("${errorsTablePage:7}")
    private int errorsTablePage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    private final LogsRepo logsRepo;

    @Autowired
    public ErrorsTable(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }
    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {
        LOG.info("DRAWING ERRORS TABLE");
        PDPage page = document.getPage(errorsTablePage);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
            monthDaysDrawer(contentStream,font);
            numberOfErrorsDrawer(contentStream,font);
        }
        LOG.info("ERRORS TABLE COMPLETED");

    }

    private void monthDaysDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        LOG.info("DRAWING Days COLUMN");
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

         for (int i = 0; i < 15; i++) {
                float x = 43;
                writeText(contentStream,x,String.valueOf(i + 1),i,font);
            }

            for (int i = 15; i < numberOfDays; i++) {
                float x = 312;
                if (i == 30)
                {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(x, 10);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText(String.valueOf(i + 1));
                    contentStream.endText();
                }
                else
                {
                    writeText(contentStream,x,String.valueOf(i + 1),i-15,font);
                }
            }
        }


    private void numberOfErrorsDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        LOG.info("DRAWING Error numbers COLUMN");
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

        List<Long> errors = logsRepo.totalErrors(month);
        for (int i = 0; i < 15; i++) {
            float x = 191;
            writeText(contentStream,x,String.valueOf(errors.get(i)),i,font);
        }
        for (int i = 15; i < numberOfDays; i++) {
            float x = 460;
            if (i == 30)
            {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(x, 10);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText(String.valueOf(i + 1));
                contentStream.endText();
            }
            else
            {
                writeText(contentStream,x,String.valueOf(errors.get(i)),i-15,font);
            }

        }
    }


    private void writeText(PDPageContentStream contentStream,float x,String s,int i, PDType0Font font) throws IOException {
        float y = 429f;
        float differenceBetweenValues = 28.5f;

        contentStream.beginText();
        contentStream.setFont(font, 12);

        contentStream.newLineAtOffset(x, y - (i * differenceBetweenValues));
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.showText(s);
        contentStream.endText();
    }
}
