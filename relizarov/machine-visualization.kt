import java.util.Scanner
import java.util.ArrayList
import java.util.ArrayDeque
import kotlin.math.max

interface Figure {
    fun render(): List<String>
    val width: Int
    val height: Int
}

class FigureName(var name: String) : Figure {
    override var width: Int = 0
    override var height: Int = 0
    var lines: List<String> = emptyList()

    fun push(char: Char) {
        name += char
    }

    override fun render(): List<String> {
        val border = "+-" + "-".repeat(name.length) + "-+"
        width = border.length
        height = 3
        lines = listOf(border, "+ $name +", border)
        return lines
    }
}

class FigureQuestionMark(val figure: Figure) : Figure {
    override var width: Int = 0
    override var height: Int = 0

    override fun render(): List<String> {
        val lines = figure.render()
        height = figure.height + 3
        width = figure.width + 6
        val result = ArrayList<String>()
        result.add(" ".repeat(width))
        result.add("+" + "-".repeat(width - 3) + ">+")
        result.add("|" + " ".repeat(width - 3) + " |")
        result.add("|  " + lines[0] + "  |")
        result.add("+->" + lines[1] + "->+")
        for (i in 2 until lines.size) {
            result.add("   " + lines[i] + "   ")
        }
        return result
    }
}

class FigurePlusSign(val figure: Figure) : Figure {
    override var width: Int = 0
    override var height: Int = 0

    override fun render(): List<String> {
        val lines = figure.render()
        height = figure.height + 2
        width = figure.width + 6
        val result = ArrayList<String>()
        result.add("   " + lines[0] + "   ")
        result.add("+->" + lines[1] + "->+")
        for (i in 2 until lines.size) {
            result.add("|  " + lines[i] + "  |")
        }
        result.add("| " + " ".repeat(width - 3) + "|")
        result.add("+<" + "-".repeat(width - 3) + "+")
        return result
    }
}

class FigureAsterisk(val figure: Figure) : Figure {
    override var width: Int = 0
    override var height: Int = 0

    override fun render(): List<String> {
        val lines = figure.render()
        height = figure.height + 5
        width = figure.width + 6
        val result = ArrayList<String>()
        result.add(" ".repeat(width))
        result.add("+" + "-".repeat(width - 3) + ">+")
        result.add("|" + " ".repeat(width - 3) + " |")
        result.add("|  " + lines[0] + "  |")
        result.add("+->" + lines[1] + "->+")
        for (i in 2 until lines.size) {
            result.add("|  " + lines[i] + "  |")
        }
        result.add("| " + " ".repeat(width - 3) + "|")
        result.add("+<" + "-".repeat(width - 3) + "+")
        return result
    }
}

class FigureSequence : Figure {
    val figures = ArrayList<Figure>()
    override var width: Int = 0
    override var height: Int = 0
    var lines: List<String> = emptyList()

    fun append(figure: Figure): FigureSequence {
        figures.add(figure)
        return this
    }

    fun push(char: Char) {
        if (char.isLetter()) {
            if (figures.isNotEmpty() && figures.last() is FigureName) {
                (figures.last() as FigureName).push(char)
            } else {
                append(FigureName(char.toString()))
            }
        } else {
            if (figures.isNotEmpty() && figures.last() is FigureName) {
                val lastNameFigure = figures.last() as FigureName
                val name = lastNameFigure.name
                if (name.length > 1) {
                    lastNameFigure.name = name.substring(0, name.length - 1)
                    figures.add(FigureName(name.last().toString()))
                }
            }
            val last = figures.removeAt(figures.size - 1)
            when (char) {
                '?' -> figures.add(FigureQuestionMark(last))
                '+' -> figures.add(FigurePlusSign(last))
                '*' -> figures.add(FigureAsterisk(last))
                else -> throw IllegalArgumentException("Unexpected char `$char`")
            }
        }
    }

    override fun render(): List<String> {
        if (figures.isEmpty()) return emptyList()
        var result = ArrayList(figures[0].render())
        width = figures[0].width
        height = figures[0].height

        for (i in 1 until figures.size) {
            val lines = figures[i].render()
            val fullWidth = 2 + lines[0].length
            if (lines.size > height) {
                val diff = lines.size - height
                for (k in 0 until diff) {
                    result.add(" ".repeat(width))
                }
                height = lines.size
            }
            width += fullWidth
            for (j in lines.indices) {
                val prefix = if (j == 1) "->" else "  "
                result[j] = result[j] + prefix + lines[j]
            }
            if (lines.size < height) {
                val spaces = " ".repeat(fullWidth)
                for (k in lines.size until height) {
                    result[k] = result[k] + spaces
                }
            }
        }
        lines = result
        return result
    }
}

class FigureAlternative : Figure {
    var isEmpty = true
    val figures = ArrayList<FigureSequence>()
    override var width: Int = 0
    override var height: Int = 0
    var lines: List<String> = emptyList()

    private fun checkEmpty() {
        if (isEmpty) {
            figures.add(FigureSequence())
            isEmpty = false
        }
    }

    fun append(figure: FigureAlternative): FigureAlternative {
        checkEmpty()
        figures.last().append(figure)
        return this
    }

    fun push(char: Char) {
        if (char == '|') {
            isEmpty = true
        } else {
            checkEmpty()
            figures.last().push(char)
        }
    }

    override fun render(): List<String> {
        var calculatedWidth = 0
        for (figure in figures) {
            figure.render()
            calculatedWidth = max(calculatedWidth, figure.width)
        }
        if (figures.size == 1) {
            width = figures[0].width
            height = figures[0].height
            lines = figures[0].lines
            return lines
        }

        width = calculatedWidth + 6
        val result = ArrayList<String>()
        val emptyLine = "|" + " ".repeat(width - 2) + "|"

        for (i in figures.indices) {
            val figure = figures[i]
            if (i > 0) {
                result.add(emptyLine)
            }
            val fLines = figure.render() 
            for (j in fLines.indices) {
                val line = fLines[j]
                var pref = "   "
                var suff = "   "
                var fill = " "
                
                if (j == 1) {
                    pref = "+->"
                    suff = "->+"
                    fill = "-"
                } else if (i == 0 && j == 0) {
                    // pass
                } else if (i + 1 == figures.size && j > 1) {
                    // pass
                } else {
                    pref = "|  "
                    suff = "  |"
                }
                
                result.add(pref + line + fill.repeat(calculatedWidth - line.length) + suff)
            }
        }
        height = result.size
        lines = result
        return result
    }
}

class FigureMain(val figure: Figure) {
    var width: Int = 0
    var height: Int = 0
    var lines: List<String> = emptyList()

    fun render(): List<String> {
        val fLines = figure.render()
        val result = ArrayList<String>()
        for (i in fLines.indices) {
            val line = fLines[i]
            val pref = if (i == 1) "S->" else "   "
            val suff = if (i == 1) "->F" else "   "
            result.add(pref + line + suff)
        }
        lines = result
        if (result.isNotEmpty()) {
            width = result[0].length
            height = result.size
        }
        return result
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    if (!scanner.hasNextLine()) return
    val text = scanner.nextLine()
    
    val stash = ArrayDeque<FigureAlternative>()
    var figure = FigureAlternative()
    
    var i = 0
    while (i < text.length) {
        val char = text[i]
        if (char == '(') {
            stash.push(figure)
            figure = FigureAlternative()
        } else if (char == ')') {
            val parent = stash.pop()
            parent.append(figure)
            figure = parent
        } else {
            figure.push(char)
        }
        i++
    }

    val result = FigureMain(figure)
    val outputLines = result.render()
    println("${result.height} ${result.width}")
    for (line in outputLines) {
        println(line)
    }
}
