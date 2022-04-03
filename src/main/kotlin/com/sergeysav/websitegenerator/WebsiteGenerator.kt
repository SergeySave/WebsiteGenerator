package com.sergeysav.websitegenerator

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.staticBasePackage
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.css.CssBuilder
import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStream
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

@Suppress("unused")
object WebsiteGenerator {
    fun run(args: Args, inner: Environment.()->Unit) {
        if (args.hostServer) {
            embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
                routing {
                    staticBasePackage = ""
                    KtorEnvironment(this).inner()
                }
            }.start(wait = true)
        } else {
            val basePath = FileSystems.getDefault().getPath(args.basePath)
            if (Files.exists(basePath)) {
                Files.walk(basePath).use { walk ->
                    walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete)
                }
            }
            FileEnvironment(args.basePath).inner()
        }
    }

    fun run(args: Array<String>, inner: Environment.()->Unit) = run(Args.parse(args), inner)

    private class KtorEnvironment(private val routing: Routing) : BaseEnvironment() {
        override fun Resource.outputHTML(html: HTML.() -> Unit) {
            // Invoke the resource to force any other resources to be generated
            buildString { appendHTML().html { html() } }
            routing.get(this.path) {
                currentResource = this@outputHTML
                call.respondHtml(HttpStatusCode.OK, html)
                currentResource = null
            }
        }

        override fun Resource.outputCSS(css: CssBuilder.() -> Unit) {
            // Invoke the resource to force any other resources to be generated
            CssBuilder().apply(css)
            routing.get(this.path) {
                currentResource = this@outputCSS
                call.respondText(CssBuilder().apply(css).toString(), ContentType.Text.CSS)
                currentResource = null
            }
        }

        override fun Resource.outputResource(localPath: String) {
            currentResource = this
            routing.resource(this.path, localPath)
            currentResource = null
        }
    }

    private class FileEnvironment(private val basePath: String) : BaseEnvironment() {
        private fun outputTo(pathString: String, inner: OutputStream.()->Unit) {
            val path = FileSystems.getDefault().getPath(basePath, pathString)
            Files.createDirectories(path.parent)
            Files.newOutputStream(path).use { stream ->
                stream.inner()
            }
        }
        private fun writeTo(pathString: String, inner: BufferedWriter.()->Unit) {
            val path = FileSystems.getDefault().getPath(basePath, pathString)
            Files.createDirectories(path.parent)
            Files.newBufferedWriter(path).use { writer ->
                writer.inner()
            }
        }

        override fun Resource.outputHTML(html: HTML.() -> Unit) = writeTo("$path/index.html") {
            // Invoke the resource to force any other resources to be generated
            buildString { appendHTML().html { html() } }
            currentResource = this@outputHTML
            appendLine("<!DOCTYPE html>").appendHTML().html { html() }
            currentResource = null
        }

        override fun Resource.outputCSS(css: CssBuilder.() -> Unit) = writeTo(path) {
            // Invoke the resource to force any other resources to be generated
            CssBuilder().apply(css).toString()
            currentResource = this@outputCSS
            append(CssBuilder().apply(css).toString())
            currentResource = null
        }

        override fun Resource.outputResource(localPath: String) = outputTo(path) {
            currentResource = this@outputResource
            Resource::class.java.classLoader.getResourceAsStream(localPath)?.copyTo(this) ?: error("Resource Not Found. Make sure the resource case matches.")
            currentResource = null
        }
    }

    private object NullEnvironment : BaseEnvironment() {
        override fun Resource.outputHTML(html: HTML.() -> Unit) { }

        override fun Resource.outputCSS(css: CssBuilder.() -> Unit) { }

        override fun Resource.outputResource(localPath: String) { }
    }
}