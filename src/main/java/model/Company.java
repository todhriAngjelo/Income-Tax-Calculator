package model;

public class Company {
	private String name;
	private String country;
	private String city;
	private String street;
	private String number;

	public Company(String name, String country, String city, String street, String number) {
		this.name = name.trim();
		this.country = country.trim();
		this.city = city.trim();
		this.street = street.trim();
		this.number = number.trim();
	}

	public String getName() {
		return name;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getNumber() {
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "\nCompany: " + name
				+"\nCountry: " + country
				+"\nCity: " + city
				+"\nStreet: " + street
				+"\nNumber: " + number;
	}
}
