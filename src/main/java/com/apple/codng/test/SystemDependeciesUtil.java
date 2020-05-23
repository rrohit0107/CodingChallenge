/**
 * 
 */
package com.apple.codng.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Rohit
 *
 */
public class SystemDependeciesUtil {
	
	
	//Define some class level variables which can be used across all the methods.
	
	List<String> dependeciesList = null;
	Map<String,List<String>> dependenciesMap = new HashMap<>();
	List<String> installedProgramList = new ArrayList<>();
	Set<String> dependentProgramList;
	
	/**
	 * This method will get invoked to execute the DEPEND command.
	 * @param commandAttributes
	 */
	public void processDependCommand(String[] commandAttributes) {
		
			dependeciesList = new LinkedList<>();
			
			//Iterate the commandAttributes for all the dependencies
			for(int i = 0; i < commandAttributes.length; i++) {
				//Ignore the first two elements as those will be the command and the actual parent program which needs to be installed.
				if(i > 1) {
					dependeciesList.add(commandAttributes[i]);
				}
			}
			
			//Maintain the mapping between the parent program and all its dependencies in the map.
			dependenciesMap.put(commandAttributes[1],dependeciesList);
		
	}
	
	/**
	 * This method will get invoked for installing a program.
	 * @param commandAttributes
	 */
	public void processInstallCommand(String[] commandAttributes) {
		
		//1st attribute will be the command and actual program will be the 2nd attribute.
		String programName  = commandAttributes[1].trim();
		
		//Check if the program is already installed.
		if(installedProgramList.contains(programName)) {
			System.out.println("\t" + programName + " is already installed.");
		}else {
			dependentProgramList = new LinkedHashSet<>();
			
			//Make a recursive call to get all the dependent programs for this program.
			getAllDependentPrograms(programName);
			
			//Iterate the set in reverse order to maintain the dependency hierarchy
			List<String> dependenciesProgram = new ArrayList<>(dependentProgramList);
			Collections.reverse(dependenciesProgram);

			for( String program : dependenciesProgram ){
				
				if(!installedProgramList.contains(program)) {
					installedProgramList.add(program);
					System.out.println("\tInstalling " + program + ".");
				}
				
			}
		}
	}
	
	/**
	 * This method will remove the installed program and all its dependent programs if eligible.
	 * @param commandAttributes
	 */
	public void processRemoveCommand(String[] commandAttributes) {
		
		//1st attribute will be the command and actual program will be the 2nd attribute.
		String programName  = commandAttributes[1].trim();
		
		//Check if the program installed or not.
		if(!installedProgramList.contains(programName)) {
			System.out.println("\t" + programName + " is not installed.");
		}else {
			boolean isRemoveProgram = true;
			
			//Check if the mentioned program is a dependency for other installed program.
			//If yes, then we can not remove the program.
			for(Map.Entry<String,List<String>> entry : dependenciesMap.entrySet()) {
				if(entry.getValue().contains(programName)
						&& installedProgramList.contains(entry.getKey())) {
					System.out.println("\t" + programName + " is still needed.");
					
					isRemoveProgram = false;
					break;
				}
			}
			
			//If the program is eligible for deletion, go ahead and delete the same.
			if(isRemoveProgram) {
				System.out.println("\tRemoving " + programName + ".");
				
				//Delete the program from the installed program list as well.
				installedProgramList.remove(programName);
				
				//Get the dependent program list from the map and then delete the reference.
				List<String> dependentProgramToRemoveList = dependenciesMap.get(programName);
				dependenciesMap.remove(programName);
				
				//The below code is trying to check if the dependent program is not needed, it will remove them as well.
				if(dependentProgramToRemoveList != null && dependentProgramToRemoveList.size() > 0) {
					for(String dependentProgramToRemove : dependentProgramToRemoveList) {
						for(Map.Entry<String,List<String>> entry : dependenciesMap.entrySet()) {
							if(entry.getValue().contains(dependentProgramToRemove)) {
								isRemoveProgram = false;
								
								break;
							}
						}
						
						if(isRemoveProgram) {
							System.out.println("\tRemoving " + dependentProgramToRemove + ".");
							installedProgramList.remove(dependentProgramToRemove);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Method to get all the dependencies recursively.
	 * @param programName
	 */
	private void getAllDependentPrograms(String programName) {
		
		dependentProgramList.add(programName);
		
		if(!dependenciesMap.containsKey(programName)) {
			return;
		}
		
		List<String> dependeciesList = dependenciesMap.get(programName);
		
		for(String dependentProgram : dependeciesList) {
			getAllDependentPrograms(dependentProgram);
		}
	}

	
	public List<String> getDependeciesList() {
		return dependeciesList;
	}

	public void setDependeciesList(List<String> dependeciesList) {
		this.dependeciesList = dependeciesList;
	}

	public Map<String, List<String>> getDependenciesMap() {
		return dependenciesMap;
	}

	public void setDependenciesMap(Map<String, List<String>> dependenciesMap) {
		this.dependenciesMap = dependenciesMap;
	}

	public List<String> getInstalledProgramList() {
		return installedProgramList;
	}

	public void setInstalledProgramList(List<String> installedProgramList) {
		this.installedProgramList = installedProgramList;
	}

	public Set<String> getDependentProgramList() {
		return dependentProgramList;
	}

	public void setDependentProgramList(Set<String> dependentProgramList) {
		this.dependentProgramList = dependentProgramList;
	}
}
