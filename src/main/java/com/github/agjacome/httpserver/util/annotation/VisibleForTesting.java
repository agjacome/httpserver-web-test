package com.github.agjacome.httpserver.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@linkplain DisallowConstruction} is an annotation used to mark methods which
 * are purposefully non-private in order to perform any testing operation. It
 * exists merely for documentation purposes.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForTesting { }
