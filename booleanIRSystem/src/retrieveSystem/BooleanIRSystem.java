package retrieveSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import fileDocID.FileDocID;

public class BooleanIRSystem {
	ExpressionParser parser;

	BooleanIRSystem(String path) throws IOException {
		parser=new ExpressionParser(path);
	}

	
	void query() throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("you could [AND, OR, NOT, ()], but don't forget to add a space around them");
		while (true) {
			System.out.println("Enter snippet to search  (or ENTER to quit):");
			String line = reader.readLine();

			if (line.trim().length() == 0)
				break;
			System.out.println("searching result is:");
			
			System.out.println(FileDocID.convertIDtoName(parser.cal(line)));
		}
	}

	public static void main(String[] args) throws IOException {
		BooleanIRSystem test=new BooleanIRSystem("invertedTokenTest");
		test.query();
	}
}
