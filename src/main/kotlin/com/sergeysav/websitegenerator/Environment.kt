package com.sergeysav.websitegenerator

import kotlinx.css.CssBuilder
import kotlinx.html.HTML

interface Environment {

    val currentResource: Resource?

    fun getResourcePath(resource: Resource): String

    fun Resource.outputHTML(html: HTML.()->Unit)

    fun Resource.outputCSS(css: CssBuilder.()->Unit)

    fun Resource.outputResource(localPath: String)

    fun <T> T.build(template: Template<T>)
}