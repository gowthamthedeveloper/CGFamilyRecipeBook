package com.impeccthreads.cgfamilyrecipebook.dto

import java.io.Serializable

class FieldValueListDto: Serializable {

    var fieldValues: MutableList<FieldValueDto> = mutableListOf()
}