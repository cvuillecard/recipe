package com.recipe.app.bean.ingredient;

import com.recipe.app.bean.recipe.RecipeBean;
import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.type.ingredient.IngredientType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public final class IngredientBean implements Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ing_id")
    private Long id;

    @Column(name = "ing_name")
    private String name;

    @Column(name = "ing_type")
    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = RecipeBean.class)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ing_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "rec_id")
    )
    private Set<Recipe> recipes;

    public IngredientBean() {}

    public IngredientBean(final String name, final IngredientType type) {
        this.name = name;
        this.type = type;
    }

    public IngredientBean(final Long id, final String name, final IngredientType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // getters
    @Override public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public IngredientType getType() { return type; }
    @Override public Set<Recipe> getRecipes() { return recipes; }
    // setters
    @Override public Ingredient setId(final Long id) { this.id = id; return this; }
    @Override public Ingredient setName(final String name) { this.name = name; return this; }
    @Override public Ingredient setType(final IngredientType type) { this.type = type; return this; }
    @Override public Ingredient setRecipes(final Set<Recipe> recipes) {  this.recipes = recipes; return this; }
}
