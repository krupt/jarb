package org.jarb.violation.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jarb.violation.ConstraintViolation;
import org.jarb.violation.ConstraintViolationType;
import org.jarb.violation.domain.Car;
import org.jarb.violation.domain.Person;
import org.jarb.violation.resolver.database.DatabaseResolver;
import org.jarb.violation.resolver.database.HibernateJpaDatabaseResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Check that a "default" constraint violation resolver works on errors generated by our HSQL driver.
 * For the resolver to work, it has to correctly recognize our database as HSQL and interpret the
 * provided exception messages as constraint violation information.
 * 
 * @author Jeroen van Schagen
 * @since 16-05-2011
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:hsql-context.xml" })
public class ConstraintViolationResolverFactoryTest {
    private ConstraintViolationResolver resolver;

    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setUpResolver() {
        DatabaseResolver databaseResolver = HibernateJpaDatabaseResolver.forEntityManager(entityManager);
        resolver = ConstraintViolationResolverFactory.build(databaseResolver);
    }

    /**
     * Column "license_number" cannot be null.
     */
    @Test
    public void testNotNull() {
        Car car = new Car();
        try {
            entityManager.persist(car);
            fail("Expected a runtime exception");
        } catch (RuntimeException e) {
            ConstraintViolation violation = resolver.resolve(e);
            assertEquals(ConstraintViolationType.CANNOT_BE_NULL, violation.getType());
            assertTrue(violation.getConstraintName().startsWith("sys_ct_"));
            assertEquals("cars", violation.getTableName());
            assertEquals("license_number", violation.getColumnName());
        }
    }

    /**
     * Column "license_number" is expected to be unique.
     */
    @Test
    public void testUniqueKey() {
        Car ferarri = new Car();
        ferarri.setLicenseNumber("12ABC1");
        entityManager.persist(ferarri);
        Car another = new Car();
        another.setLicenseNumber("12ABC1");
        try {
            entityManager.persist(another);
            fail("Expected a runtime exception");
        } catch (RuntimeException e) {
            ConstraintViolation violation = resolver.resolve(e);
            assertEquals(ConstraintViolationType.UNIQUE_VIOLATION, violation.getType());
            assertEquals("uk_cars_license_number", violation.getConstraintName());
            assertEquals("cars", violation.getTableName());
        }
    }

    /**
     * Column "license_number" has a maximum length of '6'.
     */
    @Test
    public void testLengthExceeded() {
        Car car = new Car();
        car.setLicenseNumber("superlonglicensenumber");
        try {
            entityManager.persist(car);
            fail("Expected a runtime exception");
        } catch (RuntimeException e) {
            ConstraintViolation violation = resolver.resolve(e);
            assertEquals(ConstraintViolationType.LENGTH_EXCEEDED, violation.getType());
        }
    }

    /**
     * Column "age" is a bigint in the database, not a string.
     */
    @Test
    public void testInvalidType() {
        Person jeroen = new Person();
        jeroen.setName("Jeroen");
        jeroen.setAge("not a number");
        try {
            entityManager.persist(jeroen);
            fail("Expected a runtime exception");
        } catch (RuntimeException e) {
            ConstraintViolation violation = resolver.resolve(e);
            assertEquals(ConstraintViolationType.INVALID_TYPE, violation.getType());
        }
    }

}
