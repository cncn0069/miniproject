package edu.pnu.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerIpUtil {
	 public static String getServerIp() {
	        try {
	            return InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
