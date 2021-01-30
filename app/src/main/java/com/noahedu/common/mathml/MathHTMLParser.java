/*
 * Created on 2003-11-06
 */
package com.noahedu.common.mathml;

/**
 * @author Tilman Walther
 */
public class MathHTMLParser {
	
	static StringBuffer tmpBuf = new StringBuffer(100);
	
	public static String parse(StringBuffer strBuf, boolean parseFormatting) {
		for (int i = 0; i < strBuf.length(); i++) {
			if (strBuf.charAt(i) == '<') {
				
				tmpBuf.setLength(0);
					
				while (strBuf.charAt(i+1) != '>') {
					strBuf.deleteCharAt(i);
					tmpBuf.append(strBuf.charAt(i));				
				}
				strBuf.delete(i,i+2);
			
				if (tmpBuf.toString().compareTo("br") == 0) {
					//strBuf.insert(i, "{br}");
				}
				else if (parseFormatting) {
					
					if (tmpBuf.toString().compareTo("b") == 0) {
						strBuf.insert(i, "{F:b}");
					}
					else if (tmpBuf.toString().compareTo("/b") == 0) {
						strBuf.insert(i, "{F:/b}");
					}
					else if (tmpBuf.toString().compareTo("i") == 0) {
						strBuf.insert(i, "{F:i}");
					}
					else if (tmpBuf.toString().compareTo("/i") == 0) {
						strBuf.insert(i, "{F:/i}");
					}
					else if (tmpBuf.toString().compareTo("u") == 0) {
						strBuf.insert(i, "{F:u}");
					}
					else if (tmpBuf.toString().compareTo("/u") == 0) {
						strBuf.insert(i, "{F:/u}");
					}
				}
				
				i--;
				
				/*
				while (strBuf.charAt(i) != '>') strBuf.deleteCharAt(i);
				strBuf.deleteCharAt(i);
				i--;
				*/
			}
			else if (strBuf.charAt(i) == '&') {
				tmpBuf.setLength(0);
				
				while (strBuf.charAt(i+1) != ';') {
					strBuf.deleteCharAt(i);
					tmpBuf.append(strBuf.charAt(i));				
				}
				strBuf.delete(i,i+2); 
				
				if (tmpBuf.toString().compareTo("amp") == 0) {
					strBuf.insert(i, '&');
				}
				else if (tmpBuf.toString().compareTo("lt") == 0) {
					strBuf.insert(i, '<');
				}
				else if (tmpBuf.toString().compareTo("gt") == 0) {
					strBuf.insert(i, '>');
				}
				else if (tmpBuf.toString().compareTo("quot") == 0) {
					strBuf.insert(i, '"');
				}
				else {
					strBuf.insert(i, '_');
				}
			}
			else if (strBuf.charAt(i) == 0xD) {
				strBuf.deleteCharAt(i);
				
				if ((i < strBuf.length()) && (strBuf.charAt(i) == 0xA)) {
					strBuf.deleteCharAt(i);
				}
				
				if ((i > 0) && (strBuf.charAt(i-1) != ' ')) {
					strBuf.insert(i, ' ');
				}
				else {
					i--; 
				}
			}
		}

		return strBuf.toString();
	}
}
