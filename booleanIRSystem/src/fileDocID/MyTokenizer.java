package fileDocID;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class MyTokenizer {

	BufferedWriter writer;
	String writePath = "";
	String readPath = "";
	HashMap mapID;
	char[] ignore = { ',', '.', ':', '\'' };

	MyTokenizer() {
	}

	MyTokenizer(String r, String w) throws IOException {
		readPath = r;
		writePath = w;
		mapID = FileDocID.loadFileID();
	}

	void writeTokenTest(String pathName) throws IOException {
		File dir = new File(pathName);
		if (!dir.isDirectory()) {
			if (dir.isFile()) {
				// System.out.print("test");
				writer = new BufferedWriter(new FileWriter(dir.getPath()
						+ ".txt"));
				// writer.write("ok");
				PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(
						new FileReader(dir.getPath()),
						new CoreLabelTokenFactory(), "");
				while (ptbt.hasNext()) {
					CoreLabel label = ptbt.next();
					String str = label.toString();
					writer.write(str.toLowerCase(Locale.ENGLISH));
					writer.newLine();
				}
				writer.close();
			}
		} 
		else {
			String[] list = dir.list();
			for (int i = 0; i < list.length; i++) {
				File file = new File(dir, list[i]);
				writeTokenTest(file.getPath());
			}
		}
	}

	void writeToken(String readp) throws IOException {
		File dir = new File(readp);
		if (!dir.isDirectory()) {
			if (dir.isFile()) {
				// System.out.print("test");

				// writer.write("ok");
				PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(
						new FileReader(dir.getPath()),
						new CoreLabelTokenFactory(), "");

				HashSet<String> hashSet = new HashSet<String>();
				while (ptbt.hasNext()) {
					CoreLabel label = ptbt.next();
					String str = label.toString();

					hashSet.add(str.toLowerCase(Locale.ENGLISH));
				}
				String fileName = dir.getPath().substring(readPath.length()+1);
				writer = new BufferedWriter(new FileWriter(writePath + "/"
						+ mapID.get(fileName)));
				
				for (Iterator<String> it=hashSet.iterator();it.hasNext();) {
					writer.write(((String) it.next()).toLowerCase(Locale.ENGLISH));
					writer.newLine();
				}
				writer.close();
			}
		} else {
			String[] list = dir.list();
			for (int i = 0; i < list.length; i++) {
				File file = new File(dir, list[i]);
				writeToken(file.getPath());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		MyTokenizer test = new MyTokenizer("shakespeare_collection", "token");
		test.writeToken(test.readPath);

	}
}
