package com.viettel.coreapi.validator;

import lombok.RequiredArgsConstructor;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ExistedRoleValidator implements ConstraintValidator<ExistedRole, String> {

	private final Enforcer enforcer;

	@Override
	public void initialize(ExistedRole constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String role, ConstraintValidatorContext context) {
		if (role == null || role.isEmpty()) {
			return true;
		}
		var permissions = enforcer.getPermissionsForUser(role);
		return !permissions.isEmpty();
	}
}
