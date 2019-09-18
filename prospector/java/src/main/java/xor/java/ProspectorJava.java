package xor.java;

import java.awt.image.BufferedImage;

import playn.java.LWJGLPlatform;
import playn.core.Graphics;
import playn.core.Canvas;
import playn.core.Scale;

import pythagoras.f.Dimension;
import pythagoras.f.IDimension;

import static xor.core.PixelConstants.*;

import xor.core.Prospector;
import xor.core.Platform;

import java.lang.reflect.Field;

public class ProspectorJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    LWJGLPlatform raw = new LWJGLPlatform(config);
    JavaPlatform platform = new JavaPlatform(raw);
    raw.graphics().setSize((int) (SCREEN_WIDTH * platform.zoom), (int) (SCREEN_HEIGHT * platform.zoom), false);
    new Prospector(platform);
    raw.start();
  }
  
  static class JavaPlatform extends Platform { 
    JavaPlatform(LWJGLPlatform raw) {
      super(raw);
    }

    @Override
    public Canvas createRawCanvas(int pixelWidth, int pixelHeight) {
      try {
        Field f = Graphics.class.getDeclaredField("scale");
        f.setAccessible(true);
        Scale temp = (Scale) f.get(raw.graphics());
        f.set(raw.graphics(), Scale.ONE);
        Canvas canvas = raw.graphics().createCanvas(pixelWidth, pixelHeight);
        f.set(raw.graphics(), temp);
        return canvas;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    
    @Override
    public IDimension availableArea() {
      IDimension screenSize = raw.graphics().screenSize();
      return new Dimension(0.8f * screenSize.width(), 0.8f * screenSize.height());
    }
    
    @Override
    public void exit() {
      System.exit(0);
    }
  }
}
