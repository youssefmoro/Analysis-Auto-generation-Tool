package logs_analysis_tool.example.analysis_tool.pdf.tables;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TopFailureWidgetsTable implements Tables{

    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {

    }
}
