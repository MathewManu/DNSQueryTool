package dnsquery;

import java.util.List;

import org.xbill.DNS.*;

public class DnsQuery {

	public static void main(String[] args) throws TextParseException {
		DNSResolver resolver = new DNSResolver();
		resolver.resolve(getDigQuery(args));
		
		DNSResolver.getDigReponse();
		System.out.println("CALLER!!");
		List<String> l = DigResponse.getCNameResponseList();
		for (int i=l.size()-1;i>=0;i--)
			System.out.println(l.get(i));
	}

	private static Dig getDigQuery(String[] args) {

		String name = "www.netapp.com";
		String type = null;

		return new Dig.QueryBuilder(name).withType(type).build();

	}
}
