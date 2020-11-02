package model;

import utils.ApplicationConstants;

import java.math.BigDecimal;

public class Receipt {
	protected String kind;
	protected String id;
	protected String date;
	protected double amount;
	protected Company company;
	
	public Receipt(String kind, String id, String date, String amount, String name, String country, String city, String street, String number){
		//this.kind = kind;
		if (kind.toLowerCase().equals(ApplicationConstants.BASIC_RECEIPT)){
			this.kind=ApplicationConstants.BASIC_RECEIPT;
		}
		else if (kind.toLowerCase().equals(ApplicationConstants.HEALTH_RECEIPT)){
			this.kind=ApplicationConstants.HEALTH_RECEIPT;
		}
		else if (kind.toLowerCase().equals(ApplicationConstants.TRAVEL_RECEIPT)){
			this.kind=ApplicationConstants.TRAVEL_RECEIPT;
		}
		else if (kind.toLowerCase().equals(ApplicationConstants.ENTERTAINMENT_RECEIPT)) {
			this.kind = ApplicationConstants.ENTERTAINMENT_RECEIPT;
		}
		else if (kind.toLowerCase().equals(ApplicationConstants.OTHER_RECEIPT)){
			this.kind=ApplicationConstants.OTHER_RECEIPT;
		}
		this.id = id;
		this.date = date;
		this.amount = Double.parseDouble(amount);
		this.company = new Company(name, country, city, street, number);
	}
	
	public String getId(){
		return id;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getKind(){
		return kind;
	}
	
	public double getAmount(){
		return (new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public Company getCompany(){
		return company;
	}

	public String toString(){
		return "ID: "+id
				+"\nDate: "+date
				+"\nKind: "+kind
				+"\nAmount: "+String.format("%.2f", amount)
				+"\nCompany: "+company.getName()
				+"\nCountry: "+company.getCountry()
				+"\nCity: "+company.getCity()
				+"\nStreet: "+company.getStreet()
				+"\nNumber: "+company.getNumber();
	}
}