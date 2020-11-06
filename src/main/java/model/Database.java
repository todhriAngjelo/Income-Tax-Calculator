package model;
import inputManagePackage.*;
import outputManagePackage.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static String taxpayersInfoFilesPath;
	private static ArrayList<Taxpayer> taxpayersArrayList = new ArrayList<>();

	public static void setTaxpayersArrayList(ArrayList<Taxpayer> taxpayersArrayList) {
		Database.taxpayersArrayList = taxpayersArrayList;
	}

	public static void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath){
		Database.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
	}
	
	public static String getTaxpayersInfoFilesPath(){
		return Database.taxpayersInfoFilesPath;
	}
	
	public static void processTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles){
		InputSystem.addTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, taxpayersAfmInfoFiles);
	}

	public static ArrayList<Taxpayer> getTaxpayersArrayList() {
		return taxpayersArrayList;
	}

	public static void addTaxpayerToList(Taxpayer taxpayer){
		taxpayersArrayList.add(taxpayer);
	}
	
	public static int getTaxpayersArrayListSize(){
		return taxpayersArrayList.size();
	}
	
	public static Taxpayer getTaxpayerFromArrayList(int index){
		return taxpayersArrayList.get(index);
	}
	
	public static void removeTaxpayerFromArrayList(int index){
		taxpayersArrayList.remove(index);
	}
	
	public static String getTaxpayerNameAfmValuesPairList(int index){
		Taxpayer taxpayer = taxpayersArrayList.get(index);
		return taxpayer.getName() + " | " + taxpayer.getAfm();
	}
	
	public static String[] getTaxpayersNameAfmValuesPairList(){
		String[] taxpayersNameAfmValuesPairList = new String[taxpayersArrayList.size()];
		
		int c = 0;
		for (Taxpayer taxpayer : taxpayersArrayList){
			taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getAfm();
		}
		
		return taxpayersNameAfmValuesPairList;
	}
	
	public static void updateTaxpayerInputFile(int index){
		File taxpayersInfoFilesPathFileObject = new File(taxpayersInfoFilesPath);
		FilenameFilter fileNameFilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
               return (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
            }
         };
		
		for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)){
			if (!file.getName().contains(taxpayersArrayList.get(index).getAfm())) continue;
			
			if (file.getName().toLowerCase().endsWith(".txt")){
				OutputSystem.saveUpdatedTaxpayerTxtInputFile(file.getAbsolutePath(), index);
			}
			if (file.getName().toLowerCase().endsWith(".xml")){
				OutputSystem.saveUpdatedTaxpayerXmlInputFile(file.getAbsolutePath(), index);
			}
			break;
		}
	}	
}