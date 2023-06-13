package org.codehaus.jettison.json;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObjectTest extends TestCase {
    public void testNotEquals() throws Exception {
    	JSONObject aJsonObj = new JSONObject("{\"x\":\"y\"}");
    	JSONObject bJsonObj = new JSONObject("{\"x\":\"b\"}");
    	assertTrue(!aJsonObj.equals(bJsonObj));
    }

    public void testNullInQuotesGetString() throws Exception {
    	JSONObject obj = new JSONObject("{\"a\":\"null\"}");
    	assertEquals("null", obj.getString("a"));
    }

    public void testExplicitNullIsNull() throws Exception {
        JSONObject obj = new JSONObject("{\"a\":null}");
        assertTrue(obj.isNull("a"));
    }

    public void testMissingIsNull() throws Exception {
        JSONObject obj = new JSONObject("{\"a\":null}");
        assertTrue(obj.isNull("b"));
    }

    public void testSlashEscapingTurnedOnByDefault() throws Exception {
       JSONObject obj = new JSONObject();
       obj.put("key", "http://example.com/foo");
       assertEquals(obj.toString(), "{\"key\":\"http:\\/\\/example.com\\/foo\"}");
    }
    
    public void testMalformedObject() throws Exception {
       try {
           new JSONObject("{/");
           fail("Failure expected on malformed JSON");
       } catch (JSONException ex) {
           // expected
       }
    }

    public void testMalformedObject2() throws Exception {
        try {
            new JSONObject("{x");
            fail("Failure expected on malformed JSON");
        } catch (JSONException ex) {
            // expected
        }
    }

    public void testMalformedObject3() throws Exception {
        try {
            new JSONObject("{/x");
            fail("Failure expected on malformed JSON");
        } catch (JSONException ex) {
            // expected
        }
    }

    public void testMalformedObject4() throws Exception {
        try {
            new JSONObject("{/*");
            fail("Failure expected on malformed JSON");
        } catch (JSONException ex) {
            // expected
        }
    }

    public void testMalformedObject5() throws Exception {
        try {
            new JSONObject("{//");
            fail("Failure expected on malformed JSON");
        } catch (JSONException ex) {
            // expected
        }
    }

    public void testMalformedArray() throws Exception {
        try {
            new JSONObject("{[/");
            fail("Failure expected on malformed JSON");
        } catch (JSONException ex) {
            // expected
        }
    }

    // https://github.com/jettison-json/jettison/issues/52
    public void testIssue52() throws Exception {
        Map map = new HashMap();
        map.put("t", map);
        new JSONObject(map);
    }

    public void testIssue52B() throws Exception {
        int originalLimit = JSONObject.getRecursionDepthLimit();
        int limit = 10;
        JSONObject.setRecursionDepthLimit(limit);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit - 1; i++) {
            sb.append("{}, ");
        }
        sb.append("{a: " + (limit - 1) + "}");
        new JSONArray("[" + sb.toString() + "]");
        sb.append(", {}");
        try {
            new JSONArray("[" + sb.toString() + "]");
        } catch (JSONException e) {
            assertTrue(e.getMessage().contains("has reached recursion depth limit"));
            // expected
        }
        JSONObject.setRecursionDepthLimit(originalLimit);
    }

    // https://github.com/jettison-json/jettison/issues/52
    public void testIssue52Recursive() throws Exception {
        try {
            Map map = new HashMap();
            Map map2 = new HashMap();
            map.put("t", map2);
            map2.put("t", map);
            new JSONObject(map);
            fail("Failure expected");
        } catch (JSONException e) {
            assertTrue(e.getMessage().contains("JSONObject has reached recursion depth limit"));
            // expected
        }
    }

    // https://github.com/jettison-json/jettison/issues/45
    public void testFuzzerTestCase() throws Exception, JSONException {
        try {
            new JSONObject("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{\"G\":[30018084,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,38,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,0]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,340282366920938463463374607431768211458,6,1,1]}:[32768,1,1,6,1,0]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,340282366920938463463374607431768211458,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,9 68,1,127,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,6,32768,1,1,6,1,9223372036854775807]}:[3,1,6,32768,1,1,6,1,1]}:[3,1,10,32768,1,1,6,1,1]}");
            fail("Failure expected");
        } catch (JSONException ex) {
            // expected
        }
    }

    public void testFuzzerTestCase2() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("{");
        }
        try {
            new JSONObject(sb.toString());
            fail("Failure expected");
        } catch (JSONException e) {
            assertTrue(e.getMessage().contains("JSONTokener has reached recursion depth limit"));
            // expected
        }
    }

    // https://github.com/jettison-json/jettison/issues/60
    public void testIssue60() throws JSONException {
        List list = new ArrayList();
        list.add(list);
        try {
            new JSONArray(list);
            fail("Failure expected");
        } catch (JSONException ex) {
            assertEquals(ex.getMessage(), "JSONArray has reached recursion depth limit of 500");
        }
    }
}
