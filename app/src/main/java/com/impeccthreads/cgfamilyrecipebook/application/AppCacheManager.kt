package com.impeccthreads.cgfamilyrecipebook.application

import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.User

object AppCacheManager {

    var currentUser: User = User()
    var allCookingRecipeList: ArrayList<CookingRecipeDetails> = arrayListOf()
    var allCookingScheduleDetailsList: ArrayList<CookingScheduleDetails> = arrayListOf()

}