package com.recipe.config.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class ReflectUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectUtil.class);
    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

      public static final <T> Map<String, Object> getFields(final T inst, final String... excluded) {
          final Map<String, Object> fields = new HashMap<>();
          final List<String> exclusions = excluded != null && excluded.length > 0 ? Arrays.asList(excluded) : Collections.EMPTY_LIST;

          for (final Method m : inst.getClass().getMethods()) {
              if (m.getName().startsWith(GET_PREFIX) && !m.getName().equals("getClass")) {
                  final String field = m.getName().substring(3, m.getName().length());
                  final String key = field.substring(0, 1).toLowerCase() + field.substring(1, field.length());
                  if (!exclusions.contains(key)) {
                      try {
                          fields.put(key, m.invoke(inst));
                      } catch (IllegalAccessException | InvocationTargetException e) {
                          LOG.error(e.getMessage());
                      } finally {
                          exclusions.remove(key);
                      }
                  }
              }
          }
          return fields;
      }
}
