package dslGroovy

import com.sksamuel.scrimage.Color$
import com.sksamuel.scrimage.Composite
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.ImageMetadata
import com.sksamuel.scrimage.Position
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.composite.AlphaComposite
import dslScala.FilterDSL
import com.sksamuel.scrimage.nio.*
import java.awt.image.BufferedImage
import static utilitiesGroovy.UtilitiesGroovy.*

class ImageDSLGroovy {

    ImageWriter writer = PngWriter.NoCompression()
    Image imageObj = new Image(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB),ImageMetadata.empty())
    String fullPath = ""

    ImageDSLGroovy (Image image) {
        imageObj = image
        writer = PngWriter.NoCompression()
        this
    }
    static ImageDSLGroovy imageDSLGroovy(params){
        if(params.format == null)
            params.format = "jpg"
        if(params.compression == null)
            params.compression = 0
        if(params.fullPath == null)
            params.fullPath = ""

        new ImageDSLGroovy(params.format, params.compression, params.fullPath)
    }
    ImageDSLGroovy (String format = "jpg", int compression = 0, String fullPath = "") {
        this.fullPath = fullPath
        def mc = new ExpandoMetaClass(ImageDSLGroovy, false, true)
        mc.initialize()
        this.metaClass = mc
        switch (format)
        {
            case "png":
                switch (compression)
                {
                    case 100: writer = PngWriter.MaxCompression(); break
                    case 0: writer = PngWriter.NoCompression(); break
                    default:
                        def comTmp = Math.ceil(compression / 10.0).toInteger()
                        if(comTmp > 9)
                            writer = new PngWriter(9)
                        else
                            writer = new PngWriter(comTmp)
                }
                break
            case "jpg":
                switch (compression)
                {
                    case 100: writer = JpegWriter.apply(0, false); break
                    case 0: writer = JpegWriter.NoCompression(); break
                    default:
                        writer = new JpegWriter(Math.abs(compression - 100), false)
                }
                break
            case "jpeg":
                switch (compression)
                {
                    case 100: writer = JpegWriter.apply(0, false); break
                    case 0: writer = JpegWriter.NoCompression(); break
                    default:
                        writer = new JpegWriter(Math.abs(compression - 100), false)
                }
                break
            case "gif": writer = new GifWriter(true); break
            default: writer = JpegWriter.NoCompression()
        }
    }

    //transformations
    def scale(params) {
        if(params.width != null && params.height != null)
        {
            scaleTo params.width, params.height
            return this
        }
        else if(params.height == null)
        {
            scaleToWidth params.width
            return this
        }
        else if(params.width == null)
        {
            scaleToHeight params.height
            return this
        }
        else this
    }
    def scaleTo(int width = 200, int height = 200) {
        imageObj = imageObj.scaleTo width, height, ScaleMethod.Bicubic$.MODULE$
        this
    }
    def scaleToWidth(int width = 200) {
        imageObj = imageObj.scaleToWidth width, ScaleMethod.Bicubic$.MODULE$
        this
    }
    def scaleToHeight(int height = 200) {
        imageObj = imageObj.scaleToHeight height, ScaleMethod.Bicubic$.MODULE$
        this
    }

    def rotate(String leftOrRight) {
        switch(leftOrRight)
        {
            case "left": imageObj = imageObj.rotateLeft(); break
            case "right": imageObj = imageObj.rotateRight(); break
        }
        this
    }
    def flip(String horizontallyOrVertically) {
        switch (horizontallyOrVertically)
        {
            case "horizontally": imageObj = imageObj.flipX(); break
            case "vertically": imageObj = imageObj.flipY(); break
        }
        this
    }

    def storage = [:]
    def propertyMissing(String name, value) {
        //storage[name] = value
        this.metaClass."$name" = value
    }
    /*
    def propertyMissing(String name) {
        storage[name]
    }
    */
    //TODO:ovaj princip?
    def mapExample(action) {
        [do: { what ->
            [on: { n -> action(what(n)) }]
        }]
    }
    def methodMissing(String name, args) {
        if(name == "rotiraj")
            this.rotate args[0]
    }
    def processing(closure) {
        this.with closure
    }

    def translate(closure) {
        this.with closure
    }
    def background(params) {
        if(params.color == "black")
            imageObj = imageObj.translate this.getProperty("x"), this.getProperty("y"), Color$.MODULE$.Black()
        else if(params.color == "white")
            imageObj = imageObj.translate this.getProperty("x"), this.getProperty("y"), Color$.MODULE$.White()
        this
    }

    def autocrop(params) {
        if(params.background == "black")
            imageObj = imageObj.autocrop Color$.MODULE$.Black()
        else if(params.background == "white")
            imageObj = imageObj.autocrop Color$.MODULE$.White()
        this
    }
    def cropBy(Double factor = 1) {
        imageObj = imageObj.resize factor, Position.Center$.MODULE$, Color$.MODULE$.Transparent()
        this
    }

    def fit(params) {
        if(params.background == "black")
            imageObj = imageObj.fit params.width, params.height, Color$.MODULE$.Black(), ScaleMethod.Bicubic$.MODULE$, Position.Center$.MODULE$
        else if(params.background == "white")
            imageObj = imageObj.fit params.width, params.height, Color$.MODULE$.White(), ScaleMethod.Bicubic$.MODULE$, Position.Center$.MODULE$
        this
    }
    def trim(int all) {
        imageObj = imageObj.trim all
        this
    }
    def trim(closure){
        this.with closure
    }
    def getTrimming()
    {
        imageObj = imageObj.trim(this.getProperty("leftSide"), this.getProperty("top"), this.getProperty("rightSide"), this.getProperty("bottom"))
        this
    }

    //effects
    ImageDSLGroovy plus(ImageDSLGroovy image) {
        imageObj = imageObj.composite new AlphaComposite(0.5), image.imageObj
        this
    }
    def addProcess(ImageDSLGroovy image, String compositType = alpha, int compositPct = 50) {
        def compositLevelFloat = new BigDecimal(compositPct / 100.0).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
        imageObj = imageObj.composite getCompositeType(compositType, compositLevelFloat), image.imageObj
        this
    }
    Composite getCompositeType(String compositType = alpha, Double compositLevel = 0.5f) {
        switch (compositType)
        {
            case alpha: return new AlphaComposite(compositLevel)
            default: return new AlphaComposite(compositLevel)
        }
    }
    def add(closure){
        this.with closure
    }
    def getAdding() {
        addProcess this.getProperty("imageForCompostion"), this.getProperty("compositType"), this.getProperty("compositPct")
    }

    def filtering(param){
        imageObj = imageObj.filter param
        this
    }
    def filterWith(filterEnum) {
         def selectedEnum = null
         switch (filterEnum)
         {
             case FilterEnum.BlackThresholdFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BlackThresholdFilter(); break
             case FilterEnum.BorderFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BorderFilter(); break
             case FilterEnum.BrightnessFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.BrightnessFilter(); break
             case FilterEnum.ChromeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ChromeFilter(); break
             case FilterEnum.ColorHalftoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ColorHalftoneFilter(); break
             case FilterEnum.ColorizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ColorizeFilter(); break
             case FilterEnum.ContourFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ContourFilter(); break
             case FilterEnum.ContrastFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ContrastFilter(); break
             case FilterEnum.CrystallizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.CrystallizeFilter(); break
             case FilterEnum.DiffuseFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.DiffuseFilter(); break
             case FilterEnum.ErrorDiffusionHalftoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ErrorDiffusionHalftoneFilter(); break
             case FilterEnum.GainBiasFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GainBiasFilter(); break
             case FilterEnum.GammaFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GammaFilter(); break
             case FilterEnum.GaussianBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GaussianBlurFilter(); break
             case FilterEnum.GlowFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.GlowFilter(); break
             case FilterEnum.HSBFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.HSBFilter(); break
             case FilterEnum.LensBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.LensBlurFilter(); break
             case FilterEnum.MotionBlurFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.MotionBlurFilter(); break
             case FilterEnum.NoiseFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.NoiseFilter(); break
             case FilterEnum.OffsetFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OffsetFilter(); break
             case FilterEnum.OilFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OilFilter(); break
             case FilterEnum.OpacityFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.OpacityFilter(); break
             case FilterEnum.PixelateFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PixelateFilter(); break
             case FilterEnum.PointillizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PointillizeFilter(); break
             case FilterEnum.PosterizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.PosterizeFilter(); break
             case FilterEnum.QuantizeFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.QuantizeFilter(); break
             case FilterEnum.RaysFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RaysFilter(); break
             case FilterEnum.RGBFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RGBFilter(); break
             case FilterEnum.RippleFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.RippleFilter(); break
             case FilterEnum.SmearFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SmearFilter(); break
             case FilterEnum.SparkleFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SparkleFilter(); break
             case FilterEnum.SwimFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.SwimFilter(); break
             case FilterEnum.ThresholdFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.ThresholdFilter(); break
             case FilterEnum.TritoneFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.TritoneFilter(); break
             case FilterEnum.TwirlFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.TwirlFilter(); break
             case FilterEnum.UnsharpFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.UnsharpFilter(); break
             case FilterEnum.VignetteFilter: selectedEnum = FilterDSL.FilterEnum$.MODULE$.VignetteFilter(); break
             default: selectedEnum = null
         }
        imageObj = imageObj.filter FilterDSL.apply(selectedEnum)
        this
     }

    //statistics
    def minPixel() {
        imageObj.pixels() toList() collect {element -> return element.toInt()} min()
    }
    def maxPixel() {
        imageObj.pixels() toList() collect {element -> return element.toInt()} max()
    }
    def averagePixel() {
        imageObj.pixels().toList().collect({element -> return element.toInt()}).sum() / imageObj.pixels().size().toDouble()
    }

    //I/O
    def importing(params) {
        importFrom params.from
    }
    def importFrom(String path = "") {
        if(this.fullPath == null)
           this.fullPath = ""
        imageObj = imageObj.fromFile new File(this.fullPath + "input/" + path)
        this
    }
    def exporting(params) {
        exportTo params.to
    }
    def exportTo(String path = "") {
        if(this.fullPath == null)
           this.fullPath = ""
        imageObj.output this.fullPath + "output/" + path, writer
        this
    }
}