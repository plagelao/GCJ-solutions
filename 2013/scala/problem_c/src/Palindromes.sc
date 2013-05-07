object Palindromes {

  abstract class Palindrome {
    def succ: Palindrome
    def init: Palindrome
    def isLast: Boolean
    def toLong: Long = toString.toLong
    def isSquareFair: Boolean = {
      val s = (this.toLong * this.toLong).toString
      s == s.reverse
    }
  }

  case class Single(val digit: Int) extends Palindrome {

    def succ: Palindrome =
      if (this.isLast) new Double(0)
      else new Single(digit + 1)

    def init: Palindrome = new Single(0)
    def isLast: Boolean = digit == 9
    override def toString: String = digit.toString
  }

  case class Double(val digit: Int) extends Palindrome {

    def succ: Palindrome =
      if (this.isLast) new General(0, new Single(0))
      else new Double(digit + 1)

    def init: Palindrome = new Double(0)
    def isLast: Boolean = digit == 9
    override def toString: String = digit.toString + digit.toString
  }

  case class General(val digit: Int, val p: Palindrome) extends Palindrome {

    def succ: Palindrome =
      if (this.isLast) new General(0, p.succ)
      else if (p.isLast) new General(digit + 1, p.init)
      else new General(digit, p.succ)

    def init: Palindrome = new General(0, p.init)
    def isLast: Boolean = digit == 9 && p.isLast
    override def toString: String = digit.toString + p.toString + digit.toString
  }

  object Pal {
    def apply(n: Int): Palindrome = {
      def f(number: String): Palindrome =
        if (number.length < 2) new Single(number.toInt)
        else if (number.length < 3) new Double(number.tail.toInt)
        else {
          val digit = number.head.toString.toInt
          val rest = number.tail.reverse.tail
          new General(digit, f(rest))
        }
      f(n.toString)
    }
  }

  def casesIn(start: Long, end: Long) = {
    val s = scala.math.ceil(scala.math.sqrt(start)).toLong
    val e = scala.math.floor(scala.math.sqrt(end)).toLong
    def fairAndSquare(p: Palindrome): List[Palindrome] =
      if (p.toLong > e) List()
      else if (p.isSquareFair && p.toLong >= s) p :: fairAndSquare(p.succ)
      else fairAndSquare(p.succ)

    val p = if (s.toString.length % 2 == 0) {
      val left = s.toString.take(s.toString.length / 2)
      left + left.reverse
    } else {
      val left = s.toString.take(1 + s.toString.length / 2)
      left + left.take(left.length - 1).reverse
    }
    fairAndSquare(Pal(p.toInt)).filter(x => x.toString.head != '0').distinct
  }

  def solutionsFor(file: String) = {
    val lines = scala.io.Source.fromFile(file).getLines().toList
    val numberOfTests = lines.head
    val s = (lines.tail map (test => test.split(" ").toList)) map { case List(start, end) => casesIn(start.toLong, end.toLong) } map (xs => xs.length)
    val solution = (s zip (1 to s.length)) map { case (s, n) => "Case #" + n + ": " + s } mkString "\n"
    val writer = new java.io.PrintWriter(new java.io.File(file.split("\\.").head + ".out"))
    writer.write(solution)
    writer.close()
  }

  def printAll(p: Palindrome, end: Long): List[Palindrome] =
    if (p.toLong > end) List()
    else p :: printAll(p.succ, end)


  def fair(x: Long) = x.toString == x.toString.reverse

  def squared(x: Long) = {
    val root = scala.math.sqrt(x)
    scala.math.abs(root.toInt - root) < 0.00001 && fair(root.toInt)
  }

  def casesInSlow(start: Long, end: Long) = {
    val s = scala.math.ceil(scala.math.sqrt(start)).toInt
    val e = scala.math.floor(scala.math.sqrt(end)).toInt
    def f(n: Long): List[Long] = if (n > e) List()
    else if (fair(n) && fair(n*n)) n :: f(n + 1)
    else f(n + 1)
    f(s)
  }


  solutionsFor("test.in")
}
