###############################################
## DNA Sequences Loader and Transformer      ##
###############################################

 This simple java main Application shows how-to:
 - load huge "DNA sequence" text files, 1GB+ each, 
   where one sequnce is splut over four-lines
 - build one \t-separated output record from each sequence
 - append output records to a file, every 20000 processed sequences
 - REDUCE the huge input file eliminating already processed lines
 - process the REMAINDER of the huge file, for the next 20000 sequences
 - and so on, till the end-of-last-REDUCED-file
 
 The App uses a self-calling recursive method
 
 Without recursion the "transformation process" may take 2-hours
 to process a 2GB input text file
 
 With recursion the "transformation process" shortens upto 30-45 minutes
 to process the same 2GB input text file
 
 This a maven project. src/main/resources contains a sample inFile
 
 To launch the App by Windows prompt, pls use the runme.cmd
 that requires the [inFile] and [outFile] pathNames, as args
 
 During transformation, if inFile has more than 20000 sequence (each of 4-lines)
 the App produces REDUCED inFile[s]
 