
# Website Generator

A static website generator.
Define your website in Kotlin using the Kotlin HTML DSL, Kotlin CSS DSL, and
automatically link all every different resource together.

This can run in two different modes:
Server and Output.
Server mode runs a Ktor server which can be used for rapid prototyping
and development.
Output mode puts the website resources into a single folder so that the folder
can be used as the working directory for any simple http server.

### Server mode

Server mode can be enabled using the `--server` or `-s` arguments.

### Output mode

Set the JVM argument `io.ktor.development=true` when running in 
server development mode. This can be done by using 
`-Dio.ktor.development=true`.

The output path can be set using the `--output` argument followed by the
path to output to. By default this path is `./output`.


