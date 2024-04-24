package logs_analysis_tool.example.analysis_tool.pdf;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class SessionDrawer {

    @Value("${sessionPage:3}")
    private int sessionPage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    private final LogsRepo logsRepo;

    @Autowired
    public SessionDrawer(LogsRepo logsRepo, FontSelection fontSelection) {
        this.logsRepo = logsRepo;
    }


    public void sessionDrawer(PDDocument document,PDType0Font font) throws IOException
    {
        PDPage page = document.getPage(sessionPage);
        try(PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false))
        {
            sessionMinimumDrawer(contentStream,font);

            sessionAverageDrawer(contentStream,font);

            sessionMaximumDrawer(contentStream,font);
        }
    }

    private void sessionMinimumDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        float x = 100;
        float y = 646;

        Long min = logsRepo.minSessionDuration(month);


        writeText(contentStream,x,y, min + " MillieSecond",font);
    }

    private void sessionAverageDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        float x = 100;
        float y = 401;


        Long avg = logsRepo.averageSessionDuration(month);


        avg /= (1000 * 60); // milliseconds to minutes


        writeText(contentStream,x,y, avg + " Minutes",font);
    }

    private void sessionMaximumDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        float x = 100;
        float y = 162;

        Long max =  logsRepo.maxSessionDuration(month);

        max /= (1000 * 60); // milliseconds to minutes

        writeText(contentStream,x,y, max + " Minutes",font);
    }

    private void writeText(PDPageContentStream contentStream,float x , float y,String s, PDType0Font font) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 24);

        contentStream.newLineAtOffset(x, y);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.showText(s);
        contentStream.endText();
    }
}
