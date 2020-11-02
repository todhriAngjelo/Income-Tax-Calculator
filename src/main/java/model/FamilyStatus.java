package model;

import utils.ApplicationConstants;

public class FamilyStatus {

    private final String familyStatus;
    private final int [] incomeLimits;
    private final double [] basicTax;
    private final double [] rates;

    private FamilyStatus(String familyStatus, int[] incomeLimits, double[] basicTax, double[] rates){
        this.familyStatus = familyStatus;
        this.incomeLimits = incomeLimits;
        this.basicTax = basicTax;
        this.rates = rates;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public int[] getIncomeLimits() {
        return incomeLimits;
    }

    public double[] getBasicTax() {
        return basicTax;
    }

    public double[] getRates() {
        return rates;
    }

    public static FamilyStatus getFamilyStatusInstance( String familyStatus) {


        switch(familyStatus.toLowerCase()){
            case(ApplicationConstants.MARRIED_FILING_JOINTLY):

                return new FamilyStatus(ApplicationConstants.MARRIED_FILING_JOINTLY,
                        ApplicationConstants.INCOME_LIMITS_MARRIED_FILING_JOINTLY,
                        ApplicationConstants.BASIC_TAX_MARRIED_FILING_JOINTLY,
                        ApplicationConstants.RATES_MARRIED_FILING_JOINTLY);

            case(ApplicationConstants.MARRIED_FILING_SEPARATELY):

                return new FamilyStatus(ApplicationConstants.MARRIED_FILING_SEPARATELY,
                        ApplicationConstants.INCOME_LIMITS_MARRIED_FILING_SEPARATELY,
                        ApplicationConstants.BASIC_TAX_MARRIED_FILING_SEPARATELY,
                        ApplicationConstants.RATES_MARRIED_FILING_SEPARATELY);

            case(ApplicationConstants.SINGLE):

                return new FamilyStatus(ApplicationConstants.SINGLE,
                        ApplicationConstants.INCOME_LIMITS_SINGLE,
                        ApplicationConstants.BASIC_TAX_SINGLE,
                        ApplicationConstants.RATES_SINGLE);

            case(ApplicationConstants.HEAD_OF_HOUSEHOLD):

                return new FamilyStatus(ApplicationConstants.HEAD_OF_HOUSEHOLD,
                        ApplicationConstants.INCOME_LIMITS_HEAD_OF_HOUSEHOLD,
                        ApplicationConstants.BASIC_TAX_HEAD_OF_HOUSEHOLD,
                        ApplicationConstants.RATES_HEAD_OF_HOUSEHOLD);
        }

        throw new IllegalArgumentException();
    }
}
