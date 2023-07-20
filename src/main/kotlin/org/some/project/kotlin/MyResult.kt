package org.some.project.kotlin

class MyResult<out T>(private val value: Any) {

    val isSuccess: Boolean
        get() = value !is Failure

    val isFailure: Boolean
        get() = value is Failure

    fun getOrNull(): T? =
        when (value) {
            is Failure -> null
            else -> value as T
        }

    fun errorMessageOrNull(): String? =
        when(value) {
            is Failure -> value.message
            else -> null
        }

    companion object {

        fun <T: Any> success(value: T): MyResult<T> = MyResult(value)

        fun <T> failure(errorMessage: String): MyResult<T> = MyResult(Failure(errorMessage))

        internal class Failure(val message: String) {
            override fun equals(other: Any?): Boolean = other is Failure && message == other.message
            override fun hashCode(): Int = message.hashCode()
            override fun toString(): String = "Failure($message)"
        }
    }
}
