package org.washcode.washpang

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher

class CustomLogger : TestWatcher{
    override fun testSuccessful(context: ExtensionContext) {
        println("너는 통과해라! -> " + context.displayName + " check!")
    }

    override fun testFailed(context: ExtensionContext, cause: Throwable?) {
        println("넌 못지나간다;; -> " + context.displayName + " fail...")
    }
}