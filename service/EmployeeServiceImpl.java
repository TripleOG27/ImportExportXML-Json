package alararestaurant.service;

import alararestaurant.domain.dtos.EmployeeImportDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEES_FILE_PATH = "C:\\Users\\Triple OG\\Downloads\\Alara Restaurant_Skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\employees.json";
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final Gson gson;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper mapper, Gson gson) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;

        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
        this.gson = gson;
    }

    @Override
    public Boolean employeesAreImported() {


        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {

        return this.fileUtil.readFile(EMPLOYEES_FILE_PATH);
    }

    @Override
    public String importEmployees(String employees) {
        StringBuilder sb = new StringBuilder();
        EmployeeImportDto[] employeeImportDtos = this.gson.fromJson(employees,EmployeeImportDto[].class);
        for (EmployeeImportDto employeeImportDto : employeeImportDtos) {
            if(!this.validationUtil.isValid(employeeImportDto)){
                sb.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }
            Employee employee = this.mapper.map(employeeImportDto,Employee.class);
            Position position = this.positionRepository.findByName(employeeImportDto.getPosition()).orElse(null);
            if(position==null){
                position=new Position();
                position.setName(employeeImportDto.getPosition());
                this.positionRepository.saveAndFlush(position);
            }
            employee.setPosition(position);
            this.employeeRepository.saveAndFlush(employee);
            sb.append(String.format("Record %s successfully imported",employee.getName())).append(System.lineSeparator());


        }
        return sb.toString().trim();
    }
}
