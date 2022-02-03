package br.com.davidmag.bingewatcher.testcommons

import org.hamcrest.BaseMatcher
import org.hamcrest.Description


class StringMatcher(private val matcher: String) : BaseMatcher<String>() {
    override fun describeTo(description: Description?) { }

    override fun matches(item: Any?): Boolean {
        if(item !is String) return false
        return matcher.toRegex().containsMatchIn(item)
    }
}