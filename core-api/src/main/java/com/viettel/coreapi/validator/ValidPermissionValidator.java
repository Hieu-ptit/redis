package com.viettel.coreapi.validator;

import com.viettel.coreapi.model.Permissions;
import com.viettel.coreapi.model.request.PermissionRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidPermissionValidator implements ConstraintValidator<ValidPermission, List<PermissionRequest>> {
	@Override
	public void initialize(ValidPermission constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(List<PermissionRequest> permissions, ConstraintValidatorContext context) {
		if (permissions == null || permissions.isEmpty()) {
			return true;
		}
		var actions = permissions.stream().map(PermissionRequest::getAction).collect(Collectors.toSet());
		return Permissions.ACTIONS.containsAll(actions);
	}
}
