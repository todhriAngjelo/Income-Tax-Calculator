package exporters;

public abstract class OutputSystem {

	public abstract void saveUpdatedTaxpayerInputFile(String filePath, int taxpayerIndex);

	public abstract void saveTaxpayerInfoLogFile(String folderSavePath, int taxpayerIndex);


}
