package org.example.practice_2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamApi {
    public Set<Integer> deleteDuplicate(List<Integer> list) {
        return new LinkedHashSet<>(list);
    }

    public Integer deleteThirdMaxValue(List<Integer> list) {
        return list.stream()
                .sorted((o1, o2) -> o2 - o1)
                .skip(2)
                .limit(1)
                .findAny().get();
    }

    public Integer deleteWithoutDuplicatesThirdMaxValue(List<Integer> list) {
        return list.stream()
                .distinct()
                .sorted((o1, o2) -> o2 - o1)
                .skip(2)
                .limit(1)
                .findAny().get();
    }

    public List<String> nameListElderlyEmployee(List<Employee> employeeList) {
        return employeeList.stream()
                .filter(employee -> Objects.equals(employee.getPost(), "Инженер"))
                .sorted((o1, o2) -> o2.getAge() - o1.getAge())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public OptionalDouble averageAgeEmployees(List<Employee> employeeList) {
        return employeeList.stream()
                .filter(employee -> Objects.equals(employee.getPost(), "Инженер"))
                .map(Employee::getAge)
                .mapToInt(value -> value)
                .average();
    }

    public Optional<String> largerString(List<String> stringList) {
        return stringList.stream()
                .max(Comparator.comparingInt(String::length));
    }

    public Map<String, Integer> mapStringCount(String str) {
        return Arrays.stream(str.split(" "))
                .collect(Collectors.toMap(String::toString, s -> 1, Integer::sum));
    }

    public List<String> getStringInOrderedLines(List<String> stringList) {
        return stringList.stream()
                .sorted((o1, o2) -> (o1.length() == o2.length()) ? o1.compareTo(o2) : o1.length() - o2.length())
                .collect(Collectors.toList());
    }

    public Optional<String> getVeryLargeStringInList(List<String> stringList) {
        return stringList.stream()
                .map(s -> Arrays.asList(s.split(" ")))
                .flatMap(Collection::stream)
                .max(Comparator.comparingInt(String::length));
    }
}
