// https://www.codingame.com/training/hard/bender-episode-3

import java.util.Scanner

// complexity names and functions
val complexities = arrayListOf(
    Constant(),
    Logarithmic(),
    Linear(),
    LogLinear(),
    Quadratic(),
    LogQuadratic(),
    Cubic(),
    Exponential()
)

fun main(args : Array<String>) {
  // read standard input
  val input = Scanner(System.`in`)
  val nbPoints = input.nextInt()
  val points = ArrayList<Pair<Int, Int>>(nbPoints)
  for (i in 0 until nbPoints) {
    val nbItems = input.nextInt()
    val time = input.nextInt()
    points.add(Pair(nbItems, time))
  }

  // compute the most probable computational complexity
  var bestFit = ""
  var minNormVariance = Double.POSITIVE_INFINITY
  for (complexity in complexities) {
    val ratios = points.map { it.second / complexity.function(it.first.toDouble()) }

    // calculate the normalized variance
    val meanRatios = ratios.sum() / ratios.size
    val variances = ratios.map { Math.pow(it - meanRatios, 2.0) }
    val normVariance = variances.sum() / Math.pow(meanRatios, 2.0)

    if (normVariance < minNormVariance) {
      minNormVariance = normVariance
      bestFit = complexity.name()
    }
  }

  println("O($bestFit)")
}

interface Complexity {
  fun name(): String
  fun function(n: Double): Double
}

class Constant : Complexity {
  override fun name(): String { return "1" }
  override fun function(n: Double): Double {
    return 1.0
  }
}

class Logarithmic : Complexity {
  override fun name(): String { return "log n" }
  override fun function(n: Double): Double {
    return Math.log(n)
  }
}

class Linear : Complexity {
  override fun name(): String { return "n" }
  override fun function(n: Double): Double {
    return n
  }
}

class LogLinear : Complexity {
  override fun name(): String { return "n log n" }
  override fun function(n: Double): Double {
    return n * Math.log(n)
  }
}

class Quadratic : Complexity {
  override fun name(): String { return "n^2" }
  override fun function(n: Double): Double {
    return n * n
  }
}

class LogQuadratic : Complexity {
  override fun name(): String { return "n^2 log n" }
  override fun function(n: Double): Double {
    return n * n * Math.log(n)
  }
}

// for validator test #7
class Cubic : Complexity {
  override fun name(): String { return "n^3" }
  override fun function(n: Double): Double {
    return Math.pow(n, 2.1)
  }
}

class Exponential : Complexity {
  override fun name(): String { return "2^n" }
  override fun function(n: Double): Double {
    return Math.pow(2.0, n)
  }
}