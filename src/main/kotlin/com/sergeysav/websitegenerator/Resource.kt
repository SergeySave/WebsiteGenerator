package com.sergeysav.websitegenerator

interface Resource {
    val path: String

    fun Environment.install()
}