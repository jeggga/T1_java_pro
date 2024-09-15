package org.example.service;

import org.example.annotation.AfterSuite;
import org.example.annotation.AfterTest;
import org.example.annotation.BeforeSuite;
import org.example.annotation.BeforeTest;
import org.example.annotation.CsvSource;
import org.example.annotation.Test;
import org.example.exception.AnnotationAfterSuiteException;
import org.example.exception.AnnotationBeforeSuiteException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;

public class TestRunner {
    public static void runTests(TestService testService) {
        Class<? extends TestService> aClass = testService.getClass();

        Method[] methods = aClass.getMethods();


        checkBeforeSuitAnnotation(methods);
        checkAfterSuitAnnotation(methods);

        executeStaticMethod(methods, testService);

        executeTests(methods, testService);

        executeMethodWithParam(methods, testService);
    }

    private static void checkBeforeSuitAnnotation(Method[] methods) {
        int count = 0;
        for (Method method : methods) {
            if (count > 1) {
                throw new AnnotationBeforeSuiteException(String.format("Methods with annotation %s more than 1", BeforeSuite.class.getSimpleName()));
            }
            if (method.getAnnotation(BeforeSuite.class) != null) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new AnnotationBeforeSuiteException("Methods should be static");
                }
                count++;
            }
        }
    }

    private static void checkAfterSuitAnnotation(Method[] methods) {
        int count = 0;
        for (Method method : methods) {
            if (count > 1) {
                throw new AnnotationAfterSuiteException(String.format("Methods with annotation %s more than 1", AfterSuite.class.getSimpleName()));
            }
            if (method.getAnnotation(AfterSuite.class) != null) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new AnnotationAfterSuiteException("Methods should be static");
                }
                count++;
            }
        }
    }

    private static void executeStaticMethod(Method[] methods, TestService testService) {
        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null || method.getAnnotation(AfterSuite.class) != null) {
                try {
                    method.invoke(testService);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private static void executeTests(Method[] methods, TestService testService) {
        Supplier<Integer> beforeTestSupplier = null;
        Supplier<Integer> afterTestSupplier = null;

        for (Method method : methods) {
            if (method.getAnnotation(BeforeTest.class) != null) {
                beforeTestSupplier = () -> {
                   try{
                       method.invoke(testService);
                       return 0;
                   } catch (Exception ex) {
                       throw new RuntimeException(ex);
                   }
                };
            } else if (method.getAnnotation(AfterTest.class) != null) {
                afterTestSupplier = () -> {
                    try{
                        method.invoke(testService);
                        return 0;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };
            }
        }

        Supplier<Integer> finalBeforeTestSupplier = beforeTestSupplier;
        Supplier<Integer> finalAfterTestSupplier = afterTestSupplier;
        Arrays.stream(methods)
                .filter(method -> method.getAnnotation(Test.class) != null)
                .sorted(Comparator.comparingInt(o -> o.getAnnotation(Test.class).priority()))
                .forEach(method -> {
                    try {
                        if (Objects.nonNull(finalBeforeTestSupplier)) {
                            finalBeforeTestSupplier.get();
                        }
                        method.invoke(testService);
                        if (Objects.nonNull(finalAfterTestSupplier)) {
                            finalAfterTestSupplier.get();
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static void executeMethodWithParam(Method[] methods, TestService testService) {
        for (Method method : methods) {
            if (method.getAnnotation(CsvSource.class) != null) {
                String[] strArr = method.getAnnotation(CsvSource.class).str().replace(" ", "").split(",");
                try {
                    int a = Integer.parseInt(strArr[0]);
                    String b = strArr[1];
                    int c = Integer.parseInt(strArr[2]);
                    boolean d = Boolean.parseBoolean(strArr[3]);
                    method.invoke(testService, a, b, c, d);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
