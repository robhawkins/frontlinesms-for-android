package net.frontlinesms.android;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class BasicTest {
  @Test public void testBasic() {
    Integer a = new Integer(7);
    assertThat(a.equals(7));
  }
}

