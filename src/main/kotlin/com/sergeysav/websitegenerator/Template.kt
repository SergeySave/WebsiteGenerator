package com.sergeysav.websitegenerator

interface Template<T> {

    fun T.install(environment: Environment)
}