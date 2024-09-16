package org.example.service;

import org.example.annotation.AfterSuite;
import org.example.annotation.AfterTest;
import org.example.annotation.BeforeSuite;
import org.example.annotation.BeforeTest;
import org.example.annotation.CsvSource;
import org.example.annotation.Test;

public class TestService {

    @BeforeTest
    public void beforeTest() {
        System.out.println("BT-----------------------------------");
    }

//    @BeforeTest
//    public void beforeTest1() {
//        System.out.println("BT11111111-----------------------------------");
//    }

    @AfterTest
    public void afterTest() {
        System.out.println("AT-----------------------------------\n");
    }

    @BeforeSuite
    public static void method1() {
        System.out.printf("Execute method with annotation %s%n", BeforeSuite.class.getSimpleName());
    }

    @AfterSuite
    public static void method2() {
        System.out.printf("Execute method with annotation %s%n", AfterSuite.class.getSimpleName());
    }

//    @BeforeSuite
//    public void method3() {}

//    @AfterSuite
//    public void method4() {}

    @Test(priority = 1)
    public void methodPriority1() {
        System.out.println("Execute method with priority 1");
    }

    @Test(priority = 2)
    public void methodPriority2() {
        System.out.println("Execute method with priority 2");
    }

    @Test(priority = 3)
    public void methodPriority3() {
        System.out.println("Execute method with priority 3");
    }

    @Test(priority = 4)
    public void methodPriority4() {
        System.out.println("Execute method with priority 4");
    }

    @Test
    public void methodPriority5() {
        System.out.println("Execute method with priority 5");
    }

    @Test(priority = 6)
    public void methodPriority6() {
        System.out.println("Execute method with priority 6");
    }

    @Test(priority = 7)
    public void methodPriority7() {
        System.out.println("Execute method with priority 7");
    }

    @Test(priority = 8)
    public void methodPriority8() {
        System.out.println("Execute method with priority 8");
    }

    @Test(priority = 9)
    public void methodPriority9() {
        System.out.println("Execute method with priority 9");
    }

    @Test(priority = 10)
    public void methodPriority10() {
        System.out.println("Execute method with priority 10");
    }

    @CsvSource(str = "10,    Java, 20, true")
    public void methodParse(int a, String b, int c, boolean d) {
        System.out.printf("a = %s%n", a);
        System.out.printf("b = %s%n", b);
        System.out.printf("c = %s%n", c);
        System.out.printf("d = %s%n", d);
    }
}
