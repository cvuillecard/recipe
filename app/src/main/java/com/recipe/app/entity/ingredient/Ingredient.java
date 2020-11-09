package com.recipe.app.entity.ingredient;

import com.recipe.app.entity.recipe.Recipe;
import com.recipe.app.type.ingredient.IngredientType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public final class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ing_id")
    private Long id;

    @Column(name = "ing_name")
    private String name;

    @Column(name = "ing_type")
    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Recipe.class)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ing_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "rec_id")
    )
    private Set<Recipe> recipes;

    public Ingredient() {}

    public Ingredient(final String name, final IngredientType type) {
        this.name = name;
        this.type = type;
    }

    public Ingredient(final Long id, final String name, final IngredientType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public IngredientType getType() { return type; }
    public Set<Recipe> getRecipes() { return recipes; }
    // setters
    public Ingredient setId(final Long id) { this.id = id; return this; }
    public Ingredient setName(final String name) { this.name = name; return this; }
    public Ingredient setType(final IngredientType type) { this.type = type; return this; }
    public Ingredient setRecipes(final Set<Recipe> recipes) {  this.recipes = recipes; return this; }
}
