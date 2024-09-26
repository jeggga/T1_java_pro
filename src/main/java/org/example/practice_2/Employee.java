package org.example.practice_2;

public class Employee {
    private String name;

    private Integer age;

    private String post;

    public Employee(String name, Integer age, String post) {
        this.name = name;
        this.age = age;
        this.post = post;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
