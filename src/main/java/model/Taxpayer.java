package model;

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
		this.name = name.trim();
		this.afm = afm.trim();
		this.familyStatus = FamilyStatus.getFamilyStatusInstance( familyStatus.trim() );
		this.income = Double.parseDouble( income.trim() );
		this.tax = calculateTax();
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
		return totalReceiptsAmount ;
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
		return income;
	}
	
	public double getTax() {
		return tax;
	}
	
	public double getTaxIncrease() {
		return taxIncrease;
	}
	
	public double getTaxDecrease() {
		return taxDecrease;
	}
	
	public double getFinalTax() {
		return finalTax;
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
		taxIncrease = 0;
		taxDecrease = 0;
		for (Receipt receipt : receipts){
			totalReceiptsAmount += receipt.getAmount();
		}

		if (  totalReceiptsAmount  < 0.2 * income ) {
			taxIncrease = tax * 0.08;
		}
		else if ( totalReceiptsAmount < 0.4 * income ) {
			taxIncrease = tax * 0.04;
		}
		else if ( totalReceiptsAmount < 0.6 * income ) {
			taxDecrease = tax * 0.15;
		}
		else {
			taxDecrease = tax * 0.30;
		}
		finalTax = tax + taxIncrease - taxDecrease;
	}
}
