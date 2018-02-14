package dnsquery;

import org.xbill.DNS.*;

public class Dnsquery {

	public static void main(String[] args) throws TextParseException {
		DNSResolver resolver = new DNSResolver();
		resolver.resolve(getDigQuery(args));
	}

	private static Dig getDigQuery(String[] args) {

		String name = "www.netapp.com";
		String type = null;

		return new Dig.QueryBuilder(name).withType(type).build();

	}
}
