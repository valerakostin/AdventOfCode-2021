fun main() {

    val literalCode = 4

    class Packet(val version: Int, val typeId: Int) {
        val children = mutableListOf<Packet>()
        private var value: Long? = null
        private var limit = Integer.MAX_VALUE
        var parent: Packet? = null


        fun addChild(p: Packet): Boolean {
            children.add(p)
            p.parent = this
            return limit == children.size
        }

        fun getParentPacket(): Packet {
            var p = parent
            while (p != null && p.isFull())
                p = p.parent
            return p!!
        }

        fun setLimit(limit: Int) {
            this.limit = limit
        }

        fun isFull(): Boolean {
            return children.size >= limit
        }

        fun setValue(value: Long) {
            this.value = value
        }

        fun versionSum(): Int {
            var sum = version
            for (c in children) {
                sum += c.versionSum()
            }
            return sum
        }

        fun getValue(): Long {
            return when (typeId) {
                4 -> value!!
                0 -> children.sumOf { it.getValue() }
                1 -> children.map { it.getValue() }.reduce { acc, i -> acc * i }
                2 -> children.minOf { it.getValue() }
                3 -> children.maxOf { it.getValue() }
                5 -> if (children[0].getValue() > children[1].getValue()) 1 else 0
                6 -> if (children[0].getValue() < children[1].getValue()) 1 else 0
                7 -> if (children[0].getValue() == children[1].getValue()) 1 else 0
                else -> 0
            }
        }

        override fun toString(): String {
            return "ver: $version type: $typeId  children: $children"
        }
    }

    val binaryMap = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    fun convertToBinaryString(input: String): String {
        return buildString {
            for (ch in input) {
                append(binaryMap.getOrDefault(ch, ""))
            }
        }
    }

    fun parsePacket(pPacket: Packet, counter: Int, bs: String) {
        if (bs.length - counter < 11)
            return

        var offset = counter
        val version = bs.substring(offset..offset + 2).toInt(2)
        offset += 3
        val typeId = bs.substring(offset..offset + 2).toInt(2)
        offset += 3

        val parent = if (pPacket.isFull()) pPacket.getParentPacket() else pPacket
        val child = Packet(version, typeId)

        if (typeId == literalCode) {
            val s = buildString {
                for (i in offset until bs.length step 5) {
                    val digit = bs.substring(i + 1..i + 4)
                    append(digit)
                    offset += 5
                    if (bs[i] == '0')
                        break
                }
            }
            val literal = s.toLong(2)
            child.setValue(literal)
            parent.addChild(child)
            parsePacket(pPacket, offset, bs)
        } else {

            if (bs[offset++] == '0') {
                val lengthStr = bs.substring(offset..offset + 14)
                val length = lengthStr.toInt(2)
                offset += 15
                val str = bs.substring(offset, offset + length)
                offset += length

                parsePacket(child, 0, str)
                parent.addChild(child)
                parsePacket(parent, offset, bs)

            } else {
                val lengthStr = bs.substring(offset..offset + 10)
                val lm = lengthStr.toInt(2)
                offset += 11
                child.setLimit(lm)
                parent.addChild(child)
                parsePacket(child, offset, bs)
            }
        }
    }

    fun part1(input: String): Int {
        val pp = Packet(0, 0)
        val bs = convertToBinaryString(input)
        parsePacket(pp, 0, bs)
        val p = pp.children[0]
        return p.versionSum()
    }

    fun part2(input: String): Long {
        val pp = Packet(0, 0)
        val bs = convertToBinaryString(input)
        parsePacket(pp, 0, bs)
        val p = pp.children[0]
        return p.getValue()
    }

    check(part1("D2FE28") == 6)
    check(part1("38006F45291200") == 9)
    check(part1("EE00D40C823060") == 14)
    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    val input = readInput("Day16")
    println(part1(input[0]))
    println(part2(input[0]))
}
