package retrieveSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

public class ExpressionParser {
	HashMap<String, Set<String>> token;
	String readPath = "";
	HashSet<String> tempSet;

	ExpressionParser(String path) throws IOException {
		token = new HashMap<String, Set<String>>();
		readPath = path;

		loadFromFile();
		System.out.println("already loaded from file");
		tempSet = new HashSet<String>();
	}

	void loadFromFile() throws IOException {
		File dir = new File(readPath);
		String[] filelist = dir.list();
		for (int i = 0; i < filelist.length; i++) {
			InputStream in = new FileInputStream(
					new File(readPath, filelist[i]));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = reader.readLine();
			Set<String> set = new HashSet<String>();
			while (line != null) {
				set.add(line);
				line = reader.readLine();
			}
			token.put(filelist[i], set);
			in.close();
			reader.close();
		}
	}

	HashSet<String> cal(String s) {
		String[] exp = s.split(" ");

		Stack<String> optStack = new Stack<String>();
		Stack<String> dataStack = new Stack<String>();
		for (int i = 0; i < exp.length; i++) {
			String word = exp[i];
			if ( word.equals("OR")||word.equals("AND")||word.equals("NOT")) {
				if (optStack.size() == 0) {
					optStack.push(word);
				} else {
					int wordPriority = getPriority(word);
					String topOpt = optStack.firstElement();
					int topOptP = getPriority(topOpt);
					if (wordPriority > topOptP) {
						optStack.push(word);
					} else {
						while (wordPriority <= topOptP) {
							optStack.pop();
							calculate(dataStack, topOpt);
							if (optStack.size() > 0) {
								topOpt = optStack.firstElement();
								topOptP = getPriority(topOpt);
							} else
								break;
						}
					}
					optStack.push(word);
				}
			} else if (word.equals("("))
				optStack.push(word);
			else if (word.equals(")")) {
				while (!optStack.firstElement().equals("(")) {
					String topOpt = optStack.firstElement();
					calculate(dataStack, topOpt);
					optStack.pop();
				}
				optStack.pop();
			} else {
				dataStack.push(word.toLowerCase());
			}
		}
		while (optStack.size() != 0) {
			String topOpt = optStack.firstElement();
			calculate(dataStack, topOpt);
			optStack.pop();
		}
		// in fact, the element is tempSet, the value is tempSet
		return tempSet;
	}

	private int getPriority(String word) {
		// TODO Auto-generated method stub
		if (word.equals("AND") || word.equals("OR"))
			return 2;
		else if (word.equals("NOT"))
			return 1;
		else if (word.equals("("))
			return 0;

		return 0;
	}

	private void calculate(Stack<String> dataStack, String opt) {
		// TODO Auto-generated method stub
		String o2 = dataStack.pop();
		String o1 = dataStack.pop();
		HashSet<String> op1, op2;
		if (!o2.equals("tempSet")) {
			op2 = (HashSet<String>) token.get(o2);
		} else {
			op2 = tempSet;
		}
		if (!o1.equals("tempSet")) {
			op1 = (HashSet<String>) token.get(o1);
		} else
			op1 = tempSet;

		if(op1==null){
			op1=new HashSet<String>();
		}
		if(op2==null){
			op2=new HashSet<String>();
		}
		
		if (opt.equals("AND") ) {
			op1.retainAll(op2);
		} else if (opt.equals("OR")) {
			op1.addAll(op2);
		} else if (opt.equals("NOT")) {
			op1.removeAll(op2);
		}
		tempSet = op1;
		// "tempSet" serves as a notifier, the value of it is in tempSet
		dataStack.push("tempSet");
	}

	public static void main(String[] args) throws IOException {
		ExpressionParser test = new ExpressionParser("test");
		test.cal("atthat AND appliance OR apple");
		System.out.print(test.tempSet);
	}
}
