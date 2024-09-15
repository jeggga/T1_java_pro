package org.example;


import org.example.service.TestRunner;
import org.example.service.TestService;

public class Main {

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        TestService testService = new TestService();

        testRunner.runTests(testService);
    }
}