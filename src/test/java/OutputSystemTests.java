import export.OutputSystem;
import persistence.Database;
import model.Receipt;
import model.Taxpayer;

import utils.ApplicationConstants;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OutputSystemTests {

    /**
     * The Database class method needs 2 params,
     * the first one is the path of the folder that contains the files
     * and the second one is a list of the files inside that folder
     **/
    private final List<String> txtTestFilenameList = new ArrayList<>(Collections.singletonList("taxpayer_test_info.txt"));
    private final List<String> xmlTestFilenameList = new ArrayList<>(Collections.singletonList("taxpayer_test_info.xml"));
    Database databaseInstance = Database.getDatabaseInstance();
    /**
     * Initialize the path to the taxpayer test files
     **/
    public OutputSystemTests() throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(txtTestFilenameList.get(0)).toURI();
        Database.getDatabaseInstance().setTaxpayersInfoFilesPath(Paths.get(uri).getParent().toString());
    }

    /**
     * This method runs after every test and clears the Database.
     **/
    @After
    public void cleanDatabase() {
        Database.getDatabaseInstance().setTaxpayersArrayList(new ArrayList<Taxpayer>());
    }


    /**
     * To test the saveUpdatedTaxpayerTxtFile when deleting a receipt
     * we create an updated Taxpayer txt file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerTxtFileOnDelete() throws IOException {
        Path expectedUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"expected_deleteReceipt.txt");
        File expected = new File(expectedUpdatedFilePath.toString());

        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        taxpayer.removeReceiptFromList(0);

        Path actualUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"testFile.txt");
        File actual = new File (actualUpdatedFilePath.toString());
        OutputSystem txtFileOutput = new OutputSystem("txt", ApplicationConstants.txtTags);
        txtFileOutput.saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(),0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test the saveUpdatedTaxpayerTxtFile when adding a receipt
     * we create an updated Taxpayer txt file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerTxtFileOnAdd() throws IOException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        Receipt testReceipt =
                new Receipt(ApplicationConstants.BASIC_RECEIPT, "2", "25/2/2014", "2000.0",
                        "Hand Made Clothes", "Greece", "Chalkida", "Avanton", "10");

        taxpayer.addReceiptToList(testReceipt);

        Path expectedUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(), "expected_addReceipt.txt");
        File expected = new File(expectedUpdatedFilePath.toString());
        Path actualUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(), "testFile.txt");
        File actual = new File (actualUpdatedFilePath.toString());

        OutputSystem txtFileOutput = new OutputSystem("txt", ApplicationConstants.txtTags);
        txtFileOutput.saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(), 0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test the saveUpdatedTaxpayerXmlFile when deleting a receipt
     * we create an updated Taxpayer.xml file and compare it to the actual one.
     */
    @Test
    public  void testSaveUpdatedTaxpayerXmlFileOnDelete() throws IOException {
        Path expectedUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"expected_deleteReceipt.xml");
        File expected = new File(expectedUpdatedFilePath.toString());

        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), xmlTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        taxpayer.removeReceiptFromList(0);

        Path actualUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"testFile.xml");
        File actual = new File (actualUpdatedFilePath.toString());
        OutputSystem xmlFileOutput = new OutputSystem("xml", ApplicationConstants.xmlTags);
        xmlFileOutput.saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(),0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test the saveUpdatedTaxpayerXmlFile when adding a receipt
     * we create an updated Taxpayer .xml file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerXmlFileOnAdd() throws IOException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), xmlTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        Receipt testReceipt =
                new Receipt(ApplicationConstants.BASIC_RECEIPT, "2", "25/2/2014", "2000.0",
                        "Hand Made Clothes", "Greece", "Chalkida", "Avanton", "10");

        taxpayer.addReceiptToList(testReceipt);

        Path expectedUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(), "expected_addReceipt.xml");
        File expected = new File(expectedUpdatedFilePath.toString());
        Path actualUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(), "testFile.xml");
        File actual = new File (actualUpdatedFilePath.toString());
        OutputSystem xmlFileOutput = new OutputSystem("xml", ApplicationConstants.xmlTags);
        xmlFileOutput.saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(), 0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }
}
