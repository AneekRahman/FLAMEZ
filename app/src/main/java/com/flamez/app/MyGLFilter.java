package com.flamez.app;

import com.daasuu.mp4compose.filter.GlFilter;
import com.daasuu.mp4compose.utils.GlUtils;


public class MyGLFilter extends GlFilter {

    private static final String FRAGMENT_SHADER =
                    "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;" +
                    "varying mediump float text_alpha_out;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {" +
                    "   vec4 color = texture2D(sTexture, vTextureCoord);" +
                    "   color.r = 1.;" +
                    "   color.g = 0.;" +
                    "   color.b = 0.;" +
                    "   color.a = 0.;" +
                    "   gl_FragColor = color;" +
                    "}";

    public MyGLFilter() {
        super(GlUtils.DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }
}

