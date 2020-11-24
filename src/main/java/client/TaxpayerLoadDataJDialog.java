package client;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;

import persistence.Database;


public class TaxpayerLoadDataJDialog extends JDialog {
	
	private final JList<String> taxpayersAfmInfoFilesJList;

	private String afmInfoFilesFolderPath;


	public TaxpayerLoadDataJDialog(final JFrame appMainWindow) {

		setResizable(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setType(Type.NORMAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 486, 332);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setTitle("Arxeia fortwshs dedomenwn");
		
		JScrollPane scrollPaneForList = new JScrollPane();
		scrollPaneForList.setBounds(10, 11, 250, 258);
		getContentPane().add(scrollPaneForList);
		
		taxpayersAfmInfoFilesJList = new JList<String>();
		taxpayersAfmInfoFilesJList.setForeground(Color.BLUE);
		taxpayersAfmInfoFilesJList.setFont(new Font("Tahoma", Font.BOLD, 11));
		scrollPaneForList.setViewportView(taxpayersAfmInfoFilesJList);
		taxpayersAfmInfoFilesJList.setVisibleRowCount(100);
		
		JButton loadDataFromSelectedAfmInfoFilesButton = new JButton();
		loadDataFromSelectedAfmInfoFilesButton.setBounds(270, 11, 198, 68);
		String text = "<html>"
				+ "fortwsh dedomenwn"
				+ "<br>"
				+ "epilegmenwn arxeiwn"
				+ "</html>";
		loadDataFromSelectedAfmInfoFilesButton.setText(text);
		loadDataFromSelectedAfmInfoFilesButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(loadDataFromSelectedAfmInfoFilesButton);
		
		JButton selectAllButton = new JButton("Epilogh olwn");
		selectAllButton.setBounds(10, 274, 250, 23);
		selectAllButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(selectAllButton);
		
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taxpayersAfmInfoFilesJList.setSelectionInterval(0, taxpayersAfmInfoFilesJList.getModel().getSize()-1);
			}
		});
		
		loadDataFromSelectedAfmInfoFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> afmInfoFilesListToLoad = taxpayersAfmInfoFilesJList.getSelectedValuesList();

				if (afmInfoFilesListToLoad.size() > 0){
					String confirmDialogText = "Fortwsh dedomenwn forologoumenwn apo ta akoloutha arxeia:\n";
					for (String afmInfoFileName : afmInfoFilesListToLoad){
						confirmDialogText += afmInfoFileName + "\n";
					}
					confirmDialogText += "Eiste sigouros?";
					
					int dialogResult = JOptionPane.showConfirmDialog (null, confirmDialogText, "Epivevaiwsh", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
						try {
							Database.processTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, afmInfoFilesListToLoad);
							JLabel totalLoadedTaxpayersJLabel = (JLabel)appMainWindow.getContentPane().getComponent(1);
							totalLoadedTaxpayersJLabel.setText(Integer.toString(Database.getDatabaseInstance().getTaxpayersArrayListSize()));
							dispose();
						} catch (FileNotFoundException exception) {
							JOptionPane.showMessageDialog(null, "Error loading taxpayer files", "Error", JOptionPane.ERROR_MESSAGE);
							exception.printStackTrace();
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Den exeis epilexei arxeio apo th lista", "Sfalma", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
	
	public void fillTaxpayersAfmInfoFilesJList(String afmInfoFilesFolderPath){
		this.afmInfoFilesFolderPath = afmInfoFilesFolderPath;
		
		File folder = new File(afmInfoFilesFolderPath);
		File[] folderFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
		    }
		});
		
		final String[] jlistValues = new String[folderFiles.length];
		int jlistValuesItems = 0;
		for (File file : folderFiles){
			if (file.isFile()){
				jlistValues[jlistValuesItems++] = file.getName();
			}
		}
		
		taxpayersAfmInfoFilesJList.setModel(new AbstractListModel() {
			final String[] values = jlistValues;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
}
