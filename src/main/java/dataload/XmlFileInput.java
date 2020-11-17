package dataload;

import model.Receipt;
import java.util.ArrayList;
import java.util.Scanner;

public class XmlFileInput extends InputSystem{

    public XmlFileInput(String[] tags) {
        super(tags);
    }

    @Override
    public ArrayList<Receipt> extractTaxpayerReceiptsFromFile(Scanner inputStream) {
        String[] tags = getTags();
        ArrayList<Receipt> receipts = new ArrayList<>();
        while (inputStream.hasNextLine()) {
            String fileLine = inputStream.nextLine();
            if (fileLine.equals("")) continue;
            if (fileLine.contains(tags[8])) continue;
            if (fileLine.contains(tags[9])) break;

            receipts.add(createReceiptFromFile(inputStream, fileLine));
        }
        return receipts;
    }
}
