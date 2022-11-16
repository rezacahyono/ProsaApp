package com.rchyn.prosa.utils

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

object Utilities {

    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
        Mockito.`when`(methodCall)

}