import org.monetdb.testinfra.Config;
import org.monetdb.testinfra.MtestLauncher;

import static java.lang.System.exit;

/**
 * Invoked by Mtest to run the TLS tests.
 */
public class OnClientTester {
	public static void main(String[] args) {
		String server = null;
		boolean verbose = false;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("-v"))
				verbose = true;
			else if (!arg.startsWith("-"))
				server = arg;
			else {
				System.err.println("Invalid flag " + arg);
				exit(1);
			}
		}

		MtestLauncher launcher = new MtestLauncher("onclient");

		launcher.setVerbose(verbose);
		if (server != null) {
			launcher.always().println("Command line: setting " + Config.SERVER_URL_PROPERTY + " to " + server);
			System.setProperty(Config.SERVER_URL_PROPERTY, server);
		}

		int status = launcher.run();
		System.exit(status);
	}
}


