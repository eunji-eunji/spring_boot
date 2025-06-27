package student_json.student.dto;

import lombok.*;

import java.util.ArrayList;

@Data // Getter Setter 한번에 해결
@AllArgsConstructor
public class StudentDto {
    public String name;
    public int age;
    public ArrayList<String> classes;

    public StudentDto(Student student) {
        this.name = student.getName();
        this.age = student.getAge();
        this.classes = student.getClasses();
    }
}
