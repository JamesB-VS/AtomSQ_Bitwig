package com.presonus.handler;

public class DoNothing implements Runnable {
    public void run() {
        // The run method does nothing.
    }

    public static void main(String[] args) {
        // Creating an instance of the DoNothing class
        DoNothing doNothing = new DoNothing();

        // Creating a thread from the runnable
        Thread thread = new Thread(doNothing);

        // Starting the thread
        thread.start();
    }
}