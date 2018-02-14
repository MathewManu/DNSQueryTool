package dnsquery;

import java.util.ArrayList;
import java.util.List;

import org.xbill.DNS.Record;

public class DigResponse {
	
	private static List<String> CNameResponseList = new ArrayList<>();
	List<Record> answerSection = new ArrayList<>();
	
	public void addCnameResponse(String res) {
		CNameResponseList.add(res);
	}

	public void addAnswerSection(Record rec) {
		answerSection.add(rec);
	}
	
	public static List<String> getCNameResponseList() {
		return CNameResponseList;
	}
	
}
