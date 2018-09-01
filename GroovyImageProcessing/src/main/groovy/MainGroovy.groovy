import com.sksamuel.scrimage.filter.*
import utilitiesGroovy.DoubleCategory
import utilitiesGroovy.IntegerCategory
import static dslGroovy.ImageDSLGroovy.*
import static utilitiesGroovy.UtilitiesGroovy.*

class MainGroovy {

static void main(String[] args) {

Integer.metaClass.mixin IntegerCategory
Double.metaClass.mixin DoubleCategory

def flower = imageDSLGroovy format:png importing from:"flower.jpg" scale height: 400.px exporting to:"flower.png"
def palm = imageDSLGroovy format:png importing from:"palm.jpg" scale height: 7.9375d.cm, width:7.9375d.cm exporting to:"palm.png"
def tulip = imageDSLGroovy format:jpg, compression:5.pct importing from:"tulip.jpg" scale height:400.px exporting to:"tulip-5pct.jpg"
def tulip2 = imageDSLGroovy format:jpg, compression:85.pct importing from:"tulip.jpg" scale height:400.px exporting to:"tulip-85pct.jpg"
def see = imageDSLGroovy format:png, compression:78.pct importing from:"see.jpg" scale height:400.px, width:600.px exporting to:"see.png"
def rock = imageDSLGroovy format:png importing from:"rock.jpg" scale width:600, height:400 exporting to:"rock.png"
def run = imageDSLGroovy format:png importing from:"run.jpg" exporting to:"run.png"
def night = imageDSLGroovy format:png
//use(IntegerCategory)
//{
night.processing {
    importing from:"night.jpg"
    scale height:800.px
    filterWith FilterEnum.GlowFilter
    rotiraj desno
    exporting to:"nightClosure.png"
}
//}
night = imageDSLGroovy format:png importing from:"night.jpg" exporting to:"night.png"

//transformations
flower.rotate left exporting to:"flowerRotateLeft.png"
palm.rotate right exporting to:"palmRotateRight.png"
tulip.flip horizontally exporting to:"tulipFlipHorizontally.png"
see.flip vertically exporting to:"seeFlipVertically.png"

flower = imageDSLGroovy format:png importing from:"flower.jpg"
flower.translate {
    x = 50.px
    y = 50.px
    background color:white
    exporting to:"flowerTranslate.jpg"
}
flower.autocrop background:white exporting to:"flowerAutocrop.png"

run.cropBy 2.scaleFactor exporting to:"runCrop.png"
run.cropBy 4.3d.inverseScaleFactor exporting to:"runCropInv.png"
run = imageDSLGroovy format:png importing from:"run.jpg"
run.trim 300.px exporting to:"runTrimAll.png"
run = imageDSLGroovy format:png importing from:"run.jpg"
run.fit width:400.px, height:300.px, background:white exporting to:"runFit.png"
flower.trim {
    top = 0.px
    bottom = 0.px
    leftSide = 555.px
    rightSide = 155.px
    trimming
    exporting to:"flowerTrimLR.png"
}

//effects
(see + rock).exporting to:"seeRock.png"
palm.add {
    imageForCompostion = rock
    compositType = alpha
    compositPct = 40.pct
    adding
    exporting to:"palmRock.png"
}

run = imageDSLGroovy format:png importing from:"run.jpg"
run.filterWith FilterEnum.TritoneFilter exporting to:"runFilterTritone.png"
run = imageDSLGroovy format:png importing from:"run.jpg"
run.filterWith FilterEnum.PixelateFilter exporting to:"runFilterPixelate.png"
run = imageDSLGroovy format:png importing from:"run.jpg"
run.filterWith FilterEnum.NoiseFilter exporting to:"runFilterNoise.png"
night.filterWith FilterEnum.GlowFilter exporting to:"nightFilterGlow.png"

//statistics
println("Min pixel: ${night.minPixel()}")
println("Max pixel: ${night.maxPixel()}")
println("Average pixel: ${night.averagePixel()}")

//higher-order functions
run = imageDSLGroovy format:png importing from:"run.jpg"
def amount = 0.2
def exposure = 0.3
def counter = 0
5.times {
    counter+=1
    run.filtering(new ChromeFilter(amount + 0.5, exposure + 1.0)) exporting to:"runFilterAdjust${counter}.png"
}

 }
}