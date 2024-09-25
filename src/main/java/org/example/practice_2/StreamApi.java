package org.example.practice_2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamApi {
    public List<Integer> deleteDuplicate(List<Integer> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Integer deleteThirdMaxValue(List<Integer> list) {
        return list.stream()
                .sorted((o1, o2) -> o2 - o1)
                .skip(2)
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    public Integer deleteWithoutDuplicatesThirdMaxValue(List<Integer> list) {
        return list.stream()
                .distinct()
                .sorted((o1, o2) -> o2 - o1)
                .skip(2)
                .limit(1)
                .findFirst().orElseThrow();
    }

    public List<String> nameListElderlyEmployee(List<Employee> employeeList) {
        return employeeList.stream()
                .filter(employee -> Objects.equals(employee.getPost(), "Инженер"))
                .sorted((o1, o2) -> o2.getAge() - o1.getAge())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public Double averageAgeEmployees(List<Employee> employeeList) {
        return employeeList.stream()
                .filter(employee -> Objects.equals(employee.getPost(), "Инженер"))
                .map(Employee::getAge)
                .collect(Collectors.averagingInt(value -> value));
    }

    public Optional<String> largerString(List<String> stringList) {
        return stringList.stream()
                .max(Comparator.comparingInt(String::length));
    }

    public Map<String, Long> mapStringCount(String str) {
        return Arrays.stream(str.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public List<String> getStringInOrderedLines(List<String> stringList) {
        return stringList.stream()
                .sorted(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }

    public String getVeryLargeStringInList(List<String> stringList) {
        return stringList.stream()
                .map(s -> Arrays.asList(s.split(" ")))
                .flatMap(Collection::stream)
                .max(Comparator.comparingInt(String::length)).orElseThrow();
    }
}
