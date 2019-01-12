package alararestaurant.domain.dtos.orderimportdtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderImportDto {
    @XmlElement(name = "customer")
    private String customer;
    @XmlElement(name = "employee")
    private String employeeName;
    @XmlElement(name = "date-time")
    private String dateTime;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "items")
    private OrderItemImportRootDto orderItemImportRootDto;

    public OrderImportDto() {
    }
    @NotNull
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    @NotNull
    @Size(min = 3,max = 30)
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    @NotNull
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s\\d{2}\\:\\d{2}$")
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    @Pattern(regexp = "(ForHere)|(ToGo)")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrderItemImportRootDto getOrderItemImportRootDto() {
        return orderItemImportRootDto;
    }

    public void setOrderItemImportRootDto(OrderItemImportRootDto orderItemImportRootDto) {
        this.orderItemImportRootDto = orderItemImportRootDto;
    }
}
