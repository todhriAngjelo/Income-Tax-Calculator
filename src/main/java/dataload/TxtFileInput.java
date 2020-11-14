package dataload;

import model.Database;
import model.Receipt;
import model.Taxpayer;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class TxtFileInput extends InputSystem {


    public void loadTaxpayerDataFromTxtFileIntoDatabase(String afmInfoFileFolderPath, String afmInfoFile){

        Scanner inputStream;
        try {
             inputStream = super.createInputStream(afmInfoFileFolderPath, afmInfoFile);
        } catch ( FileNotFoundException e) {
            e.printStackTrace();
            return; //TODO return an error code to handle the error from the client.
        }


        String taxpayerName = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Name: ");
        String taxpayerAFM = getParameterValueFromTxtFileLine(inputStream.nextLine(), "AFM: ");
        String taxpayerStatus = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Status: ");
        String taxpayerIncome = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Income: ");
        Taxpayer newTaxpayer = new Taxpayer(taxpayerName, taxpayerAFM, taxpayerStatus, taxpayerIncome);

        String fileLine;
        while (inputStream.hasNextLine())
        {
            fileLine = inputStream.nextLine();
            if (fileLine.equals("")) continue;
            if (fileLine.contains("Receipts:")) continue;

            String receiptID = getParameterValueFromTxtFileLine(fileLine, "Receipt ID: ");
            String receiptDate = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Date: ");
            String receiptKind = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Kind: ");
            String receiptAmount = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Amount: ");
            String receiptCompany = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Company: ");
            String receiptCountry = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Country: ");
            String receiptCity = getParameterValueFromTxtFileLine(inputStream.nextLine(), "City: ");
            String receiptStreet = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Street: ");
            String receiptNumber = getParameterValueFromTxtFileLine(inputStream.nextLine(), "Number: ");
            Receipt newReceipt = new Receipt(receiptKind, receiptID, receiptDate, receiptAmount, receiptCompany, receiptCountry, receiptCity, receiptStreet, receiptNumber);

            newTaxpayer.addReceiptToList(newReceipt);
        }

        Database.addTaxpayerToList(newTaxpayer);
    }

    private static String getParameterValueFromTxtFileLine(String fileLine, String parameterName){
        return fileLine.substring(parameterName.length(), fileLine.length());
    }
}
