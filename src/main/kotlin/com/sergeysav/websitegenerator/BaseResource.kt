package com.sergeysav.websitegenerator

abstract class BaseResource(
    path: String,
    lowercase: Boolean = true
) : Resource {
    final override val path: String = (if (path.startsWith("/")) path else "/$path").let {
        if (lowercase) {
            it.lowercase()
        } else {
            it
        }
    }
}
