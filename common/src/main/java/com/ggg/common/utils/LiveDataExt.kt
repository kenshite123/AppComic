package com.ggg.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


fun <X, Y> LiveData<X>.map(func: (X?) -> Y?): MutableLiveData<Y?> {
    return MediatorLiveData<Y>().apply {
        addSource(this@map) { x -> value = func(x) }
    }
}

fun <X, Y> LiveData<X>.mapSkipNulls(func: (X) -> Y): MutableLiveData<Y> {
    return MediatorLiveData<Y>().apply {
        addSource(this@mapSkipNulls) { x ->
            x ?: return@addSource
            value = func(x)
        }
    }
}

fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }
    }
}
fun <A, B, C> combineLatest(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>): LiveData<Triple<A?, B?, C?>> {

    fun Triple<A?, B?, C?>?.copyWithFirst(first: A?): Triple<A?, B?, C?> {
        if (this@copyWithFirst == null) return Triple<A?, B?, C?>(first, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Triple<A?, B?, C?>?.copyWithSecond(second: B?): Triple<A?, B?, C?> {
        if (this@copyWithSecond == null) return Triple<A?, B?, C?>(null, second, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Triple<A?, B?, C?>?.copyWithThird(third: C?): Triple<A?, B?, C?> {
        if (this@copyWithThird == null) return Triple<A?, B?, C?>(null, null, third)
        return this@copyWithThird.copy(third = third)
    }

    return MediatorLiveData<Triple<A?, B?, C?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
    }
}

fun <A, B, C, D> combineLatest(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>): LiveData<Quadruple<A?, B?, C?, D?>> {

    fun Quadruple<A?, B?, C?, D?>?.copyWithFirst(first: A?): Quadruple<A?, B?, C?, D?> {
        if (this@copyWithFirst == null) return Quadruple<A?, B?, C?, D?>(first, null, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Quadruple<A?, B?, C?, D?>?.copyWithSecond(second: B?): Quadruple<A?, B?, C?, D?> {
        if (this@copyWithSecond == null) return Quadruple<A?, B?, C?, D?>(null, second, null, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Quadruple<A?, B?, C?, D?>?.copyWithThird(third: C?): Quadruple<A?, B?, C?, D?> {
        if (this@copyWithThird == null) return Quadruple<A?, B?, C?, D?>(null, null, third, null)
        return this@copyWithThird.copy(third = third)
    }

    fun Quadruple<A?, B?, C?, D?>?.copyWithFourth(four: D?): Quadruple<A?, B?, C?, D?> {
        if (this@copyWithFourth == null) return Quadruple<A?, B?, C?, D?>(null, null, null, four)
        return this@copyWithFourth.copy(four = four)
    }

    return MediatorLiveData<Quadruple<A?, B?, C?, D?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
        addSource(d) { value = value.copyWithFourth(it) }
    }
}
public data class Quadruple<out A, out B, out C, out D>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D
) {
    public override fun toString(): String = "($first, $second, $third, $four)"
}

fun <A, B, C, D, E> combineLatest(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>): LiveData<Quintuple<A?, B?, C?, D?, E?>> {

    fun Quintuple<A?, B?, C?, D?, E?>?.copyWithFirst(first: A?): Quintuple<A?, B?, C?, D?, E?> {
        if (this@copyWithFirst == null) return Quintuple<A?, B?, C?, D?, E?>(first, null, null, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Quintuple<A?, B?, C?, D?, E?>?.copyWithSecond(second: B?): Quintuple<A?, B?, C?, D?, E?> {
        if (this@copyWithSecond == null) return Quintuple<A?, B?, C?, D?, E?>(null, second, null, null, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Quintuple<A?, B?, C?, D?, E?>?.copyWithThird(third: C?): Quintuple<A?, B?, C?, D?, E?> {
        if (this@copyWithThird == null) return Quintuple<A?, B?, C?, D?, E?>(null, null, third, null, null)
        return this@copyWithThird.copy(third = third)
    }

    fun Quintuple<A?, B?, C?, D?, E?>?.copyWithFourth(four: D?): Quintuple<A?, B?, C?, D?, E?> {
        if (this@copyWithFourth == null) return Quintuple<A?, B?, C?, D?, E?>(null, null, null, four, null)
        return this@copyWithFourth.copy(four = four)
    }
    fun Quintuple<A?, B?, C?, D?, E?>?.copyWithFive(five: E?): Quintuple<A?, B?, C?, D?, E?> {
        if (this@copyWithFive == null) return Quintuple<A?, B?, C?, D?, E?>(null, null, null, null, five)
        return this@copyWithFive.copy(five = five)
    }

    return MediatorLiveData<Quintuple<A?, B?, C?, D?, E?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
        addSource(d) { value = value.copyWithFourth(it) }
        addSource(e) { value = value.copyWithFive(it) }
    }
}

public data class Quintuple<out A, out B, out C, out D, out E>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five)"
}
fun <A, B, C, D, E, F> combineLatest(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>, f: LiveData<F>): LiveData<Sextuple<A?, B?, C?, D?, E?, F?>> {

    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithFirst(first: A?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithFirst == null) return Sextuple<A?, B?, C?, D?, E?, F?>(first, null, null, null, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithSecond(second: B?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithSecond == null) return Sextuple<A?, B?, C?, D?, E?, F?>(null, second, null, null, null, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithThird(third: C?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithThird == null) return Sextuple<A?, B?, C?, D?, E?, F?>(null, null, third, null, null, null)
        return this@copyWithThird.copy(third = third)
    }

    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithFourth(four: D?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithFourth == null) return Sextuple<A?, B?, C?, D?, E?, F?>(null, null, null, four, null, null)
        return this@copyWithFourth.copy(four = four)
    }
    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithFive(five: E?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithFive == null) return Sextuple<A?, B?, C?, D?, E?, F?>(null, null, null, null, five, null)
        return this@copyWithFive.copy(five = five)
    }
    fun Sextuple<A?, B?, C?, D?, E?, F?>?.copyWithSix(six: F?): Sextuple<A?, B?, C?, D?, E?, F?> {
        if (this@copyWithSix == null) return Sextuple<A?, B?, C?, D?, E?, F?>(null, null, null, null, null, six)
        return this@copyWithSix.copy(five = five)
    }

    return MediatorLiveData<Sextuple<A?, B?, C?, D?, E?, F?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
        addSource(d) { value = value.copyWithFourth(it) }
        addSource(e) { value = value.copyWithFive(it) }
        addSource(f) { value = value.copyWithSix(it) }
    }
}
public data class Sextuple<out A, out B, out C, out D, out E, out F>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E,
        public val six: F
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five, $six)"
}

public data class Septuple<out A, out B, out C, out D, out E, out F, out G>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E,
        public val six: F,
        public val seven: G
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five, $six, $seven)"
}

public data class Octuple<out A, out B, out C, out D, out E, out F, out G, out H>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E,
        public val six: F,
        public val seven: G,
        public val eight: H
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five, $six, $seven, $eight)"
}

public data class Nonuple<out A, out B, out C, out D, out E, out F, out G, out H, out I>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E,
        public val six: F,
        public val seven: G,
        public val eight: H,
        public val nine: I
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five, $six, $seven, $eight, $nine)"
}

public data class Decuple<out A, out B, out C, out D, out E, out F, out G, out H, out I, out J>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val four: D,
        public val five: E,
        public val six: F,
        public val seven: G,
        public val eight: H,
        public val nine: I,
        public val ten: J
) {
    public override fun toString(): String = "($first, $second, $third, $four, $five, $six, $seven, $eight, $nine, $ten)"
}