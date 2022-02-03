package br.com.davidmag.bingewatcher.presentation.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.testcommons.EspressoIdlingResource
import br.com.davidmag.bingewatcher.testcommons.StringMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {

    private var activityScenario : ActivityScenario<HomeActivity>? = null

    @Before
    fun before(){
        EspressoIdlingResource.clearCounting()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.counting)
        activityScenario = ActivityScenario.launch(HomeActivity::class.java)
    }

    @After
    fun after(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.counting)
    }

    @Test
    fun when_searching_text_return_shows_with_text() {
        onView(withId(R.id.searchView)).perform(typeText("beat\n"))

        onView(withId(R.id.home_recycler)).check(
            ViewAssertions.selectedDescendantsMatch(
                withId(R.id.show_title),
                withText(StringMatcher("(?i)(?<=\\s|^|\\W)beat(?=\\s|\$|\\W)"))
            )
        )
    }
}