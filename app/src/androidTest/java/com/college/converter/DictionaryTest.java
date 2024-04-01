package com.college.converter;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
/**
 * This DictionaryTest includes 4 tests.(Another test is in the MainToDictonaryActivityTest Class)
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DictionaryTest {

    @Rule
    public ActivityScenarioRule<Dictionary> mActivityScenarioRule =
            new ActivityScenarioRule<>(Dictionary.class);

    /**
     * This function check if user enter "tea" for looking up the definition,
     * the result will be get from internet and displayed the first definition
     * that should be "A drug smoked or ingested for euphoric effect, cannabis.".
     */
    @Test
    public void dictionaryLookUpTest() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText_word));
        appCompatEditText.perform(replaceText("tea"), closeSoftKeyboard());


        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button_search), withText("Look up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.record), withText("A drug smoked or ingested for euphoric effect, cannabis."),
                        withParent(withParent(withId(R.id.recycleView_def))),
                        isDisplayed()));
        textView.check(matches(withText("A drug smoked or ingested for euphoric effect, cannabis.")));
    }
    /**
     * This function check if user click save button, the word should be stored in search history list.
     */
    @Test
    public void dictionarySaveTest() {

       ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText_word),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("tea"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button_search), withText("Look up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.button_save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button_read), withText("View"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.record), withText("tea"),
                        withParent(withParent(withId(R.id.recycleView))),
                        isDisplayed()));
        textView.check(matches(withText("tea")));
    }

    /**
     * This function check if user click help toolbar, a AlertDialog which including the API instruction
     * content pops up
     */
    @Test
    public void dictionaryHelpMenuTest() {

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.help), withContentDescription("Help"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.message), withText("The APP support user to look up the definitions of word which is entered by the user. User can click save button to save the search term and definitions in the database and click view button to see the history of the search term and definitions."),
                        withParent(withParent(withId(com.google.android.material.R.id.scrollView))),
                        isDisplayed()));
        textView.check(matches(withText("The APP support user to look up the definitions of word which is entered by the user. User can click save button to save the search term and definitions in the database and click view button to see the history of the search term and definitions.")));
    }

    /**
     * This function check if user click the "back-home" button, the app will go back to the main activity.
     */
    @Test
    public void dictionaryBackHomeTest() {


        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                1),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView2), withText("Welcome"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Welcome")));
    }

//    /**
//     * This function check if user undo the deleting, the history word should display at the same position on the screen.
//     */
//    @Test
//    public void dictionaryUndoTest() {
//
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.editText_word),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText.perform(click());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.editText_word),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText2.perform(replaceText("tea"), closeSoftKeyboard());
//
//        pressBack();
//
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.button_search), withText("Look up"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                4),
//                        isDisplayed()));
//        materialButton.perform(click());
//
//        ViewInteraction materialButton2 = onView(
//                allOf(withId(R.id.button_save), withText("Save"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                5),
//                        isDisplayed()));
//        materialButton2.perform(click());
//
//        ViewInteraction materialButton3 = onView(
//                allOf(withId(R.id.button_read), withText("View"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                6),
//                        isDisplayed()));
//        materialButton3.perform(click());
//
//        ViewInteraction appCompatEditText3 = onView(
//                allOf(withId(R.id.editText_word), withText("tea"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText3.perform(replaceText("see"));
//
//        ViewInteraction appCompatEditText4 = onView(
//                allOf(withId(R.id.editText_word), withText("see"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatEditText4.perform(closeSoftKeyboard());
//
//        ViewInteraction materialButton4 = onView(
//                allOf(withId(R.id.button_search), withText("Look up"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                4),
//                        isDisplayed()));
//        materialButton4.perform(click());
//
//        ViewInteraction materialButton5 = onView(
//                allOf(withId(R.id.button_save), withText("Save"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                5),
//                        isDisplayed()));
//        materialButton5.perform(click());
//
//        ViewInteraction recyclerView = onView(
//                allOf(withId(R.id.recycleView),
//                        childAtPosition(
//                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                8)));
//        recyclerView.perform(actionOnItemAtPosition(1, longClick()));
//
//        ViewInteraction materialButton6 = onView(
//                allOf(withId(android.R.id.button1), withText("Yes"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(com.google.android.material.R.id.buttonPanel),
//                                        0),
//                                3)));
//        materialButton6.perform(scrollTo(), click());
//
//        ViewInteraction materialButton7 = onView(
//                allOf(withId(com.google.android.material.R.id.snackbar_action), withText("Undo"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("com.google.android.material.snackbar.Snackbar$SnackbarLayout")),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton7.perform(click());
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.record), withText("see"),
//                        withParent(withParent(withId(R.id.recycleView))),
//                        isDisplayed()));
//        textView.check(matches(withText("see")));
//    }

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
