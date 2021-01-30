/*
 * 图像放缩之临近点插值 
 * */
package com.noahedu.Image;

public class NearNeighbourZoom
{
	public NearNeighbourZoom() {  
        
    }  
  
    /** 
     * (X-Xmin)/(Xmax-Xmin) = (Y-Ymin)/(Ymax-Ymin) 
     * assume Xmin and Ymin are zero, then the formula will be f(x) = kx (k - coefficent, slope) 
     *  
     */  
    public int[] imgScale(int[] inPixelsData, int srcW, int srcH, int destW, int destH) {  
        int[][][] inputThreeDeminsionData = processOneToThreeDeminsion(inPixelsData, srcH, srcW);  
        int[][][] outputThreeDeminsionData = new int[destH][destW][4];  
        float rowRatio = ((float)srcH)/((float)destH);  
        float colRatio = ((float)srcW)/((float)destW);  
        for(int row=0; row<destH; row++) {  
            // convert to three dimension data  
            int srcRow = Math.round(((float)row)*rowRatio);  
            if(srcRow >=srcH) {  
                srcRow = srcH - 1;  
            }  
            for(int col=0; col<destW; col++) {  
                int srcCol = Math.round(((float)col)*colRatio);  
                if(srcCol >= srcW) {  
                    srcCol = srcW - 1;  
                }  
                outputThreeDeminsionData[row][col][0] = inputThreeDeminsionData[srcRow][srcCol][0]; // alpha  
                outputThreeDeminsionData[row][col][1] = inputThreeDeminsionData[srcRow][srcCol][1]; // red  
                outputThreeDeminsionData[row][col][2] = inputThreeDeminsionData[srcRow][srcCol][2]; // green  
                outputThreeDeminsionData[row][col][3] = inputThreeDeminsionData[srcRow][srcCol][3]; // blue  
            }  
        }  
        return convertToOneDim(outputThreeDeminsionData, destW, destH);  
    }  
      
    /* <p> The purpose of this method is to convert the data in the 3D array of ints back into </p> 
     * <p> the 1d array of type int. </p> 
     *  
     */  
    public int[] convertToOneDim(int[][][] data, int imgCols, int imgRows) {  
        // Create the 1D array of type int to be populated with pixel data  
        int[] oneDPix = new int[imgCols * imgRows * 4];  
  
        // Move the data into the 1D array. Note the  
        // use of the bitwise OR operator and the  
        // bitwise left-shift operators to put the  
        // four 8-bit bytes into each int.  
        for (int row = 0, cnt = 0; row < imgRows; row++) {  
            for (int col = 0; col < imgCols; col++) {  
                oneDPix[cnt] = ((data[row][col][0] << 24) & 0xFF000000)  
                        | ((data[row][col][1] << 16) & 0x00FF0000)  
                        | ((data[row][col][2] << 8) & 0x0000FF00)  
                        | ((data[row][col][3]) & 0x000000FF);  
                cnt++;  
            }// end for loop on col  
  
        }// end for loop on row  
  
        return oneDPix;  
    }// end convertToOneDim  
      
    private int[][][] processOneToThreeDeminsion(int[] oneDPix2, int imgRows, int imgCols) {  
        int[][][] tempData = new int[imgRows][imgCols][4];  
        for(int row=0; row<imgRows; row++) {  
              
            // per row processing  
            int[] aRow = new int[imgCols];  
            for (int col = 0; col < imgCols; col++) {  
                int element = row * imgCols + col;  
                aRow[col] = oneDPix2[element];  
            }  
              
            // convert to three dimension data  
            for(int col=0; col<imgCols; col++) {  
                tempData[row][col][0] = (aRow[col] >> 24) & 0xFF; // alpha  
                tempData[row][col][1] = (aRow[col] >> 16) & 0xFF; // red  
                tempData[row][col][2] = (aRow[col] >> 8) & 0xFF;  // green  
                tempData[row][col][3] = (aRow[col]) & 0xFF;       // blue  
            }  
        }  
        return tempData;  
    }  
}
