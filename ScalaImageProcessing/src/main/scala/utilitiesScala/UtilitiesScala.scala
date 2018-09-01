package utilitiesScala

import java.math.BigDecimal

object UtilitiesScala {

  abstract class NumberCm(syntax: Double) {
    def cm: Int = new BigDecimal(syntax * 37.795275590551).setScale(1, BigDecimal.ROUND_HALF_UP).intValue()
  }
  trait NumberSyntax {
    type T
    def scaleFactor: T
    var number: Any = 0
    def printValue = println("Value: " + number)
  }

  type SyntaxInt = Int
  class IntegerSyntax (syntax: SyntaxInt) extends NumberCm(syntax: SyntaxInt) with NumberSyntax {
    type T = SyntaxInt
    number = syntax
    def pct = syntax
    def px = syntax
    def scaleFactor = syntax
    def inverseScaleFactor = 1/syntax.toDouble

    def times(operation: => Unit): Unit = {
      def times(syntax: Int, operation: => Unit): Unit = {
        operation
        if (syntax > 1)
          times(syntax-1, operation)
      }
      times(syntax, operation)
    }
  }
  implicit def IntSyntax(syntax: SyntaxInt) = new IntegerSyntax(syntax)

  type SyntaxDouble = Double
  class DoubleSyntax(syntax: SyntaxDouble) extends NumberCm(syntax: SyntaxDouble) with NumberSyntax {
    type T = SyntaxDouble
    number = syntax
    def scaleFactor = syntax
  }
  implicit def DoubleSyntaxHelper(syntax: SyntaxDouble) = new DoubleSyntax(syntax)

  sealed abstract class LeftOrRight(val label: String)
  case object left extends LeftOrRight("left")
  case object right extends LeftOrRight("right")

  sealed abstract class HorizontallyOrVertically(val label: String)
  case object horizontally extends HorizontallyOrVertically("horizontally")
  case object vertically extends HorizontallyOrVertically("vertically")

  sealed abstract class Format(val label: String)
  case object png extends Format("png")
  case object jpg extends Format("jpg")
  case object jpeg extends Format("jpeg")
  case object gif extends Format("gif")

  sealed abstract class CompositType(val label: String)
  case object alpha extends CompositType("alpha")

  sealed abstract class Background(val label: String)
  case object white extends Background("white")
  case object black extends Background("black")
}
