package br.com.felipezorzo.sonar.plsql.api;

import static org.sonar.sslr.tests.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class VariableDeclarationTest extends RuleTest {

    @Before
    public void init() {
        setRootRule(PlSqlGrammar.VARIABLE_DECLARATION);
    }

    @Test
    public void matchesSimpleDeclaration() {
        assertThat(p).matches("var number;");
    }
    
    @Test @Ignore
    public void matchesDeclarationWithPrecisionAndScale() {
        assertThat(p).matches("var number(1,1);");
    }
    
    @Test @Ignore
    public void matchesDeclarationWithInitialization() {
        assertThat(p).matches("var number := 1;");
    }
    
    @Test @Ignore
    public void matchesDeclarationWithDefaultValue() {
        assertThat(p).matches("var number default 1;");
    }
    
    @Test @Ignore
    public void matchesDeclarationWithNotNullConstraint() {
        assertThat(p).matches("var number not null := 1;");
        assertThat(p).matches("var number not null default 1;");
    }
    
    @Test @Ignore
    public void matchesTypeAnchoredDeclaration() {
        assertThat(p).matches("var custom%type;");
    }
    
    @Test @Ignore
    public void matchesObjectDeclaration() {
        assertThat(p).matches("var custom;");
    }

    @Test @Ignore
    public void matchesTableAnchoredDeclaration() {
        assertThat(p).matches("var table%rowtype;");
    }
    
    @Test @Ignore
    public void matchesTableColumnAnchoredDeclaration() {
        assertThat(p).matches("var table.column%type;");
    }
    
    @Test @Ignore
    public void matchesRefObjectDeclaration() {
        assertThat(p).matches("var ref custom;");
    }
    
    @Test @Ignore
    public void matchesSimpleConstant() {
        assertThat(p).matches("var constant number := 1;");
        assertThat(p).matches("var constant number default 1;");
    }
    
    @Test @Ignore
    public void matchesSimpleConstantWithConstraints() {
        assertThat(p).matches("var constant number not null := 1;");
        assertThat(p).matches("var constant number not null default 1;");
    }
    
    @Test
    public void notMatchesDeclarationWithIncompleteNotNullConstraint() {
        assertThat(p).notMatches("var number not null;");
    }
    
}