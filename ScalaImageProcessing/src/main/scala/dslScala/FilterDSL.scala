package dslScala

import java.awt.Color

import com.sksamuel.scrimage.filter.EdgeAction.ZeroEdges
import com.sksamuel.scrimage.filter._
import com.sksamuel.scrimage.{Filter, RGBColor}
import dslScala.FilterDSL.FilterEnum.FilterEnum

object FilterDSL {

  object FilterEnum extends Enumeration {
    type FilterEnum = Value
    val BlackThresholdFilter, BorderFilter,
    BrightnessFilter, ChromeFilter ,
    ColorizeFilter, ContourFilter, ContrastFilter, CrystallizeFilter,
    DiffuseFilter, ErrorDiffusionHalftoneFilter, GammaFilter,
    GlowFilter, HSBFilter, GaussianBlurFilter,
    LensBlurFilter, MaximumFilter, MotionBlurFilter,
    NoiseFilter, OffsetFilter, OilFilter,
    OpacityFilter, PixelateFilter, PointillizeFilter,
    QuantizeFilter, RaysFilter, RGBFilter,
    RippleFilter, SmearFilter, SparkleFilter, SwimFilter,
    ThresholdFilter, TritoneFilter, TwirlFilter,
    UnsharpFilter, VignetteFilter, PosterizeFilter,
    ColorHalftoneFilter,GainBiasFilter = Value
    //BlurFilter,BumpFilter,DespeckleFilter,DitherFilter,SharpenFilter
    //EdgeFilter,EmbossFilter,GrayscaleFilter,InvertAlphaFilter,InvertFilter
    //KaleidoscopeFilter,MinimumFilter,MaximumFilter,SolarizeFilter
  }

  def apply (filterEnum: FilterEnum): Filter = {
    getFilter(filterEnum)
  }
  def getFilter(filterEnum: FilterEnum = FilterEnum.DiffuseFilter): Filter = {
    filterEnum match {
      case FilterEnum.BlackThresholdFilter => new BlackThresholdFilter(35)
      case FilterEnum.BorderFilter => new BorderFilter(8)
      case FilterEnum.BrightnessFilter => new BrightnessFilter(1.3f)
      case FilterEnum.ChromeFilter => new ChromeFilter(0.5f, 1.0f)
      case FilterEnum.ColorHalftoneFilter => new ColorHalftoneFilter(1.2f)
      case FilterEnum.ColorizeFilter => new ColorizeFilter(RGBColor(255, 0, 0, 50))
      case FilterEnum.ContourFilter => new ContourFilter(3)
      case FilterEnum.ContrastFilter => new ContrastFilter(1.3f)
      case FilterEnum.CrystallizeFilter => new CrystallizeFilter(16, 0.4, 0xff000000, 0.2)
      case FilterEnum.DiffuseFilter => new DiffuseFilter(4)
      case FilterEnum.ErrorDiffusionHalftoneFilter => new ErrorDiffusionHalftoneFilter(127)
      case FilterEnum.GainBiasFilter => new GainBiasFilter(0.5f, 0.5f)
      case FilterEnum.GammaFilter => new GammaFilter(2)
      case FilterEnum.GaussianBlurFilter => new GaussianBlurFilter(2)
      case FilterEnum.GlowFilter => new GlowFilter(0.5f)
      case FilterEnum.HSBFilter => new HSBFilter(0.5f, 0, 0)
      case FilterEnum.LensBlurFilter => new LensBlurFilter(5, 2, 255, 5, 5)
      case FilterEnum.MotionBlurFilter => new MotionBlurFilter(Math.PI / 3.0, 20, 0, 0)
      case FilterEnum.NoiseFilter => new NoiseFilter(25, 1)
      case FilterEnum.OffsetFilter => new OffsetFilter(60, 40)
      case FilterEnum.OilFilter => new OilFilter(3, 256)
      case FilterEnum.OpacityFilter => new OpacityFilter(0.5f)
      case FilterEnum.PixelateFilter => new PixelateFilter(4)
      case FilterEnum.PointillizeFilter => new PointillizeFilter(0.0f, 6, 0.4f, 0xff000000, false, 0.1f, PointillizeGridType.Square)
      case FilterEnum.PosterizeFilter => new PosterizeFilter(8f, 8f, 8f, 8, 8f)
      case FilterEnum.QuantizeFilter => new QuantizeFilter(256, false)
      case FilterEnum.RaysFilter => new RaysFilter(0.1f, 0.6f, 0.5f)
      case FilterEnum.RGBFilter => new RGBFilter(0.4f, 0.6f, 0.5f)
      case FilterEnum.RippleFilter => new RippleFilter(RippleType.Sine, 2f, 2f, 6f, 6f)
      case FilterEnum.SmearFilter => new SmearFilter(SmearType.Circles, 0, 0.3f, 0, 3, 0.4f)
      case FilterEnum.SparkleFilter => new SparkleFilter(0, 0, 50, 25, 50)
      case FilterEnum.SwimFilter => new SwimFilter(6f, 2f)
      case FilterEnum.ThresholdFilter => new ThresholdFilter(127, 0xffffff, 0x000000)
      case FilterEnum.TritoneFilter => new TritoneFilter(RGBColor(255, 0, 0, 50).toARGBInt, RGBColor(0, 255, 0, 50).toARGBInt, RGBColor(0, 0, 0, 50).toARGBInt)
      case FilterEnum.TwirlFilter => new TwirlFilter((Math.PI / 1.5), 75, 0.5f, 0.5f)
      case FilterEnum.UnsharpFilter => new UnsharpFilter(0.5f, 1, ZeroEdges)
      case FilterEnum.VignetteFilter => new VignetteFilter(0.85f, 0.95f, 0.3f, Color.BLACK)
      case _ => null
    }
  }
}
