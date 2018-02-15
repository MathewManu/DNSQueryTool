package dnsquery;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DNSResolver {

	private Name name = null;
	private static DigResponse digReponse = new DigResponse();
	private static String digType;
	
	public static DigResponse getDigReponse() {
		return digReponse;
	}

	public void resolve(Dig digQuery) {
		setName(digQuery.getURL());
		setType(digQuery.getDigType());
		
		try {
			Message response = sendQuery(digQuery);
			responseHandler(response, digQuery.getIsCnameMsg());
		} catch (IOException e) {
			System.out.println("ERROR: " + digQuery.getURL());
		}

	}
	
	/*
	 * create a newQuery using the user entered URL send the query to the server
	 * based on the resolver. return : response Message from the server.
	 */
	private Message sendQuery(Dig digQuery) throws IOException {
		Message msg = Message.newQuery(Record.newRecord(getName(), getType(), DClass.IN));
		return digQuery.getResolver().send(msg);

	}

	private void responseHandler(Message res, boolean isCnameMsg) {

		List<Record> myList = new ArrayList<>();
		int answerSectionCount = res.getHeader().getCount(Section.ANSWER);
		
		if (answerSectionCount == 0) {
			Record[] rec = res.getSectionArray(Section.AUTHORITY);
			for (int i = 0; i < rec.length; i++) {
				myList.add(rec[i]);
			}
			// as of now invoke with first result.
			// getAdditionalName gives the address from the result.
			invokeNextLevelQuery(myList.get(0).getAdditionalName().toString(), isCnameMsg);

		} else {
			/*
			 * There could be cases like the following. 1. Answer section has Both CNAME & A
			 * record. In that case, returning the A record IP should be good enough 2.
			 * Answer section only has CNAME. In this case, we would need to again query for
			 * the CNAME recursively. eg: www.cs.stonybrook.edu.
			 */
			
			/* in case of NS & MX records, CNAME need not be resolved further 
			 * Output answer section, and any Authority sections present.
			 */
			Record[] rec = res.getSectionArray(Section.ANSWER);
			if (getDigType() != null && (getDigType().equalsIgnoreCase("MX") || getDigType().equalsIgnoreCase("NS"))) {

				/* adding answer section */
				for (int i = 0; i < rec.length; i++) {
					digReponse.addCnameResponseAns(rec[i].toString());
				}

				Record[] auth = res.getSectionArray(Section.AUTHORITY);
				for (int i = 0; i < auth.length; i++) {
					digReponse.addCnameResponseAuth(auth[i].toString());
				}
				return;
			}
		
			for (int i = 0; i < rec.length; i++) {
				if (answerSectionCount == 1 && rec[i].getType() == Type.CNAME) {
					String cnameLine = rec[i].toString();
					String cname = cnameLine.substring(cnameLine.indexOf("CNAME") + 5).trim();
					/* CNAME recursive resolve */
					handleCNAMEresolve(cname);
				}
				if (isCnameMsg == true) {
					/* type A */
					digReponse.addCnameResponseAns(rec[i].toString());
					return;
				}
				digReponse.addCnameResponseAns(rec[i].toString());
			}
		}

	}
	
	private void handleCNAMEresolve(String cname) {
		
		Dig cnameQuery = new Dig.QueryBuilder(cname).withCNameType(true).withType(getDigType()).build();
		new DNSResolver().resolve(cnameQuery);	
	}

	/*
	 * Invoke next level with the the new URL
	 * Call the same function  responseHandler to handle the response
	 */
	private void invokeNextLevelQuery(String nextLevelUrl, boolean isCnameMsg) {
		Message res = null;
		Message req = Message.newQuery(Record.newRecord(getName(), Type.A, DClass.IN));
		SimpleResolver resolver = getResolver(nextLevelUrl);

		try {
			if (null != resolver) {
				res = resolver.send(req);
			}
		} catch (IOException e) {
			System.out.println("ERROR: while sending : " + getName() + " " + nextLevelUrl);
		}
		responseHandler(res, isCnameMsg);
	}
	
	/*
	 * Resolver is with the URL is returned.
	 * Root-server is generated from Dig class. Not here.
	 */
	private static SimpleResolver getResolver(String name) {
		SimpleResolver r = null;
		try {
			r = new SimpleResolver(name);
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Unknown Host : " + name);
		}
		return r;
	}

	/* returns Name object */
	private Name getName() {
		return name;
	}

	/*
	 * Appending . at the end of URL 
	 */
	private void setName(String url) {
		if (!url.substring(url.length() - 1).equals(".")) {
			url += ".";
		}
		try {
			name = new Name(url);
		} catch (TextParseException e) {
			System.out.println("ERROR while setting Name for URL : " + url);
		}
	}
	
	private void setType(String type) {
		digType = type;
	}

	private static String getDigType() {
		return digType;
	}
	
	public static int getType() {
		if (digType == null)
			return Type.A;
		if (digType.equalsIgnoreCase("A"))
			return Type.A;
		else if (digType.equalsIgnoreCase("MX"))
			return Type.MX;
		else if (digType.equalsIgnoreCase("NS"))
			return Type.NS;
		return Type.A;
	}
}
