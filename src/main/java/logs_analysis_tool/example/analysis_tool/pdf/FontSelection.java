package logs_analysis_tool.example.analysis_tool.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FontSelection {

    private final File blackFile = new File("src/main/resources/fonts/Cera Pro Black.ttf");

    private final File boldFile = new File("src/main/resources/fonts/Cera Pro Bold.ttf");

    private final File lightFile = new File("src/main/resources/fonts/Cera Pro Light.ttf");

    private final File mediumFile = new File("src/main/resources/fonts/Cera Pro Medium.ttf");

    private final File regularItalicFile = new File("src/main/resources/fonts/Cera Pro Regular Italic.ttf");

    public PDType0Font CeraBoldFont(PDDocument document) throws IOException {
        return PDType0Font.load(document,boldFile);
    }


    public PDType0Font CeraBlackFont(PDDocument document) throws IOException {
        return PDType0Font.load(document,blackFile);
    }

    public PDType0Font CeraLightFont(PDDocument document) throws IOException {
        return PDType0Font.load(document,lightFile);
    }

    public PDType0Font CeraMediumFont(PDDocument document) throws IOException {
        return PDType0Font.load(document,mediumFile);
    }

    public PDType0Font CeraRegularItalicFont(PDDocument document) throws IOException {
        return PDType0Font.load(document,regularItalicFile);
    }

}
