import dslJava.ImageDSLJava;
import static dslJava.ImageDSLJava.imageDSLJava;
import static utilitiesGroovy.UtilitiesGroovy.*;
import static java.lang.System.*;

public class MainJava {

    public static void main(String[] args) {
        String jpg="jpg", jpeg="jpeg", png="png", gif="gif";
        String left="left", right="right";
        String horizontally="horizontally", vertically="vertically";
        String alpha="alpha";
        String black="black",white="white";

        ImageDSLJava flower = imageDSLJava(png).importFrom("flower.jpg").scaleToHeight(400).exportTo("flower.png");
        ImageDSLJava palm = imageDSLJava(png).importFrom("palm.jpg").scaleTo(300, 300).exportTo("palm.png");
        ImageDSLJava tulip = imageDSLJava(jpg, 5).importFrom("tulip.jpg").scaleToHeight(400).exportTo("tulip-5pct.jpg");
        ImageDSLJava tulip2 = imageDSLJava(jpg, 85).importFrom("tulip.jpg").scaleToHeight(400).exportTo("tulip-85pct.jpg");
        ImageDSLJava see = imageDSLJava(png, 78).importFrom("see.jpg").scaleTo(600, 400).exportTo("see.png");
        ImageDSLJava rock = imageDSLJava(png).importFrom("rock.jpg").scaleTo(600,400).exportTo("rock.png");

        //(1)
        ImageDSLJava run = imageDSLJava(png).importFrom("run.jpg").exportTo("run.png");
        //
        ImageDSLJava night = imageDSLJava(png);
        //(2)
        night.importFrom("night.jpg")
                .scaleToHeight(800)
                .filterWith(FilterEnum.GlowFilter)
                .rotate(right)
                .exportTo("nightBlock.png");
        //(3)
        night.processing(img -> {
            img.importFrom("night.jpg");
            img.scaleToHeight(800);
            img.filterWith(FilterEnum.GlowFilter);
            img.rotate(right);
            img.exportTo("nightLambdaBlock.png");
        });
        night = imageDSLJava(png).importFrom("night.jpg").exportTo("night.png");

        //transformations
        flower.rotate(left).exportTo("flowerRotateLeft.png");
        palm.rotate(right).exportTo("palmRotateRight.png");
        tulip.flip(horizontally).exportTo("tulipFlipHorizontally.png");
        see.flip(vertically).exportTo("seeFlipVertically.png");

        flower = imageDSLJava(png).importFrom("flower.jpg");
        flower.translate(50, 50, white)
              .exportTo("flowerTranslate.png");
        flower.autocrop(white).exportTo("flowerAutocrop.png");

        run.cropBy(2.0).exportTo("runCrop.png");
        run.cropBy(0.25).exportTo("runCropInv.png");
        run = imageDSLJava(png).importFrom("run.jpg");
        run.trim(300).exportTo("runTrimAll.png");
        run = imageDSLJava(png).importFrom("run.jpg");
        run.fit(400, 300, white).exportTo("runFit.png");
        flower.trim(555, 0, 155, 0)
              .exportTo("flowerTrimLR.png");

        //effects
        see.add(rock).exportTo("seeRock.png");
        palm.composing(rock, alpha, 40)
            .exportTo("palmRock.png");

        run = imageDSLJava(png).importFrom("run.jpg");
        run.filterWith(FilterEnum.TritoneFilter).exportTo("runFilterTritone.png");
        run = imageDSLJava(png).importFrom("run.jpg");
        run.filterWith(FilterEnum.PixelateFilter).exportTo("runFilterPixelate.png");
        run = imageDSLJava(png).importFrom("run.jpg");
        run.filterWith(FilterEnum.NoiseFilter).exportTo("runFilterNoise.png");
        night.filterWith(FilterEnum.GlowFilter).exportTo("nightFilterGlow.png");

        //statistics
        out.println("Min pixel: " + night.minPixel());
        out.println("Max pixel: " + night.maxPixel());
        out.println("Average pixel: " + night.averagePixel());
    }
}
