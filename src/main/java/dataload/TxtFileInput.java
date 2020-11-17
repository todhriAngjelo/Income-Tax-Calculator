package dataload;

import model.Receipt;

import java.util.ArrayList;
import java.util.Scanner;

public class TxtFileInput extends InputSystem {

    public TxtFileInput(String[] tags) {
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

            receipts.add(createReceiptFromFile(inputStream, fileLine));
        }
        return receipts;
    }


}
