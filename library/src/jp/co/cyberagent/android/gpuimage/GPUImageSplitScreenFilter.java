package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/**
 * @author nevaryyy
 */
public class GPUImageSplitScreenFilter extends GPUImageFilter {

    public static final String SPLIT_SCREEN_2_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = textureCoordinate.xy;\n" +
            "    float y;\n" +
            "    if (uv.y >= 0.0 && uv.y <= 0.5) {\n" +
            "        y = uv.y + 0.25;\n" +
            "    } else {\n" +
            "        y = uv.y - 0.25;\n" +
            "    }\n" +
            "    gl_FragColor = texture2D(inputImageTexture, vec2(uv.x, y));\n" +
            "}";

    public static final String SPLIT_SCREEN_3_FRAGMENT_SHADER="" +
            "precision highp float;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = textureCoordinate.xy;\n" +
            "    if (uv.y < 1.0/3.0) {\n" +
            "        uv.y = uv.y + 1.0/3.0;\n" +
            "    } else if (uv.y > 2.0/3.0){\n" +
            "        uv.y = uv.y - 1.0/3.0;\n" +
            "    }\n" +
            "    gl_FragColor = texture2D(inputImageTexture, uv);\n" +
            "}";

    public static final String SPLIT_SCREEN_4_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = textureCoordinate.xy;\n" +
            "    if(uv.x <= 0.5){\n" +
            "        uv.x = uv.x * 2.0;\n" +
            "    }else{\n" +
            "        uv.x = (uv.x - 0.5) * 2.0;\n" +
            "    }\n" +
            "\n" +
            "    if (uv.y<= 0.5) {\n" +
            "        uv.y = uv.y * 2.0;\n" +
            "    }else{\n" +
            "        uv.y = (uv.y - 0.5) * 2.0;\n" +
            "    }\n" +
            "\n" +
            "    gl_FragColor = texture2D(inputImageTexture, uv);\n" +
            "}";

    public static final String SPLIT_SCREEN_6_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = textureCoordinate.xy;\n" +
            "\n" +
            "    if(uv.x <= 1.0 / 3.0){\n" +
            "        uv.x = uv.x + 1.0/3.0;\n" +
            "    }else if(uv.x >= 2.0/3.0){\n" +
            "        uv.x = uv.x - 1.0/3.0;\n" +
            "    }\n" +
            "\n" +
            "    if(uv.y <= 0.5){\n" +
            "        uv.y = uv.y + 0.25;\n" +
            "    }else {\n" +
            "        uv.y = uv.y - 0.25;\n" +
            "    }\n" +
            "\n" +
            "    gl_FragColor = texture2D(inputImageTexture, uv);\n" +
            "}";

    public static final String SPLIT_SCREEN_9_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = textureCoordinate.xy;\n" +
            "    if (uv.x < 1.0 / 3.0) {\n" +
            "        uv.x = uv.x * 3.0;\n" +
            "    } else if (uv.x < 2.0 / 3.0) {\n" +
            "        uv.x = (uv.x - 1.0 / 3.0) * 3.0;\n" +
            "    } else {\n" +
            "        uv.x = (uv.x - 2.0 / 3.0) * 3.0;\n" +
            "    }\n" +
            "    if (uv.y <= 1.0 / 3.0) {\n" +
            "        uv.y = uv.y * 3.0;\n" +
            "    } else if (uv.y < 2.0 / 3.0) {\n" +
            "        uv.y = (uv.y - 1.0 / 3.0) * 3.0;\n" +
            "    } else {\n" +
            "        uv.y = (uv.y - 2.0 / 3.0) * 3.0;\n" +
            "    }\n" +
            "    gl_FragColor = texture2D(inputImageTexture, uv);\n" +
            "}";
    public GPUImageSplitScreenFilter() {
        this(0);
    }

    /**
     * Acceptable values for split, which sets the distance in pixels to sample out
     * from the center, are 1, 2, 3, and 4.
     *
     * @param shader 1, 2, 3 or 4
     */
    public GPUImageSplitScreenFilter(int shader) {
        this(NO_FILTER_VERTEX_SHADER, getFragmentShader(shader));
    }

    private GPUImageSplitScreenFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
    }
    private static String getFragmentShader(int shader) {
        switch (shader) {
            case 0:
                return SPLIT_SCREEN_2_FRAGMENT_SHADER;
            case 1:
                return SPLIT_SCREEN_3_FRAGMENT_SHADER;
            case 2:
                return SPLIT_SCREEN_4_FRAGMENT_SHADER;
            case 3:
                return SPLIT_SCREEN_6_FRAGMENT_SHADER;
            case 4:
                return SPLIT_SCREEN_9_FRAGMENT_SHADER;
            default:
                return SPLIT_SCREEN_2_FRAGMENT_SHADER;
        }
    }
}
