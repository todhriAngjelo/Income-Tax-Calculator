import persistence.Database;
import model.Taxpayer;

import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaxpayerTests {

    private final List<String> TestFilenameList = new ArrayList<>(
            Collections.singletonList("married_filing_jointly_taxpayer_test_info.txt")
    );

    /**
     * Initialize the path to the taxpayer test files
     **/
    public TaxpayerTests() throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(TestFilenameList.get(0)).toURI();
        Database.setTaxpayersInfoFilesPath(Paths.get(uri).getParent().toString());
    }

    /**
     * This method runs after every test and cleans the Database.
     **/
    @After
    public void cleanDatabase() {
        Database.setTaxpayersArrayList(new ArrayList<Taxpayer>());
    }

    /**
     * To test the taxpayer tax and final tax after decrease or increase we can calculate the actual
     * amounts and compare them with the expected ones.
     **/
    @Test
    public void testTaxCalculationResult() {
        Database.processTaxpayersDataFromFilesIntoDatabase(Database.getTaxpayersInfoFilesPath(), TestFilenameList);

        double actualTax = Database.getTaxpayersArrayList().get(0).getTax();
        double actualFinalTax = Database.getTaxpayersArrayList().get(0).getFinalTax();

        double expectedTax = BigDecimal.valueOf( (5.35 / 100) * 22570 ).doubleValue();
        double expectedFinalTax = BigDecimal.valueOf( expectedTax + 0.08 * expectedTax ).doubleValue();

        assert  expectedTax == actualTax && expectedFinalTax == actualFinalTax;
    }

}
