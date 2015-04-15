package hkust.cse.calendar.diskstorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {
	private static FileManager instance = new FileManager();
	
	public FileManager(){
		
	}
	
	public static FileManager getInstance(){
		return instance;
	}
	
	public void writeToFile(String s, String fileName){
		try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println(s);
			writer.close();
			System.out.println("FileManager/writeToFile Saving To Disk at " + fileName);
		}catch(Exception e){
			System.out.println("FileManager/writeToFile Error in Saving to Disk at " + fileName);
		}
	}
	
	public String loadFromFile(String fileName){
		BufferedReader br = null;
		String result = "";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				result+=sCurrentLine;
			}
			System.out.println("FileManager/loadFromFile Loading from Disk at " + fileName);
		} catch (IOException e) {
			System.out.println("FileManager/loadFromFile Error in Loading from Disk at " + fileName);

		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				System.out.println("FileManager/loadFromFile Error in Loading from Disk at " + fileName);
			}
		}
		return result;
	}
}
