package testCases;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Human {
    @Getter @Setter
    String name;
    @Getter Integer age;
    @Getter Date birthDate;

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
