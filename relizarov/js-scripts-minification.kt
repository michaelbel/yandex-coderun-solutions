import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashMap
import java.util.HashSet

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))

    // Helper to read non-empty lines (skip potentially empty lines between inputs)
    fun readNextLine(): String? {
        var line = reader.readLine()
        while (line != null && line.trim().isEmpty()) {
            line = reader.readLine()
        }
        return line
    }

    // 1. Read Reserved Tokens
    val nStr = readNextLine() ?: return
    val n = try { nStr.trim().toInt() } catch (e: Exception) { 0 }

    val keywords = ArrayList<String>()
    if (n > 0) {
        val kwLine = reader.readLine()
        if (kwLine != null) {
            // Split by whitespace
            val parts = kwLine.trim().split("\\s+".toRegex())
            for (p in parts) {
                if (p.isNotEmpty()) keywords.add(p)
            }
        }
    }

    // Sort keywords: Longest first, then lexicographically
    Collections.sort(keywords, object : Comparator<String> {
        override fun compare(a: String, b: String): Int {
            if (a.length != b.length) return b.length - a.length
            return a.compareTo(b)
        }
    })
    
    val keywordSet = HashSet<String>(keywords)
    val maxKeywordLen = if (keywords.isNotEmpty()) keywords[0].length else 0

    // 2. Read Source Code
    val mStr = readNextLine() ?: return
    val m = try { mStr.trim().toInt() } catch (e: Exception) { 0 }

    val rawTokens = ArrayList<String>()

    fun isDigit(c: Char) = c in '0'..'9'
    fun isWordStart(c: Char) = (c in 'a'..'z') || (c in 'A'..'Z') || c == '_' || c == '$'
    fun isWordPart(c: Char) = isWordStart(c) || isDigit(c)

    // Parsing function: extracts the first token from text at startIdx
    fun parseTokenAt(text: String, startIdx: Int): String {
        val len = text.length
        if (startIdx >= len) return ""
        
        var bestEnd = startIdx
        
        // Option A: Number
        if (isDigit(text[startIdx])) {
            var temp = startIdx
            while (temp < len && isDigit(text[temp])) temp++
            if (temp > bestEnd) bestEnd = temp
        }
        
        // Option B: Word
        if (isWordStart(text[startIdx])) {
            var temp = startIdx
            while (temp < len && isWordPart(text[temp])) temp++
            if (temp > bestEnd) bestEnd = temp
        }
        
        // Option C: Keyword (Longest Match)
        for (kw in keywords) {
            // Optimization: keywords are sorted by length desc. 
            // If kw is shorter than current best match, it cannot win (unless we assume kw > word of same len).
            // Logic: "matches both... role of reserved token". 
            // This implies if lengths are equal, reserved wins.
            if (kw.length < (bestEnd - startIdx)) break
            
            if (startIdx + kw.length <= len) {
                if (text.regionMatches(startIdx, kw, 0, kw.length)) {
                    bestEnd = startIdx + kw.length
                    break 
                }
            }
        }
        
        return if (bestEnd > startIdx) text.substring(startIdx, bestEnd) else ""
    }

    // Parse all lines
    for (i in 0 until m) {
        val line = reader.readLine() ?: break
        var idx = 0
        val len = line.length
        while (idx < len) {
            // Skip spaces
            while (idx < len && line[idx] == ' ') idx++
            if (idx == len) break
            // Skip comments
            if (line[idx] == '#') break 
            
            val token = parseTokenAt(line, idx)
            if (token.isNotEmpty()) {
                rawTokens.add(token)
                idx += token.length
            } else {
                // Should not happen with valid input and comprehensive keywords, 
                // but to avoid infinite loop on unknown symbol:
                idx++ 
            }
        }
    }

    // 3. Rename Identifiers
    val renameMap = HashMap<String, String>()
    val mappedTokens = ArrayList<String>()
    
    // Generator for short names
    var nameCounter = 0
    fun nextName(): String {
        while (true) {
            var num = nameCounter++
            val sb = StringBuilder()
            // Bijective base-26 generation: a, b...z, aa, ab...
            do {
                val rem = num % 26
                sb.append(('a'.code + rem).toChar())
                num = num / 26 - 1
            } while (num >= 0)
            val name = sb.reverse().toString()
            if (!keywordSet.contains(name)) return name
        }
    }

    for (t in rawTokens) {
        // Renaming criteria:
        // 1. Not a number (numbers start with digit)
        // 2. Not a reserved token
        if ((t.isNotEmpty() && isDigit(t[0])) || keywordSet.contains(t)) {
            mappedTokens.add(t)
        } else {
            if (!renameMap.containsKey(t)) {
                renameMap[t] = nextName()
            }
            mappedTokens.add(renameMap[t]!!)
        }
    }

    // 4. Greedy Minification
    val result = StringBuilder()
    var i = 0
    val count = mappedTokens.size
    
    while (i < count) {
        // If not the very first group, we need a space to separate from the previous group
        if (i > 0) result.append(" ")
        
        var j = i + 1
        while (j < count) {
            // Try to extend the current group [i, j-1] by adding mappedTokens[j]
            // We must ensure that for ANY point k (i <= k < j), 
            // the sequence tokens[k]...tokens[j] still parses starting with tokens[k].
            
            var ok = true
            var k = j - 1
            
            while (k >= i) {
                // Construct temporary string for validation
                val sbCheck = StringBuilder()
                for (p in k..j) {
                    sbCheck.append(mappedTokens[p])
                }
                val combined = sbCheck.toString()
                
                val parsed = parseTokenAt(combined, 0)
                
                if (parsed != mappedTokens[k]) {
                    ok = false
                    break
                }
                
                // Heuristic optimization: 
                // If the combined string is significantly longer than any possible keyword 
                // plus the first token, the first token is stable.
                if (combined.length > maxKeywordLen + mappedTokens[k].length + 2) {
                    break
                }
                k--
            }
            
            if (!ok) break
            j++
        }
        
        // Append the valid group
        for (p in i until j) {
            result.append(mappedTokens[p])
        }
        i = j
    }
    
    println(result.toString())
}
