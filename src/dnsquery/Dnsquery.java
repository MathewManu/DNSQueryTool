package dnsquery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.xbill.DNS.*;

public class Dnsquery {

	// static SimpleResolver r;

	public static void main(String[] args) throws TextParseException {
		// append . at the end of hostname here.

		// DnsQueryProcessor.setquery("www.cnn.com.");
		 //DnsQueryProcessor.setquery("www.cs.stonybrook.edu.");
		DnsQueryProcessor.setquery("ec2-107-22-178-157.compute-1.amazonaws.com.");
		DnsQueryProcessor.process();

	}
}
	