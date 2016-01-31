package com.github.agjacome.httpserver.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@linkplain DisallowConstruction} is an annotation used to mark constructors
 * which are purposefully private in order to disable their construction. It
 * exists merely for documentation purposes.
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
public @interface DisallowConstruction { }