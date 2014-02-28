package org.jarbframework.constraint.violation.resolver.vendor;

import static org.jarbframework.constraint.violation.DatabaseConstraintType.FOREIGN_KEY;
import static org.jarbframework.constraint.violation.DatabaseConstraintType.INVALID_TYPE;
import static org.jarbframework.constraint.violation.DatabaseConstraintType.LENGTH_EXCEEDED;
import static org.jarbframework.constraint.violation.DatabaseConstraintType.NOT_NULL;
import static org.jarbframework.constraint.violation.DatabaseConstraintType.UNIQUE_KEY;
import static org.jarbframework.constraint.violation.DatabaseConstraintViolation.builder;

import org.jarbframework.constraint.violation.DatabaseConstraintViolation;
import org.jarbframework.constraint.violation.resolver.PatternViolationResolver;
import org.jarbframework.utils.DatabaseProduct;

/**
 * Hypersonic SQL based constraint violation resolver.
 * 
 * @author Jeroen van Schagen
 * @since 16-05-2011
 */
public class HsqlViolationResolver extends PatternViolationResolver implements DatabaseProductSpecific {

    public HsqlViolationResolver() {
        registerNotNull();
        registerUniqueKey();
        registerForeignKey();
        registerLengthExceeded();
        registerInvalidType();
    }

    private void registerNotNull() {
        register("integrity constraint violation: NOT NULL check constraint; (.+) table: (.+) column: (.+)", new ViolationBuilder() {
            
            @Override
            public DatabaseConstraintViolation build(VariableAccessor variables) {
                return builder(NOT_NULL)
                            .constraint(variables.get(1).toLowerCase())
                            .table(variables.get(2).toLowerCase())
                            .column(variables.get(3).toLowerCase())
                                .build();
            }
            
        });
    }

    private void registerUniqueKey() {
        register("integrity constraint violation: unique constraint or index violation; (.+) table: (.+)", new ViolationBuilder() {
            
            @Override
            public DatabaseConstraintViolation build(VariableAccessor variables) {
                return builder(UNIQUE_KEY)
                            .constraint(variables.get(1).toLowerCase())
                            .table(variables.get(2).toLowerCase())
                                .build();
            }
            
        });
    }

    private void registerForeignKey() {
        register("integrity constraint violation: foreign key no \\w+; (.+) table: (.+)", new ViolationBuilder() {
            
            @Override
            public DatabaseConstraintViolation build(VariableAccessor variables) {
                return builder(FOREIGN_KEY)
                        .constraint(variables.get(1).toLowerCase())
                        .table(variables.get(2).toLowerCase())
                            .build();
            }
            
        });
    }

    private void registerLengthExceeded() {
        register("data exception: (.+) data, right truncation", new ViolationBuilder() {
            
            @Override
            public DatabaseConstraintViolation build(VariableAccessor variables) {
                return builder(LENGTH_EXCEEDED).valueType(variables.get(1).toLowerCase()).build();
            }
            
        });
    }

    private void registerInvalidType() {
        register("data exception: invalid (.+) value for cast", new ViolationBuilder() {
            
            @Override
            public DatabaseConstraintViolation build(VariableAccessor variables) {
                return builder(INVALID_TYPE).valueType(variables.get(1).toLowerCase()).build();
            }
            
        });
    }
    
    @Override
    public boolean supports(DatabaseProduct product) {
        return "HSQL Database Engine".equals(product.getName());
    }

}
