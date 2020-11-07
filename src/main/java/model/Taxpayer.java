package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Taxpayer {
	private String name;
	private String afm;
	private FamilyStatus familyStatus;
	private double income;
	private double tax;
	private double taxIncrease;
	private double taxDecrease;
	private double finalTax;
	private ArrayList<Receipt> receipts;


	public Taxpayer(String name, String afm, String familyStatus, String income) {
		this.name = name.trim().replace("\n", "");
		this.afm = afm.trim().replace("\n", "");
		this.familyStatus = FamilyStatus.getFamilyStatusInstance(familyStatus.trim().replace("\n", ""));
		this.income = Double.parseDouble(income.trim().replace("\n", ""));
		this.tax = calculateTax();
		taxIncrease = 0;
		taxDecrease = 0;
		receipts = new ArrayList<>();
	}
	
	public double calculateTax() {
		double tax;
		double totalIncome = this.income;
		int[] incomeLimits = this.familyStatus.getIncomeLimits();
		double[] basicTax = this.familyStatus.getBasicTax();
		double[] rates = this.familyStatus.getRates();

		if ( totalIncome < incomeLimits[0] ) {
			tax = (rates[0]) * totalIncome;
		}
		else if ( totalIncome < incomeLimits[1] ) {
			tax = basicTax[1] + ( (rates[1]) * (totalIncome - incomeLimits[0]) );
		}
		else if ( totalIncome < incomeLimits[2] ) {
			tax = basicTax[2] + ( (rates[2]) * (totalIncome - incomeLimits[1]) );
		}
		else if ( totalIncome < incomeLimits[3] ) {
			tax = basicTax[3] + ( (rates[3]) * (totalIncome - incomeLimits[2]) );
		}
		else {
			tax = basicTax[4] + ( (rates[4]) * (totalIncome - incomeLimits[3]) );
		}
		
		return tax;
	}

	@Override
	public String toString() {
		return "Name: "+name
				+"\nAFM: "+afm
				+"\nStatus: "+ familyStatus.getFamilyStatus()
				+"\nIncome: "+String.format("%.2f", income)
				+"\nTax: "+String.format("%.2f", tax)
				+"\nTax Increase: "+String.format("%.2f", taxIncrease)
				+"\nTax Decrease: "+String.format("%.2f", taxDecrease)
				+"\nFinal Tax: "+String.format("%.2f", finalTax);
	}
	
	public Receipt getReceipt(int receiptID) {
		return receipts.get(receiptID);
	}
	
	public ArrayList<Receipt> getReceiptsArrayList() {
		return receipts;
	}
	
	public String[] getReceiptsList() {
		String[] receiptsList = new String[receipts.size()];
		
		int c = 0;
		for (Receipt receipt : receipts){
			receiptsList[c++] = receipt.getId() + " | " + receipt.getDate() + " | " + receipt.getAmount();
		}
		
		return receiptsList;
	}

	public double getReceiptsTotalAmountByType(String receiptType) {
		double totalAmount = 0;

		for (Receipt receipt : receipts){
			if (receipt.getKind().equals(receiptType)){
				totalAmount += receipt.getAmount();
			}
		}
		return totalAmount;
	}

	public double getTotalReceiptsAmount() {
		double totalReceiptsAmount = 0;
		
		for (Receipt receipt : receipts){
			totalReceiptsAmount += receipt.getAmount();
		}
		
		return (new BigDecimal(totalReceiptsAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public String getName() {
		return name;
	}
	
	public String getAfm() {
		return afm;
	}
	
	public String getFamilyStatus() {
		return familyStatus.getFamilyStatus();
	}

	public ArrayList<Receipt> getReceipts()  {
		return receipts;
	}
	
	public double getIncome() {
		return (new BigDecimal(income).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTax() {
		return (new BigDecimal(tax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTaxIncrease() {
		return (new BigDecimal(taxIncrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTaxDecrease() {
		return (new BigDecimal(taxDecrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getFinalTax() {
		return (new BigDecimal(finalTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}

	public void setReceipts(ArrayList<Receipt> receipts)  {
		this.receipts = receipts;
	}

	public void setTaxIncrease(double taxIncrease) {
		this.taxIncrease = taxIncrease;
	}

	public void addReceiptToList(Receipt receipt) {
		receipts.add(receipt);

		calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
	}

	public void removeReceiptFromList(int index) {
		receipts.remove(index);

		calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
	}

	public void calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts() {
		double totalReceiptsAmount = 0;
		for (Receipt receipt : receipts){
			totalReceiptsAmount += receipt.getAmount();
		}

		if (  totalReceiptsAmount  < 0.2 * income ) {
			taxIncrease = tax * 0.08;
			finalTax = tax + taxIncrease;
		}
		else if ( totalReceiptsAmount < 0.4 * income ) {
			taxIncrease = tax * 0.04;
			finalTax = tax + taxIncrease;
		}
		else if ( totalReceiptsAmount < 0.6 * income ) {
			taxDecrease = tax * 0.15;
			finalTax = tax - taxDecrease;
		}
		else {
			taxDecrease = tax * 0.30;
			finalTax = tax - taxDecrease;
		}
	}
}
