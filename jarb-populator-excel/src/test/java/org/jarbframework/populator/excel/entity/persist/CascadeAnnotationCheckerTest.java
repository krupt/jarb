package org.jarbframework.populator.excel.entity.persist;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.metamodel.Attribute;

import org.jarbframework.populator.excel.DefaultExcelTestDataCase;
import org.junit.Test;

import domain.entities.Employee;
import domain.entities.Project;
import domain.entities.VeryImportantCustomer;

public class CascadeAnnotationCheckerTest extends DefaultExcelTestDataCase {

    @Test
    public void testHasNecessaryCascadeAnnotations() {
        Attribute<?, ?> attribute = getEntityManagerFactory().getMetamodel().entity(Project.class).getAttribute("customer");
        assertFalse(CascadeAnnotationChecker.hasNecessaryCascadeAnnotations(attribute));
    }

    @Test
    public void testDoesNotHaveNecessaryCascadeAnnotations() {
        Attribute<?, ?> attribute = getEntityManagerFactory().getMetamodel().entity(Employee.class).getAttribute("vehicle");
        assertFalse(CascadeAnnotationChecker.hasNecessaryCascadeAnnotations(attribute));
    }

    @Test
    public void testOtherAnnotation() {
        Attribute<?, ?> attribute = getEntityManagerFactory().getMetamodel().entity(Employee.class).getAttribute("salary");
        assertFalse(CascadeAnnotationChecker.hasNecessaryCascadeAnnotations(attribute));
    }

    @Test
    public void testAllCascadeTypesOnManyToMany() {
        Attribute<?, ?> attribute = getEntityManagerFactory().getMetamodel().entity(VeryImportantCustomer.class).getAttribute("gifts");
        assertTrue(CascadeAnnotationChecker.hasNecessaryCascadeAnnotations(attribute));
    }

}
