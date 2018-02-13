package dnsquery;

import java.io.IOException;
import org.xbill.DNS.*;

public class Dnsquery {

	public static void main(String[] args) throws TextParseException {
		// append . at the end of hostname here.

		// DnsQueryProcessor.setquery("www.cnn.com.");
		 //DnsQueryProcessor.setquery("www.cs.stonybrook.edu.");
		//DnsQueryProcessor.setquery("ec2-107-22-178-157.compute-1.amazonaws.com.");
		//DnsQueryProcessor.process();

		DNSResolver resolver = new DNSResolver();
		resolver.resolve(getDigQuery(args));
	}

	private static Dig getDigQuery(String[] args) {
		
		String name = "www.cs.stonybrook.edu.";
		String type = null;
		
		return new Dig.QueryBuilder(name)
				.withType(type).build();
		
	}
}
	