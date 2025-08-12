import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.ceil
import java.util.Locale

enum class UnitType { MASS, VOLUME, COUNT }

fun convertToBaseUnits(amount: Double, unit: String): Pair<Double, UnitType> {
    return when (unit) {
        "g" -> amount to UnitType.MASS
        "kg" -> amount * 1000.0 to UnitType.MASS
        "ml" -> amount to UnitType.VOLUME
        "l" -> amount * 1000.0 to UnitType.VOLUME
        "cnt" -> amount to UnitType.COUNT
        "tens" -> amount * 10.0 to UnitType.COUNT
        else -> throw IllegalArgumentException("Unknown unit: $unit")
    }
}

data class IngredientNeedForDish(
    val name: String,
    val amountPerServingBase: Double,
    val unitType: UnitType
)

data class Dish(
    val name: String,
    val servings: Int,
    val ingredients: List<IngredientNeedForDish>
)

data class IngredientPriceInfo(
    val name: String,
    val price: Int,
    val packageAmountBase: Double,
    val unitType: UnitType
)

data class IngredientNutritionInfo(
    val name: String,
    val nutritionBaseAmount: Double,
    val unitType: UnitType,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double
)

data class TotalIngredientNeed(
    var totalAmountBase: Double,
    val unitType: UnitType
)

data class DishNutritionResult(
    val name: String,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
    val totalCalories: Double
)

fun main() {
    Locale.setDefault(Locale.US)
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val dishes = mutableListOf<Dish>()
    val totalNeeds = mutableMapOf<String, TotalIngredientNeed>()
    repeat(n) {
        val dishParts = reader.readLine().split(" ")
        val dishName = dishParts[0]
        val servings = dishParts[1].toInt()
        val ingredientCount = dishParts[2].toInt()
        val currentDishIngredients = mutableListOf<IngredientNeedForDish>()
        repeat(ingredientCount) {
            val ingredientParts = reader.readLine().split(" ")
            val ingredientName = ingredientParts[0]
            val amount = ingredientParts[1].toDouble()
            val unit = ingredientParts[2]
            val (baseAmountPerServing, unitType) = convertToBaseUnits(amount, unit)
            val totalAmountForAllServings = baseAmountPerServing * servings
            currentDishIngredients.add(IngredientNeedForDish(ingredientName, baseAmountPerServing, unitType))
            val existingNeed = totalNeeds[ingredientName]
            if (existingNeed != null) {
                if (existingNeed.unitType != unitType) {
                    throw IllegalStateException("Inconsistent unit types for ingredient: $ingredientName")
                }
                existingNeed.totalAmountBase += totalAmountForAllServings
            } else {
                totalNeeds[ingredientName] = TotalIngredientNeed(totalAmountForAllServings, unitType)
            }
        }
        dishes.add(Dish(dishName, servings, currentDishIngredients))
    }
    val k = reader.readLine().toInt()
    val priceCatalogMap = mutableMapOf<String, IngredientPriceInfo>()
    val priceCatalogList = mutableListOf<IngredientPriceInfo>()
    repeat(k) {
        val priceParts = reader.readLine().split(" ")
        val name = priceParts[0]
        val price = priceParts[1].toInt()
        val amount = priceParts[2].toDouble()
        val unit = priceParts[3]
        val (basePackageAmount, unitType) = convertToBaseUnits(amount, unit)
        val priceInfo = IngredientPriceInfo(name, price, basePackageAmount, unitType)
        priceCatalogMap[name] = priceInfo
        priceCatalogList.add(priceInfo)
    }
    val m = reader.readLine().toInt()
    val nutritionCatalog = mutableMapOf<String, IngredientNutritionInfo>()
    repeat(m) {
        val nutritionParts = reader.readLine().split(" ")
        val name = nutritionParts[0]
        val amount = nutritionParts[1].toDouble()
        val unit = nutritionParts[2]
        val protein = nutritionParts[3].toDouble()
        val fat = nutritionParts[4].toDouble()
        val carbs = nutritionParts[5].toDouble()
        val calories = nutritionParts[6].toDouble()
        val (baseNutritionAmount, unitType) = convertToBaseUnits(amount, unit)
        nutritionCatalog[name] = IngredientNutritionInfo(
            name, baseNutritionAmount, unitType, protein, fat, carbs, calories
        )
    }
    var totalCost = 0L
    val packagesToBuy = mutableMapOf<String, Long>()
    priceCatalogList.forEach { packagesToBuy[it.name] = 0L }
    totalNeeds.forEach { (ingredientName, need) ->
        val priceInfo = priceCatalogMap[ingredientName]
        if (priceInfo != null) {
            if (priceInfo.packageAmountBase > 1e-9) {
                val packagesNeeded = ceil(need.totalAmountBase / priceInfo.packageAmountBase).toLong()
                totalCost += packagesNeeded * priceInfo.price
                packagesToBuy[ingredientName] = packagesNeeded
            } else if (need.totalAmountBase > 1e-9) {
                throw IllegalStateException("Need ingredient $ingredientName but package size is zero.")
            }
        } else {
            throw IllegalStateException("Price info not found for needed ingredient: $ingredientName")
        }
    }
    val dishNutritionResults = mutableListOf<DishNutritionResult>()
    dishes.forEach { dish ->
        var dishTotalProtein = 0.0
        var dishTotalFat = 0.0
        var dishTotalCarbs = 0.0
        var dishTotalCalories = 0.0
        dish.ingredients.forEach { ingredientNeed ->
            val nutritionInfo = nutritionCatalog[ingredientNeed.name]
            if (nutritionInfo != null) {
                if (nutritionInfo.nutritionBaseAmount > 1e-9) {
                    val factor = ingredientNeed.amountPerServingBase / nutritionInfo.nutritionBaseAmount
                    dishTotalProtein += nutritionInfo.protein * factor
                    dishTotalFat += nutritionInfo.fat * factor
                    dishTotalCarbs += nutritionInfo.carbs * factor
                    dishTotalCalories += nutritionInfo.calories * factor
                }
            } else {
                throw IllegalStateException("Nutrition info not found for ingredient: ${ingredientNeed.name}")
            }
        }
        dishNutritionResults.add(
            DishNutritionResult(dish.name, dishTotalProtein, dishTotalFat, dishTotalCarbs, dishTotalCalories)
        )
    }
    writer.write(totalCost.toString())
    writer.newLine()
    priceCatalogList.forEach { priceInfo ->
        writer.write("${priceInfo.name} ${packagesToBuy[priceInfo.name]!!}")
        writer.newLine()
    }
    dishNutritionResults.forEach { result ->
        writer.write(
            String.format(
                Locale.US,
                "%s %.10f %.10f %.10f %.10f",
                result.name,
                result.totalProtein,
                result.totalFat,
                result.totalCarbs,
                result.totalCalories
            )
        )
        writer.newLine()
    }
    reader.close()
    writer.flush()
    writer.close()
}
