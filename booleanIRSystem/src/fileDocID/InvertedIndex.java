package fileDocID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InvertedIndex {
	Map<String, HashSet<String>> tokenIndex;
	String readPath = "";
	String writePath = "invertedToken";

	InvertedIndex(String path) {
		readPath = path;
		tokenIndex = new HashMap<String, HashSet<String>>();
	}

	void createInvertedIndex() throws IOException {
		loadFile();
		writeFile();
	}

	void loadFile() throws IOException {
		File dir = new File(readPath);
		String[] filelist = dir.list();
		for (int i = 0; i < filelist.length; i++) {
			File f = new File(dir, filelist[i]);
			InputStream in = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			String word = reader.readLine();
			while (word != null) {
				if (tokenIndex.containsKey(word)) {
					HashSet<String> articleSet = tokenIndex.get(word);
					articleSet.add(filelist[i]);
				} else {
					HashSet<String> articleSet = new HashSet<String>();
					articleSet.add(filelist[i]);
					tokenIndex.put(word, articleSet);
				}
				word = reader.readLine();
			}
			in.close();
			reader.close();
		}
	}

	void writeFile() throws IOException {
		tokenIndex.remove(".");
		int COUNT = 200;
		// the file system is too huge?
		// for (Entry<String, HashSet<String>> entry : tokenIndex.entrySet()) {
		// HashSet<String> articleSet = entry.getValue();
		//
		// BufferedWriter writer = new BufferedWriter(new FileWriter(writePath
		// + "/" + entry.getKey()));
		// for(Iterator<String> it=articleSet.iterator();it.hasNext();){
		// writer.write(it.next());
		// writer.newLine();
		// }
		// writer.close();
		// }

		int i = 0;
		for (Entry<String, HashSet<String>> entry : tokenIndex.entrySet()) {
			HashSet<String> articleSet = entry.getValue();

			File file=new File(writePath+ "/" + i%200 + "/" + entry.getKey());
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			i++;
			for (Iterator<String> it = articleSet.iterator(); it.hasNext();) {
				writer.write(it.next());
				writer.newLine();
			}
			writer.close();
		}
	}

	static public void main(String[] args) throws IOException {
		InvertedIndex test = new InvertedIndex("token");
		test.createInvertedIndex();
	}
}
