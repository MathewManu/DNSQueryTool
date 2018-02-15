package dnsquery;

import java.net.UnknownHostException;

import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

/* 
 * Builder class for Dig object.
 * Can be extended for multiple arguments easily
 * Builder pattern reference: 
 * https://jlordiales.me/2012/12/13/the-builder-pattern-in-practice/
 * 
 * Also select root server as well.
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

		/* Root server return */
		private SimpleResolver getRootServerResolver() {
			SimpleResolver r = null;
			try {
				/* should read from file with retry */
				r = new SimpleResolver("a.root-servers.net");
			} catch (UnknownHostException e) {
				System.out.println("ERROR: Unknown Host : ");
			}
			return r;
		}

	}
}