package dslScala

import java.io.File
import java.math.BigDecimal

import com.sksamuel.scrimage.composite._
import com.sksamuel.scrimage.nio._
import com.sksamuel.scrimage.{Color, Composite, Filter, Image, RGBColor}
import dslScala.FilterDSL.FilterEnum.FilterEnum
import utilitiesScala.UtilitiesScala.{white, _}

class ImageDSLScala(width: Int = 200, height: Int = 200) {

  implicit var writer: ImageWriter = PngWriter NoCompression
  private var imageObj = Image(200, 200)
  def getImageObj = imageObj
  def setImageObj(img: Image) = imageObj = img

  def apply (image: Image) = {
    imageObj = image
    writer = PngWriter.NoCompression
    this
  }
  def apply (format: Format = jpg, compression: Int = 0) = {
    format label match {
      case png.label =>
        compression match {
          case 100 => writer = PngWriter MaxCompression
          case 0 => writer = PngWriter NoCompression
          case _ =>
          {
            val comTmp = math.ceil(compression / 10.toDouble).toInt
            if(comTmp > 9)
              writer = PngWriter(9)
            else
              writer = PngWriter(comTmp)
          }
        }
      case jpg.label =>
        compression match {
          case 100 => writer = JpegWriter apply(0, false)
          case 0 => writer = JpegWriter NoCompression
          case _ => writer = JpegWriter(math abs compression - 100, false)
        }
      case jpeg.label =>
        compression match {
          case 100 => writer = JpegWriter.apply(0, false)
          case 0 => writer = JpegWriter NoCompression
          case _ => writer = JpegWriter(math abs compression - 100, false)
        }
      case gif.label =>
        writer = GifWriter(true)
      case _ =>
        writer = JpegWriter NoCompression
    }
    this
  }

  //transformations
  def scaleTo(width: Int = 200, height: Int = 200) = {
    imageObj = imageObj scaleTo(width, height)
    this
  }
  def scaleToWidth(width: Int = 200) = {
    imageObj = imageObj scaleToWidth width
    this
  }
  def scaleToHeight(height: Int = 200) = {
    imageObj = imageObj scaleToHeight height
    this
  }
  def rotate(leftOrRight: LeftOrRight) = {
    leftOrRight label match {
      case left.label => imageObj = getImageObj rotateLeft
      case right.label => imageObj = getImageObj rotateRight
    }
    this
  }
  def flip(horizontallyOrVertically: HorizontallyOrVertically) = {
    horizontallyOrVertically label match {
      case horizontally.label => imageObj = getImageObj flipX
      case vertically.label => imageObj = getImageObj flipY
    }
    this
  }

  def translate(): Horizontal = {
    new Horizontal(this)
  }
  class Horizontal(imageDSL: ImageDSLScala) {
    def horizontally(x: Int = 0) = {
      new Vertical(x, imageDSL)
    }
  }
  class Vertical(x: Int = 0, imageDSL: ImageDSLScala) {
    def vertically(y: Int = 0) = {
      new ByColor(x, y, imageDSL)
    }
  }
  def translateBy(x: Int = 0, y: Int = 0) = {
    new ByColor(x, y,this)
  }
  class ByColor(x: Int = 0, y: Int = 0, imageDSL: ImageDSLScala) {
    def background(color: Color = Color White)= {
      imageObj = imageObj translate(x, y, color)
      imageDSL
    }
  }

  def autocrop(background: Background = white) =  {
    background label match {
      case white.label => imageObj = imageObj autocrop(RGBColor(255, 255, 255))
      case black.label => imageObj = imageObj autocrop(RGBColor(0, 0, 0))
      case _ => imageObj = imageObj autocrop(RGBColor(255, 255, 255))
    }
    this
  }
  def cropBy(factor: Double = 1) = {
    imageObj = imageObj resize factor
    this
  }
  def fit(width: Int = 0, height: Int = 0, color: Color = Color.White) = {
    imageObj = imageObj fit(width, height, color)
    this
  }
  def trim(all: Int) = {
    imageObj = imageObj trim all
    this
  }
  def trim(left: Int = 0, right: Int = 0, bottom: Int = 0, top: Int = 0) = {
    imageObj = imageObj trim(left, top, right, bottom)
    this
  }

  //effects
  def +(image: ImageDSLScala) = {
    imageObj = imageObj composite(new AlphaComposite(0.5f), image getImageObj)
    this
  }
  def add(image: ImageDSLScala, compositType: CompositType = alpha, compositPct: Int = 50) = {
    val compositLevelFloat = new BigDecimal(compositPct / 100.toDouble).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
    imageObj = imageObj composite(getCompositeType(compositType, compositLevelFloat), image getImageObj)
    this
  }
  def getCompositeType(compositType: CompositType = alpha, compositLevel: Double = 0.5f): Composite = {
    compositType label match {
      case alpha.label => new AlphaComposite(compositLevel);
      /*case "average" => new AverageComposite(compositLevel)
      case "blue" => new BlueComposite(compositLevel)
      case "color" => new ColorComposite(compositLevel)
      case "colorburn" => new ColorBurnComposite(compositLevel)
      case "colordodge" => new ColorDodgeComposite(compositLevel)
      case "diff" => new DifferenceComposite(compositLevel)
      case "green" => new GreenComposite(compositLevel)
      case "grow" => new GlowComposite(compositLevel)
      case "hue" => new HueComposite(compositLevel)
      case "hard" => new HardLightComposite(compositLevel)
      case "heat" => new HeatComposite(compositLevel)
      case "lighten" => new LightenComposite(compositLevel)
      case "negation" => new NegationComposite(compositLevel)
      case "luminosity" => new LuminosityComposite(compositLevel)
      case "multiply" => new MultiplyComposite(compositLevel)
      case "overlay" => new OverlayComposite(compositLevel)
      case "red" => new RedComposite(compositLevel)
      case "reflect" => new ReflectComposite(compositLevel)
      case "saturation" => new SaturationComposite(compositLevel)
      case "screen" => new ScreenComposite(compositLevel)
      case "subtract" => new SubtractComposite(compositLevel)*/
      case _ => new AlphaComposite(compositLevel)
    }
  }
  def filterWith(filterEnum: FilterEnum) = {
    imageObj = imageObj filter FilterDSL(filterEnum)
    this
  }

  def filterWith(adjust: () => Filter) = {
    imageObj = imageObj filter adjust()
    this
  }

  def processing(processing:() => ImageDSLScala) = {
    processing()
    this
  }

  //statistics
  def minPixel = imageObj.pixels reduceLeft(_.toInt min _.toInt)
  def maxPixel = imageObj.pixels reduceLeft(_.toInt max _.toInt)
  def averagePixel = {
      val sum = imageObj.pixels reduceLeft(_.toInt + _.toInt)
      sum.toInt / imageObj.pixels.size.toDouble
  }

  //I/O
  def importFrom(path: String = "") = {
    imageObj = Image fromFile new File("input/" + path)
    //imageObj = Image fromResource("/" + path)
    this
  }
  def exportTo(path: String = "") = {
    (imageObj output "output/" + path)(writer)
    this
  }
}

object ImageDSLScala {

  def apply (width: Int, height: Int) = {
    new ImageDSLScala(width, height)
  }
  def apply (image: Image) = {
    new ImageDSLScala apply image
  }
  def apply (format: Format, compression: Int) = {
    new ImageDSLScala apply(format, compression)
  }
  def apply (format: Format = jpg) = {
    new ImageDSLScala apply(format, 0)
  }
  def apply () = {
    new ImageDSLScala apply(jpg, 0)
  }
}