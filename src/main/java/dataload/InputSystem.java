package dataload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class InputSystem {


	public Scanner createInputStream(String afmInfoFileFolderPath, String afmInfoFile) throws FileNotFoundException {
		return new Scanner(
				new FileInputStream(String.valueOf( Paths.get(afmInfoFileFolderPath, afmInfoFile) ) )
		);
	}

}