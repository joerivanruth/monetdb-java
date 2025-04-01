import org.monetdb.testinfra.Config;
import org.monetdb.testinfra.MtestLauncher;

import static java.lang.System.exit;

/**
 * Invoked by Mtest to run the TLS tests.
 */
public class TLSTester {
	public static void main(String[] args) {
		String tlstester = null;
		boolean verbose = false;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("-v"))
				verbose = true;
			else if (arg.equals("-a"))
				i++;
			else if (!arg.startsWith("-"))
				tlstester = arg;
			else {
				System.err.println("Invalid flag " + arg);
				exit(1);
			}
		}

		MtestLauncher launcher = new MtestLauncher("tls");

		launcher.setVerbose(verbose);
		if (tlstester != null) {
			launcher.always().println("Command line: setting " + Config.TLSTESTER_PROPERTY + " to " + tlstester);
			System.setProperty(Config.TLSTESTER_PROPERTY, tlstester);
		}

		int status = launcher.run();
		System.exit(status);
	}
}


