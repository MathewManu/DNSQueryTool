package dnsquery;

import org.xbill.DNS.*;

public class DnsQuery {

	public static void main(String[] args) throws TextParseException {
		//System.out.println("haiii");
		if (args.length == 0) {
			printUsageAndExit();
		}

		DigResponse.init();

		DNSResolver resolver = new DNSResolver();
		resolver.resolve(getDigQuery(args));

		DigResponse.printConsole();
	}

	private static void printUsageAndExit() {
		System.out.println("Usage: ./mydig <hostname> <type>");
		System.exit(0);
	}

	private static Dig getDigQuery(String[] args) {

		String name = null;
		String type = null;
		if (args.length == 1)
			name = args[0];
		else if (args.length == 2) {
			name = args[0];
			type = args[1];
		}

		return new Dig.QueryBuilder(name).withType(type).build();
	}

}
