package com.ggg.common.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by nguyenminhtuan on 21/06/2018.
 */
class RxBus {
    private val publisher = PublishSubject.create<Any>()
    private var busDownloadImageDone = PublishSubject.create<HashMap<String, Long>>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun toObservableDownloadImageDone() : Observable<HashMap<String, Long>> {
        return busDownloadImageDone
    }

    fun sendDownloadImageDone(hm: HashMap<String, Long>) {
        busDownloadImageDone.onNext(hm)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

}