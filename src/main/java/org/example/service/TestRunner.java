package org.example.service;

import org.example.annotation.AfterSuite;
import org.example.annotation.AfterTest;
import org.example.annotation.BeforeSuite;
import org.example.annotation.BeforeTest;
import org.example.annotation.CsvSource;
import org.example.annotation.Test;
import org.example.exception.AnnotationAfterSuiteException;
import org.example.exception.AnnotationBeforeSuiteException;
import org.example.exception.AnnotationBeforeTestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class TestRunner {
    public static void runTests(TestService testService) {
        Class<? extends TestService> aClass = testService.getClass();

        Method[] methods = aClass.getDeclaredMethods();

        Map<String, List<Method>> interfaceMethodsMap = getInterfaceMethodsMap(methods);


        executeStaticMethod(BeforeSuite.class.getSimpleName(), interfaceMethodsMap, testService);
        executeStaticMethod(AfterSuite.class.getSimpleName(), interfaceMethodsMap, testService);

        executeTests(interfaceMethodsMap, testService);

        executeMethodWithParam(interfaceMethodsMap, testService);
    }

    private static Map<String, List<Method>> getInterfaceMethodsMap(Method[] methods) {
        Map<String, List<Method>> interfaceMethodsMap = new HashMap<>();
        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) {
                addToMap(interfaceMethodsMap, BeforeSuite.class.getSimpleName(), method);
            } else if (method.getAnnotation(AfterSuite.class) != null) {
                addToMap(interfaceMethodsMap, AfterSuite.class.getSimpleName(), method);
            } else if (method.getAnnotation(BeforeTest.class) != null) {
                addToMap(interfaceMethodsMap, BeforeTest.class.getSimpleName(), method);
            } else if (method.getAnnotation(AfterTest.class) != null) {
                addToMap(interfaceMethodsMap, AfterTest.class.getSimpleName(), method);
            } else if (method.getAnnotation(Test.class) != null) {
                addToMap(interfaceMethodsMap, Test.class.getSimpleName(), method);
            } else if (method.getAnnotation(CsvSource.class) != null) {
                addToMap(interfaceMethodsMap, CsvSource.class.getSimpleName(), method);
            }
        }
        return interfaceMethodsMap;
    }

    private static void addToMap(Map<String, List<Method>> interfaceMethodsMap, String key, Method value) {
        if (interfaceMethodsMap.get(key) != null) {
            interfaceMethodsMap.get(key).add(value);
        } else {
            interfaceMethodsMap.put(key, new ArrayList<>(List.of(value)));
        }
    }

    private static void executeStaticMethod(String simpleNameInterface,
                                            Map<String, List<Method>> interfaceMethodsMap,
                                            TestService testService) {
        List<Method> methods = interfaceMethodsMap.get(simpleNameInterface);
        if (Objects.nonNull(methods) && !methods.isEmpty()) {
            if (methods.size() > 1) {
                throw new AnnotationAfterSuiteException(String.format("Methods with annotation %s more than 1", simpleNameInterface));
            }

            try {
                Method method = methods.get(0);
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new AnnotationAfterSuiteException("Methods should be static");
                }
                method.invoke(testService);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void executeTests(Map<String, List<Method>> interfaceMethodsMap,
                                     TestService testService) {
        List<Supplier<Integer>> beforeTestSuppliers = getSupplierForTest(BeforeTest.class.getSimpleName(), interfaceMethodsMap, testService);


        List<Supplier<Integer>> afterTestSuppliers = getSupplierForTest(AfterTest.class.getSimpleName(), interfaceMethodsMap, testService);

        interfaceMethodsMap.get(Test.class.getSimpleName()).stream()
                .sorted(Comparator.comparingInt(o -> o.getAnnotation(Test.class).priority()))
                .forEach(method -> {
                    try {
                        for (Supplier<Integer> supplier: beforeTestSuppliers) {
                            supplier.get();
                        }
                        method.invoke(testService);
                        for (Supplier<Integer> supplier: afterTestSuppliers) {
                            supplier.get();
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

    }

    private static List<Supplier<Integer>> getSupplierForTest(String simpleNameInterface,
                                                        Map<String, List<Method>> interfaceMethodsMap,
                                                        TestService testService) {
        List<Supplier<Integer>> testSupplier = new ArrayList<>();
        List<Method> methodsBeforeTest = interfaceMethodsMap.get(simpleNameInterface);
        if (Objects.nonNull(methodsBeforeTest)) {
            for (Method method : methodsBeforeTest) {
                testSupplier.add(() -> {
                    try {
                        method.invoke(testService);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return 0;
                });
            }
        }
        return testSupplier;
    }

    public static void executeMethodWithParam(Map<String, List<Method>> interfaceMethodsMap,
                                              TestService testService) {
        for (Method method : interfaceMethodsMap.get(CsvSource.class.getSimpleName())) {
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
