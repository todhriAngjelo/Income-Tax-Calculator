package outputManagePackage;
import model.*;
import model.Receipt;

import java.awt.Dialog.ModalExclusionType;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import utils.ApplicationConstants;

public class OutputSystem {
	
	private static DefaultPieDataset receiptPieChartDataset;
	private static JFreeChart receiptPieJFreeChart;
	private static PiePlot piePlot;
	
	public static DefaultPieDataset getReceiptPieChartDataset() {
		return receiptPieChartDataset;
	}

	public static JFreeChart getReceiptPieJFreeChart() {
		return receiptPieJFreeChart;
	}

	public static PiePlot getPiePlot() {
		return piePlot;
	}

	public static ChartFrame getReceiptPieChartFrame() {
		return receiptPieChartFrame;
	}

	private static ChartFrame receiptPieChartFrame;

	public static void saveUpdatedTaxpayerTxtInputFile(String filePath, int taxpayerIndex){
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(new FileOutputStream(filePath));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Problem opening: "+filePath);
		}
		
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		outputStream.println("Name: "+taxpayer.getName());
		outputStream.println("AFM: "+taxpayer.getAfm());
		outputStream.println("Status: "+taxpayer.getFamilyStatus());
		outputStream.println("Income: "+taxpayer.getIncome());
		
		if (taxpayer.getReceiptsArrayList().size() > 0){
			outputStream.println();
			outputStream.println("Receipts:");
			outputStream.println();
			
			for (Receipt receipt : taxpayer.getReceiptsArrayList()){
				outputStream.println("Receipt ID: "+receipt.getId());
				outputStream.println("Date: "+receipt.getDate());
				outputStream.println("Kind: "+receipt.getKind());
				outputStream.println("Amount: "+receipt.getAmount());
				outputStream.println("Company: "+receipt.getCompany().getName());
				outputStream.println("Country: "+receipt.getCompany().getCountry());
				outputStream.println("City: "+receipt.getCompany().getCity());
				outputStream.println("Street: "+receipt.getCompany().getStreet());
				outputStream.println("Number: "+receipt.getCompany().getNumber());
				outputStream.println();
			}
		}
		
		outputStream.close();
	}
	
	public static void saveUpdatedTaxpayerXmlInputFile(String filePath, int taxpayerIndex){
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(new FileOutputStream(filePath));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Problem opening: "+filePath);
		}
		
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		outputStream.println("<Name> "+taxpayer.getName()+" </Name>");
		outputStream.println("<AFM> "+taxpayer.getAfm()+" </AFM>");
		outputStream.println("<Status> "+taxpayer.getFamilyStatus()+" </Status>");
		outputStream.println("<Income> "+taxpayer.getIncome()+" </Income>");
		
		if (taxpayer.getReceiptsArrayList().size() > 0){
			outputStream.println();
			outputStream.println("<Receipts>");
			outputStream.println();
			
			for (Receipt receipt : taxpayer.getReceiptsArrayList()){
				outputStream.println("<ReceiptID> "+receipt.getId()+" </ReceiptID>");
				outputStream.println("<Date> "+receipt.getDate()+" </Date>");
				outputStream.println("<Kind> "+receipt.getKind()+" </Kind>");
				outputStream.println("<Amount> "+receipt.getAmount()+" </Amount>");
				outputStream.println("<Company> "+receipt.getCompany().getName()+" </Company>");
				outputStream.println("<Country> "+receipt.getCompany().getCountry()+" </Country>");
				outputStream.println("<City> "+receipt.getCompany().getCity()+" </City>");
				outputStream.println("<Street> "+receipt.getCompany().getStreet()+" </Street>");
				outputStream.println("<Number> "+receipt.getCompany().getNumber()+" </Number>");
				outputStream.println();
			}
			
			outputStream.println("</Receipts>");
		}
		
		outputStream.close();
	}

	public static void saveTaxpayerInfoToTxtLogFile(String folderSavePath, int taxpayerIndex){
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		
		PrintWriter outputStream = null;
		try
		{
			if( !System.getProperty("os.name").contains("Windows") ) {
				outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "/" + taxpayer.getAfm() + "_LOG.txt"));
			}
			else{
				outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAfm() + "_LOG.txt"));
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Problem opening: "+folderSavePath+"//"+taxpayer.getAfm()+"_LOG.txt");
		}
		
		outputStream.println("Name: " + taxpayer.getName());
		outputStream.println("AFM: " + taxpayer.getAfm());
		outputStream.println("Income: " + taxpayer.getIncome());
		outputStream.println("Basic Tax: " + taxpayer.getTax());
		if (taxpayer.getTaxIncrease()!=0){
			outputStream.println("Tax Increase: " + taxpayer.getTaxIncrease());
		}else{
			outputStream.println("Tax Decrease: " + taxpayer.getTaxDecrease());
		}
		outputStream.println("Total Tax: " + taxpayer.getFinalTax());
		outputStream.println("Total Receipts Amount: " + taxpayer.getTotalReceiptsAmount());
		outputStream.println("Entertainment: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT));
		outputStream.println("Basic: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT));
		outputStream.println("Travel: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT));
		outputStream.println("Health: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT));
		outputStream.println("Other: " + taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT));
		
		outputStream.close();
	}
	
	public static void saveTaxpayerInfoToXmlLogFile(String folderSavePath, int taxpayerIndex){
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		
		PrintWriter outputStream = null;
		try
		{
			if( !System.getProperty("os.name").contains("Windows") ) {
				outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "/" + taxpayer.getAfm() + "_LOG.xml"));
			}
			else{
				outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAfm() + "_LOG.xml"));
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Problem opening: "+folderSavePath+"//"+taxpayer.getAfm()+"_LOG.xml");
		}
		
		outputStream.println("<Name> "+taxpayer.getName()+" </Name>");
		outputStream.println("<AFM> "+taxpayer.getAfm()+" </AFM>");
		outputStream.println("<Income> "+taxpayer.getIncome()+" </Income>");
		outputStream.println("<BasicTax> "+taxpayer.getTax()+" </BasicTax>");
		if (taxpayer.getTaxIncrease()!=0){
			outputStream.println("<TaxIncrease> "+taxpayer.getTaxIncrease()+" </TaxIncrease>");
		}else{
			outputStream.println("<TaxDecrease> "+taxpayer.getTaxDecrease()+" </TaxDecrease>");
		}
		outputStream.println("<TotalTax> "+taxpayer.getFinalTax()+" </TotalTax>");
		outputStream.println("<Receipts> "+taxpayer.getTotalReceiptsAmount()+" </Receipts>");
		outputStream.println("<Entertainment> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT)+" </Entertainment>");
		outputStream.println("<Basic> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT)+" </Basic>");
		outputStream.println("<Travel> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT)+" </Travel>");
		outputStream.println("<Health> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT)+" </Health>");
		outputStream.println("<Other> "+taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT)+" </Other>");
		
		outputStream.close();
	}
	
	public static void createTaxpayerReceiptsPieJFreeChart(int taxpayerIndex){
		receiptPieChartDataset = new DefaultPieDataset();
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		
		receiptPieChartDataset.setValue("Basic", taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.BASIC_RECEIPT));
		receiptPieChartDataset.setValue("Entertainment", taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.ENTERTAINMENT_RECEIPT));
		receiptPieChartDataset.setValue("Travel", taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.TRAVEL_RECEIPT));
		receiptPieChartDataset.setValue("Health", taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.HEALTH_RECEIPT));
		receiptPieChartDataset.setValue("Other", taxpayer.getReceiptsTotalAmountByType(ApplicationConstants.OTHER_RECEIPT));
		
		receiptPieJFreeChart = ChartFactory.createPieChart("Receipt Pie Chart", receiptPieChartDataset);
		piePlot = (PiePlot)receiptPieJFreeChart.getPlot();
		PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0}: {1}$ ({2})", new DecimalFormat("0.00"), new DecimalFormat("0.00%"));
		piePlot.setLabelGenerator(generator); 

		receiptPieChartFrame = new ChartFrame(Database.getTaxpayerNameAfmValuesPairList(taxpayerIndex), receiptPieJFreeChart);
		receiptPieChartFrame.pack();
		receiptPieChartFrame.setResizable(false);
		receiptPieChartFrame.setLocationRelativeTo(null);
		receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		receiptPieChartFrame.setVisible(true);
	}
	
	public static void createTaxpayerTaxAnalysisBarJFreeChart(int taxpayerIndex){
		DefaultCategoryDataset taxAnalysisBarChartDataset = new DefaultCategoryDataset();
		Taxpayer taxpayer = Database.getTaxpayerFromArrayList(taxpayerIndex);
		
		String taxVariationType = taxpayer.getTaxIncrease()!=0? "Tax Increase" : "Tax Decrease";
		double taxVariationAmount = taxpayer.getTaxIncrease()!=0? taxpayer.getTaxIncrease() : taxpayer.getTaxDecrease()*(-1);
		
		taxAnalysisBarChartDataset.setValue(taxpayer.getTax(), "Tax", "Basic Tax");
		taxAnalysisBarChartDataset.setValue(taxVariationAmount, "Tax", taxVariationType);
		taxAnalysisBarChartDataset.setValue(taxpayer.getFinalTax(), "Tax", "Total Tax");

		JFreeChart taxAnalysisJFreeChart = ChartFactory.createBarChart("Tax Analysis Bar Chart", "",  "Tax Analysis in $", taxAnalysisBarChartDataset, PlotOrientation.VERTICAL, true, true, false);

		ChartFrame receiptPieChartFrame = new ChartFrame(Database.getTaxpayerNameAfmValuesPairList(taxpayerIndex), taxAnalysisJFreeChart);
		receiptPieChartFrame.pack();
		receiptPieChartFrame.setResizable(false);
		receiptPieChartFrame.setLocationRelativeTo(null);
		receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		receiptPieChartFrame.setVisible(true);
	}
}
