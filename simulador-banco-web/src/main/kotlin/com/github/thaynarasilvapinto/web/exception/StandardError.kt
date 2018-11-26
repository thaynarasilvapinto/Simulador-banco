package com.github.thaynarasilvapinto.web.exception

import java.io.Serializable

open class StandardError(var status: Int, var message: String) : Serializable{
    companion object {
        const val serialVersionUID = 1L
    }
}