package com.github.thaynarasilvapinto.web.exception

import java.io.Serializable

open class FieldMessage(var fildName: String, var message: String) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}