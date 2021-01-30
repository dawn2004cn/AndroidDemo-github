/*
 * Created on 04.11.2003
 */
package com.noahedu.common.mathml;

import java.io.*;
import java.util.LinkedList;


/**
 * Parses MathML output of Mathcad (2001) into LaTeX using de.tilman.mathParser.mathMLParser.
 * <br><br>
 * Technical notes: The class looks for all &lt;div&gt; sections and indentifies the
 * 'mcd:region' property. &quot;TXT&quot; regions are parsed into the output directly,
 * &quot;EQN&quot; regions are passed to and parsed by a MathMLParser object.
 * All quick'n'dirty. 
 * 
 * @author Tilman Walther
 * @author Martin Wilke
 */
public class MathcadParser {
	
	protected final String DIVREGION = "<div mcd:region=\"";
	protected final String HEADER_FILE = "header.txt";
	
	BufferedReader inStream = null;
	protected File inFile;
	
	protected LinkedList		data;
	protected MathMLParser		mathMLParser;
	
	protected StringBuffer resBuf = new StringBuffer(128000);
	protected StringBuffer tmpBuf = new StringBuffer(100);
	
	protected boolean	wrappedEntities;
	protected boolean   addHeader;
	protected boolean	skipUnknownEntities;
	protected boolean	parseFormatting;
	protected int		formulaFormatting;
	
	public MathcadParser(MathMLParser mathMLParser, boolean wrappedEntities, boolean addHeader, boolean skipUnknownEntities, boolean parseFormatting, int formulaFormatting) {
		this.wrappedEntities		= wrappedEntities;
		this.addHeader				= addHeader;
		this.skipUnknownEntities	= skipUnknownEntities;
		this.parseFormatting		= parseFormatting;
		this.formulaFormatting		= formulaFormatting;
		this.mathMLParser 			= mathMLParser;
	}
	
	public LinkedList parse(String filePath) throws IOException {
		return parse(new File(filePath));
	}
	
	public LinkedList parse(File inFile) throws IOException {
		
		this.inFile = inFile;
		data = new LinkedList();
		
		if (addHeader) addHeaderToData(HEADER_FILE);
				
		inStream = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		
		int in1;
		char ch;
		StringBuffer buf = new StringBuffer(25);
		
		try
		{			
			while ((in1 = inStream.read()) != -1)
			{
				ch = (char) in1;
				
				//System.out.print(ch);
				
				buf.append(ch);
				
				if ( !DIVREGION.substring(0,buf.length()).contentEquals(buf) ) {
					buf.setLength(0);
				}
				else if (buf.length() == DIVREGION.length()) {

					buf.setLength(0);

					for (int i=0; i<3; i++) {
						buf.append((char) inStream.read());
					}
					
					if ("EQN".contentEquals(buf)) {
						
						// if line before was an TXT region, delete paragraph at the end
						
						if (!data.isEmpty()) {
							String lastLine = (String) data.getLast();
							
							if ((lastLine.length() > 0) && (lastLine.charAt(lastLine.length()-1) == '\\') && (lastLine.charAt(lastLine.length()-2) == '\\')) {
								data.removeLast();
								data.add(lastLine.substring(0,lastLine.length()-2));
							}
						}
						
						
						// embed, set off or enumerate formulas
						
						String formulaPrefix;
						String formulaSuffix;
						
						if (formulaFormatting == 0) {
							formulaPrefix = "$";
							formulaSuffix = formulaPrefix;
						}
						else if (formulaFormatting == 1) {
							formulaPrefix = "\\[";
							formulaSuffix = "\\]";
						}
						else {
							// TODO Gibt's daf�r auch eine Kurzform?
							formulaPrefix = "\\begin{equation}\n  ";
							formulaSuffix = "\n\\end{equation}";
						}
						
						String s = mathMLParser.parse(getEmbedArea(getDivContent()), wrappedEntities, skipUnknownEntities);
						
						if (s != null) {
							data.add(formulaPrefix + s + formulaSuffix);
						}
						
						//System.out.println("parsed EQN: '"+data.getLast()+"'");
					}
					else if ("TXT".contentEquals(buf)) {
						data.add(MathHTMLParser.parse(getDivContent(), parseFormatting)+"\\\\");
						//System.out.println("parsed TXT: '"+data.getLast()+"'");
					}
					
					buf.setLength(0);
				}
			}
		}
		finally
		{
			inStream.close();
		}
		
		if (addHeader) data.add("\n\\end{document}");
		
		return data;
	}

	private StringBuffer getDivContent() throws IOException {
		
		final String divEnd = "</div>";

		tmpBuf.setLength(0);
		resBuf.setLength(0);
		
		int in1 = 0;
		
		//skip rest of 'div' tag
		while ((char) (in1 = inStream.read()) != '>') {
			if (in1 == -1) throw new IOException("Error while reading "+inFile.getName()+": Unexpected end of file.");
		}
		
		while ( (!divEnd.contentEquals(tmpBuf)) && ((in1 = inStream.read()) != -1))
		{
			resBuf.append((char) in1);
			if (!divEnd.substring(0,tmpBuf.length()+1).contentEquals(tmpBuf.append((char) in1))) { 
				tmpBuf.setLength(0);
			}
		}
		
		resBuf.delete(resBuf.length()-divEnd.length(),resBuf.length());
		
		if (in1 == -1) throw new IOException("Error while reading "+inFile.getName()+": Unexpected end of file.");
		
		return new StringBuffer(resBuf.toString());
	}
	
	private StringBuffer getEmbedArea(StringBuffer divArea) {
		
		int in;
		
		if ((in = divArea.indexOf("<embed")) > -1) {
			
			divArea.delete(0, (in+6));
			divArea.delete(0, (divArea.indexOf("mmldata=\"")+9));
			
			divArea.delete(divArea.indexOf("</embed>"), divArea.length());
			
			return divArea;
		}
		else if ((in = divArea.indexOf("<img ")) != -1) {
			
			// cut out the image file name
			divArea.delete(0, (in+5));
			divArea.delete(0, (divArea.indexOf("\"")+1));
			divArea.delete(divArea.indexOf("\" "), divArea.length());
			divArea.delete(0, (divArea.lastIndexOf("\\")+1));
			
			
			/*
			\begin{figure}[htbp]
			 \begin{center}
			  \includegraphics[width=1.00\textwidth]{image.jpg}
			 \end{center}
			 \caption{Das ist die Bildunterschrift}
			\end{figure}
			*/
			
			data.add("\\begin{figure}[htbp]\n \\begin{center}\n  \\includegraphics[width=1.00\\textwidth]{"+divArea+"}\n \\end{center}\n \\caption{"+divArea+"}\n\\end{figure}");
			
		}
		
		return null;
	}
	
	/*
	 * Skips all characters from the input file until a given String is found.
	 * 
	 * @param str the String to seek
	 * @throws IOException if an I/O error occurs
	 *
	private void seekSequence(String str) throws IOException {
		
		final String seq = str;
		StringBuffer buf = new StringBuffer(seq.length());
		int in1;
		char ch;
		
		while ( ((in1 = inStream.read()) != -1) && (!seq.contentEquals(buf)) )
		{
			ch = (char) in1;

			if (ch == seq.charAt(buf.length())) {
				buf.append(ch);
			}
			else {
				buf.setLength(0);
			}
		}
		
		if (in1 == -1) throw new IOException("Error while reading "+inFile.getName()+": Unexpected end of file.");
	}*/
	
	
	/* Old implementation of seekSequence, should be faster but it is 'hacky' and
	 * hard to read. Can be reused in case of performace problems
	private void seekSequence(String str) throws IOException {
		
		final int length = str.length();
		char[] buf = new char[length];
		int end = 0;
		
		int in1;
		
		while ((in1 = inStream.read()) != -1) {
			buf[end % length] = (char) in1;
			
			int i = 1;
			while ( (i <= length) && (buf[(end+i)%length] == str.charAt(i-1)) ) i++;
			
			if (i == length+1) return;
			
			end++;
		}
		
		throw new IOException("Error while reading "+inFile.getName()+": Unexpected end of file.");
	}
	*/
	
	private void saveToFile(String filePath) {
	}
	
	private void saveToFile(File file) {
	}
	
	public void parseFormatting(boolean val) {
		parseFormatting = val;
	}
	
	void addHeaderToData(String fileName)
	throws IOException {
		
		BufferedReader file;
	
		try {
			file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		}
		catch (FileNotFoundException fnfe) {
			try {
				file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
			}
			catch (NullPointerException npe) {
				throw new IOException("The Latex header file ("+fileName+") could not be read.");
			}
		}
	
		String line;
	
		try
		{
			while ((line = file.readLine()) != null)
			{
				data.add(line);
			}
		}
		catch (IOException ioe)
		{
			throw ioe;
		}
		finally
		{
			try
			{
				file.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();				
			}
		}
	}
}
