package com.sergeysav.websitegenerator

class StaticResource(
    private val localPath: String,
    remotePath: String = localPath,
    lowercase: Boolean = true
) : BaseResource(remotePath, lowercase) {

    override fun Environment.install() = outputResource(localPath)

    override fun equals(other: Any?): Boolean {
        return this.localPath == (other as? StaticResource)?.localPath
    }

    override fun hashCode(): Int {
        return localPath.hashCode()
    }
}
