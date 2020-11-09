package com.recipe.app.entity.recipe;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.recipe.RecipeType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "RECIPE")
public final class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id")
    private Long id;

    @Column(name = "rec_name")
    private String name;

    @Column(name = "rec_type")
    @Enumerated(EnumType.STRING)
    private RecipeType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Ingredient.class)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "ing_id")
    )
    private Set<Ingredient> ingredients;

    public Recipe() {}

    public Recipe(final String name, final RecipeType type) {
        this.name = name;
        this.type = type;
    }

    public Recipe(final Long id, final String name, final RecipeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public RecipeType getType() { return type; }
    public Set<Ingredient> getIngredients() { return ingredients; }
    // setters
    public Recipe setId(final Long id) { this.id = id; return this; }
    public Recipe setName(final String name) { this.name = name; return this; }
    public Recipe setType(final RecipeType type) { this.type = type; return this; }
    public Recipe setIngredients(final Set<Ingredient> ingredients) { this.ingredients = ingredients; return this; }
}
