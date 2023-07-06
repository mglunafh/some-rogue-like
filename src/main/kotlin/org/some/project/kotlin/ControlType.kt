package org.some.project.kotlin

sealed class ControlType {

    object PlayerControl: ControlType()

    object ComputerControl: ControlType()
}