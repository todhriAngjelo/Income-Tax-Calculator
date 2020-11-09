import model.Database;
import model.Receipt;
import model.Taxpayer;
import outputManagePackage.OutputSystem;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


import utils.ApplicationConstants;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ApplicationTests {

    /**
     * The Database class method needs 2 params,
     * the first one is the path of the folder that contains the files
     * and the second one is a list of the files inside that folder
     **/
    private final List<String> txtTestFilenameList = new ArrayList<>(Collections.singletonList("taxpayer_test_info.txt"));
    private final List<String> xmlTestFilenameList = new ArrayList<>(Collections.singletonList("taxpayer_test_info.xml"));


    /**
     * Initialize the path to the taxpayer test files
     **/
    public ApplicationTests() throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(txtTestFilenameList.get(0)).toURI();
        Database.setTaxpayersInfoFilesPath(Paths.get(uri).getParent().toString());
    }

    /**
     * This method runs after every test and clears the Database.
     **/
    @After
    public void Setup() throws URISyntaxException {
        Database.setTaxpayersArrayList(new ArrayList<Taxpayer>()); // clear the database for next tests
    }

    /**
     * To test the taxpayer info from the txt file we can make a dummy taxpayer with the expected data and
     * a dummy receipt with the expected data and then compare them with the actual objects in memory
     **/
    @Test
    public void testTaxpayerInfoFromTxt() {

        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);

        // Get the taxpayer from the Database
        Taxpayer actualTaxpayerInfoTxt = Database.getTaxpayerFromArrayList(0);

        // Test user info from txt file
        Taxpayer expectedTaxpayerInfoTxt = new Taxpayer("Apostolos Zarras", "130456093",
                ApplicationConstants.MARRIED_FILING_JOINTLY, "22570");

        Receipt expectedTaxpayerReceiptFromTxt =
                new Receipt(ApplicationConstants.BASIC_RECEIPT, "1", "25/2/2014", "2000",
                        "Hand Made Clothes", "Greece", "Ioannina", "Kaloudi", "10");

        expectedTaxpayerInfoTxt.setReceipts(new ArrayList<>( Collections.singletonList(expectedTaxpayerReceiptFromTxt)));
        expectedTaxpayerInfoTxt.calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();

        assertEquals(expectedTaxpayerReceiptFromTxt.toString(), actualTaxpayerInfoTxt.getReceipts().get(0).toString());
        assertEquals(expectedTaxpayerInfoTxt.toString(), actualTaxpayerInfoTxt.toString());

    }

    /**
     * To test the taxpayer info from the xml file we follow the same approach
     **/
    @Test
    public void testTaxpayerInfoFromXml() {

        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), xmlTestFilenameList);

        // Get the taxpayer from the Database
        Taxpayer actualTaxpayerInfoXml = Database.getTaxpayerFromArrayList(0);

        // Test user info from xml file
        Taxpayer expectedTaxpayerInfoFromXml = new Taxpayer("Nikos Zisis", "130456094",
                ApplicationConstants.SINGLE, "40000.0");

        Receipt expectedTaxpayerReceiptFromXml  =
                new Receipt(ApplicationConstants.OTHER_RECEIPT, "1", "25/2/2014", "2000.0",
                        "Omega Watches", "Greece", "Ioannina", "Kaloudi", "4");

        expectedTaxpayerInfoFromXml.setReceipts( new ArrayList<>( Collections.singletonList(expectedTaxpayerReceiptFromXml)));
        expectedTaxpayerInfoFromXml.calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();

        assertEquals(expectedTaxpayerReceiptFromXml.toString(), actualTaxpayerInfoXml.getReceipts().get(0).toString());
        assertEquals(expectedTaxpayerInfoFromXml.toString(), actualTaxpayerInfoXml.toString());

    }

    /**
     * To test the taxpayer tax and final tax after decrease or increase we can calculate the actual
     * amounts and compare them with the expected ones.
     **/
    @Test
    public void testTaxCalculationResult() {
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);

        double actualTax = Database.getTaxpayersArrayList().get(0).getTax();
        double actualFinalTax = Database.getTaxpayersArrayList().get(0).getFinalTax();

        double expectedTax = new BigDecimal(( 5.35 / 100 ) * 22570)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double expectedFinalTax = new BigDecimal(expectedTax + 0.08 * expectedTax)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        assert  expectedTax == actualTax && expectedFinalTax == actualFinalTax;
    }

    /**
     * To test if the .txt info files are written correctly
     * we create a .txt info file and compare it to the expected one.
     */
    @Test
    public void testTxtLogFile() throws IOException {

        Path expectedLogFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"expected_130456093_LOG.txt");
        File expected = new File(expectedLogFilePath.toString());
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        OutputSystem.saveTaxpayerInfoToTxtLogFile(Database.getTaxpayersInfoFilesPath(), 0);
        Path actualLogFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"130456093_LOG.txt");
        File actual = new File(actualLogFilePath.toString());

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test if the .xml info files are written correctly
     * we create a .xml info file and compare it to the expected one.
     */
    @Test
    public void testXmlLogFile() throws IOException {
        Path expectedLogFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"expected_130456093_LOG.xml");
        File expected = new File(expectedLogFilePath.toString());
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        OutputSystem.saveTaxpayerInfoToXmlLogFile(Database.getTaxpayersInfoFilesPath(),0);
        Path actualLogFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"130456093_LOG.xml");
        File actual = new File (actualLogFilePath.toString());
        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * To test methods that create charts
     * we will match the contents of the charts
     * to the objects on memory.
     */
    @Test
    public void testReceiptPieChart(){
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = Database.getTaxpayerFromArrayList(0);
        double actualBasicSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT);
        double actualEntertainmentSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT);
        double actualHealthSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT);
        double actualTravelSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT);
        double actualOtherSum = taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT);
        assertEquals(2000,actualBasicSum,0.00);
        assertEquals(0,actualEntertainmentSum,0.00);
        assertEquals(0,actualHealthSum,0.00);
        assertEquals(0,actualTravelSum,0.00);
        assertEquals(0,actualOtherSum,0.00);
    }

    /**
     * To test methods that create charts
     * we will match the contents of the charts
     * to the objects on memory.
     */
    @Test
    public void testTaxAnalysisBarChart(){
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = Database.getTaxpayerFromArrayList(0);
        double actualTax= taxpayer.getTax();
        double actualFinalTax = taxpayer.getFinalTax();
        double actualTaxVariationAmount = taxpayer.getTaxIncrease()!=0? taxpayer.getTaxIncrease() : taxpayer.getTaxDecrease()*(-1);
        assertEquals(1207.49,actualTax,0);
        assertEquals(1304.09,actualFinalTax,0);
        assertEquals(96.6,actualTaxVariationAmount,0);
    }
    /**
     * Testing saveUpdatedTaxpayerTxtFile on deleting a receipt
     * We create an updated Taxpayer .txt file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerTxtFileOndDelete() throws IOException {
        Path expectedUpdatedFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"expected_deleteReceipt.txt");
        File expected = new File(expectedUpdatedFilePath.toString());

        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = Database.getTaxpayerFromArrayList(0);
        taxpayer.removeReceiptFromList(0);

        Path actualUpdatedFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"testFile");
        File actual = new File (actualUpdatedFilePath.toString());
        OutputSystem.saveUpdatedTaxpayerTxtInputFile(actualUpdatedFilePath.toString(),0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }

    /**
     * Testing saveUpdatedTaxpayerTxtFile on adding a receipt
     * We create an updated Taxpayer .txt file and compare it to the actual one.
     */
    @Test
    public void testSaveUpdatedTaxpayerTxtFileOnAdd() throws IOException {
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), txtTestFilenameList);
        Taxpayer taxpayer = Database.getTaxpayerFromArrayList(0);
        Receipt testReceipt =
                new Receipt(ApplicationConstants.BASIC_RECEIPT, "2", "25/2/2014", "2000.0",
                        "Hand Made Clothes", "Greece", "Chalkida", "Avanton", "10");

        taxpayer.addReceiptToList(testReceipt);

        Path expectedUpdatedFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"expected_addReceipt.txt");
        File expected = new File(expectedUpdatedFilePath.toString());
        Path actualUpdatedFilePath = Paths.get(Database.getTaxpayersInfoFilesPath(),"testFile.txt");
        File actual = new File (actualUpdatedFilePath.toString());

        OutputSystem.saveUpdatedTaxpayerTxtInputFile(actualUpdatedFilePath.toString(),0);

        assertEquals(FileUtils.readLines(expected, StandardCharsets.UTF_8),
                FileUtils.readLines(actual, StandardCharsets.UTF_8));
        actual.delete();
    }
}
