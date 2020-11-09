import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ InputSystemTests.class, TaxpayerTests.class, OutputSystemTests.class })
public class TestSuite {
    //no need to add sth here. The above directives simply run all tests
}