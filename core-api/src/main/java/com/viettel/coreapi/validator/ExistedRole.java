package com.viettel.coreapi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistedRoleValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistedRole {
	String message() default "This role must exist";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
