package edu.isistan.fmframework.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVUtils {
	
	public static CSVWriter newCSVWriter(String filePath) throws IOException {
		return new CSVWriter(new FileWriter(new File(filePath)));
	}
	
	public static void appendRow(String file, String ...line) throws IOException {
		CSVWriter writer=new CSVWriter(new FileWriter(new File(file),true));
		writer.writeNext(line);
		writer.close();
	}
	
	public static void appendRows(String output,String ...files) throws IOException{
		CSVReader reader=new CSVReader(new FileReader(new File(files[0])));
		CSVWriter writer=new CSVWriter(new FileWriter(new File(output)));

		for(String[] line:reader){
			writer.writeNext(line);
		}
		reader.close();
		
		for(int i=1;i<files.length;i++){
			reader=new CSVReader(new FileReader(new File(files[i])));
			Iterator<String[]> iter=reader.iterator();
			iter.next();
			while(iter.hasNext()){
				String[] line=iter.next();
				writer.writeNext(line);
			}
			reader.close();
		}
		writer.close();
	}
	
	public static void appendRows(String output,String []files,String []id) throws IOException{
		CSVReader reader=new CSVReader(new FileReader(new File(files[0])));
		CSVWriter writer=new CSVWriter(new FileWriter(new File(output)));

		Iterator<String[]> iter=reader.iterator();
		writer.writeNext(ArrayUtils.addAll(new String[]{"ID"}, iter.next()));
		while(iter.hasNext()){
			String[] line=iter.next();
			writer.writeNext(ArrayUtils.addAll(new String[]{id[0]}, line));
		}
		reader.close();
		
		for(int i=1;i<files.length;i++){
			reader=new CSVReader(new FileReader(new File(files[i])));
			iter=reader.iterator();
			iter.next();
			while(iter.hasNext()){
				String[] line=iter.next();
				writer.writeNext(ArrayUtils.addAll(new String[]{id[i]}, line));
			}
			reader.close();
		}
		writer.close();
	}
	
	public static void appendColumns(String output,String file1,String file2,int []columnIds) throws IOException{
		CSVReader reader=new CSVReader(new FileReader(new File(file1)));
		CSVReader reader2=new CSVReader(new FileReader(new File(file2)));
		CSVWriter writer=new CSVWriter(new FileWriter(new File(output)));
		
		Iterator<String[]> iter2=reader2.iterator();
		for(String[] line:reader){
			String[] line2=iter2.next();
			for(int i=0;i<columnIds.length;i++){
				line=ArrayUtils.add(line,line2[columnIds[i]]);
			}
			writer.writeNext(line);
		}
		writer.close();
		reader.close();
		reader2.close();
	}
	
	public static void appendColumns(String output,String ...files) throws IOException{
		CSVReader[] readers=new CSVReader[files.length];
		Iterator<String[]>[] iters=new Iterator[files.length];
		for(int i=0;i<files.length;i++){
			readers[i]=new CSVReader(new FileReader(new File(files[i])));
			iters[i]=readers[i].iterator();
		}
		
		CSVWriter writer=new CSVWriter(new FileWriter(new File(output)));
		
		int[] colums=new int[files.length];
		String[] line =new String[0];
		for(int i=0;i<files.length;i++){
			if(iters[i].hasNext()){
				String[] next = iters[i].next();
				colums[i]=next.length;
				line=ArrayUtils.addAll(line, next);
			}else{
				line=ArrayUtils.addAll(line, "");
				colums[i]=0;
			}
		}
		writer.writeNext(line);
		
		while(hasNext(iters)){
			
			line=new String[0];
			for(int i=0;i<files.length;i++){
				if(iters[i].hasNext()){
					line=ArrayUtils.addAll(line, iters[i].next());
				}else{
					for(int j=0;j<colums[i];j++)
						line=ArrayUtils.addAll(line, "");
				}
			}
			writer.writeNext(line);
		}
		writer.close();
		for(int i=0;i<files.length;i++)
			readers[i].close();
	}
	
	private static boolean hasNext(Iterator<String[]>[] iters) {
		for(Iterator<String[]> iter: iters){
			if (iter.hasNext())
				return true;
		}
		return false;
	}

	public static void deleteColumns(String output,String file,int ...columnId) throws IOException{
		CSVReader reader=new CSVReader(new FileReader(new File(file)));
		CSVWriter writer=new CSVWriter(new FileWriter(new File(output)));

		for(String[] line:reader){
			line=ArrayUtils.removeAll(line,columnId);
			writer.writeNext(line);
		}
		writer.close();
		reader.close();
	}
	
	public static void main(String[] args) throws IOException {
//		CSVUtils.appendRows("SALIDA.csv", "Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Correctness.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-ResponseTime.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Values.csv");
//		CSVUtils.appendRows("SALIDA.csv", new String[]{"Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Correctness.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-ResponseTime.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Values.csv"},new String[]{"A","D","C"});
//		CSVUtils.appendColumns("SALIDA.csv", "Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Correctness.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-ResponseTime.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Values.csv");
//		CSVUtils.appendColumns("SALIDA.csv", "Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Correctness.csv","Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-ResponseTime.csv",2);
//		CSVUtils.deleteColumns("SALIDA.csv", "Bug_Exp4_GAFES_and_SPLConfig_LimitRatios-0.5-Correctness.csv",1,2);
		CSVUtils.appendRow("HOLA.CSV", "1");
		CSVUtils.appendRow("HOLA.CSV", "1","2");
	}




}
