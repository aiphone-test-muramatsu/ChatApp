package jp.co.aiphone.chatapp.chatfunc


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import jp.co.aiphone.chatapp.MainActivity
import jp.co.aiphone.chatapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// 参考
// 「What is the purpose of @SmallTest, @MediumTest, and @LargeTest annotations in Android?」
// https://stackoverflow.com/questions/4671923/what-is-the-purpose-of-smalltest-mediumtest-and-largetest-annotations-in-an
// 該当のテストでどの範囲で機能を使用するかを示す。何に使っているのかまでは調べられていない
@LargeTest
// @RunWith is required only if you use a mix of JUnit3 and JUnit4.
// →JUnit3を使用しない限り、必須ではない
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    // 参考
    // 「JUnit - @Rule・@ClassRule使用方法の備忘録」
    // https://qiita.com/Chayata/items/dd31e22bca6df6c91793
    @Rule
    // https://qiita.com/Shotaro_Mori_/items/eaf10fa34b8f44735b51
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun loginTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // onView等の基本メソッド参考
        // 「Espresso の基本」
        // https://developer.android.com/training/testing/espresso/basics?hl=ja
        val appCompatEditText = onView(
                allOf(withId(R.id.textMail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.textMail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)


        val appCompatEditText3 = onView(
                allOf(withId(R.id.textMail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("test@test.com"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
                allOf(withId(R.id.textMail), withText("test@test.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText4.perform(pressImeActionButton())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.textPass),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("test12"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
                allOf(withId(R.id.textPass), withText("test12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()))
        appCompatEditText6.perform(pressImeActionButton())

        val appCompatButton = onView(
                allOf(withId(R.id.buttonLogin), withText("ログイン"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // ログインが成功したことを確認
        // 参考
        // レシピ
        //https://developer.android.com/training/testing/espresso/recipes?hl=ja#kotlin

        onView(withId(R.id.textMail)).check(ViewAssertions.doesNotExist())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
