package com.noahedu.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.noahedu.common.filedownloader.DownloadConfiguration;
import com.noahedu.common.filedownloader.DownloadFileInfo;
import com.noahedu.common.filedownloader.DownloadStatusConfiguration;
import com.noahedu.common.filedownloader.FileDownloadConfiguration;
import com.noahedu.common.filedownloader.FileDownloadConfiguration.Builder;
import com.noahedu.common.filedownloader.FileDownloader;
import com.noahedu.common.filedownloader.listener.OnDeleteDownloadFileListener;
import com.noahedu.common.filedownloader.listener.OnDeleteDownloadFilesListener;
import com.noahedu.common.filedownloader.listener.OnDetectBigUrlFileListener;
import com.noahedu.common.filedownloader.listener.OnMoveDownloadFileListener;
import com.noahedu.common.filedownloader.listener.OnMoveDownloadFilesListener;
import com.noahedu.common.filedownloader.listener.OnRenameDownloadFileListener;
import com.noahedu.common.util.FilenameUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.DownloadFileListAdapter;
import com.noahedu.demo.adapter.DownloadFileListAdapter.OnItemSelectListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo Test MainActivity
 * <br/>
 * 测试主界面
 *
 * @author wlf(Andy)
 * @email 411086563@qq.com
 */
public class DownloadFileDemo extends BaseActivity implements OnItemSelectListener {

    // adapter
    private DownloadFileListAdapter mDownloadFileListAdapter;

    // toast
    private Toast mToast;

    private LinearLayout mLnlyOperation;
    private Button mBtnDelete;
    private Button mBtnMove;
    private Button mBtnRename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init FileDownloader
        initFileDownloader();
        setContentView(R.layout.download_file_activity);

        mLnlyOperation = (LinearLayout) findViewById(R.id.lnlyOperation);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnMove = (Button) findViewById(R.id.btnMove);
        mBtnRename = (Button) findViewById(R.id.btnRename);

        // ListView
        ListView lvDownloadFileList = (ListView) findViewById(R.id.lvDownloadFileList);
        mDownloadFileListAdapter = new DownloadFileListAdapter(this);
        lvDownloadFileList.setAdapter(mDownloadFileListAdapter);

        mDownloadFileListAdapter.setOnItemSelectListener(this);

        // registerDownloadStatusListener 
        
        URL url;
		try {
			url = new URL("http://ximalayaos.cos.xmcdn.com/storages/d717-ximalayaos/08/14/CMCoC10BfI3vAA7pQgABL_Mg.mp3?sign=1573797165-uhy1vywzo-0-8ccdf55489d437a0dfebc179bcfd9042");
	        LogUtils.v(FilenameUtils.getBaseName(url.getPath())); // -> file
            LogUtils.v(FilenameUtils.getExtension(url.getPath())); // -> xml
            LogUtils.v(FilenameUtils.getName(url.getPath())); // -> file.xml
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        boolean isDownloadStatusConfigurationTest = false;// TEST

        if (!isDownloadStatusConfigurationTest) {
            // register to listen all
            FileDownloader.registerDownloadStatusListener(mDownloadFileListAdapter);
        } else {
            // register to only listen special url
            DownloadStatusConfiguration.Builder builder = new DownloadStatusConfiguration.Builder();
            builder.addListenUrl("https://github.com/Trinea/trinea-download/blob/master/styled-dialogs-demo.apk");
            FileDownloader.registerDownloadStatusListener(mDownloadFileListAdapter, builder.build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDownloadFileListAdapter != null) {
            mDownloadFileListAdapter.updateShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // pause all downloads
        FileDownloader.pauseAll();
        // unregisterDownloadStatusListener
        FileDownloader.unregisterDownloadStatusListener(mDownloadFileListAdapter);

        // release FileDownloader
        releaseFileDownloader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);// init OptionsMenu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// handle OptionsMenu
        switch (item.getItemId()) {
            // new download
            case R.id.optionsNew:
                // show new download dialog
                showNewDownloadDialog();
                return true;
            // new multi download
            case R.id.optionsNews:
                // show new multi download dialog
                showMultiNewDownloadDialog();
                return true;
            // new download(custom)
            case R.id.optionsNewWithDetect:
                // show new download(custom) dialog
                showCustomNewDownloadDialog();
                return true;
            // new big file download
            case R.id.optionsNewBigFileWithDetect:
                showNewBigDownloadDialog();
                return true;
            // new https file download
            case R.id.optionsNewHttps:
                showNewHttpsDownloadDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // show new download dialog
    private void showNewDownloadDialog() {

        final EditText etUrl = new EditText(this);
        // apk file, the url with special character
        etUrl.setText("  http://182.254.149.157/ftp/image/shop/product/Kids Addition & Subtraction 1.0.apk ");

        // etUrl.setText("http://yjh.t4s.cn/Uploads/Download/2016-01-13/56962102baf32.apk");

        // etUrl.setText("http://yjh.t4s.cn/Home/new/downloadFile/id/31");

        //        etUrl.setText("http://openapi.shafa.com/v1/redirect?a=download&app_key=NVdVOkqg49GR090O&l=com
        // .gitvdemo" +
        //                ".video&to=http%3A%2F%2Fapps.sfcdn.org%2Fapk%2Fcom.gitvdemo.video
        // .7e9b0a7643b0c5bfbc5ddd05f41f783c" +
        //                ".apk&sign=c2046ccd3928abf6cee0d6f5d06ba6e5");
        //        etUrl.setText("http://m.25az
        // .com/upload/ad/uc/m/%e6%a2%a6%e5%b9%bb%e9%a9%af%e9%be%99%e8%ae%b0/uc-3_5011991_163309b77bf2.apk");

        // etUrl.setText(" http://cdn.saofu.cn/appss/74b6a96f-e056-4fbf-8b36-579a7d4f2ad8.apk");// only for testing
        // error url

        // test Baidu SkyDrive 
        //        etUrl.setText("https://pcscdns.baidu" +
        //                ".com/file/9d2525e48beae74df9839bfd53ea0659?bkt=p3" +
        //                "-14009d2525e48beae74df9839bfd53ea0659120c4aed0000003439bb&xcode" +
        //                "=b55811de01cef039a63384887845d600365bdbc88d13f3a00b2977702d3e6764&fid=4080794744-250528" +
        //                "-405950091149355&time=1458524792&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203
        // -6HU2GtKiI5uAmRbD" +
        //                "%2BxWDQ0Ue54I%3D&to=se&fm=Yan,B,T,t&sta_dx=3&sta_cs=8&sta_ft=apk&sta_ct=1&fm2=Yangquan,B,
        // T," +
        //                "t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey
        // =14009d2525e48beae74df9839bfd53ea0659120c4aed0000003439bb" +
        //                "&sl=69402703&expires=8h&rt=sh&r=888204069&mlogid=1864872734416013050&vuk=3339143945&vbdid
        // =1177775768" +
        //                "&fin=%E5%BE%AE%E4%BF%A1QQ%E5%8F%8C%E5%BC%80%E5%8A%A9%E6%89%8B" +
        //                ".apk&fn=%E5%BE%AE%E4%BF%A1QQ%E5%8F%8C%E5%BC%80%E5%8A%A9%E6%89%8B" +
        //                ".apk&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=1864872734416013050&dp-callid=0.1.1");

        etUrl.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main__please_input_download_file)).setView(etUrl).setNegativeButton
                (getString(R.string.main__dialog_btn_cancel), null);
        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // file url
                String url = etUrl.getText().toString().trim();

                boolean isDownloadConfigurationTest = false;// TEST

                if (!isDownloadConfigurationTest) {
                    FileDownloader.start(url);
                } else {
                    // TEST DownloadConfiguration
                    DownloadConfiguration.Builder builder1 = new DownloadConfiguration.Builder();
                    builder1.addHeader("Accept", "*/*");
                    FileDownloader.start(url, builder1.build());
                }
            }
        });
        builder.show();
    }

    // show new multi download dialog

    private void showMultiNewDownloadDialog() {

        final EditText etUrl1 = new EditText(this);
        etUrl1.setText("http://img13.360buyimg.com/n1/g14/M01/1B/1F/rBEhVlM03iwIAAAAAAFJnWsj5UAAAK8_gKFgkMAAUm1950" +
                ".jpg");// web image file,jpg
        etUrl1.setFocusable(true);

        final EditText etUrl2 = new EditText(this);
        etUrl2.setText("http://sqdd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");// apk file,tencent qq
        etUrl2.setFocusable(true);

        final EditText etUrl3 = new EditText(this);
        etUrl3.setText("http://ximalayaos.cos.xmcdn.com/storages/e24d-ximalayaos/32/B3/CMCoDBABfI3yAAnszgABL_Ms.mp3?sign=1573293114-uhy1vywzo-0-537467a9e0cad1e19d91ac1a353c2ff3");// exe file,thunder
        etUrl3.setFocusable(true);

        final EditText etUrl4 = new EditText(this);
        etUrl4.setText("http://ximalayaos.cos.xmcdn.com/storages/d717-ximalayaos/08/14/CMCoC10BfI3vAA7pQgABL_Mg.mp3?sign=1573293083-uhy1vywzo-0-e586515a8c810d6f512d3d293ca71ade");// mp4 file,mv
        etUrl4.setFocusable(true);

        final EditText etUrl5 = new EditText(this);
        etUrl5.setText("http://ximalayaos.cos.xmcdn.com/storages/4058-ximalayaos/A3/99/CMCoC10BfI3xABWFagABL_Mr.mp3?sign=1573286802-uhy1vywzo-0-f794d14edffbe328a47727fb29523b1b");// apk file, with special characters
        etUrl5.setFocusable(true);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(etUrl1, params);
        linearLayout.addView(etUrl2, params);
        linearLayout.addView(etUrl3, params);
        linearLayout.addView(etUrl4, params);
        linearLayout.addView(etUrl5, params);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main__please_input_multi_download_files)).setView(linearLayout)
                .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // file urls
                String url1 = etUrl1.getText().toString().trim();
                String url2 = etUrl2.getText().toString().trim();
                String url3 = etUrl3.getText().toString().trim();
                String url4 = etUrl4.getText().toString().trim();
                String url5 = etUrl5.getText().toString().trim();

                List<String> urls = new ArrayList<String>();
                urls.add(url1);
                urls.add(url2);
                urls.add(url3);
                urls.add(url4);
                urls.add(url5);

                boolean isDownloadConfigurationTest = false;// TEST

                if (!isDownloadConfigurationTest) {
                    FileDownloader.start(urls);
                } else {
                    // TEST DownloadConfiguration
                    DownloadConfiguration.MultiBuilder builder1 = new DownloadConfiguration.MultiBuilder();
                    builder1.addHeaderWithUrl(url1, "Accept", "*/*");
                    builder1.addHeaderWithUrl(url2, "Date", "Tue, 15 Nov 2015 08:12:31 GMT");
                    builder1.addHeaderWithUrl(url3, "Pragma", "no-cache");
                    builder1.addHeader("Pragma", "no-cache-common");
                    builder1.replaceHeaderWithUrl(url2, "Date", "Tue, 15 Nov 2016 08:12:31 GMT");
                    // builder1.configRequestMethod("GET");
                    builder1.configRequestMethodWithUrl(url2, "POST");
                    FileDownloader.start(urls, builder1.build());
                }
            }
        });
        builder.show();
    }

    // show new download(custom) dialog
    private void showCustomNewDownloadDialog() {

        final EditText etUrlCustom = new EditText(this);
        // mp4 file, the url with params
        etUrlCustom.setText("http://183.57.151" +
                ".208/download/videos/47CDA700A098E497/2015/12/17/1_1449832690_1449833760" +
                ".mp4?a=836e48885e3a571404b85948aadb4797a4f6dec200407c1f48710c1a16fca32b&u" +
                "=2819e7dec4dd32a780d6713df83b1b9df0c5bc193b52c5c1cacf932893b42327");
        etUrlCustom.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main__please_input_download_file)).setView(etUrlCustom).setNegativeButton
                (getString(R.string.main__dialog_btn_cancel), null);
        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // file url
                String url = etUrlCustom.getText().toString().trim();
                FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
                    // ----------------------detect url file callback----------------------
                    @Override
                    public void onDetectNewDownloadFile(final String url, String fileName, final String saveDir, long
                            fileSize) {
                        final TextView tvFileDir = new TextView(DownloadFileDemo.this);
                        tvFileDir.setText(getString(R.string.main__save_path));

                        final EditText etFileDir = new EditText(DownloadFileDemo.this);
                        etFileDir.setText(saveDir);
                        etFileDir.setFocusable(true);

                        final TextView tvFileName = new TextView(DownloadFileDemo.this);
                        tvFileName.setText(getString(R.string.main__save_file_name));

                        final EditText etFileName = new EditText(DownloadFileDemo.this);
                        etFileName.setText(fileName);
                        etFileName.setFocusable(true);

                        final TextView tvFileSize = new TextView(DownloadFileDemo.this);
                        float size = fileSize / 1024f / 1024f;
                        tvFileSize.setText(getString(R.string.main__file_size) + ((float) (Math.round(size * 100)) /
                                100) + "M");

                        LinearLayout linearLayout = new LinearLayout(DownloadFileDemo.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                                .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linearLayout.addView(tvFileDir, params);
                        linearLayout.addView(etFileDir, params);
                        linearLayout.addView(tvFileName, params);
                        linearLayout.addView(etFileName, params);
                        linearLayout.addView(tvFileSize, params);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                        builder.setTitle(getString(R.string.main__confirm_save_path_and_name)).setView(linearLayout)
                                .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                                .OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // save file dir
                                String newFileDir = etFileDir.getText().toString().trim();
                                // save file name
                                String newFileName = etFileName.getText().toString().trim();
                                // create download
                                showToast(getString(R.string.main__new_download) + newFileName);
                                Log.e("wlf", "探测文件，新建下载：" + newFileName);
                                FileDownloader.createAndStart(url, newFileDir, newFileName);
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onDetectUrlFileExist(String url) {
                        showToast(getString(R.string.main__continue_download) + url);
                        Log.e("wlf", "探测文件，继续下载：" + url);
                        // continue download
                        FileDownloader.start(url);
                    }

                    @Override
                    public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                        String msg = null;
                        if (failReason != null) {
                            msg = failReason.getMessage();
                            if (TextUtils.isEmpty(msg)) {
                                Throwable t = failReason.getCause();
                                if (t != null) {
                                    msg = t.getLocalizedMessage();
                                }
                            }
                        }
                        showToast(getString(R.string.main__detect_file_error) + msg + "," + url);
                        Log.e("wlf", "出错回调，探测文件出错：" + msg + "," + url);
                    }
                });
            }
        });
        builder.show();
    }

    // show new download(big file download) dialog
    private void showNewBigDownloadDialog() {

        final EditText etUrlCustom = new EditText(this);
        // big download file witch bigger than 2G to download
        etUrlCustom.setText("http://dx500.downyouxi.com/minglingyuzhengfu4.rar");
        etUrlCustom.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main__please_input_download_file)).setView(etUrlCustom).setNegativeButton
                (getString(R.string.main__dialog_btn_cancel), null);
        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // file url
                String url = etUrlCustom.getText().toString().trim();
                FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
                    // ----------------------detect url file callback----------------------
                    @Override
                    public void onDetectNewDownloadFile(final String url, String fileName, final String saveDir, long
                            fileSize) {
                        final TextView tvFileDir = new TextView(DownloadFileDemo.this);
                        tvFileDir.setText(getString(R.string.main__save_path));

                        final EditText etFileDir = new EditText(DownloadFileDemo.this);
                        etFileDir.setText(saveDir);
                        etFileDir.setFocusable(true);

                        final TextView tvFileName = new TextView(DownloadFileDemo.this);
                        tvFileName.setText(getString(R.string.main__save_file_name));

                        final EditText etFileName = new EditText(DownloadFileDemo.this);
                        etFileName.setText(fileName);
                        etFileName.setFocusable(true);

                        final TextView tvFileSize = new TextView(DownloadFileDemo.this);
                        float size = fileSize / 1024f / 1024f;
                        tvFileSize.setText(getString(R.string.main__file_size) + ((float) (Math.round(size * 100)) /
                                100) + "M");

                        LinearLayout linearLayout = new LinearLayout(DownloadFileDemo.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                                .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linearLayout.addView(tvFileDir, params);
                        linearLayout.addView(etFileDir, params);
                        linearLayout.addView(tvFileName, params);
                        linearLayout.addView(etFileName, params);
                        linearLayout.addView(tvFileSize, params);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                        builder.setTitle(getString(R.string.main__confirm_save_path_and_name)).setView(linearLayout)
                                .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                                .OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // save file dir
                                String newFileDir = etFileDir.getText().toString().trim();
                                // save file name
                                String newFileName = etFileName.getText().toString().trim();
                                // create download
                                showToast(getString(R.string.main__new_download) + url);
                                Log.e("wlf", "探测文件，新建下载：" + url);
                                FileDownloader.createAndStart(url, newFileDir, newFileName);
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onDetectUrlFileExist(String url) {
                        showToast(getString(R.string.main__continue_download) + url);
                        Log.e("wlf", "探测文件，继续下载：" + url);
                        // continue download
                        FileDownloader.start(url);
                    }

                    @Override
                    public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                        String msg = null;
                        if (failReason != null) {
                            msg = failReason.getMessage();
                            if (TextUtils.isEmpty(msg)) {
                                Throwable t = failReason.getCause();
                                if (t != null) {
                                    msg = t.getLocalizedMessage();
                                }
                            }
                        }
                        showToast(getString(R.string.main__detect_file_error) + msg + "," + url);
                        Log.e("wlf", "出错回调，探测文件出错：" + msg + "," + url);
                    }
                });
            }
        });
        builder.show();
    }

    // show new download(https file download) dialog
    private void showNewHttpsDownloadDialog() {

        final EditText etUrlCustom = new EditText(this);
        // big download file witch bigger than 2G to download
        etUrlCustom.setText("https://raw.githubusercontent" +
                ".com/wlfcolin/file-downloader/master/design/file-downloader" + " uml.eap");
        etUrlCustom.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.main__please_input_download_file)).setView(etUrlCustom).setNegativeButton
                (getString(R.string.main__dialog_btn_cancel), null);
        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // file url
                String url = etUrlCustom.getText().toString().trim();
                FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
                    // ----------------------detect url file callback----------------------
                    @Override
                    public void onDetectNewDownloadFile(final String url, String fileName, final String saveDir, long
                            fileSize) {
                        final TextView tvFileDir = new TextView(DownloadFileDemo.this);
                        tvFileDir.setText(getString(R.string.main__save_path));

                        final EditText etFileDir = new EditText(DownloadFileDemo.this);
                        etFileDir.setText(saveDir);
                        etFileDir.setFocusable(true);

                        final TextView tvFileName = new TextView(DownloadFileDemo.this);
                        tvFileName.setText(getString(R.string.main__save_file_name));

                        final EditText etFileName = new EditText(DownloadFileDemo.this);
                        etFileName.setText(fileName);
                        etFileName.setFocusable(true);

                        final TextView tvFileSize = new TextView(DownloadFileDemo.this);
                        float size = fileSize / 1024f / 1024f;
                        tvFileSize.setText(getString(R.string.main__file_size) + ((float) (Math.round(size * 100)) /
                                100) + "M");

                        LinearLayout linearLayout = new LinearLayout(DownloadFileDemo.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                                .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linearLayout.addView(tvFileDir, params);
                        linearLayout.addView(etFileDir, params);
                        linearLayout.addView(tvFileName, params);
                        linearLayout.addView(etFileName, params);
                        linearLayout.addView(tvFileSize, params);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                        builder.setTitle(getString(R.string.main__confirm_save_path_and_name)).setView(linearLayout)
                                .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                        builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                                .OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // save file dir
                                String newFileDir = etFileDir.getText().toString().trim();
                                // save file name
                                String newFileName = etFileName.getText().toString().trim();
                                // create download
                                showToast(getString(R.string.main__new_download) + url);
                                Log.e("wlf", "探测文件，新建下载：" + url);
                                FileDownloader.createAndStart(url, newFileDir, newFileName);
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onDetectUrlFileExist(String url) {
                        showToast(getString(R.string.main__continue_download) + url);
                        Log.e("wlf", "探测文件，继续下载：" + url);
                        // continue download
                        FileDownloader.start(url);
                    }

                    @Override
                    public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                        String msg = null;
                        if (failReason != null) {
                            msg = failReason.getMessage();
                            if (TextUtils.isEmpty(msg)) {
                                Throwable t = failReason.getCause();
                                if (t != null) {
                                    msg = t.getLocalizedMessage();
                                }
                            }
                        }
                        showToast(getString(R.string.main__detect_file_error) + msg + "," + url);
                        Log.e("wlf", "出错回调，探测文件出错：" + msg + "," + url);
                    }
                });
            }
        });
        builder.show();
    }

    // show toast
    private void showToast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void updateAdapter() {
        if (mDownloadFileListAdapter == null) {
            return;
        }
        mDownloadFileListAdapter.updateShow();
    }

    @Override
    public void onSelected(final List<DownloadFileInfo> selectDownloadFileInfos) {

        mLnlyOperation.setVisibility(View.VISIBLE);

        mBtnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                builder.setTitle(getString(R.string.main__confirm_whether_delete_save_file));
                builder.setNegativeButton(getString(R.string.main__confirm_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDownloadFiles(false, selectDownloadFileInfos);
                    }
                });
                builder.setPositiveButton(getString(R.string.main__confirm_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDownloadFiles(true, selectDownloadFileInfos);
                    }
                });
                builder.show();
            }

            private void deleteDownloadFiles(boolean deleteDownloadedFile, List<DownloadFileInfo>
                    selectDownloadFileInfos) {

                List<String> urls = new ArrayList<String>();

                for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                    if (downloadFileInfo == null) {
                        continue;
                    }
                    urls.add(downloadFileInfo.getUrl());
                }

                // single delete
                if (urls.size() == 1) {
                    FileDownloader.delete(urls.get(0), deleteDownloadedFile, new OnDeleteDownloadFileListener() {
                        @Override
                        public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                            showToast(getString(R.string.main__delete_succeed));
                            updateAdapter();

                            Log.e("wlf", "onDeleteDownloadFileSuccess 成功回调，单个删除" + downloadFileDeleted.getFileName()
                                    + "成功");
                        }

                        @Override
                        public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {
                            if (downloadFileNeedDelete != null) {
                                showToast(getString(R.string.main__deleting) + downloadFileNeedDelete.getFileName());
                            }
                        }

                        @Override
                        public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                               DeleteDownloadFileFailReason failReason) {
                            showToast(getString(R.string.main__delete) + downloadFileInfo.getFileName() + getString(R
                                    .string.main__failed));

                            Log.e("wlf", "onDeleteDownloadFileFailed 出错回调，单个删除" + downloadFileInfo.getFileName() +
                                    "失败");
                        }
                    });
                }
                // multi delete
                else {
                    Log.e("wlf_deletes", "点击开始批量删除");
                    FileDownloader.delete(urls, deleteDownloadedFile, new OnDeleteDownloadFilesListener() {

                        @Override
                        public void onDeletingDownloadFiles(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                            List<DownloadFileInfo> downloadFilesDeleted,
                                                            List<DownloadFileInfo> downloadFilesSkip,
                                                            DownloadFileInfo downloadFileDeleting) {
                            if (downloadFileDeleting != null) {
                                showToast(getString(R.string.main__deleting) + downloadFileDeleting.getFileName() +
                                        getString(R.string.main__progress) + (downloadFilesDeleted.size() +
                                        downloadFilesSkip.size()) + getString(R.string.main__failed2) +
                                        downloadFilesSkip.size() + getString(R.string
                                        .main__skip_and_total_delete_division) +
                                        downloadFilesNeedDelete.size());
                            }
                            updateAdapter();
                        }

                        @Override
                        public void onDeleteDownloadFilesPrepared(List<DownloadFileInfo> downloadFilesNeedDelete) {
                            showToast(getString(R.string.main__need_delete) + downloadFilesNeedDelete.size());
                        }

                        @Override
                        public void onDeleteDownloadFilesCompleted(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                                   List<DownloadFileInfo> downloadFilesDeleted) {

                            String text = getString(R.string.main__delete_finish) + downloadFilesDeleted.size() +
                                    getString(R.string.main__failed3) + (downloadFilesNeedDelete.size() -
                                    downloadFilesDeleted.size());

                            showToast(text);

                            updateAdapter();

                            Log.e("wlf", "onDeleteDownloadFilesCompleted 完成回调，" + text);
                        }
                    });
                }
            }
        });

        mBtnMove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String oldDirPath = FileDownloader.getDownloadDir();

                final EditText etFileDir = new EditText(DownloadFileDemo.this);
                etFileDir.setText(oldDirPath);
                etFileDir.setFocusable(true);

                LinearLayout linearLayout = new LinearLayout(DownloadFileDemo.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                        .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.addView(etFileDir, params);

                AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                builder.setTitle(getString(R.string.main__confirm_the_dir_path_move_to)).setView(linearLayout)
                        .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // move to file dir
                        String newDirPath = etFileDir.getText().toString().trim();

                        List<String> urls = new ArrayList<String>();

                        for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                            if (downloadFileInfo == null) {
                                continue;
                            }
                            urls.add(downloadFileInfo.getUrl());
                        }

                        // single move
                        if (urls.size() == 1) {
                            FileDownloader.move(urls.get(0), newDirPath, new OnMoveDownloadFileListener() {

                                @Override
                                public void onMoveDownloadFileSuccess(DownloadFileInfo downloadFileMoved) {
                                    showToast(getString(R.string.main__move_succeed) + downloadFileMoved.getFilePath());
                                    updateAdapter();
                                }

                                @Override
                                public void onMoveDownloadFilePrepared(DownloadFileInfo downloadFileNeedToMove) {
                                    if (downloadFileNeedToMove != null) {
                                        showToast(getString(R.string.main__moving) + downloadFileNeedToMove
                                                .getFileName());
                                    }
                                }

                                @Override
                                public void onMoveDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                                     MoveDownloadFileFailReason failReason) {
                                    showToast(getString(R.string.main__move) + downloadFileInfo.getFileName() +
                                            getString(R.string.main__failed));
                                    Log.e("wlf", "出错回调，移动" + downloadFileInfo.getFileName() + "失败");
                                }
                            });
                        }
                        // multi move
                        else {
                            FileDownloader.move(urls, newDirPath, new OnMoveDownloadFilesListener() {

                                @Override
                                public void onMoveDownloadFilesPrepared(List<DownloadFileInfo> downloadFilesNeedMove) {
                                    showToast(getString(R.string.main__need_move) + downloadFilesNeedMove.size());
                                }

                                @Override
                                public void onMovingDownloadFiles(List<DownloadFileInfo> downloadFilesNeedMove,
                                                                  List<DownloadFileInfo> downloadFilesMoved,
                                                                  List<DownloadFileInfo> downloadFilesSkip,
                                                                  DownloadFileInfo downloadFileMoving) {
                                    if (downloadFileMoving != null) {
                                        showToast(getString(R.string.main__moving) + downloadFileMoving.getFileName() +
                                                getString(R.string.main__progress) + (downloadFilesMoved.size() +
                                                downloadFilesSkip.size()) + getString(R.string.main__failed2) +

                                                downloadFilesSkip.size() + getString(R.string
                                                .main__skip_and_total_delete_division) + downloadFilesNeedMove.size());
                                    }
                                    updateAdapter();

                                }

                                @Override
                                public void onMoveDownloadFilesCompleted(List<DownloadFileInfo> downloadFilesNeedMove,
                                                                         List<DownloadFileInfo> downloadFilesMoved) {
                                    showToast(getString(R.string.main__move_finish) + downloadFilesMoved.size() +
                                            getString(R.string.main__failed3) + (downloadFilesNeedMove.size() -
                                            downloadFilesMoved.size()));
                                }
                            });
                        }

                    }
                });
                builder.show();
            }
        });

        mBtnRename.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final List<String> urls = new ArrayList<String>();

                for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                    if (downloadFileInfo == null) {
                        continue;
                    }
                    if (TextUtils.isEmpty(downloadFileInfo.getUrl())) {
                        return;
                    }
                    urls.add(downloadFileInfo.getUrl());
                }

                if (urls.size() == 1) {

                    DownloadFileInfo downloadFileInfoNeedToRename = null;

                    for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                        if (downloadFileInfo == null) {
                            continue;
                        }
                        if (urls.get(0).equals(downloadFileInfo.getUrl())) {
                            downloadFileInfoNeedToRename = downloadFileInfo;
                            break;
                        }
                    }

                    if (downloadFileInfoNeedToRename == null) {
                        showToast(getString(R.string.main__can_not_rename));
                        return;
                    }

                    String oldName = downloadFileInfoNeedToRename.getFileName();

                    final EditText etFileName = new EditText(DownloadFileDemo.this);
                    etFileName.setText(oldName);
                    etFileName.setFocusable(true);

                    final CheckBox cbIncludedSuffix = new CheckBox(DownloadFileDemo.this);
                    cbIncludedSuffix.setChecked(true);
                    cbIncludedSuffix.setText(getString(R.string.main__rename_included_suffix));

                    LinearLayout linearLayout = new LinearLayout(DownloadFileDemo.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                            .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout.addView(etFileName, params);
                    params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams
                            .WRAP_CONTENT);
                    linearLayout.addView(cbIncludedSuffix, params);

                    AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFileDemo.this);
                    builder.setTitle(getString(R.string.main__confirm_rename_info)).setView(linearLayout)
                            .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                    builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String newName = etFileName.getText().toString();

                            FileDownloader.rename(urls.get(0), newName, cbIncludedSuffix.isChecked(), new
                                    OnRenameDownloadFileListener() {

                                @Override
                                public void onRenameDownloadFilePrepared(DownloadFileInfo downloadFileNeedRename) {

                                }

                                @Override
                                public void onRenameDownloadFileSuccess(DownloadFileInfo downloadFileRenamed) {
                                    showToast(getString(R.string.main__rename_succeed));
                                    updateAdapter();
                                }

                                @Override
                                public void onRenameDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                                       RenameDownloadFileFailReason failReason) {
                                    showToast(getString(R.string.main__rename_failed));
                                    Log.e("wlf", "出错回调，重命名失败");
                                }
                            });
                        }
                    });
                    builder.show();
                } else {
                    showToast(getString(R.string.main__rename_failed_note));
                }
            }
        });

    }

    @Override
    public void onNoneSelect() {
        mLnlyOperation.setVisibility(View.GONE);
    }

    // init FileDownloader
    private void initFileDownloader() {

        // 1.create FileDownloadConfiguration.Builder
        Builder builder = new FileDownloadConfiguration.Builder(this);

        // 2.config FileDownloadConfiguration.Builder
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "FileDownloader"); // config the download path
        // builder.configFileDownloadDir("/storage/sdcard1/FileDownloader");

        // allow 3 download tasks at the same time
        builder.configDownloadTaskSize(3);

        // config retry download times when failed
        builder.configRetryDownloadTimes(5);

        // enable debug mode
        //builder.configDebugMode(true);

        // config connect timeout
        builder.configConnectTimeout(25000); // 25s

        // 3.init FileDownloader with the configuration
        FileDownloadConfiguration configuration = builder.build(); // build FileDownloadConfiguration with the builder
        FileDownloader.init(configuration);
    }

    // release FileDownloader
    private void releaseFileDownloader() {
        FileDownloader.release();
    }
}
