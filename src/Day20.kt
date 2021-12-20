fun main() {


    class Image(val decode: String, val pixels: MutableSet<Point>) {

        fun getPixel(x: Int, y: Int): Char {

            val points = listOf(
                Point(x - 1, y - 1),
                Point(x, y - 1),
                Point(x + 1, y - 1),

                Point(x - 1, y),
                Point(x, y),
                Point(x + 1, y),

                Point(x - 1, y + 1),
                Point(x, y + 1),
                Point(x + 1, y + 1)
            )


            val number = buildString()
            {
                for (p in points) {
                    if (p in pixels)
                        append('1')
                    else
                        append('0')
                }
            }

            val n = number.toInt(2)
            return decode[n]
        }

        fun getCount() = pixels.count()

        fun getNextImage(step: Int): Image {


            val offset = 1
            val xMin = pixels.minOf { it.x } - offset
            val xMax = pixels.maxOf { it.x } + offset

            val yMin = pixels.minOf { it.y } - offset
            val yMax = pixels.maxOf { it.y } + offset

            val output = mutableSetOf<Point>()


            if (decode[0] == '#' && decode.last() == '.' && step % 2 == 0) {

                for (x in (xMin - 1)..(xMax + 1)) {
                    pixels.add(Point(x, yMin - 1))
                    pixels.add(Point(x, yMin))
                    pixels.add(Point(x, yMax))
                    pixels.add(Point(x, yMax + 1))
                }

                for (y in (yMin - 1)..(yMax + 1)) {
                    pixels.add(Point(xMin, y))
                    pixels.add(Point(xMin - 1, y))
                    pixels.add(Point(xMax, y))
                    pixels.add(Point(xMax + 1, y))
                }
            }

            //   printImage()
            for (x in xMin..xMax) {
                for (y in yMin..yMax) {
                    val p = Point(x, y)
                    val v = getPixel(x, y)
                    if (v == '#') {
                        output.add(p)
                    }
                }
            }

            return Image(decode, output)
        }

//        fun printImage() {
//            val xMin = pixels.minOf { it.x }
//            val xMax = pixels.maxOf { it.x }
//
//            val yMin = pixels.minOf { it.y }
//            val yMax = pixels.maxOf { it.y }
//
//            for (x in xMin..xMax) {
//                for (y in yMin - 1..yMax + 1) {
//                    val p = Point(y, x)
//                    if (p in pixels)
//                        print('#')
//                    else
//                        print(".")
//                }
//                println()
//            }
//            println()
//        }
    }


    fun computeImage(input: Image, step: Int): Int {
        var img = input
        repeat(step) {
            img = img.getNextImage(it + 1)
        }
        return img.getCount()
    }

    fun part1(image: Image): Int {
        return computeImage(image, 2)
    }

    fun part2(image: Image): Int {
        return computeImage(image, 50)
    }


    fun parseImage(input: List<String>): Image {
        val line = input[0]
        val pixels = mutableSetOf<Point>()
        val row = input.size
        val column = input[2].length
        for (r in 2 until row) {
            val l = input[r]
            for (c in 0 until column) {
                if (l[c] == '#')
                    pixels.add(Point(c, r - 2))
            }
        }
        return Image(line, pixels)
    }

    val testInput = readInput("Day20_test")
    check(part1(parseImage(testInput)) == 35)
    val input = readInput("Day20")
    println(part1(parseImage(input)))
    check(part2(parseImage(testInput)) == 3351)
    println(part2(parseImage(input)))
}
