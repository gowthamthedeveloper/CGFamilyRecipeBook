package com.impeccthreads.cgfamilyrecipebook.application

import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.User

object AppCacheManager {

    var currentUser: User = User()
    var allCookingRecipeList: ArrayList<CookingRecipeDetails> = arrayListOf()
    var allCookingScheduleDetailsList: ArrayList<CookingScheduleDetails> = arrayListOf()
}
//
//var isDebugMode: Boolean = false
//
//val recipeJsonString = """ [
//  {
//    "combination": "Idly, Dosa, Chapathi",
//    "ingredeints": "Grated Coconut - 1/4\nPotukadalai - half of the coconut quantity\nGreen Chilli - 2\nGarlic - 2\nTamarind - 1 seed\nCurry leaves\nSalt\nAdd water and grind it.\nAdd coriander leaves and just rough grind\nFor garnishing,\nOil\nKadugu\nPerungayam - 2 press\nCurry leaves",
//    "isNewRecipe": false,
//    "isVeg": true,
//    "methods": "",
//    "note": "",
//    "preparationTime": "30 Minutes",
//    "recipeName": "Green Chilli Chutney",
//    "session": "TIFFEN",
//    "subTitle": "Coconut Chutney",
//    "title": "Chutney",
//    "Chef": "Bhuvaneshwari"
//  },
//  {
//    "combination": "Idly, Dosa, Chapathi",
//    "ingredeints": "Grated Coconut - 1/4\nPotukadalai - equal quantity of the coconut\nRed Dry Chilli - 2\nGarlic - 2\nTamarind - 1 seed\nCurry leaves\nSalt\nAdd water and grind it.\n\nFor garnishing,\nOil\nKadugu\nPerungayam - 2 press\nCurry leaves",
//    "isNewRecipe": false,
//    "isVeg": true,
//    "methods": "",
//    "note": "",
//    "preparationTime": "30 Minutes",
//    "recipeName": "Red Chiii Chutney",
//    "session": "TIFFEN",
//    "subTitle": "Coconut Chutney",
//    "title": "Chutney",
//    "Chef": "Bhuvaneshwari"
//  },
//  {
//    "combination": "Idly, Dosa, White Rice",
//    "ingredeints": "Fry,\nOil\nKalla Parupu - 4 Finger 1 time\nUluntham Parupu - 4 Finger 1 time\nCurry Leaves \nDry Red Chilli - 5\nGarted Coconut - 1/4 \nTamarind - 1 seed\nGarlic - 2 no\nSalt\nGrind with water",
//    "isNewRecipe": false,
//    "isVeg": true,
//    "methods": "",
//    "note": "",
//    "preparationTime": "30 Minutes",
//    "recipeName": "Kalla Parupu Chutney",
//    "session": "TIFFEN & LUNCH",
//    "subTitle": "Coconut Chutney",
//    "title": "Chutney",
//    "Chef": "Bhuvaneshwari"
//  },
//  {  "combination": "Idly, Dosa",
//    "ingredeints": "",
//    "isNewRecipe": false,
//    "isVeg": true,
//    "methods": "",
//    "note": "",
//    "preparationTime": "30 Minutes",
//    "recipeName": "Amma Tomato Chutney",
//    "session": "TIFFEN",
//    "subTitle": "Coconut Chutney",
//    "title": "Chutney",
//    "Chef": "Bhuvaneshwari"
//  },
//  {  "combination": "Idly, Dosa",
//    "ingredeints": "Oil\nKadugu\nKalla parupu ulutham parupu\nRed dry chilli - 8\nSmall onion - 1 hand\nGarlic - 7 piece\nToamto - 2 big\nCoconut - 1/4\nCurry leaves\nCoriander leaves - 1 bunch\nMint leaves - 1 bunch\nSalt\nCool it and mix it in mixer grinde",
//    "isNewRecipe": false,
//    "isVeg": true,
//    "methods": "",
//    "note": "",
//    "preparationTime": "30 Minutes",
//    "recipeName": "Appa Tomato Chutney (With Podhina)",
//    "session": "TIFFEN",
//    "subTitle": "Coconut Chutney",
//    "title": "Chutney",
//    "Chef": "Bhuvaneshwari"
//  } ]"""