package logs_analysis_tool.example.analysis_tool.pdf.headlines;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogoBuilder {

    @Value("${logoPath}")
    private String logoPath;

    @Value("${logoPage:0}")
    private int logoPage;

    public void insertBankLogo(PDDocument document) throws IOException {
        float x = 193;
        float y = 764;
        float width = 42.29f;
        float height = 45;

        PDPage page = document.getPage(logoPage);


        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
            PDImageXObject imageXObject = PDImageXObject.createFromFile(logoPath,document);

            contentStream.drawImage(imageXObject,x,y,width,height);
        }
    }

}
