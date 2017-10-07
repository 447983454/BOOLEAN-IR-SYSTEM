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

//to give a certain ID for each file in given directory

public class FileDocID {

//	static final int CATE_NUM = 1;
//	static final int BOOK_NUM = 2;
//	static final int ACT_NUM = 1;
//	static final int SCENE_NUM = 1;
//	static final int TOTAL = CATE_NUM + BOOK_NUM + ACT_NUM + SCENE_NUM;

	static String originReadPath = "";
	static final String WRITE_PATH = "fileDocID.txt";

	BufferedWriter writer;

	FileDocID(String s) throws IOException {
		originReadPath=s;
		writer = new BufferedWriter(new FileWriter(WRITE_PATH));

		find(originReadPath, "");
		writer.close();
			
	}

	// use relative path? right
	// prefix null :prefix id
	// index 1 :number of file in current dir

	void find(String pathName, String prefix) throws IOException {
		File dir = new File(pathName);
		if (!dir.exists()) {
			return;
		}
		if (!dir.isDirectory()) {
			if (dir.isFile()) {
				String str=pathName.substring(originReadPath.length()+1);
				writer.write(str);
				writer.newLine();
				// String str = prefix + Integer.toString(index);
				writer.write(prefix);
				writer.newLine();
				return;
			}
		} else {
			String[] fileList = dir.list();
			String newp;
			for (int i = 0; i < fileList.length; i++) {
				String str = fileList[i];
				File file = new File(dir.getPath(), str);

				if (prefix.length() < 1) {
					newp = prefix + Integer.toString(i + 1);
				} else if (prefix.length() >= 5) {
					newp = prefix + Integer.toString(i + 1);
				} else {
					if (i + 1 < 10) {
						newp = prefix + "0" + Integer.toString(i + 1);
					} else {
						newp = prefix + Integer.toString(i + 1);
					}
				}

				find(file.getPath(), newp);
			}
		}

	}
	
	//key fileName
	//value fileID
	public static HashMap<String, String> loadFileID() throws IOException{
		InputStream in=new FileInputStream(WRITE_PATH);
		BufferedReader reader=new BufferedReader(new InputStreamReader(in));
		
		HashMap<String, String> hashmap=new HashMap<String, String>();
		String line=reader.readLine();
		while(line!=null){
			hashmap.put(line, reader.readLine());
			line=reader.readLine();
		}
		return hashmap;
	}

	public static HashSet<String> convertIDtoName(HashSet<String> idSet) throws IOException{
		HashSet<String>nameSet=new HashSet<String>();
		HashMap<String, String> idfile=new HashMap<String, String>();
		idfile=loadFileID();
		
		for(Iterator<String> it=idSet.iterator();it.hasNext();){
			String id=it.next();
			nameSet.add(idfile.get(id));
		}
		return nameSet;
		
	}
	public static void main(String[] args) throws IOException {
		FileDocID test = new FileDocID("shakespeare_collection");

	}
}
