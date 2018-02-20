package dnsquery;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

/* 
 * Builder class for Dig object.
 * Can be extended for multiple arguments easily.
 * Builder pattern reference: 
 * https://jlordiales.me/2012/12/13/the-builder-pattern-in-practice/
 * 
 * Also select a working root server 
 */

public class Dig {

	private final String URL;
	private final SimpleResolver resolver;
	private int type;
	private String digType;
	private boolean isCnameMsg;

	private Dig(QueryBuilder queryBuilder) {
		URL = queryBuilder.URL;
		resolver = queryBuilder.resolver;
		type = queryBuilder.type;
		isCnameMsg = queryBuilder.isCnameMsg;
		digType = queryBuilder.digType;
	}

	public String getURL() {
		return URL;
	}

	public boolean getIsCnameMsg() {
		return isCnameMsg;
	}

	public int getType() {
		return type;
	}

	public String getDigType() {
		return digType;
	}

	public SimpleResolver getResolver() {
		return resolver;
	}

	/* static query builder class */
	public static class QueryBuilder {

		private final String URL;
		private int type;
		private String digType;
		private SimpleResolver resolver;
		private boolean isCnameMsg = false;

		public QueryBuilder(String name) {
			URL = name;
			resolver = getRootServerResolver();
		}

		public QueryBuilder withType(String type) {
			digType = type;
			if (type == null) {
				this.type = Type.A;
				return this;
			}
			if (type.equalsIgnoreCase("A"))
				this.type = Type.A;
			else if (type.equalsIgnoreCase("NS"))
				this.type = Type.NS;
			else if (type.equalsIgnoreCase("MX"))
				this.type = Type.MX;
			else
				this.type = Type.A;

			return this;
		}

		public QueryBuilder withCNameType(boolean isCnameMsg) {
			this.isCnameMsg = isCnameMsg;
			return this;
		}

		/* return Dig object with Querybuilder arg */
		
		public Dig build() {
			
			DigResponse.setType(digType);
			if (DigResponse.getURL() == null)
				DigResponse.setURL(URL);
			
			return new Dig(this);
		}

		/* create a resolver from a root server.
		 * if one root-server isn't available next one 
		 * is queried.
		 */
		private SimpleResolver getRootServerResolver() {
			List<String> rootsevers = Arrays.asList("a.root-servers.net", "b.root-servers.net", "c.root-servers.net",
					"d.root-servers.net", "e.root-servers.net", "f.root-servers.net", "g.root-servers.net",
					"h.root-servers.net", "i.root-servers.net", "j.root-servers.net", "k.root-servers.net",
					"l.root-servers.net", "m.root-servers.net");
			SimpleResolver r = null;
			int pos = 0;
			int failure = 0;
			while (pos == 0 || failure == 1) {
				try {
					r = new SimpleResolver(rootsevers.get(pos));
					failure = 0; pos++;
				} catch (UnknownHostException e) {
					System.out.println("Trying next root server... ");
					failure = 1;
					pos++;
					if (pos > 12) {
						System.out.println("ERROR: could not get root server to query !!!");
						break;
					}
				}
			}
			return r;
		}

	}
}