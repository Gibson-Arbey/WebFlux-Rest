package co.webflux_rest.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class ProductEntity {
    
    @Id
    private String id;

    @NotEmpty(message = "El nombre no puede estar vacio")
    private String name;

    @NotNull(message = "El precio no puede estar vacio")
    private Double price;

    private LocalDate createAt;

    @Field(targetType = FieldType.OBJECT_ID)
    private String categoryId;

    private String photo;

    @Transient
    private CategoryEntity category;

    public ProductEntity(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public ProductEntity(String name, Double price, CategoryEntity category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

}
