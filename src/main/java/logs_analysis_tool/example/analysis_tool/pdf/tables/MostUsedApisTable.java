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
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.Position;

import java.io.IOException;
import java.util.List;

@Component
public class MostUsedApisTable implements Tables{


    private final Logger LOG = LoggerFactory.getLogger(MostUsedApisTable.class);

    public final LogsRepo logsRepo;

    @Value("${mostUsedApiTablePage:8}")
    private int mostUsedApiTablePage;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public MostUsedApisTable(LogsRepo logsRepo)
    {
        this.logsRepo = logsRepo;
    }

    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {
        LOG.info("DRAWING MOST USED APIs TABLE");
        topTenUsedAPIs(document,font);
        LOG.info("MOST USED APIs TABLE COMPLETED");
    }

    private void topTenUsedAPIs(PDDocument document, PDType0Font font) throws IOException {
        PDPage apiPage = document.getPage(mostUsedApiTablePage);
        try(PDPageContentStream contentStream = new PDPageContentStream(document,apiPage, PDPageContentStream.AppendMode.APPEND, false))
        {
            // Draw the text box
            float x = 53;
            float y = 666.45f;
            float width = 338;
            float height = 39.67f;


            Paragraph paragraph = new Paragraph();
            // Write text inside the text box
            List<String> topTenApis = logsRepo.topApiUsed(month);

            for (int i = 0;i<10;i++)
            {
                paragraph.removeLast();
                paragraph.addText(topTenApis.get(i),12, font);
                paragraph.setMaxWidth(width);
                paragraph.drawText(contentStream,new Position(x,(y - (i * (height + 11.34f)))), Alignment.Left,null);
            }
            //mapToWidgets(contentStream,topTenApis);
        }
    }

    private void mapToWidgets(PDPageContentStream contentStream,List<String> apiList) throws IOException {
        float x = 405;
        float y = 646.65f;
        float height = 51;

        for(int i = 0; i < 10 ; i++)
        {
            contentStream.beginText();
            contentStream.newLineAtOffset(x,y - (i * height));
            // Mapping Logic
            //contentStream.showText(apiList.get(i));
            contentStream.endText();
        }
    }
}
