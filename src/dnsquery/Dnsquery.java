package dnsquery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xbill.DNS.*;

public class DnsQuery {

	public static void main(String[] args) throws TextParseException {
		DNSResolver resolver = new DNSResolver();
		long start = System.currentTimeMillis();
		resolver.resolve(getDigQuery(args));

		DNSResolver.getDigReponse();
		// System.out.println("CALLER!!");

		List<String> l = DigResponse.getCNameResponseList();
		for (int i = l.size() - 1; i >= 0; i--)
			System.out.println(l.get(i));

		long end = System.currentTimeMillis();

		System.out.println("\nQuery time : " + (end - start) + " msec");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
		Date date = new Date();
		System.out.println("WHEN : " + dateFormat.format(date));
	}

	private static Dig getDigQuery(String[] args) {

		String name = "www.netapp.com";
		String type = null;

		return new Dig.QueryBuilder(name).withType(type).build();

	}
}
