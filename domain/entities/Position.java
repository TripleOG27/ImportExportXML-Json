package alararestaurant.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity(name = "positions")
public class Position extends BaseEntity {
    private String name;
    private List<Employee> employees;

    public Position() {
    }
    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @OneToMany(targetEntity = Employee.class,cascade = CascadeType.ALL,mappedBy = "position")
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
