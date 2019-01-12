package alararestaurant.repository;

import alararestaurant.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Optional<Employee> findByName(String name);
    @Query(value = "select * from employees e\n" +
            "join positions p on e.position_id = p.id\n" +
            "join orders o on e.id = o.employee_id\n" +
            "join order_items oi on o.id = oi.order_id\n" +
            "join items i on oi.item_id = i.id\n" +
            "where p.name='Burger Flipper'\n" +
            "group by e.name "+
            "order by e.name asc,o.id,i.price,oi.quantity,i.name;",nativeQuery = true)
    List<Employee> exportOrdersFinishedByBurgerFlippers();
}
