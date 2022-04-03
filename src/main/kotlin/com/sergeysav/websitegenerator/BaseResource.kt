package com.sergeysav.websitegenerator

abstract class BaseResource(
    path: String
) : Resource {
    final override val path: String = (if (path.startsWith("/")) path else "/$path").lowercase()
}
