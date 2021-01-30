package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * @author nevaryyy
 */
public class GPUImageSoulOutFilter extends GPUImageFilter {
    //attribute 只能在顶点着色器使用的变量
    //uniform 外部程序传递给（vertex和fragment）shader的变量
    //varying vertex和fragment shader之间做数据传递用的变量
    public static final String BILATERAL_VERTEX_SHADER = "" +
            "uniform mat4 uTexMatrix;\n" +
            "attribute vec2 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform mat4 uMvpMatrix;\n" +
            "void main(){\n" +
            "    gl_Position = uMvpMatrix * vec4(position,0.1,1.0);\n" +
            "    vTextureCoord = (uTexMatrix * inputTextureCoordinate).xy;\n" +
            "}\n";
    public static final String BILATERAL_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "uniform float uAlpha;\n" +
            "void main(){\n" +
            "    gl_FragColor = vec4(texture2D(inputImageTexture,vTextureCoord).rgb,uAlpha);\n" +
            "}\n";

    //当前动画进度
    private float mProgress = 0.0f;
    //当前地帧数
    private int mFrames = 0;
    //动画最大帧数
    private static final int mMaxFrames = 15;
    //动画完成后跳过的帧数
    private static final int mSkipFrames = 8;
    //放大矩阵
    private float[] mMvpMatrix;
    private float mAlpha;
    //opengl 参数位置
    private int mMvpMatrixLocation;
    private int mAlphaLocation;
    private int mUniformTexMatrixLocation;

    public GPUImageSoulOutFilter() {
        super(BILATERAL_VERTEX_SHADER, BILATERAL_FRAGMENT_SHADER);
        mMvpMatrix = new float[16];
        Matrix.setIdentityM(mMvpMatrix, 0);
    }

    @Override
    public void onInit() {
        super.onInit();

        mUniformTexMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uTexMatrix"); // This does assume a name of "inputImageTexture2" for second input texture in the fragment shader
        mMvpMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uMvpMatrix"); // This does assume a name of "inputImageTexture2" for second input texture in the fragment shader
        mAlphaLocation = GLES20.glGetUniformLocation(getProgram(), "uAlpha"); // This does assume a name of "inputImageTexture2" for second input texture in the fragment shader
        setUniformMatrix4f(mMvpMatrixLocation,mMvpMatrix);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();

        //因为这里是两个图层，所以开启混合模式
       /* glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA);*/
        mProgress = (float) mFrames / mMaxFrames;
        if (mProgress > 1f) {
            mProgress = 0f;
        }
        mFrames++;
        if (mFrames > mMaxFrames + mSkipFrames) {
            mFrames = 0;
        }
        Matrix.setIdentityM(mMvpMatrix, 0);//初始化矩阵
        //第一帧是没有放大的，所以这里直接赋值一个单位矩阵
        setUniformMatrix4f(mMvpMatrixLocation, mMvpMatrix);
        //底层图层的透明度
        float backAlpha = 1f;
        //放大图层的透明度
        float alpha = 0f;
        if (mProgress > 0f) {
            alpha = 0.2f - mProgress * 0.2f;
            backAlpha = 1 - alpha;
        }
        setAlpha(backAlpha);
        /*setUniformMatrix4f(mUniformTexMatrixLocation,  texMatrix);
        //初始化顶点着色器数据，包括纹理坐标以及顶点坐标
        mRendererInfo.getVertexBuffer().position(0);
        glVertexAttribPointer(mAttrPositionLocation, 2,
                GL_FLOAT, false, 0, mRendererInfo.getVertexBuffer());
        mRendererInfo.getTextureBuffer().position(0);
        glVertexAttribPointer(mAttrTexCoordLocation, 2,
                GL_FLOAT, false, 0, mRendererInfo.getTextureBuffer());
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        //绘制底部原图
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);*/
        if (mProgress > 0f) {
            //这里绘制放大图层
            setAlpha(alpha);
            float scale = 1.0f + 1f * mProgress;
            Matrix.scaleM(mMvpMatrix, 0, scale, scale, scale);
            setUniformMatrix4f(mMvpMatrixLocation, mMvpMatrix);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        }

       /* GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        GLES20.glUseProgram(0);
        glDisable(GL_BLEND);*/
    }


    public void setAlpha(final float alpha) {
        mAlpha = alpha;
        setFloat(mAlphaLocation, mAlpha);
    }
}
