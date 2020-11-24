import exporters.TxtFileOutput;
import exporters.XmlFileOutput;
import persistence.Database;
import model.Receipt;
import model.Taxpayer;
import exporters.OutputSystem;

import utils.ApplicationConstants;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
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
     * To test the methods that create charts we will compare the expected content values of the charts
     * to the actual values in memory.
     */
    @Test
    public void testReceiptPieChart() throws FileNotFoundException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        double actualBasicSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT);
        double actualEntertainmentSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT);
        double actualHealthSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT);
        double actualTravelSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT);
        double actualOtherSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT);
        assertEquals(2000, actualBasicSum, 0.00);
        assertEquals(0, actualEntertainmentSum, 0.00);
        assertEquals(0, actualHealthSum, 0.00);
        assertEquals(0, actualTravelSum, 0.00);
        assertEquals(0, actualOtherSum, 0.00);
    }

    @Test
    public void testTaxAnalysisBarChart() throws FileNotFoundException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        double actualTax = taxpayer.getTax();
        double actualFinalTax = taxpayer.getFinalTax();
        double actualTaxVariationAmount = taxpayer.getTaxIncrease() != 0 ? taxpayer.getTaxIncrease() : taxpayer.getTaxDecrease()*(-1);
        assertEquals(1207.495, actualTax, 0);
        assertEquals(1304.0946, actualFinalTax, 0);
        assertEquals(96.5996, actualTaxVariationAmount, 0);
    }

    /**
     * To test the saveUpdatedTaxpayerTxtFile when deleting a receipt
     * we create an updated Taxpayer txt file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerTxtFileOndDelete() throws IOException {
        Path expectedUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"expected_deleteReceipt.txt");
        File expected = new File(expectedUpdatedFilePath.toString());

        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = databaseInstance.getTaxpayerFromArrayList(0);
        taxpayer.removeReceiptFromList(0);

        Path actualUpdatedFilePath = Paths.get(databaseInstance.getTaxpayersInfoFilesPath(),"testFile");
        File actual = new File (actualUpdatedFilePath.toString());
        TxtFileOutput.getTxtFileOutputInstance().saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(),0);

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

        TxtFileOutput.getTxtFileOutputInstance().saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(), 0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test the saveUpdatedTaxpayerXmlFile when deleting a receipt
     * we create an updated Taxpayer .xml file and compare it to the actual one.
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
        XmlFileOutput.getXmlFileOutputInstance().saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(),0);

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

        XmlFileOutput.getXmlFileOutputInstance().saveUpdatedTaxpayerInputFile(actualUpdatedFilePath.toString(), 0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }
}
