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

public class DnsQueryProcessor {

	private static String query;
	private static Name name = null;

	public static void process() {
		
		Message res = null;
		try {
			Message m = Message.newQuery(Record.newRecord(getName(), Type.A, DClass.IN));
			SimpleResolver r = getRootServerResolver();
			res = r.send(m);
			responseHandler(res);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * called for every response.
	 * if ANSWER section count == 0
	 * next level query is sent  
	 */
	private static void responseHandler(Message res) {

		List<Record> myList = new ArrayList<>();
		int answerSectionCount = res.getHeader().getCount(Section.ANSWER);
		System.out.println("count is :" + answerSectionCount);
		
		if (answerSectionCount == 0) {
			
			//System.out.println(res);
			Record[] rec = res.getSectionArray(Section.AUTHORITY);
			for (int i = 0; i < rec.length; i++) {
				System.out.println(rec[i].getAdditionalName());
				myList.add(rec[i]);
			}
			// as of now invoke with first result.
			// getAdditionalName gives the address from the result.
			invokeNextLevelQuery(myList.get(0).getAdditionalName().toString());

		} else {
			System.out.println("answer section found !!!!");
			/*
			 * There could be cases like the following.
			 *  1. Answer section has Both CNAME & A
			 * record. In that case, returning the A record IP should be good enough 
			 * 2. Answer section only has CNAME.
			 *  In this case, we would need to again query for
			 * the CNAME recursively. eg: www.cs.stonybrook.edu.
			 */
			//System.out.println(res);
			Record[] rec = res.getSectionArray(Section.ANSWER);
			for (int i = 0; i < rec.length; i++) {
				System.out.println(rec[i]);

				// System.out.println(rec[i].getAdditionalName());
				myList.add(rec[i]);
			}

			/*
			 * if (answerSectionCount == 2) {
			 * 
			 * } else if (answerSectionCount == 1) {
			 * 
			 * } myList.add(arg0)
			 */
		}

	}

	private static void invokeNextLevelQuery(String name) {
		System.out.println("====Next Leve========");
		
		Message res = null;
		Message m = Message.newQuery(Record.newRecord(getName(), Type.A, DClass.IN));
		SimpleResolver r = getResolver(name);

		try {
			if (null != r) {
				res = r.send(m);
			}
		} catch (IOException e) {
			System.out.println("ERROR: while sending : " + getName() + " " + name);
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

	private static Name getName() {
		try {
			if (name == null)
				name = new Name(getQuery());
		} catch (TextParseException e) {
			System.out.println("ERROR: " + getQuery());
		}
		return name;
	}

	private static SimpleResolver getRootServerResolver() {
		SimpleResolver r = null;
		try {
			r = new SimpleResolver("a.root-servers.net");
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Unknown Host : ");
		}
		return r;
	}

	private static String getQuery() {
		return query;
	}

	public static void setquery(String url) {
		query = url;
	}

}
