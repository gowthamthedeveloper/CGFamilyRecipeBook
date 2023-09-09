package com.impeccthreads.cgfamilyrecipebook.application

import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueDto
import com.impeccthreads.cgfamilyrecipebook.dto.User

interface Constants {

    companion object {
        val recipeSheetName = "Sheet1" //"RecipieList"
        val cookingScheduleSheetName = "Sheet2" //"Cooking Schedule" //Sheet2!A2:H
        val recipeDocumentNameWithExtension = "CG_Family_Recipe_Book.xls"
        val recipeSheetId = "16ETQMlDpyyhanlhjIuLeJph753-hCz40GpRR3M8VqXo"
        val sheetAPIKey = "AIzaSyCGhAzJWqSU2QpPLl06hwH2dk_XS6k7aRw"
        val authClientID = "47037525262-ns7q9qfvq2bgo7brd6ddgaohtsbt13rv.apps.googleusercontent.com"
        val apiKey = "AIzaSyCGhAzJWqSU2QpPLl06hwH2dk_XS6k7aRw"
        val getSheetValueURL = "https://sheets.googleapis.com/v4/spreadsheets/16ETQMlDpyyhanlhjIuLeJph753-hCz40GpRR3M8VqXo/values/Sheet1?valueRenderOption=FORMATTED_VALUE&key=AIzaSyCGhAzJWqSU2QpPLl06hwH2dk_XS6k7aRw"


        //        GET https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/{range}
//        PUT https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/{range}

        var generalDateFormat = "EEE, d MMM yyyy"

        var familyList = listOf<String>(Family.cgfamily.toString(), Family.madhanafamily.toString(), Family.saranyafamily.toString())
        var foodType = listOf<String>(FoodType.veg.toString(), FoodType.nonveg.toString())
        var sessionList = listOf<String>("TIFFEN", "LUNCH", "TIFFEN & LUNCH", "SNACKS")
        var Chef = listOf<String>(FoodType.veg.toString(), FoodType.nonveg.toString())


        val cookDeliciousRecipe = "Cook Delicious Recipe"
        val cookingSchedule = "Cooking Schedule"

        val addCookingSchedule = "Add Cooking Schedule"
        val editCookingScheduleActivity = "Edit Cooking Schedule"

        val addCookingRecipeCategory = "Add Cooking Recipe"
        val addCookingRecipeItem = "Add Cooking Recipe Item"

        val recipeBookDocUrl = "https://docs.google.com/spreadsheets/d/16ETQMlDpyyhanlhjIuLeJph753-hCz40GpRR3M8VqXo/edit#gid=0"

        val recipeItemFields: ArrayList<String> = arrayListOf(
//            CookingRecipeDetailsTable.key.toString(),
            CookingRecipeDetailsTable.title.toString(),
            CookingRecipeDetailsTable.subTitle.toString(),
            CookingRecipeDetailsTable.recipeName.toString(),
            CookingRecipeDetailsTable.isVeg.toString(),
            CookingRecipeDetailsTable.session.toString(),
            CookingRecipeDetailsTable.preparationTime.toString(),
            CookingRecipeDetailsTable.combination.toString(),
            CookingRecipeDetailsTable.ingredeints.toString(),
            CookingRecipeDetailsTable.note.toString(),
            CookingRecipeDetailsTable.isNewRecipe.toString())

        val cookingScheduleDetailsFields: ArrayList<String> = arrayListOf(
//            CookingScheduleDetailsTable.key.toString(),
            CookingScheduleDetailsTable.scheduledate.toString(),
            CookingScheduleDetailsTable.breakfast.toString(),
            CookingScheduleDetailsTable.lunch.toString(),
            CookingScheduleDetailsTable.evening.toString(),
            CookingScheduleDetailsTable.dinner.toString(),
            CookingScheduleDetailsTable.others.toString(),
            CookingScheduleDetailsTable.family.toString())


        fun getAllowedUser(): ArrayList<User> {

            val userList: ArrayList<User> = arrayListOf()

            userList.add(getUserForMobileNo("Gowtham", "9698137713", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Cibi", "9788653705", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Madhana","9443686465", Family.madhanafamily.toString()))
            userList.add(getUserForMobileNo("Saranya","7339311880", Family.saranyafamily.toString()))
            userList.add(getUserForMobileNo("My Sweet Darling","9941956321", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Sekar","9976694625", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Bhuvana","9715180383", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Brindha","9750347749", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Saroja","7530018509", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Ramana","9865815397", Family.cgfamily.toString()))
            userList.add(getUserForMobileNo("Shamla","9176290022", Family.cgfamily.toString()))

            return userList
        }

        fun getUserForMobileNo(username: String, mobileNo: String, family: String) : User {

            val user = User()
            user.mobileno = mobileNo
            user.username = username

            return user
        }

        fun getNanoSecString(): String {
            return System.nanoTime().toString()
        }

        fun getAddCookingScheduleFieldValueList() : ArrayList<FieldValueDto> {

            var fieldValueList: ArrayList<FieldValueDto> = arrayListOf()
//            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.key.toString(), true, 500))
            fieldValueList.add(getDropDownFieldTypeValueDto(CookingScheduleDetailsTable.family.toString(), true, familyList))
            fieldValueList.add(getDateFieldTypeValueDto(CookingScheduleDetailsTable.scheduledate.toString(), true, generalDateFormat))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.breakfast.toString(), false, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.lunch.toString(), false, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.evening.toString(), false, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.dinner.toString(), false, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingScheduleDetailsTable.others.toString(), false, 500))

            return fieldValueList
        }

        fun getAddCookingRecipeCategoryFieldValueList() : ArrayList<FieldValueDto> {

            var fieldValueList: ArrayList<FieldValueDto> = arrayListOf()
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.title.toString(), true, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.subTitle.toString(), true, 500))

            return fieldValueList
        }

        fun getAddCookingRecipeFieldValueList() : ArrayList<FieldValueDto> {

            var fieldValueList: ArrayList<FieldValueDto> = arrayListOf()
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.Chef.toString(), true, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.title.toString(), true, 500, false))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.subTitle.toString(), true, 500, false))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.recipeName.toString(), true, 500))
            fieldValueList.add(getDropDownFieldTypeValueDto(CookingRecipeDetailsTable.foodType.toString(), true, foodType))
            fieldValueList.add(getDropDownFieldTypeValueDto(CookingRecipeDetailsTable.session.toString(), true, sessionList))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.combination.toString(), false, 500))
            fieldValueList.add(getDefaultFieldTypeValueDto(CookingRecipeDetailsTable.preparationTime.toString(), false, 500))

            return fieldValueList
        }

        fun getDefaultFieldTypeValueDto(name: String, mandatory: Boolean, maxCharCount: Int, isEditable: Boolean = true): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.default
            fieldValue.maxCharCount = maxCharCount
            fieldValue.isEditable = isEditable

            return fieldValue
        }

        fun getAlphabetsFieldTypeValueDto(name: String, mandatory: Boolean, maxCharCount: Int): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.alphabets
            fieldValue.maxCharCount = maxCharCount

            return fieldValue
        }

        fun getNumberFieldTypeValueDto(name: String, value: String, mandatory: Boolean, maxCharCount: Int): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = value
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.number
            fieldValue.maxCharCount = maxCharCount

            return fieldValue
        }

        fun getDecimalFieldTypeValueDto(name: String, mandatory: Boolean, maxCharCount: Int): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.decimal
            fieldValue.maxCharCount = maxCharCount

            return fieldValue
        }

        fun getDateFieldTypeValueDto(name: String, mandatory: Boolean, dateAndTimeFormat: String, isEditable: Boolean = true): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.date
            fieldValue.dateAndTimeFormat = dateAndTimeFormat
            fieldValue.isEditable = isEditable
            return fieldValue
        }

        fun getTimeFieldTypeValueDto(name: String, mandatory: Boolean, dateAndTimeFormat: String, isRailwayFormat: Boolean = false): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.time
            fieldValue.dateAndTimeFormat = dateAndTimeFormat
            fieldValue.is24HoursFormat = isRailwayFormat

            return fieldValue
        }

        fun getDropDownFieldTypeValueDto(name: String, mandatory: Boolean, selectionList: List<String>): FieldValueDto {
            val fieldValue = FieldValueDto()
            fieldValue.name = name
            fieldValue.value = ""
            fieldValue.isMandatory = mandatory
            fieldValue.fieldType = AddTableFieldType.dropdown
            fieldValue.selectionList = selectionList

            return fieldValue
        }
    }

    interface SharedPrefKeys {

        companion object {
            val isRecipeBookSynced = "IS_RECIPE_BOOK_SYNCED"
            val isRecipeBookDownloaded = "IS_RECIPE_BOOK_DOWNLOADED"
            val isUserRegistered = "IS_USER_REGISTERED"
            val localUserDetails = "USER_DETAILS"
            val userMobileNo = "USER_MOBILE_NO"
        }
    }

    interface AppKeys {
        companion object {
            val DESTINATION_FOLDER_NAME = "CGFamilyRecipeBook"
        }
    }

    interface AlertMessages {
        companion object {
            val recipeBookUpdated = "Recipe Book Updated"
            val recipeBookDownloaded = "Recipe Book Downloaded"
            val enterName = "Please enter the name"
            val enterMobile = "Please enter the mobile number"
            val enterValidMobile = "Please enter valid mobile number"
            val recipeCategoryDuplicate = "Entered recipe category is already added"
            val recipeNameDuplicate = "Entered recipe name is already added"

        }
    }
}

enum class Family {
    cgfamily,
    madhanafamily,
    saranyafamily;

    override fun toString(): String {
        return super.toString().toUpperCase()
    }
}

enum class FoodType {
    veg,
    nonveg;

    override fun toString(): String {
        return super.toString().toUpperCase()
    }
}

enum class AddTableFieldType {
    default,
    number,
    decimal,
    alphabets,
    date,
    time,
    datetime,
    dropdown;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}
