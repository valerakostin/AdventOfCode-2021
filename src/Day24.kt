fun main() {

    fun compute(number: String): Long {
        var z = 0L
        if (number.isNotEmpty()) {
            z = number[0].digitToInt() + 2L
        }
        if (number.length > 1) {
            z = z * 26 + 16 + number[1].digitToInt()
        }
        if (number.length > 2) {
            z = z * 26 + 9 + number[2].digitToInt()
        }
        if (number.length > 3) {
            z = z * 26 + number[3].digitToInt()
        }
        if (number.length > 4) {
            val x = (z % 26) - 8
            var zl = z / 26
            if (x == number[4].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[4].digitToInt().toLong() + 1)
            }
        }
        if (number.length > 5) {
            z = z * 26 + 12 + number[5].digitToInt()
        }

        if (number.length > 6) {
            val x = (z % 26) - 16
            var zl = z / 26
            if (x == number[6].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[6].digitToInt().toLong() + 6)
            }
        }
        if (number.length > 7) {
            val x = (z % 26) - 4
            var zl = z / 26
            if (x == number[7].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[7].digitToInt().toLong() + 6)
            }
        }

        if (number.length > 8) {
            z = z * 26 + 3 + number[8].digitToInt()
        }

        if (number.length > 9) {
            val x = (z % 26) - 3
            var zl = z / 26
            if (x == number[9].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[9].digitToInt().toLong() + 5)
            }
        }
        if (number.length > 10) {
            z = z * 26 + 9 + number[10].digitToInt()
        }

        if (number.length > 11) {
            val x = (z % 26) - 7
            var zl = z / 26
            if (x == number[11].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[11].digitToInt().toLong() + 3)
            }
        }

        if (number.length > 12) {
            val x = (z % 26) - 15
            var zl = z / 26
            if (x == number[12].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[12].digitToInt().toLong() + 2)
            }
        }

        if (number.length == 14) {
            val x = (z % 26) - 7
            var zl = z / 26
            if (x == number[13].digitToInt().toLong()) {
                z /= 26
            } else {
                zl *= 26
                z = zl + (number[13].digitToInt().toLong() + 3)
            }
        }

        return z
    }


    fun createNextCandidateSet(input: MutableSet<Long>): MutableSet<Long> {
        val output = mutableSetOf<Long>()
        for (i in 1..9) {
            for (c in input) {
                output.add((c.toString() + i.toString()).toLong())
            }
        }
        input.clear()
        return output
    }

    fun createNextValidNumberSet(input: MutableSet<Long>, transform: (Long) -> Long): MutableSet<Long> {
        val output = mutableSetOf<Long>()
        for (c in input) {
            val z = compute(c.toString())
            val x = transform(z)
            if (x in 1..9)
                output.add((c.toString() + x.toString()).toLong())
        }
        input.clear()
        return output
    }

    fun computeValidNumbers(): Collection<Long> {

        val first4 = mutableSetOf<Long>()
        for (candidate in 1111..9999) { //first 4 digit
            if (candidate.toString().indexOf("0") == -1) {
                first4.add(candidate.toLong())
            }
        }

        val first5 = createNextValidNumberSet(first4) { (it % 26) - 8 }
        val first6 = createNextCandidateSet(first5)
        val first7 = createNextValidNumberSet(first6) { (it % 26) - 16 }
        val first8 = createNextValidNumberSet(first7) { (it % 26) - 4 }
        val first9 = createNextCandidateSet(first8)
        val first10 = createNextValidNumberSet(first9) { (it % 26) - 3 }
        val first11 = createNextCandidateSet(first10)
        val first12 = createNextValidNumberSet(first11) { (it % 26) - 7 }
        val first13 = createNextValidNumberSet(first12) { (it % 26) - 15 }
        return createNextValidNumberSet(first13) { (it % 26) - 7 }
    }


    fun part1(): Long {
        return computeValidNumbers().maxOf { it }
    }

    fun part2(): Long {
        return computeValidNumbers().minOf { it }
    }

    println(part1())
    println(part2())
}
