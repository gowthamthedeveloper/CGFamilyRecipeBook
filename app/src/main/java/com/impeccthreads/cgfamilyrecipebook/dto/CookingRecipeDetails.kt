package com.impeccthreads.cgfamilyrecipebook.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.impeccthreads.cgfamilyrecipebook.application.CookingRecipeDetailsTable
import com.impeccthreads.cgfamilyrecipebook.application.CookingScheduleDetailsTable
import java.util.*

class CookingRecipeDetails() : Parcelable {

    var title: String = ""
    var subTitle: String = ""
    var recipeName: String = ""
    var isVeg: Boolean = true
    var session: String = ""
    var combination: String = ""
    var ingredeints: String = ""
    var methods: String = ""
    var note: String = ""
    var preparationTime: String = ""
    var isNewRecipe: Boolean = false

    //Internal Logics
    var key: String? = null
    var ref: DatabaseReference? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString().toString()
        subTitle = parcel.readString().toString()
        recipeName = parcel.readString().toString()
        isVeg = parcel.readByte() != 0.toByte()
        session = parcel.readString().toString()
        combination = parcel.readString().toString()
        ingredeints = parcel.readString().toString()
        methods = parcel.readString().toString()
        note = parcel.readString().toString()
        preparationTime = parcel.readString().toString()
        isNewRecipe = parcel.readByte() != 0.toByte()
        key = parcel.readString().toString()
    }

    constructor(snapshot: DataSnapshot) : this() {
        key = snapshot.key
        ref = snapshot.ref

        val snapshotValue = snapshot.value as HashMap<String, Any>
        title = snapshotValue.get(CookingRecipeDetailsTable.title.toString()) as String
        subTitle = snapshotValue.get(CookingRecipeDetailsTable.subTitle.toString()) as String
        recipeName = snapshotValue.get(CookingRecipeDetailsTable.recipeName.toString()) as String
        isVeg = snapshotValue.get(CookingRecipeDetailsTable.isVeg.toString()) as Boolean
        session = snapshotValue.get(CookingRecipeDetailsTable.session.toString()) as String
        combination = snapshotValue.get(CookingRecipeDetailsTable.combination.toString()) as String
        ingredeints = snapshotValue.get(CookingRecipeDetailsTable.ingredeints.toString()) as String
        methods = snapshotValue.get(CookingRecipeDetailsTable.methods.toString()) as String
        note = snapshotValue.get(CookingRecipeDetailsTable.note.toString()) as String
        preparationTime = snapshotValue.get(CookingRecipeDetailsTable.preparationTime.toString()) as String
        isNewRecipe = snapshotValue.get(CookingRecipeDetailsTable.isNewRecipe.toString()) as Boolean

    }

    fun getValueForField(fieldName: String): String {

        when(fieldName) {
            CookingRecipeDetailsTable.key.toString() -> return key.toString()
            CookingRecipeDetailsTable.title.toString() -> return title.toString()
            CookingRecipeDetailsTable.subTitle.toString() -> return subTitle.toString()
            CookingRecipeDetailsTable.recipeName.toString() -> return recipeName.toString()
            CookingRecipeDetailsTable.isVeg.toString() -> return isVeg.toString()
            CookingRecipeDetailsTable.session.toString() -> return session.toString()
            CookingRecipeDetailsTable.combination.toString() -> return combination.toString()
            CookingRecipeDetailsTable.ingredeints.toString() -> return ingredeints.toString()
            CookingRecipeDetailsTable.methods.toString() -> return ingredeints.toString()
            CookingRecipeDetailsTable.note.toString() -> return note.toString()
            CookingRecipeDetailsTable.preparationTime.toString() -> return preparationTime.toString()
            CookingRecipeDetailsTable.isNewRecipe.toString() -> return isNewRecipe.toString()
        }


        return  ""
    }

    fun toHashMap() : HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()

        hashMap.put(CookingRecipeDetailsTable.title.toString(), title!!)
        hashMap.put(CookingRecipeDetailsTable.subTitle.toString(), subTitle!!)
        hashMap.put(CookingRecipeDetailsTable.recipeName.toString(), recipeName!!)
        hashMap.put(CookingRecipeDetailsTable.isVeg.toString(), isVeg!!)
        hashMap.put(CookingRecipeDetailsTable.session.toString(), session!!)
        hashMap.put(CookingRecipeDetailsTable.session.toString(), session!!)
        hashMap.put(CookingRecipeDetailsTable.combination.toString(), combination!!)
        hashMap.put(CookingRecipeDetailsTable.ingredeints.toString(), ingredeints!!)
        hashMap.put(CookingRecipeDetailsTable.methods.toString(), methods!!)
        hashMap.put(CookingRecipeDetailsTable.note.toString(), note!!)
        hashMap.put(CookingRecipeDetailsTable.preparationTime.toString(), preparationTime!!)
        hashMap.put(CookingRecipeDetailsTable.isNewRecipe.toString(), isNewRecipe!!)

        return hashMap
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(recipeName)
        parcel.writeByte(if (isVeg) 1 else 0)
        parcel.writeString(session)
        parcel.writeString(combination)
        parcel.writeString(ingredeints)
        parcel.writeString(methods)
        parcel.writeString(note)
        parcel.writeString(preparationTime)
        parcel.writeByte(if (isNewRecipe) 1 else 0)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CookingRecipeDetails> {
        override fun createFromParcel(parcel: Parcel): CookingRecipeDetails {
            return CookingRecipeDetails(parcel)
        }

        override fun newArray(size: Int): Array<CookingRecipeDetails?> {
            return arrayOfNulls(size)
        }
    }

}


class ItemWidth {
    var fieldName: String = ""
    var maxChar: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ItemWidth?
        return fieldName == that!!.fieldName
    }

    override fun hashCode(): Int {
        return Objects.hash(fieldName)
    }

}
