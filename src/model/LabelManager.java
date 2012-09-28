package model;

import java.util.Hashtable;

import exception.DuplicateLabelException;

public class LabelManager
{
	Hashtable<String, Label> labels;
	
	public LabelManager() {
		labels = new Hashtable<String, Label>(50);
	}
	
	/**
	 * Adds a label to the Label Manager
	 * @param label the label to add
	 * @throws DuplicateLabelException throws an exception if a label with the same name already exists
	 */
	public void addLabel(Label label) throws DuplicateLabelException{
		if (labels.containsKey(label.getName()))
			throw new DuplicateLabelException();
		labels.put(label.getName(), label);
	}
	
	/**
	 * Returns the Label with the given label name
	 * @param labelName the label name to return
	 * @return the Label with the given name, returns null if it doesn't exist
	 */
	public Label getLabel(String labelName){
		return labels.get(labelName);
	}
	
	public int getLabelCount(){
		return labels.size();
	}
}