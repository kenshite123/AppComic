package com.ggg.common.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by nguyenminhtuan on 21/06/2018.
 */
class RxBus {
    private val publisher = PublishSubject.create<Any>()
    private var busDownloadImageSuccess = PublishSubject.create<Long>()
    private var busDownloadImageDone = PublishSubject.create<Long>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun toObservableDownloadImageSuccess() : Observable<Long> {
        return busDownloadImageSuccess
    }

    fun sendDownloadImageSuccess(comicId: Long) {
        busDownloadImageSuccess.onNext(comicId)
    }

    fun toObservableDownloadImageDone() : Observable<Long> {
        return busDownloadImageDone
    }

    fun sendDownloadImageDone(comicId: Long) {
        busDownloadImageDone.onNext(comicId)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

}