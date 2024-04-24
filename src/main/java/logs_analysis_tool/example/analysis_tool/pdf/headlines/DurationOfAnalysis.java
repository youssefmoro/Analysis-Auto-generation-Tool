package logs_analysis_tool.example.analysis_tool.pdf.headlines;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;


@Component
public class DurationOfAnalysis {
    private final Logger LOG = LoggerFactory.getLogger(DurationOfAnalysis.class);

    @Value("${titlePage:0}")
    private int titlePage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    private LocalDate firstDayOfMonth;

    private LocalDate lastDayOfMonth;

    @PostConstruct
    public void postConstruct()
    {
        firstDayOfMonth =LocalDate.of(LocalDate.now().getYear(),month,1);
        lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(),month,1).with(TemporalAdjusters.lastDayOfMonth());
    }


    public void addDuration(PDDocument document, PDType0Font font) throws IOException {
        LOG.info("ADDING DURATION");
        PDPage firstPage = document.getPage(titlePage);

        // Define the coordinates and dimensions of the rectangular area
        float x = 32; // X-coordinate of the top-left corner
        float y = 665; // Y-coordinate of the top-left corner

        // Define the line spacing
        float lineHeight = 18;


        try (PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, PDPageContentStream.AppendMode.APPEND, false)) {
            contentStream.beginText();
            contentStream.setFont(font, 12);

            // Write each line
            contentStream.newLineAtOffset(x, y); // Move to initial position
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText("Duration:");

            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText("From: " + firstDayOfMonth);

            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText("To: " + lastDayOfMonth);

            contentStream.endText();
            LOG.info("DURATION ADDED");
        }
    }

    public void addTotal(PDDocument document,PDType0Font font) throws IOException {

        PDPage firstPage = document.getPage(titlePage);

        // Define the coordinates and dimensions of the rectangular area
        float x = 203; // X-coordinate of the top-left corner
        float y = 665; // Y-coordinate of the top-left corner

        // Define the line spacing
        float lineHeight = 18;


        try (PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, PDPageContentStream.AppendMode.APPEND, false)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(x, y); // Move to the next column
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText("Total");
            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText(YearMonth.of(LocalDate.now().getYear(), month).lengthOfMonth() + " Days");
            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText("Generated on " + LocalDate.now());

            contentStream.endText();
        }
    }

}
