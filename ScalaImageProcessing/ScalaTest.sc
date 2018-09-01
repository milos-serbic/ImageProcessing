import dslScala._
import utilitiesScala.UtilitiesScala._

var dsl1 = ImageDSLScala(png, compression=61 pct) importFrom "lanzarote.jpg" scaleToWidth 800 //exportTo "lanzarote.png"
var dsl2 = ImageDSLScala(format=png, compression=48 pct) importFrom "vossen.jpg" scaleTo(600 px, 400 px) //exportTo "vossen.png"

dsl1 + dsl2 exportTo "exportAlpha1.png"

//println("test")