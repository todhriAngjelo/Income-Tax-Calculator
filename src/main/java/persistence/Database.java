package persistence;
import dataload.*;
import export.*;
import model.Taxpayer;
import utils.ApplicationConstants;
import utils.ApplicationErrors;
import utils.FileTypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private String taxpayersInfoFilesPath;
	private ArrayList<Taxpayer> taxpayersArrayList = new ArrayList<>();
	private static final Database databaseInstance = new Database();

	private Database() {}

	public static Database getDatabaseInstance() {
		return databaseInstance;
	}

	public void setTaxpayersArrayList(ArrayList<Taxpayer> taxpayersArrayList) {
		this.taxpayersArrayList = taxpayersArrayList;
	}

	public void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath){
		this.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
	}
	
	public String getTaxpayersInfoFilesPath(){
		return this.taxpayersInfoFilesPath;
	}
	
	public static void processTaxpayersDataFromFilesIntoDatabase(
			String afmInfoFilesFolderPath, List<String> inputFiles) throws FileNotFoundException {

		for (String inputFile : inputFiles)
		{
			if (inputFile.endsWith(".txt")){
				InputSystem txtFileInputInput = new InputSystem(ApplicationConstants.txtTags);
				txtFileInputInput.loadTaxpayersDataFromFileIntoDatabase(afmInfoFilesFolderPath, inputFile);
			}
			else if (inputFile.endsWith(".xml")){
				InputSystem xmlFileInputInput = new InputSystem(ApplicationConstants.xmlTags);
				xmlFileInputInput.loadTaxpayersDataFromFileIntoDatabase(afmInfoFilesFolderPath, inputFile);
			} else {
				throw new IllegalArgumentException(
						ApplicationErrors.INPUT_FILE_TYPE_ERROR
				);
			}
		}
	}

	public ArrayList<Taxpayer> getTaxpayersArrayList() {
		return this.taxpayersArrayList;
	}

	public void addTaxpayerToList(Taxpayer taxpayer){
		this.taxpayersArrayList.add(taxpayer);
	}
	
	public int getTaxpayersArrayListSize(){
		return this.taxpayersArrayList.size();
	}
	
	public Taxpayer getTaxpayerFromArrayList(int index){
		return this.taxpayersArrayList.get(index);
	}
	
	public void removeTaxpayerFromArrayList(int index){
		this.taxpayersArrayList.remove(index);
	}
	
	public String getTaxpayerNameAfmValuesPairList(int index){
		Taxpayer taxpayer = this.taxpayersArrayList.get(index);
		return taxpayer.getName() + " | " + taxpayer.getAfm();
	}
	
	public String[] getTaxpayersNameAfmValuesPairList(){
		String[] taxpayersNameAfmValuesPairList = new String[this.taxpayersArrayList.size()];
		
		int c = 0;
		for (Taxpayer taxpayer : this.taxpayersArrayList){
			taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getAfm();
		}
		
		return taxpayersNameAfmValuesPairList;
	}
	
	public void updateTaxpayerInputFile(int index) throws FileNotFoundException {
		File taxpayersInfoFilesPathFileObject = new File(this.taxpayersInfoFilesPath);
		FilenameFilter fileNameFilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
               return (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
            }
         };

		for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)){
			if (!file.getName().contains(this.taxpayersArrayList.get(index).getAfm())) continue;
			
			if (file.getName().toLowerCase().endsWith(".txt")){
				OutputSystem txtFileOutput = new OutputSystem(FileTypes.TXT);
				txtFileOutput.saveUpdatedTaxpayerInputFile(file.getAbsolutePath(), index);
			}
			if (file.getName().toLowerCase().endsWith(".xml")){
				OutputSystem xmlFileOutput = new OutputSystem(FileTypes.XML);
				xmlFileOutput.saveUpdatedTaxpayerInputFile(file.getAbsolutePath(), index);
			}
			break;
		}
	}	
}