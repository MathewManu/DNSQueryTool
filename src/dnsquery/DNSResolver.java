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
	
	public void resolve(Dig digQuery) {
		setName(digQuery.getURL());
		try {
			Message response = sendQuery(digQuery);
			responseHandler(response);
		} catch (IOException e) {
			System.out.println("ERROR: " + digQuery.getURL());
		}

	}

	/*
	 * create a newQuery using the user entered URL send the query to the server
	 * based on the resolver. return : response Message from the server.
	 */
	private Message sendQuery(Dig digQuery) throws IOException {
		Message msg = Message.newQuery(Record.newRecord(getName(), Type.A, DClass.IN));
		return digQuery.getResolver().send(msg);

	}

	private void responseHandler(Message res) {

		List<Record> myList = new ArrayList<>();
		int answerSectionCount = res.getHeader().getCount(Section.ANSWER);
		//System.out.println("count is :" + answerSectionCount);

		if (answerSectionCount == 0) {
			Record[] rec = res.getSectionArray(Section.AUTHORITY);
			for (int i = 0; i < rec.length; i++) {
				//System.out.println(rec[i].getAdditionalName());
				myList.add(rec[i]);
			}
			// as of now invoke with first result.
			// getAdditionalName gives the address from the result.
			invokeNextLevelQuery(myList.get(0).getAdditionalName().toString());

		} else {
			System.out.println("answer section found !!!!");
			/*
			 * There could be cases like the following. 1. Answer section has Both CNAME & A
			 * record. In that case, returning the A record IP should be good enough 2.
			 * Answer section only has CNAME. In this case, we would need to again query for
			 * the CNAME recursively. eg: www.cs.stonybrook.edu.
			 */
			
			Record[] rec = res.getSectionArray(Section.ANSWER);
			for (int i = 0; i < rec.length; i++) {
				if (answerSectionCount == 1 && rec[i].getType() == Type.CNAME) {
					String cnameLine = rec[i].toString();
					String cname = cnameLine.substring(cnameLine.indexOf("CNAME") + 5).trim();
					System.out.println("found!!!! " + cname);
					String cnameresponse = handleCNAMEresolve(cname);
					System.out.println("---> : "  + cnameresponse);
				}
				System.out.println(rec[i]);
				myList.add(rec[i]);
			}
		}

	}
	
	private String  handleCNAMEresolve(String cname) {
		
		Dig cnameQuery = new Dig.QueryBuilder(cname).build();
		
		return cname;
		// TODO Auto-generated method stub
		
	}

	private void invokeNextLevelQuery(String nextLevelUrl) {
		System.out.println("====Next Leve========");
		
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
		responseHandler(res);

	}
	
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

	private void setName(String url) {
		/* append . if url is not ending with ." */
		if (url.substring(url.length()-1) != ".") {
			url += ".";
		}
		try {
			name = new Name(url);
		} catch (TextParseException e) {
			System.out.println("ERROR: " + url);
		}

	}

}
