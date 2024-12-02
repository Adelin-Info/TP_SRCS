package srcs.interpretor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.PrintStream;

public class Cat implements Command{
	private String path;
	
	public Cat(String s){
		if ((s.isEmpty()) || (!(Files.isRegularFile(Paths.get(s))))){
			throw new IllegalArgumentException();
		}
		path = s;
	}
	
	public void execute(PrintStream out) throws IOException{
		FileInputStream file = new FileInputStream(path);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			out.println(scanner.nextLine());
		}
		scanner.close();
	}
}
