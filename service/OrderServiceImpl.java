package alararestaurant.service;

import alararestaurant.domain.dtos.orderimportdtos.OrderImportDto;
import alararestaurant.domain.dtos.orderimportdtos.OrderImportRootDto;
import alararestaurant.domain.dtos.orderimportdtos.OrderItemImportDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Item;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import alararestaurant.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ORDERS_FILE_PATH = "C:\\Users\\Triple OG\\Downloads\\Alara Restaurant_Skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\orders.xml";
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final XmlParser parser;
    private final ModelMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository, EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository, FileUtil fileUtil, ValidationUtil validationUtil, XmlParser parser, ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Boolean ordersAreImported() {

        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {

        return this.fileUtil.readFile(ORDERS_FILE_PATH);
    }

    @Override
    public String importOrders() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        OrderImportRootDto orderImportRootDtos = this.parser.parseXml(OrderImportRootDto.class,ORDERS_FILE_PATH);

        //using a pre-generated list in order to lower the amount of calls to DB to only 1
        List<String>items = this.itemRepository.findAll().stream().map(Item::getName).collect(Collectors.toList());
        System.out.println();
        for (OrderImportDto orderImportDto : orderImportRootDtos.getOrderImportDtos()) {
            if(!this.validationUtil.isValid(orderImportDto)){
                sb.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }
            Employee employee = this.employeeRepository.findByName(orderImportDto.getEmployeeName()).orElse(null);
            if(employee==null){
                continue;
            }
            boolean itemExists = items.containsAll(orderImportDto.getOrderItemImportRootDto().getOrderItemImportDtos()
                    .stream().map(OrderItemImportDto::getName).collect(Collectors.toList()));
            if(!itemExists){
                continue;
            }
            Order order = this.mapper.map(orderImportDto,Order.class);
            List<OrderItem> orderItemList = new ArrayList<>();
            for (OrderItemImportDto orderItemImportDto : orderImportDto.getOrderItemImportRootDto().getOrderItemImportDtos()) {
                Item item = this.itemRepository.findByName(orderItemImportDto.getName()).orElse(null);
                OrderItem orderItem = this.mapper.map(orderItemImportDto,OrderItem.class);
                orderItem.setItem(item);
                orderItem.setOrder(order);
                orderItemList.add(orderItem);


            }
            order.getOrderItems().addAll(orderItemList);
            order.setEmployee(employee);
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            order.setDateTime(LocalDateTime.parse(orderImportDto.getDateTime(),formatter));

            System.out.println();
            this.orderItemRepository.saveAll(orderItemList);
            this.orderRepository.saveAndFlush(order);
            sb.append(String.format("Order for %s on %s added",order.getCustomer(),orderImportDto.getDateTime())).append(System.lineSeparator());



        }
        return sb.toString().trim();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        List<Employee>employees= this.employeeRepository.exportOrdersFinishedByBurgerFlippers();
        StringBuilder sb = new StringBuilder();
        for (Employee employee : employees) {
            sb.append(String.format("Name: %s",employee.getName())).append(System.lineSeparator());
            sb.append("Orders:").append(System.lineSeparator());
            for (Order order : employee.getOrders()) {
                sb.append(String.format("  Customer: %s",order.getCustomer())).append(System.lineSeparator());
                for (OrderItem orderItem : order.getOrderItems()) {
                    sb.append(String.format("    Name: %s",orderItem.getItem().getName())).append(System.lineSeparator())
                            .append(String.format("    Price: %.2f",orderItem.getItem().getPrice())).append(System.lineSeparator())
                            .append(String.format("    Quantity: %d",orderItem.getQuantity())).append(System.lineSeparator())
                            .append(System.lineSeparator());


                }
            }
        }


        return sb.toString().trim();
    }
}
