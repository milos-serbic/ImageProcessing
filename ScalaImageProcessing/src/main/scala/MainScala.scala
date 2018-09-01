import com.sksamuel.scrimage.filter.ChromeFilter
import dslScala.FilterDSL.FilterEnum
import dslScala.ImageDSLScala
import utilitiesScala.UtilitiesScala._
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.Color
//import com.sksamuel.scrimage.Color
//import FilterDSL.FilterEnum
//import com.sksamuel.scrimage.Image
object MainScala extends App {

var flower = ImageDSLScala(format=png) importFrom "flower.jpg" scaleToHeight 400 exportTo "flower.png"
var palm = ImageDSLScala(png) importFrom "palm.jpg" scaleTo(7.9375 cm, 7.9375 cm) exportTo "palm.png"
var tulip = ImageDSLScala(format=jpg, compression=5 pct) importFrom "tulip.jpg" scaleToHeight 400 exportTo "tulip-5pct.jpg"
var tulip2 = ImageDSLScala(format=jpg, compression=85 pct) importFrom "tulip.jpg" scaleToHeight 400 exportTo "tulip-85pct.jpg"
var see = ImageDSLScala(format=png, compression=78 pct) importFrom "see.jpg" scaleTo(height=400 px, width=600 px) exportTo "see.png"
var rock = ImageDSLScala(png) importFrom "rock.jpg" scaleTo(600, 400) exportTo "rock.png"
var run = ImageDSLScala(png) importFrom "run.jpg" exportTo "run.png"

//(1)
var night = ImageDSLScala(png) importFrom "night.jpg" exportTo "night.png"
//(2)
night = ImageDSLScala(png)
night.processing(() => {
  night importFrom "night.jpg"
  night scaleToHeight 800
  night filterWith FilterEnum.GlowFilter
  night rotate right
  night exportTo "nightLambdaBlock.png"
})
night = ImageDSLScala(png) importFrom "night.jpg"

//transformations
flower rotate left exportTo "flowerRotateLeft.png"
palm rotate right exportTo "palmRotateRight.png"
tulip flip horizontally exportTo "tulipFlipHorizontally.png"
see flip vertically exportTo "seeFlipVertically.png"

flower = ImageDSLScala(format=png) importFrom "flower.jpg"
flower translateBy(x=50 px, y=50 px) background Color.White exportTo "flowerTranslate.png"
flower autocrop(background=white) exportTo "flowerAutocrop.png"
tulip translate() horizontally 100 vertically 40 background Color.Black exportTo "tulipTranslate.png"
tulip autocrop(background=black) exportTo "tulipAutocrop.png"

run cropBy(2 scaleFactor) exportTo "runCrop.png"
run cropBy(4 inverseScaleFactor) exportTo "runCropInv.png"
run = ImageDSLScala(png) importFrom "run.jpg"
run trim(300 px) exportTo "runTrimAll.png"
run = ImageDSLScala(png) importFrom "run.jpg"
run fit(400 px, 300 px, Color.White) exportTo "runFit.png"
flower trim(left=555 px, top=0 px, right=155 px, bottom=0 px) exportTo "flowerTrimLR.png"

//effects
see + rock exportTo "seeRock.png"
palm add (rock, compositType=alpha, compositPct=40 pct) exportTo "palmRock.png"

run = ImageDSLScala(png) importFrom "run.jpg"
run filterWith FilterEnum.TritoneFilter exportTo "runFilterTritone.png"
run = ImageDSLScala(png) importFrom "run.jpg"
run filterWith FilterEnum.PixelateFilter exportTo "runFilterPixelate.png"
run = ImageDSLScala(png) importFrom "run.jpg"
run filterWith FilterEnum.NoiseFilter exportTo "runFilterNoise.png"
night filterWith FilterEnum.GlowFilter exportTo "nightFilterGlow.png"

//statistics
println(s"Min pixel: ${night minPixel}")
println(s"Max pixel: ${night maxPixel}")
println(s"Average pixel: ${night averagePixel}")

//wrapper
implicit def wrapperingToDSL(image: Image) = ImageDSLScala(image)
var imageWrapped = Image.fromResource("/flower.jpg")
imageWrapped = imageWrapped.rotateLeft().getImageObj
imageWrapped = imageWrapped.filterWith(FilterEnum.GlowFilter).getImageObj
imageWrapped.exportTo("flowerWrapped.png")

//higher-order functions
run = ImageDSLScala(png) importFrom "run.jpg"
var amount = 0.2f
var exposure = 0.3f
var counter = 0
5 times {
  counter+=1
  run filterWith(() => new ChromeFilter(amount + 0.5f, exposure + 1.0f)) exportTo s"runFilterAdjust${counter}.png"
}

}
