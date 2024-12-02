package srcs.interpretor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLClassLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class CommandInterpretor{
	
	private static Map<String, Class<? extends Command>> commandes;
	private static List<URL> lurls;
	
	public CommandInterpretor() {
		commandes = new HashMap<String, Class<? extends Command>>();
		commandes.put("cat", Cat.class);
		commandes.put("echo", Echo.class);
		commandes.put("deploy", CommandInterpretor.Deploy.class);
		commandes.put("undeploy", CommandInterpretor.Undeploy.class);
		commandes.put("save", CommandInterpretor.Save.class);
		lurls = new ArrayList<URL>();
	}
	
	@SuppressWarnings("unchecked")
	public CommandInterpretor(String file) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("_URL"));
		int size = ois.readInt();
		URL[] urls = (URL[]) ois.readObject();
		lurls = new ArrayList<URL>();
		for(int i=0; i<size; i++) {
			lurls.add(urls[i]);
		}
		CustomObjectInputStream cois = new CustomObjectInputStream(new FileInputStream(file), urls);
		commandes = (Map<String, Class<?extends Command>>) cois.readObject();
		ois.close();
		cois.close();
	}
	
	public Class<? extends Command> getClassOf(String s){
		return commandes.get(s);
	}
	
	public void perform(String s, OutputStream out) throws Exception{
		if(!s.isEmpty()) {
			PrintStream ps = new PrintStream(out);
			StringTokenizer st = new StringTokenizer(s);
			String cmd = st.nextToken();
			
			if(!commandes.containsKey(cmd)) {
				throw new CommandNotFoundException();
			}
			
			String args = "";
			Class<? extends Command> cl;
			Object o;
			Method m;
			switch (cmd) {
				case "echo":
					args = cmd;
					while(st.hasMoreTokens()) {
						args += " " + st.nextToken();
						
					}
					cl = commandes.get(cmd);
					o = cl.getConstructor(String.class).newInstance(args);
					m = cl.getMethod("execute", PrintStream.class);
					m.invoke(o, ps);
					break;
			
				case "cat":
					while(st.hasMoreTokens()) {
						args += st.nextToken();
						
					}
					cl = commandes.get(cmd);
					o = cl.getConstructor(String.class).newInstance(args);
					m = cl.getMethod("execute", PrintStream.class);
					m.invoke(o, ps);
					break;
				
				case "deploy":
					String new_cmd = st.nextToken();
					String path = st.nextToken();
					String nameClass = st.nextToken();
					cl = commandes.get(cmd);
					o = cl.getConstructor(String.class, String.class, String.class).newInstance(new_cmd, path, nameClass);
					m = cl.getMethod("execute", PrintStream.class);
					try {
						m.invoke(o, ps);
						break;
					} catch (InvocationTargetException ite) {
						throw new IllegalArgumentException(ite.getCause());
					}
					
				case "undeploy":
					String del_cmd = st.nextToken();					
					cl = commandes.get(cmd);
					o = cl.getConstructor(String.class).newInstance(del_cmd);
					m = cl.getMethod("execute", PrintStream.class);
					m.invoke(o, ps);
					break;
				
				case "save":
					String file = st.nextToken();
					cl = commandes.get(cmd);
					o = cl.getConstructor(String.class).newInstance(file);
					m = cl.getMethod("execute", PrintStream.class);
					m.invoke(o, ps);
					break;
				
				default:
					String op1 = st.nextToken();
					String op2 = st.nextToken();
					List<String> largs = new ArrayList<>();
					largs.add(cmd);
					largs.add(op1);
					largs.add(op2);
					cl = commandes.get(cmd);
					o = cl.getConstructor(List.class).newInstance(largs);
					m = cl.getMethod("execute", PrintStream.class);
					m.invoke(o, ps);
					break;
			}
		}
	}

	public static class Deploy implements Command{
		private String cmd;
		private String path;
		private String nameClass;
		private URL url;
		
		public Deploy(String cmd, String path, String nameClass) throws MalformedURLException {
			this.cmd = cmd;
			this.path = path;
			this.nameClass = nameClass;
			url = new File(path).toURI().toURL();
			lurls.add(url);
		}
		
		
		@SuppressWarnings({ "unchecked", "resource" })
		public void execute(PrintStream out) throws IOException{
			File dir = new File(path);
			if((!dir.exists()) || (!dir.isDirectory())){
				throw new IllegalArgumentException();
			}
			
			if(commandes.containsKey(cmd)) {
				throw new IllegalArgumentException();
			}
			
			try {
				URLClassLoader ucl = new URLClassLoader(new URL[]{url});
				Class<? extends Command> classCommand = (Class<? extends Command>) ucl.loadClass(nameClass);
				commandes.put(cmd, classCommand);	
			} catch (Exception e) {
				throw new IllegalArgumentException();
			}
		}	
	}
	
	public static class Undeploy implements Command{
		private String cmd;
		
		public Undeploy(String cmd) {
			this.cmd = cmd;
		}
		
		public void execute(PrintStream out) throws IOException{
			commandes.remove(cmd);
		}
	}
	
	public static class Save implements Command{
		private String file;
		
		public Save(String file) {
			this.file = file;		
		}
		
		public void execute(PrintStream out) throws IOException{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("_URL"));
			oos2.writeInt(lurls.size());
			URL[] urls = lurls.toArray(new URL[lurls.size()]);
			oos2.writeObject(urls);
			oos2.flush();
			oos.writeObject(commandes);
			oos.flush();
			oos.close();
			oos2.close();
		}
	}
	
	public class CustomObjectInputStream extends ObjectInputStream{
		private ClassLoader classLoader;
		
		public CustomObjectInputStream(InputStream is, URL[] urls) throws IOException{
			super(is);
			classLoader = new URLClassLoader(urls);
		}
		
		@Override
		protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException{
			try {
				return super.resolveClass(objectStreamClass);
			} catch(ClassNotFoundException e) {
				return Class.forName(objectStreamClass.getName(), false, classLoader);
			}
		}
	}
}
