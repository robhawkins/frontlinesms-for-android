package net.frontlinesms.android;

import android.widget.Button;
import android.widget.TextView;

import net.frontlinesms.android.activity.KeywordList;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.tester.android.view.TestMenu;
import org.robolectric.tester.android.view.TestMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricGradleTestRunner.class)
public class BasicTest {
    private KeywordList keywordList;
    private Button pressMeButton;
    private TextView results;

    @Before
    public void setUp() throws Exception {
        KeywordList keywordList = Robolectric.buildActivity(KeywordList.class).create().get();
    }

    @Test
    public void testBasic() throws Exception {
        TestMenu menu = new TestMenu();
        keywordList.onPrepareOptionsMenu(menu);
        TestMenuItem saveMenuItem = (TestMenuItem) menu.getItem(0);
        assertThat(saveMenuItem.isEnabled(), equalTo(true));
    }
}

