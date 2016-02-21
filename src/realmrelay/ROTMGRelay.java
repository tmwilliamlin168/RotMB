package realmrelay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import realmrelay.crypto.GUID;
import realmrelay.packets.Packet;


public final class ROTMGRelay {
	
	public static final ROTMGRelay instance = new ROTMGRelay();
	
	// #settings
	
	public boolean bUseProxy = false;
	public String proxyHost = "socks4or5.someproxy.net";
	public int proxyPort = 1080;
	
	public String remoteHost = "54.226.214.216";
	public int remotePort = 2050;
	
	public String key0 = "311f80691451c71d09a13a2a6e";
	public String key1 = "72c5583cafb6818995cdd74b80";

	// #settings end
	
	private final Map<Integer, InetSocketAddress> gameIdSocketAddressMap = new Hashtable<Integer, InetSocketAddress>();
	private final Map<String, Object> globalVarMap = new Hashtable<String, Object>();
	
	public static long startTime;
	
	private ROTMGRelay() {
		Properties p = new Properties();
		p.setProperty("bUseProxy", String.valueOf(this.bUseProxy));
		p.setProperty("proxyHost", this.proxyHost);
		p.setProperty("proxyPort", String.valueOf(this.proxyPort));
		p.setProperty("remoteHost", this.remoteHost);
		p.setProperty("remotePort", String.valueOf(this.remotePort));
		p.setProperty("key0", this.key0);
		p.setProperty("key1", this.key1);
		File file = new File("settings.properties");
		if (!file.isFile()) {
			try {
				OutputStream out = new FileOutputStream(file);
				p.store(out, null);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		p = new Properties(p);
		try {
			InputStream in = new FileInputStream(file);
			p.load(in);
			in.close();
			this.bUseProxy = Boolean.parseBoolean(p.getProperty("bUseProxy"));
			this.proxyHost = p.getProperty("proxyHost");
			this.proxyPort = Integer.parseInt(p.getProperty("proxyPort"));
			this.remoteHost = p.getProperty("remoteHost");
			this.remotePort = Integer.parseInt(p.getProperty("remotePort"));
			this.key0 = p.getProperty("key0");
			this.key1 = p.getProperty("key1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * error message
	 * @param message
	 */
	public static void error(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timestamp = sdf.format(new Date());
		String raw = timestamp + " " + message;
		System.err.println(raw);
	}
	
	/**
	 * echo message
	 * @param message
	 */
	public static void echo(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timestamp = sdf.format(new Date());
		String raw = timestamp + " " + message;
		System.out.println(raw);
	}
	
	public Object getGlobal(String var) {
		return this.globalVarMap.get(var);
	}
	
	public InetSocketAddress getSocketAddress(int gameId) {
		InetSocketAddress socketAddress = this.gameIdSocketAddressMap.get(gameId);
		if (socketAddress == null) {
			return new InetSocketAddress(this.remoteHost, this.remotePort);
		}
		return socketAddress;
	}
	
	public void setGlobal(String var, Object value) {
		this.globalVarMap.put(var, value);
	}
	
	public void setSocketAddress(int gameId, String host, int port) {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		this.gameIdSocketAddressMap.put(gameId, socketAddress);
	}
	
	public static void main(String[] args) {
		try {
			GETXmlParse.parseXMLData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet.init();
		Scanner sc = new Scanner(System.in);
		String email = "";
		String password = "";
		boolean loginSuccess = false;
		int charId = 0;
		while(!loginSuccess)
		{
			System.out.println("Enter your email: ");
			email = sc.nextLine();
			System.out.println("Enter your password: ");
			password = sc.nextLine();
			System.out.println("Logging in...");
			try
			{
				HttpURLConnection con = (HttpURLConnection) new URL("https://realmofthemadgodhrd.appspot.com/char/list?guid="+email+"&password="+password).openConnection();
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
				document.getDocumentElement().normalize();
				if(document.getDocumentElement().getTagName().equals("Error"))
					System.out.println("Error: "+document.getDocumentElement().getTextContent());
				else
				{
					loginSuccess = true;
					charId = Integer.parseInt(document.getDocumentElement().getAttribute("nextCharId"))-1;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		sc.close();
		User user = null;
		try
		{
			user = new User();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		ROTMGRelay.echo("Initializing bot");
		ROTMGRelay.startTime = System.currentTimeMillis();
		user.scriptManager.trigger("onEnable", "27.7.0", GUID.encrypt(email), GUID.encrypt(password), charId);
		//user.scriptManager.
		while(true)
		{
			user.process();
			try
			{
				Thread.sleep(25);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
