package com.sergeysav.websitegenerator

data class Args(
    val hostServer: Boolean = false,
    val basePath: String = "./output"
) {
    companion object {
        fun parse(args: Array<String>): Args = Args(
            hostServer = args.isSet("server", 's'),
            basePath = args.getString("output") ?: "./output"
        )

        private fun Array<String>.isSet(full: String, short: Char, vararg aliases: String): Boolean {
            for (arg in this) {
                if (arg.matches(Regex("--$full"))) return true
                if (arg.matches(Regex("-$short"))) return true
                for (alias in aliases) {
                    if (arg.matches(Regex("--$alias"))) return true
                }
            }

            return false
        }

        private fun Array<String>.getString(full: String): String? {
            for (i in indices) {
                val arg = this[i]
                val next = if (i + 1 in indices) this[i+1] else continue

                if (arg.matches(Regex("--$full"))) return next
            }

            return null
        }
    }
}
