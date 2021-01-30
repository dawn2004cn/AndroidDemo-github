/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;

import jp.co.cyberagent.android.gpuimage.view.GLTextureView;

import static jp.co.cyberagent.android.gpuimage.GPUImage.SURFACE_TYPE_SURFACE_VIEW;
import static jp.co.cyberagent.android.gpuimage.GPUImage.SURFACE_TYPE_TEXTURE_VIEW;
public class GPUImageView extends FrameLayout {
    private String TAG = GPUImageView.class.getSimpleName();
    private int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
    private View mGLSurfaceView;
    private GPUImage mGPUImage;
    private boolean isShowLoading = true;
    private GPUImageFilter mFilter;
    public Size mForceSize = null;
    private float mRatio = 0.0f;
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    public GPUImageView(Context context) {
        super(context);
        init(context, null);
    }

    public GPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GPUImageView, 0, 0);
            try {
                surfaceType = a.getInt(R.styleable.GPUImageView_gpuimage_surface_type, surfaceType);
                isShowLoading = a.getBoolean(R.styleable.GPUImageView_gpuimage_show_loading, isShowLoading);
            } finally {
                a.recycle();
            }
        }
        mGPUImage = new GPUImage(context);
        if (surfaceType == SURFACE_TYPE_TEXTURE_VIEW) {
            mGLSurfaceView = new GPUImageGLTextureView(context, attrs);
            mGPUImage.setGLTextureView((GLTextureView) mGLSurfaceView);
        } else {
            mGLSurfaceView = new GPUImageGLSurfaceView(context, attrs);
            mGPUImage.setGLSurfaceView((GLSurfaceView) mGLSurfaceView);
        }
        addView(mGLSurfaceView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio != 0.0f) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            int newHeight;
            int newWidth;
            if (width / mRatio < height) {
                newWidth = width;
                newHeight = Math.round(width / mRatio);
            } else {
                newHeight = height;
                newWidth = Math.round(height * mRatio);
            }

            int newWidthSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY);
            int newHeightSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY);
            super.onMeasure(newWidthSpec, newHeightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Retrieve the GPUImage instance used by this view.
     *
     * @return used GPUImage instance
     */
    public GPUImage getGPUImage() {
        return mGPUImage;
    }

    /**
     * Sets the background color
     *
     * @param red red color value
     * @param green green color value
     * @param blue red color value
     */
    public void setBackgroundColor(float red, float green, float blue) {
        mGPUImage.setBackgroundColor(red, green, blue);
    }

    /**
     * Set the rendering mode. When renderMode is
     * RENDERMODE_CONTINUOUSLY, the renderer is called
     * repeatedly to re-render the scene. When renderMode
     * is RENDERMODE_WHEN_DIRTY, the renderer only rendered when the surface
     * is created, or when {@link #requestRender} is called. Defaults to RENDERMODE_CONTINUOUSLY.
     *
     * @param renderMode one of the RENDERMODE_X constants
     * @see #RENDERMODE_CONTINUOUSLY
     * @see #RENDERMODE_WHEN_DIRTY
     * @see GLSurfaceView#setRenderMode(int)
     * @see GLTextureView#setRenderMode(int)
     */
    public void setRenderMode(int renderMode) {
        if (mGLSurfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) mGLSurfaceView).setRenderMode(renderMode);
        } else if (mGLSurfaceView instanceof GLTextureView) {
            ((GLTextureView) mGLSurfaceView).setRenderMode(renderMode);
        }
    }
    // TODO Should be an xml attribute. But then GPUImage can not be distributed as .jar anymore.
    public void setRatio(float ratio) {
        mRatio = ratio;
        mGLSurfaceView.requestLayout();
        mGPUImage.deleteImage();
    }

    /**
     * Set the scale type of GPUImage.
     *
     * @param scaleType the new ScaleType
     */
    public void setScaleType(GPUImage.ScaleType scaleType) {
        mGPUImage.setScaleType(scaleType);
    }

    /**
     * Sets the rotation of the displayed image.
     *
     * @param rotation new rotation
     */
    public void setRotation(Rotation rotation) {
        mGPUImage.setRotation(rotation);
        requestRender();
    }

    /**
     * Set the filter to be applied on the image.
     *
     * @param filter Filter that should be applied on the image.
     */
    public void setFilter(GPUImageFilter filter) {
        mFilter = filter;
        mGPUImage.setFilter(filter);
        requestRender();
    }

    /**
     * Get the current applied filter.
     *
     * @return the current filter
     */
    public GPUImageFilter getFilter() {
        return mFilter;
    }

    /**
     * Sets the image on which the filter should be applied.
     *
     * @param bitmap the new image
     */
    public void setImage(final Bitmap bitmap) {
        mGPUImage.setImage(bitmap);
    }

    /**
     * Sets the image on which the filter should be applied from a Uri.
     *
     * @param uri the uri of the new image
     */
    public void setImage(final Uri uri) {
        mGPUImage.setImage(uri);
    }

    /**
     * Sets the image on which the filter should be applied from a File.
     *
     * @param file the file of the new image
     */
    public void setImage(final File file) {
        mGPUImage.setImage(file);
    }

    public void requestRender() {
        if (mGLSurfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) mGLSurfaceView).requestRender();
        } else if (mGLSurfaceView instanceof GLTextureView) {
            ((GLTextureView) mGLSurfaceView).requestRender();
        }
    }

    /**
     * Save current image with applied filter to Pictures. It will be stored on
     * the default Picture folder on the phone below the given folderName and
     * fileName. <br>
     * This method is async and will notify when the image was saved through the
     * listener.
     *
     * @param folderName the folder name
     * @param fileName the file name
     * @param listener the listener
     */
    public void saveToPictures(final String folderName, final String fileName,
                               final OnPictureSavedListener listener) {
        new SaveTask(folderName, fileName, listener).execute();
    }

    /**
     * Save current image with applied filter to Pictures. It will be stored on
     * the default Picture folder on the phone below the given folderName and
     * fileName. <br>
     * This method is async and will notify when the image was saved through the
     * listener.
     *
     * @param folderName the folder name
     * @param fileName   the file name
     * @param width      requested output width
     * @param height     requested output height
     * @param listener   the listener
     */
    public void saveToPictures(final String folderName, final String fileName,
                               int width, int height,
                               final OnPictureSavedListener listener) {
        new SaveTask(folderName, fileName, width, height, listener).execute();
    }

    /**
     * Retrieve current image with filter applied and given size as Bitmap.
     *
     * @param width  requested Bitmap width
     * @param height requested Bitmap height
     * @return Bitmap of picture with given size
     * @throws InterruptedException
     */
    public Bitmap capture(final int width, final int height) throws InterruptedException {
        // This method needs to run on a background thread because it will take a longer time
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Do not call this method from the UI thread!");
        }

        mForceSize = new Size(width, height);

        final Semaphore waiter = new Semaphore(0);

        // Layout with new size
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                waiter.release();
            }
        });
        post(new Runnable() {
            @Override
            public void run() {
                // Show loading
                addView(new LoadingView(getContext()));

                mGLSurfaceView.requestLayout();
            }
        });
        waiter.acquire();

        // Run one render pass
        mGPUImage.runOnGLThread(new Runnable() {
            @Override
            public void run() {
                waiter.release();
            }
        });
        requestRender();
        waiter.acquire();
        Bitmap bitmap = capture();


        mForceSize = null;
        post(new Runnable() {
            @Override
            public void run() {
                mGLSurfaceView.requestLayout();
            }
        });
        requestRender();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remove loading view
                removeViewAt(1);
            }
        }, 300);

        return bitmap;
    }

    /**
     * Capture the current image with the size as it is displayed and retrieve it as Bitmap.
     * @return current output as Bitmap
     * @throws InterruptedException
     */
    public Bitmap capture() throws InterruptedException {
        final Semaphore waiter = new Semaphore(0);

        final int width = mGLSurfaceView.getMeasuredWidth();
        final int height = mGLSurfaceView.getMeasuredHeight();

        // Take picture on OpenGL thread
        final int[] pixelMirroredArray = new int[width * height];
        mGPUImage.runOnGLThread(new Runnable() {
            @Override
            public void run() {
                final IntBuffer pixelBuffer = IntBuffer.allocate(width * height);
                GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
                int[] pixelArray = pixelBuffer.array();

                // Convert upside down mirror-reversed image to right-side up normal image.
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i * width + j];
                    }
                }
                waiter.release();
            }
        });
        requestRender();
        waiter.acquire();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
        return bitmap;
    }

    /**
     * Pauses the GLSurfaceView.
     */
    public void onPause() {
        if (mGLSurfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) mGLSurfaceView).onPause();
        } else if (mGLSurfaceView instanceof GLTextureView) {
            ((GLTextureView) mGLSurfaceView).onPause();
        }
    }

    /**
     * Resumes the GLSurfaceView.
     */
    public void onResume() {
        if (mGLSurfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) mGLSurfaceView).onResume();
        } else if (mGLSurfaceView instanceof GLTextureView) {
            ((GLTextureView) mGLSurfaceView).onResume();
        }
    }

    public static class Size {
        int width;
        int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private class GPUImageGLSurfaceView extends GLSurfaceView {
        public GPUImageGLSurfaceView(Context context) {
            super(context);
        }

        public GPUImageGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (mForceSize != null) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mForceSize.width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mForceSize.height, MeasureSpec.EXACTLY));
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    private class GPUImageGLTextureView extends GLTextureView {
        public GPUImageGLTextureView(Context context) {
            super(context);
        }

        public GPUImageGLTextureView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (mForceSize != null) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mForceSize.width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mForceSize.height, MeasureSpec.EXACTLY));
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    private class LoadingView extends FrameLayout {
        public LoadingView(Context context) {
            super(context);
            init();
        }

        public LoadingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public LoadingView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            ProgressBar view = new ProgressBar(getContext());
            view.setLayoutParams(
                    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            addView(view);
            setBackgroundColor(Color.BLACK);
        }
    }

    private class SaveTask extends AsyncTask<Void, Void, Void> {
        private final String mFolderName;
        private final String mFileName;
        private final int mWidth;
        private final int mHeight;
        private final OnPictureSavedListener mListener;
        private final Handler mHandler;

        public SaveTask(final String folderName, final String fileName,
                        final OnPictureSavedListener listener) {
            this(folderName, fileName, 0, 0, listener);
        }

        public SaveTask(final String folderName, final String fileName, int width, int height,
                        final OnPictureSavedListener listener) {
            mFolderName = folderName;
            mFileName = fileName;
            mWidth = width;
            mHeight = height;
            mListener = listener;
            mHandler = new Handler();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                Bitmap result = mWidth != 0 ? capture(mWidth, mHeight) : capture();
                saveImage(mFolderName, mFileName, result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void saveImage(final String folderName, final String fileName, final Bitmap image) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, folderName + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                Log.v(TAG,"saveImage file path:"+file.getAbsolutePath());
                image.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(getContext(),
                        new String[]{
                                file.toString()
                        }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path, final Uri uri) {
                                if (mListener != null) {
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            mListener.onPictureSaved(uri);
                                        }
                                    });
                                }
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }
}
