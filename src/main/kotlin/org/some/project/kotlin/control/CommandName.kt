package org.some.project.kotlin.control

interface CommandName {

    // TODO: think of a list of aliases for a command to match by any of them
    val commandName: String

    val description: String

    val needsTarget: Boolean

}
