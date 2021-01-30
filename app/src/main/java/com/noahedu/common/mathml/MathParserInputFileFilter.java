/*
 * Created on 29.11.2003
 */
package com.noahedu.common.mathml;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Tilman Walther
 */
public class MathParserInputFileFilter implements FileFilter {
	
	final String[] extensions = {"htm", "html", "mml"};
	
	public boolean accept(File f) {
		
		if (f.isDirectory()) {
			return true;
		}

		String ext = null;
		String s = f.getName();
		int in1 = s.lastIndexOf('.');
		
		if (in1 > 0 &&  in1 < s.length() - 1) {
			ext = s.substring(in1+1).toLowerCase();
		}
        
		if (ext != null) {
	        for (int i=0; i < extensions.length; i++) {
	        	if (ext.equals(extensions[i])) {
	        		return true;
	        	} 
	        }
		}
        
		return false;
	}

	public String getDescription() {
		return "MathML/HTML Dateien";
	}
}
