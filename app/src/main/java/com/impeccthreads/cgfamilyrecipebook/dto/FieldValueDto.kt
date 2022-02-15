package com.impeccthreads.cgfamilyrecipebook.dto

import com.impeccthreads.cgfamilyrecipebook.application.AddTableFieldType
import java.io.Serializable

class FieldValueDto: Serializable {

    var name: String = ""
    var value: String = ""
    var isPrimaryFilter: Boolean = false
    var isMandatory: Boolean = false
    var fieldType: AddTableFieldType = AddTableFieldType.default
    var dateAndTimeFormat: String = ""
    var selectionList: List<String> = listOf()
    var maxCharCount: Int = 50
    var isEditable: Boolean = true
    var is24HoursFormat: Boolean = false

}