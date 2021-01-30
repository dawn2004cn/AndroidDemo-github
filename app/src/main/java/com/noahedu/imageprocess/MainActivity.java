package com.noahedu.imageprocess;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.noahedu.Image.OilPaintFilter;
import com.noahedu.demo.R;
import com.noahedu.utils.Fingerprint;
import com.noahedu.utils.ImageTools;
import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import java.io.File;

public class MainActivity extends Activity
{
	TextView image01 = null;
	TextView image02 = null;
	TextView image03 = null;
	TextView image04 = null;
	String nameString = null;
	int w = 0;
	int h = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.activity_main);

		image01 = (TextView)findViewById(R.id.image01);
		image02 = (TextView)findViewById(R.id.image02);
		image03 = (TextView)findViewById(R.id.image03);
		image04 = (TextView)findViewById(R.id.image04);*/
		

		nameString = Fingerprint.strSDCardPathString+"images1/person.jpg";
		File sim_file = new File(nameString);
		Bitmap sim = ImageTools.readImage(sim_file);
		image01.setBackgroundDrawable(ImageUtils.bitmapToDrawable(sim));
		
		w = sim.getWidth();
		h = sim.getHeight();
	//	Bitmap bisim = AmplificatingShrinking.bilinearityInterpolation(sim, 2,2);
	//	image02.setBackgroundDrawable(ImageUtil.bitmapToDrawable(bisim));	
	//	Bitmap nnsim = AmplificatingShrinking.NearNaighborInterpolationScale(sim, 2,2);
	//	image03.setBackgroundDrawable(ImageUtil.bitmapToDrawable(nnsim));	
	//	Bitmap bcsim = AmplificatingShrinking.biCubicInterpolationScale(sim, 2,2);
	//	image04.setBackgroundDrawable(ImageUtil.bitmapToDrawable(bcsim));
		
		OilPaintFilter flFilter = new OilPaintFilter();
		Bitmap oilBitmap = null;
		oilBitmap= flFilter.filter(sim, oilBitmap);
		image02.setBackgroundDrawable(ImageUtils.bitmapToDrawable(oilBitmap));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
