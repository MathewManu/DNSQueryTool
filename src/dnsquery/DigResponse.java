package dnsquery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xbill.DNS.Record;

public class DigResponse {

	private static String type;
	private static String URL;
	private static List<String> CNameResponseList = new ArrayList<>();
	private static List<String> CNameResponseListAuth = new ArrayList<>();
	private static List<String> CNameResponseListAns = new ArrayList<>();
	List<Record> answerSection = new ArrayList<>();
	private static long startTime;

	public void addCnameResponse(String res) {
		CNameResponseList.add(res);
	}

	public void addCnameResponseAuth(String res) {
		CNameResponseListAuth.add(res);
	}

	public void addCnameResponseAns(String res) {
		CNameResponseListAns.add(res);
	}

	public void addAnswerSection(Record rec) {
		answerSection.add(rec);
	}

	public static List<String> getCNameResponseList() {
		return CNameResponseList;
	}

	public static List<String> getCNameResponseListAuth() {
		return CNameResponseListAuth;
	}

	public static void printConsole() {

		// String output = String.format("%s = %d", "joe", 35);
		System.out.println(">> QUESTION SECTION:");
		System.out.println(getURL() + "\t" + getType());

		/*System.out.println("\n>> ANSWER SECTION:");
		for (int i = CNameResponseList.size() - 1; i >= 0; i--)
			System.out.println(CNameResponseList.get(i));
*/
		System.out.println("\n>> ANSWER SECTION:");
		for (int i = CNameResponseListAns.size() - 1; i >= 0; i--)
			System.out.println(CNameResponseListAns.get(i));
		
		System.out.println("\n>> AUTHORITY SECTION:");
		for (int i = CNameResponseListAuth.size() - 1; i >= 0; i--)
			System.out.println(CNameResponseListAuth.get(i));

		long end = System.currentTimeMillis();

		System.out.println("\nQuery time : " + (end - startTime) + " msec");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
		Date date = new Date();
		System.out.println("WHEN : " + dateFormat.format(date));

	}

	public static void init() {
		startTime = System.currentTimeMillis();
	}

	public static void setType(String digType) {
		type = digType;
	}

	public static void setURL(String url) {
		URL = url;
	}

	public static String getType() {
		return type == null ? "A" : type;
	}

	public static String getURL() {
		return URL;
	}

}
