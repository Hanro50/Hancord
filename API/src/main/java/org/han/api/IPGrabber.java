package org.han.api;

import java.net.*;
import java.io.*;

public class IPGrabber {
	public static String getIP() throws IOException {
		URL whatismyip;
		whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		String ip = in.readLine();
		return ip;
	}

}
