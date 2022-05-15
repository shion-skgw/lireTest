package com.skgw.lireTest.extension

import java.io.Closeable

inline fun <T1 : Closeable?, T2 : Closeable?, R> Pair<T1, T2>.use(block: (T1, T2) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this.first, this.second)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        if (this.second != null) this.second.closeFinally(exception)
        if (this.first != null) this.first.closeFinally(exception)
    }
}

inline fun <T1 : Closeable?, T2 : Closeable?, T3 : Closeable?, R> Triple<T1, T2, T3>.use(block: (T1, T2, T3) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this.first, this.second, this.third)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        if (this.third != null) this.third.closeFinally(exception)
        if (this.second != null) this.second.closeFinally(exception)
        if (this.first != null) this.first.closeFinally(exception)
    }
}

fun Closeable?.closeFinally(cause: Throwable?) {
    when {
        this == null -> {}
        cause == null -> close()
        else ->
            try {
                close()
            } catch (closeException: Throwable) {
                cause.addSuppressed(closeException)
            }
    }
}
