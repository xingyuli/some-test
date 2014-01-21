package org.swordess.test.sample;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.swordess.test.Boundary;
import org.swordess.test.Cover;
import org.swordess.test.CoverChecker;
import org.swordess.test.EquivalentCondition;
import org.swordess.test.EquivalentCondition.Condition;
import org.swordess.test.TestCaseAnalysis;
import org.swordess.test.TestCaseAnalysis.MethodAnalysis;

@TestCaseAnalysis({
	@MethodAnalysis(
		signature = ArrayUtilsTest.SIGNATURE_EQUALS,
		equivalentConditions = {
			@EquivalentCondition(
				name    = "source array",
				valid   = @Condition(nbr = 1, desc = "any")
			),
			@EquivalentCondition(
				name    = "start point of source array",
				valid   = @Condition(nbr = 2, desc = "[0, src.length)"),
				invalid = {
					@Condition(nbr = 3, desc = "(?, -1]"),
					@Condition(nbr = 4, desc = "[src.length, ?)")
				}
			),
			@EquivalentCondition(
				name  = "destination array",
				valid = @Condition(nbr = 5, desc = "any")
			),
			@EquivalentCondition(
				name    = "start point of destination array",
				valid   = @Condition(nbr = 6, desc = "[0, dest.length)"),
				invalid = {
					@Condition(nbr = 7, desc = "(?, -1]"),
					@Condition(nbr = 8, desc = "[dest.length, ?)")
				}
			),
			@EquivalentCondition(
				name    = "number of elements to compare",
				valid   = {
					@Condition(nbr = 9, desc = "> 0"),
					@Condition(nbr = 10, desc = "srcOffset+length <= src.length(10)"),
					@Condition(nbr = 11, desc = "destOffset+length <= dest.length(11)")
				},
				invalid = {
					@Condition(nbr = 12, desc = "<= 0"),
					@Condition(nbr = 13, desc = "srcOffset+length > src.length(13)"),
					@Condition(nbr = 14, desc = "destOffset+length > dest.length(14)")
				}
			)
		},
		boundaries = {
			@Boundary(nbr = 1,  desc = "null source array"),
			@Boundary(nbr = 2,  desc = "empty source array"),
			@Boundary(nbr = 3,  desc = "source array with one element"),
			@Boundary(nbr = 4,  desc = "srcOffset = -1"),
			@Boundary(nbr = 5,  desc = "srcOffset = 0"),
			@Boundary(nbr = 6,  desc = "srcOffset = 1"),
			@Boundary(nbr = 7,  desc = "srcOffset = src.length - 1"),
			@Boundary(nbr = 8,  desc = "srcOffset = src.length"),
			@Boundary(nbr = 9,  desc = "srcOffset = src.length + 1"),
			@Boundary(nbr = 10, desc = "null destination array"),
			@Boundary(nbr = 11, desc = "empty destination array"),
			@Boundary(nbr = 12, desc = "destination array with one element"),
			@Boundary(nbr = 13, desc = "destOffset = -1"),
			@Boundary(nbr = 14, desc = "destOffset = 0"),
			@Boundary(nbr = 15, desc = "destOffset = 1"),
			@Boundary(nbr = 16, desc = "destOffset = dest.length - 1"),
			@Boundary(nbr = 17, desc = "destOffset = dest.length"),
			@Boundary(nbr = 18, desc = "destOffset = dest.length + 1"),
			@Boundary(nbr = 19, desc = "length = -1"),
			@Boundary(nbr = 20, desc = "length = 0"),
			@Boundary(nbr = 21, desc = "length = 1"),
			@Boundary(nbr = 22, desc = "length = src.length - srcOffset"),
			@Boundary(nbr = 23, desc = "length = src.length - srcOffset - 1"),
			@Boundary(nbr = 24, desc = "length = src.length - srcOffset + 1"),
			@Boundary(nbr = 25, desc = "length = dest.length - destOffset"),
			@Boundary(nbr = 26, desc = "length = dest.length - destOffset - 1"),
			@Boundary(nbr = 27, desc = "length = dest.length - destOffset + 1"),
			@Boundary(nbr = 28, desc = "no elements equal"),
			@Boundary(nbr = 29, desc = "only the first element equals"),
			@Boundary(nbr = 30, desc = "all elements equal but the last one")
		}
	)
})
@RunWith(CoverChecker.class)
public class ArrayUtilsTest {
	
	static final String SIGNATURE_EQUALS = "equals(byte[], int, byte[], int, int)"; 
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src = null", "dest = null"}, validECs = {1,5}, boundaries = {1,10})
	@Test
	public void testConditionEquals1() {
		assertTrue(ArrayUtils.equals(null, anyInt(), null, anyInt(), anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src = null", "dest = any"}, boundaries = 11)
	@Test
	public void testConditionEquals2() {
		assertTrue(!ArrayUtils.equals(null, anyInt(), new byte[0], anyInt(), anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src = any not null", "dest = null"}, boundaries = 2)
	@Test
	public void testConditionEquals3() {
		assertTrue(!ArrayUtils.equals(new byte[0], anyInt(), null, anyInt(), anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src.length = 0", "dest.length = 0"})
	@Test
	public void testConditionEquals4() {
		assertTrue(ArrayUtils.equals(new byte[0], anyInt(), new byte[0], anyInt(), anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src.length = 0", "dest.length > 0"}, validECs = 6, boundaries = {12,14,16})
	@Test
	public void testConditionEquals5() {
		assertTrue(!ArrayUtils.equals(new byte[0], anyInt(), new byte[]{1}, 0, anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = {"src.length > 0", "dest.length = 0"}, validECs = 2, boundaries = {3,5,7})
	@Test
	public void testConditionEquals6() {
		assertTrue(!ArrayUtils.equals(new byte[]{1}, 0, new byte[0], anyInt(), anyInt()));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "srcOffset < 0", invalidECs = 3, boundaries = 4)
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testConditionEquals7() {
		ArrayUtils.equals(new byte[]{1}, -1, new byte[]{2}, 0, 1);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "srcOffset >= src.length", invalidECs = 4, boundaries = {6,8})
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testConditionEquals8() {
		ArrayUtils.equals(new byte[]{1}, 1, new byte[]{2}, 0, 1);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "destOffset < 0", invalidECs = 7, boundaries = 13)
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testConditionEquals9() {
		ArrayUtils.equals(new byte[]{2}, 0, new byte[]{1}, -1, 1);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "destOffset >= dest.length", invalidECs = 8, boundaries = {15,17})
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testConditionEquals10() {
		ArrayUtils.equals(new byte[]{2}, 0, new byte[]{1}, 1, 1);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "length <= 0", invalidECs = 12, boundaries = 20)
	@Test(expected = IllegalArgumentException.class)
	public void testConditionEquals11() {
		ArrayUtils.equals(new byte[]{2}, 0, new byte[]{1}, 0, 0);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "srcOffset + length > src.length", invalidECs = 13, boundaries = 24)
	@Test(expected = IllegalArgumentException.class)
	public void testConditionEquals12() {
		ArrayUtils.equals(new byte[]{2}, 0, new byte[]{1,2}, 0, 2);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "destOffset + length > dest.length", invalidECs = 14, boundaries = 27)
	@Test(expected = IllegalArgumentException.class)
	public void testConditionEquals13() {
		ArrayUtils.equals(new byte[]{1,2}, 0, new byte[]{2}, 0, 2);
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "src[i] != dest[i]", validECs = {9,10,11}, boundaries = {22,26,30})
	@Test
	public void testConditionEquals14() {
		assertTrue(!ArrayUtils.equals(
				new byte[]{1,1,2,3,5,8}, 1,
				new byte[]{1,2,3,5,7,9}, 0,
				5
			));
	}
	
	@Cover(
		methodSignature = SIGNATURE_EQUALS,
		conditions = "src[any] = dest[any]", boundaries = {23,25})
	@Test
	public void testConditionEquals15() {
		assertTrue(ArrayUtils.equals(
				new byte[]{1,3,5,7,9,11}, 1,
				new byte[]{1,2,3,5,7,9}, 2,
				4
			));
	}
	
	@Cover(methodSignature = SIGNATURE_EQUALS, boundaries = 9)
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoundaryEquals1() {
		ArrayUtils.equals(new byte[]{1,2}, 3, new byte[]{1}, 0, 1);
	}
	
	@Cover(methodSignature = SIGNATURE_EQUALS, boundaries = 18)
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoundaryEquals2() {
		ArrayUtils.equals(new byte[]{1}, 0, new byte[]{1,2}, 3, 1);
	}
	
	@Cover(methodSignature = SIGNATURE_EQUALS, boundaries = 19)
	@Test(expected = IllegalArgumentException.class)
	public void testBoundaryEquals3() {
		ArrayUtils.equals(new byte[]{1,2}, 0, new byte[]{1,2}, 0, -1);
		Mockito.anyInt();
	}
	
	@Cover(methodSignature = SIGNATURE_EQUALS, boundaries = {21,28})
	@Test
	public void testBoundaryEquals4() {
		assertTrue(!ArrayUtils.equals(new byte[]{1}, 0, new byte[2], 0, 1));
	}
	
	@Cover(methodSignature = SIGNATURE_EQUALS, boundaries = {29})
	@Test
	public void testBoundaryEquals5() {
		assertTrue(!ArrayUtils.equals(
				new byte[] { 1, 10, 20, 30 }, 1,
				new byte[] { 1, 1, 2, 3, 5 }, 2,
				2
			));
	}
	
}
