package dataload;

import persistence.Database;
import model.Receipt;
import model.Taxpayer;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class XmlFileInput extends InputSystem{

    public void loadTaxpayersDataFromXmlFileIntoDatabase(String afmInfoFileFolderPath, String afmInfoFile){

        Scanner inputStream;
        try {
            inputStream = super.createInputStream(afmInfoFileFolderPath, afmInfoFile);
        } catch ( FileNotFoundException e) {
            e.printStackTrace();
            return; //TODO return an error code to handle the error from the client.
        }

        String taxpayerName = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Name> ", " </Name>");
        String taxpayerAFM = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<AFM> ", " </AFM>");
        String taxpayerStatus = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Status> ", " </Status>");
        String taxpayerIncome = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Income> ", " </Income>");
        Taxpayer newTaxpayer = new Taxpayer(taxpayerName, taxpayerAFM, taxpayerStatus, taxpayerIncome);

        String fileLine;
        while (inputStream.hasNextLine())
        {
            fileLine = inputStream.nextLine();
            if (fileLine.equals("")) continue;
            if (fileLine.contains("<Receipts>")) continue;
            if (fileLine.contains("</Receipts>")) break;

            String receiptID = getParameterValueFromXmlFileLine(fileLine, "<ReceiptID> ", " </ReceiptID>");
            String receiptDate = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Date> ", " </Date>");
            String receiptKind = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Kind> ", " </Kind>");
            String receiptAmount = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Amount> ", " </Amount>");
            String receiptCompany = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Company> ", " </Company>");
            String receiptCountry = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Country> ", " </Country>");
            String receiptCity = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<City> ", " </City>");
            String receiptStreet = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Street> ", " </Street>");
            String receiptNumber = getParameterValueFromXmlFileLine(inputStream.nextLine(), "<Number> ", " </Number>");
            Receipt newReceipt = new Receipt(receiptKind, receiptID, receiptDate, receiptAmount, receiptCompany, receiptCountry, receiptCity, receiptStreet, receiptNumber);

            newTaxpayer.addReceiptToList(newReceipt);
        }

        Database.addTaxpayerToList(newTaxpayer);
    }

    private static String getParameterValueFromXmlFileLine(String fileLine, String parameterStartField, String parameterEndField){
        return fileLine.substring(parameterStartField.length(), fileLine.length()-parameterEndField.length());
    }
}
