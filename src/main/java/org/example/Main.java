package org.example;


import org.example.practice_1.service.TestRunner;
import org.example.practice_1.service.TestService;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        TestRunner testRunner = new TestRunner();
        TestService testService = new TestService();

        testRunner.runTests(testService);
    }
}