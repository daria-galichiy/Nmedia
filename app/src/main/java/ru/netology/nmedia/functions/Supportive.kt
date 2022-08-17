package ru.netology.nmedia.functions

fun likesToText(likes: Int): String {
    val result = when {
        likes >= 1_000_000 -> {
            val millions = likes / 1_000_000
            val thousands = likes % 1_000_000 / 100_000
            if (thousands == 0) {
                "$millions" + "M"
            } else {
                "$millions" + "." + "$thousands" + "M"
            }
        }
        likes >= 10_000 -> {
            val thousands = likes / 1000
            "$thousands" + "K"
        }
        likes > 999 -> {
            val thousands = likes / 1000
            val hundreds = likes % 1000 / 100
            if (hundreds == 0) {
                "$thousands" + "K"
            } else {
                "$thousands" + "." + "$hundreds" + "K"
            }
        }
        else -> {
            "$likes"
        }
    }
    return result
}