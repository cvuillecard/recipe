package com.recipe.app.bean.recipe;

import com.recipe.app.bean.ingredient.IngredientBean;
import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.type.recipe.RecipeType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "RECIPE")
public final class RecipeBean implements Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id")
    private Long id;

    @Column(name = "rec_name")
    private String name;

    @Column(name = "rec_type")
    @Enumerated(EnumType.STRING)
    private RecipeType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = IngredientBean.class)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "ing_id")
    )
    private Set<Ingredient> ingredients;

    public RecipeBean() {}

    public RecipeBean(final String name, final RecipeType type) {
        this.name = name;
        this.type = type;
    }

    public RecipeBean(final Long id, final String name, final RecipeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // getters
    @Override public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public RecipeType getType() { return type; }
    @Override public Set<Ingredient> getIngredients() { return ingredients; }
    // setters
    @Override public Recipe setId(final Long id) { this.id = id; return this; }
    @Override  public Recipe setName(final String name) { this.name = name; return this; }
    @Override public Recipe setType(final RecipeType type) { this.type = type; return this; }
    @Override public Recipe setIngredients(final Set<Ingredient> ingredients) { this.ingredients = ingredients; return this; }
}
