package utilitiesGroovy

class DoubleCategory extends IntegerCategory implements NumberCm, PrintValue {

    static Double getScaleFactor(Double self){
        1/self.toDouble()
    }
}
