package dslJava;

import com.sksamuel.scrimage.*;
import com.sksamuel.scrimage.composite.AlphaComposite;
import com.sksamuel.scrimage.nio.GifWriter;
import com.sksamuel.scrimage.nio.ImageWriter;
import com.sksamuel.scrimage.nio.JpegWriter;
import com.sksamuel.scrimage.nio.PngWriter;
import dslScala.FilterDSL;
import static utilitiesGroovy.UtilitiesGroovy.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImageDSLJava {

    ImageWriter writer = PngWriter.NoCompression();
    Image imageObj = new Image(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB),ImageMetadata.empty());
    String fullPath = "";
    public ImageDSLJava setFullPath(String path) {fullPath=path; return this;}

    public static ImageDSLJava imageDSLJava(String format, int compression){
        return new ImageDSLJava (format, compression);
    }
    public static ImageDSLJava imageDSLJava(String format){
        return new ImageDSLJava (format, 0);
    }
    public static ImageDSLJava imageDSLJava(int compression){
        return new ImageDSLJava ("jpg", compression);
    }
    public static ImageDSLJava imageDSLJava(){
        return new ImageDSLJava ("jpg", 0);
    }
    ImageDSLJava (String format, int compression) {
        switch (format)
        {
            case "png":
                switch (compression)
                {
                    case 100: writer = PngWriter.MaxCompression(); break;
                    case 0: writer = PngWriter.NoCompression(); break;
                    default:
                        int comTmp = new Double(Math.ceil(compression / 10.0)).intValue();
                        if(comTmp > 9)
                            writer = new PngWriter(9);
                        else
                            writer = new PngWriter(comTmp);
                }
                break;
            case "jpg":
                switch (compression)
                {
                    case 100: writer = JpegWriter.apply(0, false); break;
                    case 0: writer = JpegWriter.NoCompression(); break;
                    default:
                        writer = new JpegWriter(Math.abs(compression - 100), false);
                }
                break;
            case "jpeg":
                switch (compression)
                {
                    case 100: writer = JpegWriter.apply(0, false); break;
                    case 0: writer = JpegWriter.NoCompression(); break;
                    default:
                        writer = new JpegWriter(Math.abs(compression - 100), false);
                }
                break;
            case "gif": writer = new GifWriter(true); break;
            default: writer = JpegWriter.NoCompression();
        }
    }

    //transformations
    public ImageDSLJava scaleTo(int width, int height) {
        imageObj = imageObj.scaleTo(width, height, ScaleMethod.Bicubic$.MODULE$);
        return this;
    }
    public ImageDSLJava scaleToWidth(int width) {
        imageObj = imageObj.scaleToWidth(width, ScaleMethod.Bicubic$.MODULE$);
        return this;
    }
    public ImageDSLJava scaleToHeight(int height) {
        imageObj = imageObj.scaleToHeight(height, ScaleMethod.Bicubic$.MODULE$);
        return this;
    }

    public ImageDSLJava rotate(String leftOrRight) {
        switch(leftOrRight)
        {
            case "left": imageObj = imageObj.rotateLeft(); break;
            case "right": imageObj = imageObj.rotateRight(); break;
        }
        return this;
    }
    public ImageDSLJava flip(String horizontallyOrVertically) {
        switch (horizontallyOrVertically) {
            case "horizontally":
                imageObj = imageObj.flipX();
                break;
            case "vertically":
                imageObj = imageObj.flipY();
                break;
        }
        return this;
    }
    public ImageDSLJava translate(int x, int y, String color) {
        if(color.equals("black"))
            imageObj = imageObj.translate(x, y, Color$.MODULE$.Black());
        else if(color.equals("white"))
            imageObj = imageObj.translate(x, y, Color$.MODULE$.White());
        return this;
    }

    public ImageDSLJava autocrop(String background) {
        if(background.equals("black"))
            imageObj = imageObj.autocrop(Color$.MODULE$.Black());
        else if(background.equals("white"))
            imageObj = imageObj.autocrop(Color$.MODULE$.White());
        return this;
    }
    public ImageDSLJava cropBy(Double factor) {
        imageObj = imageObj.resize(factor, Position.Center$.MODULE$, Color$.MODULE$.Transparent());
        return this;
    }

    public ImageDSLJava fit(int width, int height, String background) {
        if(background.equals("black"))
            imageObj = imageObj.fit(width, height, Color$.MODULE$.Black(), ScaleMethod.Bicubic$.MODULE$, Position.Center$.MODULE$);
        else if(background.equals("white"))
            imageObj = imageObj.fit(width, height, Color$.MODULE$.White(), ScaleMethod.Bicubic$.MODULE$, Position.Center$.MODULE$);
        return this;
    }
    public ImageDSLJava trim(int all) {
        imageObj = imageObj.trim(all);
        return this;
    }
    public ImageDSLJava trim(int leftSide, int top, int rightSide, int bottom) {
        imageObj = imageObj.trim(leftSide, top, rightSide, bottom);
        return this;
    }

    //effects
    public ImageDSLJava add(ImageDSLJava image) {
        imageObj = imageObj.composite(new AlphaComposite(0.5), image.imageObj);
        return this;
    }
    public ImageDSLJava composing(ImageDSLJava image, String compositType, int compositPct) {
        double compositLevelFloat = new BigDecimal(compositPct / 100.0).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        imageObj = imageObj.composite(getCompositeType(compositType, compositLevelFloat), image.imageObj);
        return this;
    }
    private Composite getCompositeType(String compositType, Double compositLevel) {
        switch (compositType)
        {
            case "alpha": return new AlphaComposite(compositLevel);
            default: return new AlphaComposite(compositLevel);
        }
    }

    public ImageDSLJava filterWith(FilterEnum filterEnum) {
        scala.Enumeration.Value selectedEnum = null;
        switch (filterEnum)
        {
            case BlackThresholdFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BlackThresholdFilter(); break;
            case BorderFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BorderFilter(); break;
            case BrightnessFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BrightnessFilter(); break;
            case ChromeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ChromeFilter(); break;
            case ColorHalftoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ColorHalftoneFilter(); break;
            case ColorizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ColorizeFilter(); break;
            case ContourFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ContourFilter(); break;
            case ContrastFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ContrastFilter(); break;
            case CrystallizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.CrystallizeFilter(); break;
            case DiffuseFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.DiffuseFilter(); break;
            case ErrorDiffusionHalftoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ErrorDiffusionHalftoneFilter(); break;
            case GainBiasFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GainBiasFilter(); break;
            case GammaFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GammaFilter(); break;
            case GaussianBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GaussianBlurFilter(); break;
            case GlowFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GlowFilter(); break;
            case HSBFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.HSBFilter(); break;
            case LensBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.LensBlurFilter(); break;
            case MotionBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.MotionBlurFilter(); break;
            case NoiseFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.NoiseFilter(); break;
            case OffsetFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OffsetFilter(); break;
            case OilFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OilFilter(); break;
            case OpacityFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OpacityFilter(); break;
            case PixelateFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PixelateFilter(); break;
            case PointillizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PointillizeFilter(); break;
            case PosterizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PosterizeFilter(); break;
            case QuantizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.QuantizeFilter(); break;
            case RaysFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RaysFilter(); break;
            case RGBFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RGBFilter(); break;
            case RippleFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RippleFilter(); break;
            case SmearFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SmearFilter(); break;
            case SparkleFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SparkleFilter(); break;
            case SwimFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SwimFilter(); break;
            case ThresholdFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ThresholdFilter(); break;
            case TritoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.TritoneFilter(); break;
            case TwirlFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.TwirlFilter(); break;
            case UnsharpFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.UnsharpFilter(); break;
            case VignetteFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.VignetteFilter(); break;
            default: selectedEnum = null;
        }
        imageObj = imageObj.filter(FilterDSL.apply(selectedEnum));
        return this;
    }

    //lambda izrazi
    public ImageDSLJava processing(Consumer<ImageDSLJava> consumer){
        consumer.accept(this);
        return this;
    }

    //statistics
    List<Pixel> pixels = new ArrayList(Arrays.asList(imageObj.pixels()));
    public int minPixel() {
        pixels = new ArrayList(Arrays.asList(imageObj.pixels()));
        return pixels.stream().mapToInt(element -> element.toInt()).min().getAsInt();
    }
    public int maxPixel() {
        pixels = new ArrayList(Arrays.asList(imageObj.pixels()));
        return pixels.stream().mapToInt(element -> element.toInt()).max().getAsInt();
    }
    public double averagePixel() {
        pixels = new ArrayList(Arrays.asList(imageObj.pixels()));
        return pixels.stream().mapToInt(element -> element.toInt()).sum() / (double)pixels.size();
    }

    //I/O
    public ImageDSLJava importFrom(String path) {
        if(this.fullPath == null)
            this.fullPath = "";
        imageObj = imageObj.fromFile(new File(this.fullPath + "input/" + path));
        return this;
    }
    public ImageDSLJava exportTo(String path) {
        if(this.fullPath == null)
            this.fullPath = "";
        imageObj.output(this.fullPath + "output/" + path, writer);
        return this;
    }
}
