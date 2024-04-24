package logs_analysis_tool.example.analysis_tool.pdf.tables;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import logs_analysis_tool.example.analysis_tool.models.LogsModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.Position;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Component
public class LongestExecutionTimeTable implements Tables {

    private final Logger LOG = LoggerFactory.getLogger(LongestExecutionTimeTable.class);
    private final LogsRepo logsRepo;
    @Value("${executionTimeTablePage:9}")
    private int executionTimeTablePage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public LongestExecutionTimeTable(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }

    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {
        LOG.info("DRAWING TOP EXECUTION TIME TABLE");
        topExecutionTime(document,font);
        LOG.info("TOP EXECUTION TIME TABLE COMPLETED");
    }

    private void topExecutionTime(PDDocument document, PDType0Font font) throws IOException {
        PDPage apiPage = document.getPage(executionTimeTablePage);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, apiPage, PDPageContentStream.AppendMode.APPEND, false)) {
            List<LogsModel> list = logsRepo.findMaximumExecutionData(month);

            executionTimeDrawer(list,contentStream,font);

            apiDrawer(list,contentStream,font);

            requestTimeDrawer(list,contentStream,font);
        }
    }

    private void apiDrawer(List<LogsModel> list,PDPageContentStream contentStream, PDType0Font font) throws IOException
    {
        LOG.info("DRAWING API COLUMN");
        //API NAME
        float urlXAxis = 51;
        float urlYAxis = 1261;
        float urlWidth = 203;
        float differenceBetweenValues2 = 62;

        Paragraph paragraph = new Paragraph();
        for (int i = 0; i < 20; i++) {
            paragraph.removeLast();
            paragraph.addText(list.get(i).getOperationUrl(), 12, font);
            paragraph.setMaxWidth(urlWidth);
            paragraph.drawText(contentStream, new Position(urlXAxis, urlYAxis - (i * differenceBetweenValues2)), Alignment.Left, null);
        }
    }

    private void executionTimeDrawer(List<LogsModel> list,PDPageContentStream contentStream, PDType0Font font) throws IOException
    {
        LOG.info("DRAWING EXECUTION TIME COLUMN");
        //Execution Time
        float executionTimeXAxis = 298;
        float executionTimeYAxis = 1227;
        float differenceBetweenValues = 62;

        for (int i = 0; i < 20; i++) {
            contentStream.beginText();
            contentStream.setFont(font, 12);

            contentStream.newLineAtOffset(executionTimeXAxis, executionTimeYAxis - (i * differenceBetweenValues));
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(String.valueOf(list.get(i).getOperationExecutionTime_milliSec() / 1000));
            contentStream.endText();
        }
    }

    private void requestTimeDrawer(List<LogsModel> list,PDPageContentStream contentStream, PDType0Font font) throws IOException
    {
        LOG.info("DRAWING REQUEST TIME COLUMN");
        //Request Time
        float requestXAxis = 431;
        float requestYAxis = 1227;
        float differenceBetweenValues3 = 62;

        for (int i = 0; i < 20; i++) {
            contentStream.beginText();
            contentStream.setFont(font, 12);

            contentStream.newLineAtOffset(requestXAxis, requestYAxis - (i * differenceBetweenValues3));
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(list.get(i).getLocalDate() + " " + list.get(i).getLocalTime());
            contentStream.endText();
        }
    }

}
