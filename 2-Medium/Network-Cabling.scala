object Solution extends App {
  val n = readInt
  case class Pos(x:Double,y:Double)
  val data = Seq.fill(n)(readLine.split(" ")).map(item => Pos(item(0).toDouble, item(1).toDouble))
  val startLine = data.minBy(_.x).x
  val endLine = data.maxBy(_.x).x
  val minLinePosY = data.minBy(_.y).y
  var linePosY = data.sortBy(pos => math.abs(pos.y - minLinePosY)).toIndexedSeq((data.size/2).toInt).y

  def totalLenght = data.map(pos => math.abs(pos.y - linePosY)).sum + math.abs(endLine - startLine)
  println(totalLenght.toLong)
}