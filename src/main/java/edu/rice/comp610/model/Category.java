package edu.rice.comp610.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;


/**
 * Category of the item being put up for auction to help buyers find what they are looking for.
 *
 * <p>The category (classification) of the item being listed for auction.</p>
 * <ul>
 * <li> id - unique identifier generated by the system. </li>
 * <li> name - one or two work name for the category. </li>
 * <li> description - more detailed description of the category</li>
 * </ul>
 *
 */
public class Category {

    private UUID id;
    private String name;
    private String description;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
