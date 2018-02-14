package dnsquery;

import java.net.UnknownHostException;

import org.xbill.DNS.SimpleResolver;

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
	private String type;
	private boolean isCnameMsg;

	private Dig(QueryBuilder queryBuilder) {
		URL = queryBuilder.URL;
		resolver = queryBuilder.resolver;
		type = queryBuilder.type;
		isCnameMsg = queryBuilder.isCnameMsg;
	}

	public String getURL() {
		return URL;
	}

	public boolean getIsCnameMsg() {
		return isCnameMsg;
	}

	public String getType() {
		return type;
	}

	public SimpleResolver getResolver() {
		return resolver;
	}

	/* static query builder class */
	public static class QueryBuilder {

		private final String URL;
		private String type;
		private SimpleResolver resolver;
		private boolean isCnameMsg = false;

		public QueryBuilder(String name) {
			URL = name;
			resolver = getRootServerResolver();
		}

		public QueryBuilder withType(String type) {
			this.type = type;
			return this;
		}

		public QueryBuilder withCNameType(boolean isCnameMsg) {
			this.isCnameMsg = isCnameMsg;
			return this;
		}

		/* return Dig object with Querybuilder arg */
		public Dig build() {
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