package itis.ru.kpfu.join.data.network.error

import io.reactivex.CompletableSource
import io.reactivex.functions.Function

interface CompletableErrorFunction: Function<Throwable, CompletableSource> {
}