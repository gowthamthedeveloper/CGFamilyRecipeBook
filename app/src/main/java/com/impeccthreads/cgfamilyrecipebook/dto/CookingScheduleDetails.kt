package com.impeccthreads.cgfamilyrecipebook.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.application.CookingRecipeDetailsTable
import com.impeccthreads.cgfamilyrecipebook.application.CookingScheduleDetailsTable
import com.impeccthreads.cgfamilyrecipebook.utility.CodeSnippet
import java.util.*

class CookingScheduleDetails() : Parcelable {
    var scheduledate: String = ""
    var breakfast: String = ""
    var lunch: String = ""
    var evening: String = ""
    var dinner: String = ""
    var others: String = ""
    var family: String = ""

    //Internal Logics
    var key: String? = null
    var ref: DatabaseReference? = null
    var scheduledatetime: Date? = null

    constructor(parcel: Parcel) : this() {
        scheduledate = parcel.readString().toString()
        breakfast = parcel.readString().toString()
        lunch = parcel.readString().toString()
        evening = parcel.readString().toString()
        dinner = parcel.readString().toString()
        others = parcel.readString().toString()
        family = parcel.readString().toString()
        key = parcel.readString()
    }

    constructor(snapshot: DataSnapshot) : this() {
        key = snapshot.key
        ref = snapshot.ref

        val snapshotValue = snapshot.value as HashMap<String, Any>
        scheduledate = snapshotValue.get(CookingScheduleDetailsTable.scheduledate.toString()) as String
        breakfast = snapshotValue.get(CookingScheduleDetailsTable.breakfast.toString()) as String
        lunch = snapshotValue.get(CookingScheduleDetailsTable.lunch.toString()) as String
        evening = snapshotValue.get(CookingScheduleDetailsTable.evening.toString()) as String
        dinner = snapshotValue.get(CookingScheduleDetailsTable.dinner.toString()) as String
        others = snapshotValue.get(CookingScheduleDetailsTable.others.toString()) as String
        family = snapshotValue.get(CookingScheduleDetailsTable.family.toString()) as String

        scheduledatetime = CodeSnippet.getDateFromDateString(scheduledate, Constants.generalDateFormat, TimeZone.getDefault())
    }

    fun convertToFieldValueListDto(filterEmptyValue: Boolean = false): FieldValueListDto {
        val fieldValueList = FieldValueListDto()

        for (field in Constants.cookingScheduleDetailsFields)
        {
            val fieldValue = FieldValueDto()
            fieldValue.name = field

            when(field) {
                CookingScheduleDetailsTable.key.toString() -> fieldValue.value = key ?: ""
                CookingScheduleDetailsTable.scheduledate.toString() -> fieldValue.value = scheduledate ?: ""
                CookingScheduleDetailsTable.breakfast.toString() -> fieldValue.value = breakfast ?: ""
                CookingScheduleDetailsTable.lunch.toString() -> fieldValue.value = lunch ?: ""
                CookingScheduleDetailsTable.evening.toString() -> fieldValue.value = evening ?: ""
                CookingScheduleDetailsTable.dinner.toString() -> fieldValue.value = dinner ?: ""
                CookingScheduleDetailsTable.others.toString() -> fieldValue.value = others ?: ""
                CookingScheduleDetailsTable.family.toString() -> fieldValue.value = family ?: ""
            }

            fieldValueList.fieldValues.add(fieldValue)
        }

        if (filterEmptyValue)
        {
            fieldValueList.fieldValues = fieldValueList.fieldValues.filter { it.value != "" }.toMutableList()
        }

        return fieldValueList
    }

    fun toHashMap() : HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()

        hashMap.put(CookingScheduleDetailsTable.scheduledate.toString(), scheduledate!!)
        hashMap.put(CookingScheduleDetailsTable.breakfast.toString(), breakfast!!)
        hashMap.put(CookingScheduleDetailsTable.lunch.toString(), lunch!!)
        hashMap.put(CookingScheduleDetailsTable.evening.toString(), evening!!)
        hashMap.put(CookingScheduleDetailsTable.dinner.toString(), dinner!!)
        hashMap.put(CookingScheduleDetailsTable.others.toString(), others!!)
        hashMap.put(CookingScheduleDetailsTable.family.toString(), family!!)

        return hashMap
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(scheduledate)
        parcel.writeString(breakfast)
        parcel.writeString(lunch)
        parcel.writeString(evening)
        parcel.writeString(dinner)
        parcel.writeString(others)
        parcel.writeString(family)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CookingScheduleDetails> {
        override fun createFromParcel(parcel: Parcel): CookingScheduleDetails {
            return CookingScheduleDetails(parcel)
        }

        override fun newArray(size: Int): Array<CookingScheduleDetails?> {
            return arrayOfNulls(size)
        }
    }

}