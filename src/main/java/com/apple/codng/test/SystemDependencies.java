/**
 * 
 */
package com.apple.codng.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Rohit
 *
 */
public class SystemDependencies {
	

	
	public static void main(String[] args) {
		
		//Read the sequence of commands from a file
		String fileName = args[0];
		
		File file;
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			//Read the commands from the file.
			file = new File(fileName);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String commandLine;
			
			
			String[] attributes;
			
			SystemDependeciesUtil systemDependeciesUtil = new SystemDependeciesUtil();
			
			while((commandLine = br.readLine()) != null) {
				
				//Check for the command line length. It should be max 80 chars.
				if(commandLine.length() < Commands.LIMIT_80_CHARS) {
					//Split the line for each attributes based on one or more spaces
					attributes = commandLine.split("\\s+");
					
					String commandName  = attributes[0].trim();
					
					//Check for command char length
					if(commandName.length() < Commands.LIMIT_10_CHARS) {
					    //process the DEPEND command
						if(commandName.equals(Commands.DEPEND_COMMAND)) {
							System.out.println(commandLine);
							
							systemDependeciesUtil.processDependCommand(attributes);
						}
						//Process the INSTALL command
						else if(commandName.equals(Commands.INSTALL_COMMAND)) {
							System.out.println(commandLine);
							
							systemDependeciesUtil.processInstallCommand(attributes);
						}
						
						//Process the REMOVE command
						else if(commandName.equals(Commands.REMOVE_COMMAND)) {
							System.out.println(commandLine);
							
							systemDependeciesUtil.processRemoveCommand(attributes);
						}
						
						//Process the LIST command
						else if(commandName.equals(Commands.LIST_COMMAND)) {
							System.out.println(commandLine);
							
							for(String programName : systemDependeciesUtil.getInstalledProgramList()) {
								System.out.println("\t" + programName);
							}
						}
						
						//Process the END command
						else if(commandName.equals(Commands.END_COMMAND)) {
							System.out.println(commandLine);
						}
						//If the mentioned command is not supported or if its in lower case.
						else {
							System.out.println("Command not found.");
						}
					}else {
						System.out.println("Command exceeds the char limit.");
					}
				}else {
					System.out.println("Command line exceeds the char limit.");
				}
				

			}
		} catch (FileNotFoundException e) {
			System.out.println("Mentioned file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
