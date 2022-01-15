package test.test;

import java.util.Arrays;

public class Test2 {
    public static void main(String[] args) {
        int x = 0;
        x = occOf15(new int[]{15, 0, -5, 15, 4, 8, 14, 15}); // returns 3
        System.out.println(x);
        x = occOf15(new int[]{});  // returns 0
        System.out.println(x);
        x = occOf15(new int[]{1}); // returns 0
        System.out.println(x);
        x = occOf15(new int[]{15, 0, 15, 0, 0}); // returns 2
        System.out.println(x);
        x = occOf15(new int[]{15}); // returns 1
        System.out.println(x);
        x = occOf15(new int[]{15, 15, 15, 15, 15}); // returns 5
        System.out.println(x);
        System.out.println();
        int last = lstStr(new String[]{"x", "moo", "dog", "x", "zzz", "rr"}, "x");
        System.out.println(last);
    }

    public static int occOf15(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int x = 0;
        if (nums[len - 1] == 15)
            x = 1;
        return occOf15(Arrays.copyOf(nums, len - 1)) + x;
    }

    public static int lstStr(String[] arr, String key) {
        int len = arr.length;
        if (arr[len - 1] == key) {
            return len - 1;
        }
        return lstStr(Arrays.copyOf(arr, len - 1), key);
    }
}
