package logs_analysis_tool.example.analysis_tool.pdf.headlines;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class PdfTitle {

    private final Logger LOG = LoggerFactory.getLogger(PdfTitle.class);

    @Value("${titlePage:0}")
    private int titlePage;

    public void addTitle(PDDocument document, String dynamicContent, PDType0Font font) throws IOException {
        LOG.info("Adding Title");
        PDPage firstPage = document.getPage(titlePage);

        // Define the coordinates and dimensions of the rectangular area
        float x = 32; // X-coordinate of the top-left corner
        float y = 705; // Y-coordinate of the top-left corner

        try (PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, PDPageContentStream.AppendMode.APPEND, false)) {
            contentStream.beginText();
            contentStream.setFont(font, 15);

            contentStream.newLineAtOffset(x, y);
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.showText(dynamicContent);
            contentStream.endText();
        }
        LOG.info("Title Added");
    }
}
