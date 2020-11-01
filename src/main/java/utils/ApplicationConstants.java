package utils;

public class ApplicationConstants {

    // Family Statuses
    public static final String MARRIED_FILING_JOINTLY = "married filing jointly";
    public static final String MARRIED_FILING_SEPARATELY = "married filing separately";
    public static final String SINGLE = "single";
    public static final String HEAD_OF_HOUSEHOLD = "head of household";

    // Income limits based on Family status
    public static final int [][] INCOME_LIMITS_MARRIED_FILING_JOINTLY
            = new int[][] {  {      0,  36080 },
                             {  36080,  90000 },
                             {  90000, 143350 },
                             { 143350, 254240 }
                          };
    public static final int [][] INCOME_LIMITS_MARRIED_FILING_SEPARATELY
            = new int[][] {  {     0,  18040 },
                             { 18040,  71680 },
                             { 71680,  90000 },
                             { 90000, 127120 }
                          };
    public static final int [][] INCOME_LIMITS_SINGLE
            = new int[][] {  {      0,  24680 },
                             {  24680,  81080 },
                             {  81080,  90000 },
                             {  90000, 152540 }
                          };
    public static final int [][] INCOME_LIMITS_HEAD_OF_HOUSEHOLD
            = new int[][] {  {      0,  30390 },
                             {  30390,  90000 },
                             {  90000, 122110 },
                             { 122110, 203390 }
                          };

    // Basic Tax based on Family status
    public static final double [] BASIC_TAX_MARRIED_FILING_JOINTLY = new double[] { 0, 1930.28, 5731.64, 9492.82, 18197.69 };
    public static final double [] BASIC_TAX_MARRIED_FILING_SEPARATELY = new double[] { 0, 965.14, 4746.76, 6184.88, 9098.80 };
    public static final double [] BASIC_TAX_SINGLE = new double[] { 0, 1320.38, 5296.58, 5996.80, 10906.19 };
    public static final double [] BASIC_TAX_HEAD_OF_HOUSEHOLD = new double[] { 0, 1625.87, 5828.38, 8092.13, 14472.61 };

    // Tax rates based on Family status
    public static final double [] RATES_MARRIED_FILING_JOINTLY = new double[] { 5.35/100, 7.05/100, 7.05/100, 7.85/100, 9.85/100 };
    public static final double [] RATES_MARRIED_FILING_SEPARATELY = new double[] { 5.35/100, 7.05/100, 7.85/100, 7.85/100, 9.85/100 };
    public static final double [] RATES_SINGLE = new double[] { 5.35/100, 7.05/100, 7.85/100, 7.85/100, 9.85/100 };
    public static final double [] RATES_HEAD_OF_HOUSEHOLD = new double[] { 5.35/100, 7.05/100, 7.05/100, 7.85/100, 9.85/100 };

}
