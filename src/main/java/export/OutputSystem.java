package export;

import model.Receipt;
import model.Taxpayer;
import persistence.Database;
import utils.ApplicationConstants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class OutputSystem {

	public final String[] tags;
	public final String filetype;

	public OutputSystem( String filetype, String[] tags) {
		this.tags = tags;
		this.filetype = filetype;
	}

	public void saveUpdatedTaxpayerInputFile(String filePath, int taxpayerIndex) throws FileNotFoundException{

		PrintWriter outputStream = createOutputStream(filePath);

		Taxpayer taxpayer = Database.getDatabaseInstance().getTaxpayerFromArrayList(taxpayerIndex);
		outputStream.println(tags[0]+ " " +taxpayer.getName()+ " " +tags[1]);
		outputStream.println(tags[2]+ " " +taxpayer.getAfm()+ " " +tags[3]);
		outputStream.println(tags[4]+ " " +taxpayer.getFamilyStatus()+ " " +tags[5]);
		outputStream.println(tags[6]+ " " +taxpayer.getIncome()+ " " +tags[7]);

		if (taxpayer.getReceiptsArrayList().size() > 0){
			outputStream.println();
			outputStream.println(tags[8]);
			outputStream.println();

			for (Receipt receipt : taxpayer.getReceiptsArrayList()){
				outputStream.println(tags[10]+ " " +receipt.getId()+ " " +tags[11]);
				outputStream.println(tags[12]+ " " +receipt.getDate()+ " " +tags[13]);
				outputStream.println(tags[14]+ " " +receipt.getKind()+ " " +tags[15]);
				outputStream.println(tags[16]+ " " +receipt.getAmount()+ " " +tags[17]);
				outputStream.println(tags[18]+ " " +receipt.getCompany().getName()+ " " +tags[19]);
				outputStream.println(tags[20]+ " " +receipt.getCompany().getCountry()+ " " +tags[21]);
				outputStream.println(tags[22]+ " " +receipt.getCompany().getCity()+ " " +tags[23]);
				outputStream.println(tags[24]+ " " +receipt.getCompany().getStreet()+ " " +tags[25]);
				outputStream.println(tags[26]+ " " +receipt.getCompany().getNumber()+ " " +tags[27]);
				outputStream.println();
			}

			outputStream.println(tags[9]);
		}

		outputStream.close();
	}

	public void saveTaxpayerInfoLogFile(String folderSavePath, int taxpayerIndex) throws FileNotFoundException{
		Taxpayer taxpayer = Database.getDatabaseInstance().getTaxpayerFromArrayList(taxpayerIndex);

		PrintWriter outputStream = createOutputStream(String.valueOf(Paths.get(folderSavePath, taxpayer.getAfm() + "_LOG."+ filetype)));

		outputStream.println(tags[0]+ " " +taxpayer.getName()+ " " +tags[1]);
		outputStream.println(tags[2]+ " " +taxpayer.getAfm()+ " " +tags[3]);
		outputStream.println(tags[28]+ " " +taxpayer.getIncome()+ " " +tags[29]);
		outputStream.println(tags[30]+ " " +taxpayer.getTax()+ " " +tags[31]);
		if (taxpayer.getTaxIncrease()!=0){
			outputStream.println(tags[32]+ " " +taxpayer.getTaxIncrease()+ " " +tags[33]);
		}else{
			outputStream.println(tags[34]+ " " +taxpayer.getTaxDecrease()+ " " +tags[35]);
		}
		outputStream.println(tags[36]+ " " +taxpayer.getFinalTax()+ " " +tags[37]);
		outputStream.println(tags[8]+ " " +taxpayer.getTotalReceiptsAmount()+ " " +tags[9]);
		outputStream.println(tags[38]+ " " +taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT)+ " " +tags[39]);
		outputStream.println(tags[40]+ " " +taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT)+ " " +tags[41]);
		outputStream.println(tags[42]+ " " +taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT)+ " " +tags[43]);
		outputStream.println(tags[44]+ " " +taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT)+ " " +tags[45]);
		outputStream.println(tags[46]+ " " +taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT)+ " " +tags[47]);

		outputStream.close();
	}

	public PrintWriter createOutputStream(String filepath) throws FileNotFoundException {
		return new PrintWriter(new FileOutputStream(filepath));
	}

}
