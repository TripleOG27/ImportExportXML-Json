package alararestaurant.domain.dtos.orderimportdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemImportRootDto {
    @XmlElement(name = "item")
    private List<OrderItemImportDto> orderItemImportDtos;

    public OrderItemImportRootDto() {
    }

    public List<OrderItemImportDto> getOrderItemImportDtos() {
        return orderItemImportDtos;
    }

    public void setOrderItemImportDtos(List<OrderItemImportDto> orderItemImportDtos) {
        this.orderItemImportDtos = orderItemImportDtos;
    }
}
