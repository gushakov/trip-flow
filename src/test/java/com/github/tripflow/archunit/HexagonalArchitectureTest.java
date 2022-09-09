package com.github.tripflow.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class HexagonalArchitectureTest {

    private static final String ROOT_PACKAGE = "com.github.tripflow";

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT_PACKAGE);

    private final DescribedPredicate<JavaClass> areStandard = resideInAnyPackage(
            "",
            "java..",
            "javax..",
            "org.slf4j..");

    private final DescribedPredicate<JavaClass> areCore = resideInAnyPackage(ROOT_PACKAGE + ".core..");

    private final DescribedPredicate<JavaClass> areInfrastructure = resideInAnyPackage(ROOT_PACKAGE + ".infrastructure..");

    @Test
    void core_should_not_depend_on_infrastructure() {
        noClasses()
                .that(areCore)
                .should()
                .dependOnClassesThat(areInfrastructure)
                .check(classes);
    }

    @Test
    void core_should_only_depend_on_itself_and_standard_classes() {
        classes()
                .that(areCore)
                .should()
                .onlyDependOnClassesThat(areCore.or(areStandard))
                .check(classes);
    }
}