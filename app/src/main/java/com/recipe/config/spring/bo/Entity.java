package com.recipe.config.spring.bo;


import java.io.Serializable;

/** Base interface for all entities used in the application */
public interface Entity<ID> extends Serializable {
	ID getId();
	Entity<ID> setId(final ID id);
}
