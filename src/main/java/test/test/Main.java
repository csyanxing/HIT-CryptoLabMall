package test.test;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        CircularArrayQueue<String> queue = new CircularArrayQueue<>();
        queue.enqueue("R");
        queue.enqueue("A");
        queue.enqueue("E");
        queue.enqueue("A");
        queue.enqueue("R");
        isPalindrome(queue);
    }

    public static boolean isPalindrome(CircularArrayQueue<String> queue){
        String s = "";
        for(int i = 0; i < queue.size(); i++){
            s+=queue.dequeue();
        }
        return s.equals(s.reverse);
    }

    public static void queueLog(Iterator<CircularArrayQueue<String>> iter){
        int cnt = 0;
        int cntPar = 0;
        while (iter.hasNext()){
            cnt++;
            if(isPalindrome(iter.next())){
                cntPar++;
            }
        }
        System.out.println(cntPar+"of "+cnt+" queues are palindromes");
    }
}
