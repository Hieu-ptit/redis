package com.viettel.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPermissionValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPermission {
	String message() default "Invalid permissions";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
