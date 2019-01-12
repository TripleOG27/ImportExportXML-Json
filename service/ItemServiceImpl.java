package alararestaurant.service;

import alararestaurant.domain.dtos.ItemImportDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemServiceImpl implements ItemService {
    private static final String ITEMS_FILE_PATH = "C:\\Users\\Triple OG\\Downloads\\Alara Restaurant_Skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\items.json";
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final Gson gson;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper mapper, Gson gson) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
        this.gson = gson;
    }

    @Override
    public Boolean itemsAreImported() {


        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {

        return this.fileUtil.readFile(ITEMS_FILE_PATH);
    }

    @Override
    public String importItems(String items) {
        StringBuilder sb = new StringBuilder();
        ItemImportDto[] itemImportDtos = this.gson.fromJson(items,ItemImportDto[].class);
        for (ItemImportDto itemImportDto : itemImportDtos) {
            if(!this.validationUtil.isValid(itemImportDto)){
                sb.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }
            Item item = this.itemRepository.findByName(itemImportDto.getName()).orElse(null);
            if(item!=null){

                //It is not mentioned that any error should be printed so not appending the error message below
                //sb.append("Invalid data format.").append(System.lineSeparator());
                continue;
            }
            Category category = this.categoryRepository.findByName(itemImportDto.getCategory()).orElse(null);
            if(category==null){
                category = new Category();
                category.setName(itemImportDto.getCategory());
                this.categoryRepository.saveAndFlush(category);
            }
            item = new Item();
            item = this.mapper.map(itemImportDto,Item.class);
            item.setCategory(category);
            this.itemRepository.saveAndFlush(item);
            sb.append(String.format("Record %s successfully imported",item.getName())).append(System.lineSeparator());

        }
        return sb.toString().trim();
    }
}
