package br.com.davidmag.bingewatcher.presentation.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.davidmag.bingewatcher.testcommons.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ShowActivityTest {

    private var activityScenario : ActivityScenario<ShowActivity>? = null

    @Before
    fun before(){
        EspressoIdlingResource.clearCounting()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.counting)
        activityScenario = ActivityScenario.launch(ShowActivity::class.java)
    }

    @After
    fun after(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.counting)
    }

    @Test
    fun given_favorite_unselected_when_favorite_clicked_then_favorite_gets_selected() {

    }
}