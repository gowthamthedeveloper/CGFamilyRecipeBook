package com.impeccthreads.cgfamilyrecipebook.application


enum class DatabaseTable {
    cookingscheduledetails,
    cookingrecipedetails;

    override fun toString(): String {
        return super.toString()
    }
}



enum class CookingScheduleDetailsTable {
    key,
    scheduledate,
    breakfast,
    lunch,
    evening,
    dinner,
    family,
    others;

    override fun toString(): String {
        return super.toString()
    }
}

enum class CookingRecipeDetailsTable {
    key,
    title,
    subTitle,
    recipeName,
    isVeg,
    session,
    combination,
    ingredeints,
    methods,
    note,
    preparationTime,
    foodType,
    isNewRecipe;

    override fun toString(): String {
        return super.toString()
    }
}