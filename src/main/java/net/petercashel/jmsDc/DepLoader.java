package net.petercashel.jmsDc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class DepLoader {

	static File libs = new File("./libs/");
	
	public static void main(String[] args) throws IOException {
		System.out.println("Downloading Core Libraries");
		Map<String, String> env = System.getenv();
		if (env.containsKey("JMSDdWkDir")) {
			libs = new File(env.get("JMSDdWkDir"));
		}		
		libs.mkdirs();
		get(new URL("https://repo1.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar"));		
		get(new URL("https://repo1.maven.org/maven2/io/netty/netty-all/5.0.0.Alpha2/netty-all-5.0.0.Alpha2.jar"));		
		get(new URL("http://htb2.petercashel.net:81/net/petercashel/JMSDd/JMSDd-Common/0.5.0/JMSDd-Common-0.5.0.jar"));		
		System.out.println("Loaded Core Libraries");
		clientMain.main(args);
	}

	private static void get(URL url) {
		try {
			String n = url.toString().substring(url.toString().lastIndexOf("/") + 1, url.toString().length());
			File f = new File(libs, n);
			if (f.exists()) {addFile(f); return;}
			System.out.println("Downloading:" + n);
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos;
			fos = new FileOutputStream(f);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			addFile(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// Based on a Stack Overflow answer. For dynamically loading classes.
	// http://stackoverflow.com/a/60766 - Credit to http://stackoverflow.com/users/2443/allain-lalonde
	///////////////////////////////////////////////////////////////////////////////////////////////
	//
	private static final Class[] parameters = new Class[] {URL.class};

	public static void addFile(String s) throws IOException
	{
		File f = new File(s);
		addFile(f);
	}

	public static void addFile(File f) throws IOException
	{
		addURL(f.toURI().toURL());
	}

	public static void addURL(URL u) throws IOException
	{
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] {u});
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}

	}

	//
	///////////////////////////////////////////////////////////////////////////////////////////////
}
