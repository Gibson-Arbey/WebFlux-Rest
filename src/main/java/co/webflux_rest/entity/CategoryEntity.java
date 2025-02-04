package co.webflux_rest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document( collection = "categories")
public class CategoryEntity {
    
    @Id
    private String id;

    @NotEmpty
    private String name;

    public CategoryEntity(String name) {
        this.name = name;
    }

}
