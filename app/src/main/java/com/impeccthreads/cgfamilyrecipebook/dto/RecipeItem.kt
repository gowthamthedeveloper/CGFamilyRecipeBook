package com.impeccthreads.cgfamilyrecipebook.dto

import android.os.Parcel
import android.os.Parcelable

class RecipeItem() : Parcelable {
    var key: String = ""
    var title: String = ""
    var subTitle: String = ""
    var recipeName: String = ""
    var isNewRecipe: Boolean = false
    var isVeg: Boolean = false

    constructor(parcel: Parcel) : this() {
        key = parcel.readString().toString()
        title = parcel.readString().toString()
        subTitle = parcel.readString().toString()
        recipeName = parcel.readString().toString()
        isNewRecipe = parcel.readByte() != 0.toByte()
        isVeg = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(recipeName)
        parcel.writeByte(if (isNewRecipe) 1 else 0)
        parcel.writeByte(if (isVeg) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeItem> {
        override fun createFromParcel(parcel: Parcel): RecipeItem {
            return RecipeItem(parcel)
        }

        override fun newArray(size: Int): Array<RecipeItem?> {
            return arrayOfNulls(size)
        }
    }

}