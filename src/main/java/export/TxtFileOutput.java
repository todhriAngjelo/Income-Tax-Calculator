package export;

import model.Receipt;
import model.Taxpayer;
import persistence.Database;
import utils.ApplicationConstants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class TxtFileOutput extends OutputSystem {
    private static final TxtFileOutput txtFileOutputInstance = new TxtFileOutput();

    private TxtFileOutput(){}

    public static TxtFileOutput getTxtFileOutputInstance() {
        return txtFileOutputInstance;
    }
    @Override
    public void saveUpdatedTaxpayerInputFile(String filePath, int taxpayerIndex){
        PrintWriter outputStream = null;
        try
        {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Problem opening: "+filePath);
        }

        Taxpayer taxpayer = Database.getDatabaseInstance().getTaxpayerFromArrayList(taxpayerIndex);
        outputStream.println("Name: "+taxpayer.getName());
        outputStream.println("AFM: "+taxpayer.getAfm());
        outputStream.println("Status: "+taxpayer.getFamilyStatus());
        outputStream.println("Income: "+taxpayer.getIncome());

        if (taxpayer.getReceiptsArrayList().size() > 0){
            outputStream.println();
            outputStream.println("Receipts:");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceiptsArrayList()){
                outputStream.println("Receipt ID: "+receipt.getId());
                outputStream.println("Date: "+receipt.getDate());
                outputStream.println("Kind: "+receipt.getKind());
                outputStream.println("Amount: "+receipt.getAmount());
                outputStream.println("Company: "+receipt.getCompany().getName());
                outputStream.println("Country: "+receipt.getCompany().getCountry());
                outputStream.println("City: "+receipt.getCompany().getCity());
                outputStream.println("Street: "+receipt.getCompany().getStreet());
                outputStream.println("Number: "+receipt.getCompany().getNumber());
                outputStream.println();
            }
        }

        outputStream.close();
    }
    @Override
    public void saveTaxpayerInfoLogFile(String folderSavePath, int taxpayerIndex){
        Taxpayer taxpayer = Database.getDatabaseInstance().getTaxpayerFromArrayList(taxpayerIndex);

        PrintWriter outputStream = null;
        try
        {
            if( !System.getProperty("os.name").contains("Windows") ) {
                outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "/" + taxpayer.getAfm() + "_LOG.txt"));
            }
            else{
                outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAfm() + "_LOG.txt"));
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Problem opening: "+folderSavePath+"//"+taxpayer.getAfm()+"_LOG.txt");
        }

        outputStream.println("Name: " + taxpayer.getName());
        outputStream.println("AFM: " + taxpayer.getAfm());
        outputStream.println("Income: " + taxpayer.getIncome());
        outputStream.println("Basic Tax: " + taxpayer.getTax());
        if (taxpayer.getTaxIncrease()!=0){
            outputStream.println("Tax Increase: " + taxpayer.getTaxIncrease());
        }else{
            outputStream.println("Tax Decrease: " + taxpayer.getTaxDecrease());
        }
        outputStream.println("Total Tax: " + taxpayer.getFinalTax());
        outputStream.println("Total Receipts Amount: " + taxpayer.getTotalReceiptsAmount());
        outputStream.println("Entertainment: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT));
        outputStream.println("Basic: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT));
        outputStream.println("Travel: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT));
        outputStream.println("Health: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT));
        outputStream.println("Other: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT));

        outputStream.close();
    }
}
