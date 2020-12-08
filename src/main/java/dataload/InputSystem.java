package dataload;

import model.Receipt;
import model.Taxpayer;
import persistence.Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class InputSystem {
	private final String[] tags;

	public String[] getTags() {
		return tags;
	}

	public InputSystem(String[] tags) {
		this.tags = tags;
	}

	public Scanner createInputStream(String afmInfoFileFolderPath, String afmInfoFile) throws FileNotFoundException {
		return new Scanner(
				new FileInputStream(String.valueOf( Paths.get(afmInfoFileFolderPath, afmInfoFile) ) )
		);
	}

	public String getParameterValueFromFileLine(String fileLine, String beginTag, String endTag) {
		return fileLine.substring(beginTag.length(), fileLine.length() - endTag.length());
	}

	public Taxpayer createTaxpayerFromFile(Scanner inputStream) {
		String taxpayerName = getParameterValueFromFileLine(inputStream.nextLine(), tags[0], tags[1]).trim();
		String taxpayerAFM = getParameterValueFromFileLine(inputStream.nextLine(), tags[2], tags[3]).trim();
		String taxpayerStatus = getParameterValueFromFileLine(inputStream.nextLine(), tags[4], tags[5]).trim();
		String taxpayerIncome = getParameterValueFromFileLine(inputStream.nextLine(), tags[6], tags[7]).trim();
		return new Taxpayer(taxpayerName, taxpayerAFM, taxpayerStatus, taxpayerIncome);
	}

	public Receipt createReceiptFromFile(Scanner inputStream,String currentLine) {
		String receiptID = getParameterValueFromFileLine(currentLine, tags[10], tags[11]).trim();
		String receiptDate = getParameterValueFromFileLine(inputStream.nextLine(), tags[12], tags[13]).trim();
		String receiptKind = getParameterValueFromFileLine(inputStream.nextLine(), tags[14], tags[15]).trim();
		String receiptAmount = getParameterValueFromFileLine(inputStream.nextLine(), tags[16], tags[17]).trim();
		String receiptCompany = getParameterValueFromFileLine(inputStream.nextLine(), tags[18], tags[19]).trim();
		String receiptCountry = getParameterValueFromFileLine(inputStream.nextLine(), tags[20], tags[21]).trim();
		String receiptCity = getParameterValueFromFileLine(inputStream.nextLine(), tags[22], tags[23]).trim();
		String receiptStreet = getParameterValueFromFileLine(inputStream.nextLine(), tags[24], tags[25]).trim();
		String receiptNumber = getParameterValueFromFileLine(inputStream.nextLine(), tags[26], tags[27]).trim();
		return new Receipt(receiptKind, receiptID, receiptDate, receiptAmount,
				receiptCompany, receiptCountry, receiptCity, receiptStreet, receiptNumber);
	}

	public ArrayList<Receipt> extractTaxpayerReceiptsFromFile(Scanner inputStream) {
		String[] tags = getTags();
		ArrayList<Receipt> receipts = new ArrayList<>();
		while (inputStream.hasNextLine()) {
			String fileLine = inputStream.nextLine();
			if (fileLine.equals("")) continue;
			if (fileLine.trim().contains(tags[8])) continue;
			if (fileLine.trim().equals(tags[9])) break;

			receipts.add(createReceiptFromFile(inputStream, fileLine));
		}
		return receipts;
	}

	public void loadTaxpayersDataFromFileIntoDatabase(
			String afmInfoFileFolderPath,
			String afmInfoFile
	) throws FileNotFoundException {

		Scanner inputStream = createInputStream(afmInfoFileFolderPath, afmInfoFile);

		Taxpayer newTaxpayer = createTaxpayerFromFile(inputStream);

		newTaxpayer.setReceipts(extractTaxpayerReceiptsFromFile(inputStream));

		newTaxpayer.calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();

		Database.getDatabaseInstance().addTaxpayerToList(newTaxpayer);
	}
}