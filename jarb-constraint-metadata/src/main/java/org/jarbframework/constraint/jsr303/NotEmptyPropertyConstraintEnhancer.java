package org.jarbframework.constraint.jsr303;

import static org.jarbframework.utils.bean.BeanAnnotationScanner.fieldOrGetter;

import org.hibernate.validator.constraints.NotEmpty;
import org.jarbframework.constraint.PropertyConstraintDescription;
import org.jarbframework.constraint.PropertyConstraintEnhancer;

/**
 * Whenever a property is annotated as @NotEmpty , the minimum length has to be at least 1.
 * 
 * @author Jeroen van Schagen
 * @since 31-05-2011
 */
public class NotEmptyPropertyConstraintEnhancer implements PropertyConstraintEnhancer {

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyConstraintDescription enhance(PropertyConstraintDescription propertyConstraints) {
        if (fieldOrGetter().hasAnnotation(propertyConstraints.toPropertyReference(), NotEmpty.class)) {
            // When a property cannot be empty, it has a minimum length of at least 1
            // If our description already has a greater minimum length, do nothing
            if (propertyConstraints.getMinimumLength() == null || propertyConstraints.getMinimumLength() == 0) {
                propertyConstraints.setMinimumLength(1);
            }
        }
        return propertyConstraints;
    }

}