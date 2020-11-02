package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Taxpayer {
	private String name;
	private String afm;
	private FamilyStatus familyStatus;
	private double income;
	private double Tax;
	private double taxIncrease;
	private double taxDecrease;
	private double finalTax;
	private ArrayList<Receipt> receipts;
	
	public Taxpayer(String name, String afm, String familyStatus, String income){
		this.name = name;
		this.afm = afm;
		this.familyStatus = FamilyStatus.getFamilyStatusInstance(familyStatus);
		this.income = Double.parseDouble(income);
		this.Tax = calculateTax();
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
	
	public String toString(){
		return "Name: "+name
				+"\nAFM: "+afm
				+"\nStatus: "+familyStatus
				+"\nIncome: "+String.format("%.2f", income)
				+"\nBasicTax: "+String.format("%.2f", Tax)
				+"\nTaxIncrease: "+String.format("%.2f", taxIncrease)
				+"\nTaxDecrease: "+String.format("%.2f", taxDecrease);
	}
	
	public Receipt getReceipt(int receiptID){
		return receipts.get(receiptID);
	}
	
	public ArrayList<Receipt> getReceiptsArrayList(){
		return receipts;
	}
	
	public String[] getReceiptsList(){
		String[] receiptsList = new String[receipts.size()];
		
		int c = 0;
		for (Receipt receipt : receipts){
			receiptsList[c++] = receipt.getId() + " | " + receipt.getDate() + " | " + receipt.getAmount();
		}
		
		return receiptsList;
	}
	
	public double getBasicReceiptsTotalAmount(){
		double basicReceiptsTotalAmount = 0;
		
		for (Receipt receipt : receipts){
			if (receipt.getKind().equals("Basic")){
				basicReceiptsTotalAmount += receipt.getAmount();
			}
		}
		
		return (new BigDecimal(basicReceiptsTotalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getEntertainmentReceiptsTotalAmount(){
		double entertainmentReceiptsTotalAmount = 0;
		
		for (Receipt receipt : receipts){
			if (receipt.getKind().equals("Entertainment")){
				entertainmentReceiptsTotalAmount += receipt.getAmount();
			}
		}
		
		return (new BigDecimal(entertainmentReceiptsTotalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTravelReceiptsTotalAmount(){
		double travelReceiptsTotalAmount = 0;
		
		for (Receipt receipt : receipts){
			if (receipt.getKind().equals("Travel")){
				travelReceiptsTotalAmount += receipt.getAmount();
			}
		}
		
		return (new BigDecimal(travelReceiptsTotalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getHealthReceiptsTotalAmount(){
		double healthReceiptsTotalAmount = 0;
		
		for (Receipt receipt : receipts){
			if (receipt.getKind().equals("Health")){
				healthReceiptsTotalAmount += receipt.getAmount();
			}
		}
		
		return (new BigDecimal(healthReceiptsTotalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}

	public double getOtherReceiptsTotalAmount(){
		double otherReceiptsTotalAmount = 0;
		
		for (Receipt receipt : receipts){
			if (receipt.getKind().equals("Other")){
				otherReceiptsTotalAmount += receipt.getAmount();
			}
		}
		
		return (new BigDecimal(otherReceiptsTotalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTotalReceiptsAmount(){
		double totalReceiptsAmount = 0;
		
		for (Receipt receipt : receipts){
			totalReceiptsAmount += receipt.getAmount();
		}
		
		return (new BigDecimal(totalReceiptsAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public String getName(){
		return name;
	}
	
	public String getAFM(){
		return afm;
	}
	
	public String getFamilyStatus(){
		return familyStatus.getFamilyStatus();
	}
	
	public double getIncome(){
		return (new BigDecimal(income).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTax(){
		return (new BigDecimal(Tax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTaxIncrease(){
		return (new BigDecimal(taxIncrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getTaxDecrease(){
		return (new BigDecimal(taxDecrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public double getFinalTax(){
		return (new BigDecimal(finalTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public void addReceiptToList(Receipt receipt){
		receipts.add(receipt);
		
		calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
	}
	
	public void removeReceiptFromList(int index){
		receipts.remove(index);
		
		calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
	}
	
	public void calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts(){
		double totalReceiptsAmount = 0;
		for (Receipt receipt : receipts){
			totalReceiptsAmount += receipt.getAmount();
		}
		
		taxIncrease = 0;
		taxDecrease = 0;
		if ((totalReceiptsAmount/(double)income) < 0.2){
			taxIncrease = Tax * 0.08;
		}
		else if ((totalReceiptsAmount/(double)income) < 0.4){
			taxIncrease = Tax * 0.04;
		}
		else if ((totalReceiptsAmount/(double)income) < 0.6){
			taxDecrease = Tax * 0.15;
		}
		else{
			taxDecrease = Tax * 0.30;
		}
		
		finalTax = Tax + taxIncrease - taxDecrease;
	}
}