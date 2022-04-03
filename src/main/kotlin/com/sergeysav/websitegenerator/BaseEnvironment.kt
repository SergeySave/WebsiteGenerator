package com.sergeysav.websitegenerator

abstract class BaseEnvironment : Environment {

    private val installed = mutableMapOf<Resource, Resource>()
    override var currentResource: Resource? = null
        protected set

    override fun getResourcePath(resource: Resource): String {
        if (!installed.containsKey(resource)) {
            installed[resource] = resource
            resource.apply { install() }
        }
        return (installed[resource] ?: resource).path
    }

    override fun <T> T.build(template: Template<T>) {
        template.apply {
            install(this@BaseEnvironment)
        }
    }
}