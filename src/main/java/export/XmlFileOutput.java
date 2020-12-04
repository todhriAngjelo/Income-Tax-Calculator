package export;

import model.Receipt;
import model.Taxpayer;
import persistence.Database;
import utils.ApplicationConstants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class XmlFileOutput extends OutputSystem{
    private static final XmlFileOutput xmlFileOutputInstance = new XmlFileOutput();

    private XmlFileOutput(){}

    public static XmlFileOutput getXmlFileOutputInstance() {
        return xmlFileOutputInstance;
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
        outputStream.println("<Name> "+taxpayer.getName()+" </Name>");
        outputStream.println("<AFM> "+taxpayer.getAfm()+" </AFM>");
        outputStream.println("<Status> "+taxpayer.getFamilyStatus()+" </Status>");
        outputStream.println("<Income> "+taxpayer.getIncome()+" </Income>");

        if (taxpayer.getReceiptsArrayList().size() > 0){
            outputStream.println();
            outputStream.println("<Receipts>");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceiptsArrayList()){
                outputStream.println("<ReceiptID> "+receipt.getId()+" </ReceiptID>");
                outputStream.println("<Date> "+receipt.getDate()+" </Date>");
                outputStream.println("<Kind> "+receipt.getKind()+" </Kind>");
                outputStream.println("<Amount> "+receipt.getAmount()+" </Amount>");
                outputStream.println("<Company> "+receipt.getCompany().getName()+" </Company>");
                outputStream.println("<Country> "+receipt.getCompany().getCountry()+" </Country>");
                outputStream.println("<City> "+receipt.getCompany().getCity()+" </City>");
                outputStream.println("<Street> "+receipt.getCompany().getStreet()+" </Street>");
                outputStream.println("<Number> "+receipt.getCompany().getNumber()+" </Number>");
                outputStream.println();
            }

            outputStream.println("</Receipts>");
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
                outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "/" + taxpayer.getAfm() + "_LOG.xml"));
            }
            else{
                outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAfm() + "_LOG.xml"));
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Problem opening: "+folderSavePath+"//"+taxpayer.getAfm()+"_LOG.xml");
        }

        outputStream.println("<Name> "+taxpayer.getName()+" </Name>");
        outputStream.println("<AFM> "+taxpayer.getAfm()+" </AFM>");
        outputStream.println("<Income> "+taxpayer.getIncome()+" </Income>");
        outputStream.println("<BasicTax> "+taxpayer.getTax()+" </BasicTax>");
        if (taxpayer.getTaxIncrease()!=0){
            outputStream.println("<TaxIncrease> "+taxpayer.getTaxIncrease()+" </TaxIncrease>");
        }else{
            outputStream.println("<TaxDecrease> "+taxpayer.getTaxDecrease()+" </TaxDecrease>");
        }
        outputStream.println("<TotalTax> "+taxpayer.getFinalTax()+" </TotalTax>");
        outputStream.println("<Receipts> "+taxpayer.getTotalReceiptsAmount()+" </Receipts>");
        outputStream.println("<Entertainment> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT)+" </Entertainment>");
        outputStream.println("<Basic> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT)+" </Basic>");
        outputStream.println("<Travel> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT)+" </Travel>");
        outputStream.println("<Health> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT)+" </Health>");
        outputStream.println("<Other> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT)+" </Other>");

        outputStream.close();
    }
}
