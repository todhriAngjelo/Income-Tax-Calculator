import model.Database;
import model.Receipt;
import model.Taxpayer;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import outputManagePackage.OutputSystem;
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
}
