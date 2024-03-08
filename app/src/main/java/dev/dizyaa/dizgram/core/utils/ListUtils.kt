package dev.dizyaa.dizgram.core.utils

/**
 * Modifies the first element in the list that matches the given condition using the provided transform function.
 *
 * @param condition A lambda expression that defines the condition to be checked for each element in the list.
 *                   The first element that matches this condition will be modified.
 * @param transform A lambda expression that specifies the modification to be applied to the matched element.
 * @return A new list with the first matching element modified, or the original list if no matching element was found.
 */
fun <T> List<T>.mapFirst(condition: (T) -> Boolean, transform: (T) -> T): List<T> {
    val modifiedList = this.toMutableList()
    for (index in modifiedList.indices) {
        if (condition(modifiedList[index])) {
            modifiedList[index] = transform(modifiedList[index])
            break
        }
    }
    return modifiedList
}