package alararestaurant.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
public class Order extends BaseEntity {
    private String customer;
    private LocalDateTime dateTime;
    private Enums type;
    private Employee employee;
    private List<OrderItem> orderItems;

    public Order() {
        this.orderItems=new ArrayList<>();
    }
    @Column(name = "customer",columnDefinition = "text",nullable = false)
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    @Column(name = "date_time",nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type",nullable = false,columnDefinition = "varchar(7) default 'ForHere'" )
    public Enums getType() {
        return type;
    }

    public void setType(Enums type) {
        this.type = type;
    }
    @ManyToOne(targetEntity = Employee.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id",referencedColumnName = "id")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    @OneToMany(targetEntity = OrderItem.class,cascade = CascadeType.ALL,mappedBy = "order")
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
