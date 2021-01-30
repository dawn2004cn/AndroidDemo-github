package com.noahedu.common.mathml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import fmath.conversion.ConvertFromLatexToMathML;
import fmath.conversion.ConvertFromMathMLToLatex;
import fmath.conversion.ConvertFromMathMLToWord;
import fmath.conversion.ConvertFromWordToMathML;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：MathMLParserUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/5$ 16:54$
 */
class MathMLParserUtils {
    public static String ConvertFromMathMLToLatex(String mathml){
        return ConvertFromMathMLToLatex.convertToLatex(mathml);
    }

    public static String ConvertFromLatexToMathML(String latex){
        return ConvertFromLatexToMathML.convertToMathML(latex);
    }

    public static void ConvertFromMathMLToWord(String outFileName,String mathml){
        ConvertFromMathMLToWord.writeWordDocFromMathML(outFileName,mathml);
    }

    public static String ConvertFromWordToMathML(String wordFileName,String charsetName){
        FileInputStream input = null;
        try{
             input = new FileInputStream(wordFileName);
        }
        catch (IOException e){

        }
        if (input != null) {
            return ConvertFromWordToMathML.getMathMLFromDocStream(input, charsetName);
        }
        return  null;
    }

    public static String ConvertFromWordToMathML(String wordFileName){
        return ConvertFromWordToMathML.getMathMLFromDocFile(wordFileName);
    }



}
