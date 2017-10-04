package org.granite.base;

/**
 * User: cbrophy Date: 10/4/17 Time: 2:27 PM
 */
public class ObjectTools {

  public static <T> T firstNonNull(T...items){
    if(items != null) {

      for (T item : items) {
        if (item != null)
          return item;
      }

    }

    return null;
  }
}
