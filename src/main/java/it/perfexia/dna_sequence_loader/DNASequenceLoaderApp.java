package it.perfexia.dna_sequence_loader;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

public class DNASequenceLoaderApp {

	String _txtOutputPreview = "";
	private String _txtFastQFilePath = "";
	private String _txtFastQOutFilePath = "";

	/** Constructor
	 * 
	 * @param inFile
	 * @param outFile
	 * @throws IOException 
	 */
	public DNASequenceLoaderApp(String inFile, String outFile) throws IOException {
		this._txtFastQFilePath = inFile;
		this._txtFastQOutFilePath = outFile;
		
        Files.copy(Paths.get(_txtFastQFilePath), 
     		       Paths.get(_txtFastQFilePath+"_orig") );

	}

	/** The App 
	 * @throws IOException */
	public static void main(String[] args) throws IOException {
		if (args == null || args.length < 2) {
			System.out.println("\n\tNO!!! Sono necessarti due parametri: inputFileName, outputFileName\n");
			return;
		}
		
		new DNASequenceLoaderApp(args[0], args[1]).doConvert1();
		//new DNASequenceLoaderApp(args[0], args[1]).doConvert2();
		//new DNASequenceLoaderApp(args[0], args[1]).doConvert3();
	}

    Long charsCounted = 0L;
    String newLine="";
    
	void doConvert1() {
	    newLine = "";
	    System.out.println("Inizio elaborazione, at: " + new Date().toString());
	    
	    try (BufferedReader lines = Files.newBufferedReader(Paths.get(_txtFastQFilePath))) {
	        // Read and display lines from the file until the end of 
	        // the file is reached, or a close operation is called.
	        String line = "";
	        int seqNumber = 1;
	        int blockCounter = 0;
	        
	        while (lines.ready() && (line = lines.readLine()) != null) {
	        	charsCounted += line.length() +1; //the LF
	        	//debug System.out.println("seqNumer: " + seqNumber + "; line: " + line);
	        	
	            blockCounter++;
	            if (blockCounter == 1)
	            {
	                newLine += line.replaceAll(":", "\t")
	                			   .replaceAll(" ", "\t")
	                			   .substring(1, line.length() - 1);
	            }
	
	            if (blockCounter == 2)
	            {
	                newLine += "\t" + line;
	            }
	
	            if (blockCounter == 4)
	            {
	                newLine += "\t" + line + System.lineSeparator();
	                blockCounter = 0;
	                seqNumber++;
	            }
	
	            // every 19999 sequences, it writes down to the outfile
	            if ((seqNumber % 20000) == 0 && blockCounter == 0)
	            {
	                _txtOutputPreview = seqNumber +  " sequences saved"; //+ System.lineSeparator();
	                System.out.println("\n\t" + _txtOutputPreview);
	                lines.close();
	                
	    	        break; //and break the cycle, in order to "reduce" the infile
	            }
	        }
			
			//saving to the outFile
	    	write (newLine);
	        
	        { /** this block REDUCES the inFile to the remainder (unread lines) of it **/
                System.out.println("how-many chars read?: " + charsCounted );
                byte[]  all =  Files.readAllBytes( Paths.get(_txtFastQFilePath) );
                System.out.println("allBytes are: " + all.length);
                if (charsCounted > all.length) {
                	charsCounted = all.length + 0L;
                }
                all = Arrays.copyOfRange(all, (int)(charsCounted+0), all.length-1);
                System.out.println("remainderBytes are: " + all.length);
                
                Files.move(Paths.get(_txtFastQFilePath), 
                		   Paths.get(_txtFastQFilePath+"_elaborated_"+System.currentTimeMillis()) );
                Files.createFile(Paths.get(_txtFastQFilePath));
                Files.write(Paths.get(_txtFastQFilePath), all);
                
                //recursive call, until: "The file could not be read: target/classes/inFile"
                charsCounted =0L; 
                doConvert1();
	        }
	    } catch (Exception e) {
		    // Let the user know what went wrong.
		    System.out.println("\n\tERROR - The file could not be read: " + _txtFastQFilePath);
	    } finally {
		    System.out.println("Termine elaborazione, at: " + new Date().toString());
	    }
	}
	
	private void write (String newLines) {
		FileWriter fw;
		try {
			fw = new FileWriter(_txtFastQOutFilePath, true); //true will append the new data
		    fw.write(newLines);//appends the string to the file
		    fw.flush(); fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}