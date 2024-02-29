package com.college.converter;

// Import statements for Espresso testing
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class) // Run with AndroidJUnit4 test runner
public class MainActivityTest2 {

    @Rule
    // Rule to launch the activity before each test method
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test // Annotation to indicate this method is a test case
    public void mainActivityTest2() {
        // Simulate a delay to wait for the app to load. This is not recommended for real tests.
        // Instead, use Espresso idling resources for synchronization.
        try {
            Thread.sleep(5161);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the EditText, click on it to ensure it's focused.
        ViewInteraction appCompatEditText = onView(withId(R.id.entryId));
        appCompatEditText.perform(click());

        // Replace the text in the EditText with "135" and close the soft keyboard
        ViewInteraction appCompatEditText2 = onView(withId(R.id.entryId));
        appCompatEditText2.perform(replaceText("135"), closeSoftKeyboard());

        // Press the IME action button (usually "Done" or "Enter") on the keyboard
        ViewInteraction appCompatEditText3 = onView(withId(R.id.entryId));
        appCompatEditText3.perform(pressImeActionButton());

        // Find the convert button by its ID and perform a click action
        ViewInteraction materialButton = onView(withId(R.id.convertButton));
        materialButton.perform(click());

        // Assert that the TextView shows the correct conversion result
        ViewInteraction textView = onView(withId(R.id.resultId));
        textView.check(matches(withText("108.0 Euros")));
    }

    // Helper method to find a child view at a specific position within a parent view
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
