package utilitiesGroovy

class IntegerCategory implements NumberCm {

    static Integer getPx(Integer self){
        self
    }
    static Integer getPct(Integer self){
        self
    }
    static Integer getScaleFactor(Integer self){
        self
    }
    static Double getInverseScaleFactor(Integer self){
        1/self.toDouble()
    }
    static Double getInverseScaleFactor(Double self){
        1/self.toDouble()
    }

    static getTimes(Integer self, closure) {
        closure()
        if (self > 1)
            getTimes(self-1, closure)
    }
}
