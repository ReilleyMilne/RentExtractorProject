package GeneralWebsiteFiles;

import java.io.File;
import java.io.IOException;

public class CSVEditorTester {
    public static void main(String[] args) throws IOException{
        File file = new File("C:\\Users\\Reilley\\OneDrive\\Desktop\\VS Code Projects\\RentExtractor\\RentExtraction\\test.csv");
        CSVEditor edit = new CSVEditor(file);

        edit.sortByLength();
    }
}