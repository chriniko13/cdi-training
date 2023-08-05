package com.example.myscope;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import jakarta.enterprise.context.NormalScope;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NormalScope(passivating = false)
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD})
@Inherited
@Documented
public @interface CustomScoped {

}
