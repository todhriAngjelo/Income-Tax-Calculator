package export;

import java.io.FileNotFoundException;

public abstract class OutputSystem {

	public abstract void saveUpdatedTaxpayerInputFile(String filePath, int taxpayerIndex) throws FileNotFoundException;

	public abstract void saveTaxpayerInfoLogFile(String folderSavePath, int taxpayerIndex) throws FileNotFoundException;

}
