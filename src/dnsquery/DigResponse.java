	package dnsquery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * This class is used for printing the dig query response 
 * Has fields to save time, answer & auth section to be printed.
 */
public class DigResponse {

	private static long startTime;
	private static String type;
	private static String URL;
	private static List<String> CNameResponseListAuth = new ArrayList<>();
	private static List<String> CNameResponseListAns = new ArrayList<>();
	private static String msgSize;
	
	public void addCnameResponseAuth(String res) {
		CNameResponseListAuth.add(res);
	}

	public void addCnameResponseAns(String res) {
		CNameResponseListAns.add(res);
	}

	public static List<String> getCNameResponseListAuth() {
		return CNameResponseListAuth;
	}

	public static void printConsole() {

		System.out.println(">> QUESTION SECTION:");
		System.out.println(getURL() + "\t" + getType());

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
		DateFormat day = new SimpleDateFormat("E");
		System.out.println("WHEN : "+ day.format(date) +" "+dateFormat.format(date));
		System.out.println(msgSize);
		
		
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

	public static String getMsgSize() {
		return msgSize;
	}

	public static void setMsgSize(String msgSize) {
		DigResponse.msgSize = msgSize;
	}

	public static String getType() {
		return type == null ? "A" : type;
	}

	public static String getURL() {
		return URL;
	}

}
