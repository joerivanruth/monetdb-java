import org.monetdb.testinfra.Config;

/**
 * Invoked by Mtest to run the TLS tests.
 */
public class OnClientTester extends JUnitTester {
	public static void main(String[] args) {
		runTests("onclient", Config.SERVER_URL_PROPERTY, args);
	}
}


