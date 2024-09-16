package org.example;


import org.example.service.TestRunner;
import org.example.service.TestService;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        TestRunner testRunner = new TestRunner();
        TestService testService = new TestService();

        testRunner.runTests(testService);
    }
}