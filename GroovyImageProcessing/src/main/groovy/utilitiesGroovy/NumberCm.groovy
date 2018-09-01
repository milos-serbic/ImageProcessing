package utilitiesGroovy

trait NumberCm {
    static Integer getCm(Double self){
        new BigDecimal(self * 37.795275590551).setScale(1, BigDecimal.ROUND_HALF_UP).intValue()
    }
    static Integer getCm(Integer self){
        new BigDecimal(self * 37.795275590551).setScale(1, BigDecimal.ROUND_HALF_UP).intValue()
    }
}
