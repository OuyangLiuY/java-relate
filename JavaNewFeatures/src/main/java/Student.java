import java.util.Objects;

public class Student {
    int age;
    String name;
    Grade grade;
    int score;

    public Student(int age, String name, Grade grade,int score) {
        this.age = age;
        this.name = name;
        this.grade = grade;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return getAge() == student.getAge() &&
                getName().equals(student.getName()) &&
                getGrade() == student.getGrade();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAge(), getName(), getGrade());
    }
}
