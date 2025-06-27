package student_json.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Student {
    public String name;
    public String password;
    public int age;

    public ArrayList<String> classes = new ArrayList<>();
    public Student(){

    }
}
