package com.bavelsoft.broccolies;

import static com.bavelsoft.broccolies.util.RegressionUtil.ru;
import com.bavelsoft.broccolies.annotation.FluentActor;
import com.bavelsoft.broccolies.annotation.FluentKey;
import com.bavelsoft.broccolies.annotation.FluentNestedSender;
import com.bavelsoft.broccolies.annotation.FluentSender;
import com.bavelsoft.broccolies.reg.J4RegBase;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@FluentActor(value="RecTestActor")
public class RecTest extends J4RegBase {
	static MySender sender;
	static RecTestActor actor;
	static int x, y;

	@org.junit.After public void clearReferences() { actor.clearReferences(); }

	@FluentSender(value=MySender.class, reference=MyReference.class)
	@BeforeClass
	public static void setup() {
		sender = new MySender();
		actor = new RecTestActor();
		actor._RecTest_MySenderToSystemUnderTest = s->{x=s.x; y=s.y;};
	}

	static class MySender {
		int x;
		int y;

		public void setX(int x) { this.x = x; }
		public void setY(int y) { this.y = y; }
	}

	static class MyReference {
		@FluentKey public int x = 123;
		int y;
		MyReference() { y=1; }
		MyReference(MyReference prev) { y=prev.y+1; }
	}

	@Test
	public void simpleReference() {
		actor.sendMySender().send();
		assertEquals(123, x);
		assertEquals(1, y);
	}

	@Test
	public void simpleReferenceAcrossTests() {
		actor.sendMySender().send();
		assertEquals(123, x);
		assertEquals(1, y);
	}

	@Test
	public void chainedReference() {
		actor.sendMySender().send();
		actor.sendMySender().send();
		assertEquals(2, y);
	}
}
