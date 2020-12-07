import model.Receipt;
import persistence.Database;
import model.Taxpayer;

import org.junit.After;
import org.junit.Test;
import utils.ApplicationConstants;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaxpayerTests {

    Database databaseInstance = Database.getDatabaseInstance();

    private final List<String> TestFilenameList = new ArrayList<>(
            Collections.singletonList("married_filing_jointly_taxpayer_test_info.txt")
    );

    /**
     * Initialize the path to the taxpayer test files
     **/
    public TaxpayerTests() throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(TestFilenameList.get(0)).toURI();
        databaseInstance.setTaxpayersInfoFilesPath(Paths.get(uri).getParent().toString());
    }

    /**
     * This method runs after every test and cleans the Database.
     **/
    @After
    public void cleanDatabase() {
        Database.getDatabaseInstance().setTaxpayersArrayList(new ArrayList<Taxpayer>());
    }

    /**
     * To test the taxpayer tax and final tax after decrease or increase we can calculate the actual
     * amounts and compare them with the expected ones.
     **/
    @Test
    public void testTaxCalculationResult() throws FileNotFoundException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), TestFilenameList);

        double actualTax = databaseInstance.getTaxpayersArrayList().get(0).getTax();
        double actualFinalTax = databaseInstance.getTaxpayersArrayList().get(0).getFinalTax();

        double expectedTax = BigDecimal.valueOf( (5.35 / 100) * 22570 ).doubleValue();
        double expectedFinalTax = BigDecimal.valueOf( expectedTax + 0.08 * expectedTax ).doubleValue();

        assert  expectedTax == actualTax && expectedFinalTax == actualFinalTax;
    }

    /**
     * To test the taxpayer final tax after adding a receipt we add a dummy receipt to the receipts list and
     * we check the new final tax with the actual one
     **/
    @Test
    public void testTaxCalculationResultOnAdd() throws FileNotFoundException {
        Database.processTaxpayersDataFromFilesIntoDatabase(databaseInstance.getTaxpayersInfoFilesPath(), TestFilenameList);

        Taxpayer taxpayer = databaseInstance.getTaxpayersArrayList().get(0);

        Receipt dummyReceipt =
                new Receipt(ApplicationConstants.BASIC_RECEIPT, "2", "25/2/2020", "5000",
                        "Hand Made Clothes", "Greece", "Ioannina", "Kaloudi", "10");
        taxpayer.addReceiptToList(dummyReceipt);
        assert  taxpayer.getFinalTax() ==  taxpayer.getTax() + 0.04 * taxpayer.getTax();
    }

}
