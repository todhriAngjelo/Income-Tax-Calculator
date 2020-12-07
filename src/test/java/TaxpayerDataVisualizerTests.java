import model.Taxpayer;
import org.junit.After;
import org.junit.Test;
import persistence.Database;
import utils.ApplicationConstants;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaxpayerDataVisualizerTests {

    private final List<String> txtTestFilenameList = new ArrayList<>(Collections.singletonList("taxpayer_test_info.txt"));
    Database databaseInstance = Database.getDatabaseInstance();
    /**
     * Initialize the path to the taxpayer test files
     **/
    public TaxpayerDataVisualizerTests() throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(txtTestFilenameList.get(0)).toURI();
        Database.getDatabaseInstance().setTaxpayersInfoFilesPath(Paths.get(uri).getParent().toString());
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
     * This method runs after every test and clears the Database.
     **/
    @After
    public void cleanDatabase() {
        Database.getDatabaseInstance().setTaxpayersArrayList(new ArrayList<Taxpayer>());
    }
}
