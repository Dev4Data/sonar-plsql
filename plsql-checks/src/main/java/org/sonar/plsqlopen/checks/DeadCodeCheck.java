/*
 * Sonar PL/SQL Plugin (Community)
 * Copyright (C) 2015 Felipe Zorzo
 * felipe.b.zorzo@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plsqlopen.checks;

import javax.annotation.Nullable;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import com.sonar.sslr.api.AstNode;

@Rule(
    key = DeadCodeCheck.CHECK_KEY,
    priority = Priority.MAJOR,
    tags = Tags.UNUSED
)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class DeadCodeCheck extends AbstractBaseCheck {
    public static final String CHECK_KEY = "DeadCode";

    @Override
    public void init() {
        subscribeTo(CheckUtils.getTerminationStatements());
    }

    @Override
    public void visitNode(AstNode node) {
        if (CheckUtils.isTerminationStatement(node)) {
            AstNode parent = node.getParent();
            while (!checkNode(parent)) {
                parent = parent.getParent();
            }
        }
    }
    
    private static boolean shouldCheckNode(@Nullable AstNode node) {
        if (CheckUtils.isProgramUnit(node)) {
            return false;
        }
        if (node.is(PlSqlGrammar.STATEMENT, PlSqlGrammar.BLOCK_STATEMENT)) {
            return true;
        }
        if (node.is(PlSqlGrammar.STATEMENTS_SECTION, PlSqlGrammar.STATEMENTS) && !node.hasDirectChildren(PlSqlGrammar.EXCEPTION_HANDLER)) {
            return true;
        }
        return false;
    }
    
    private boolean checkNode(AstNode node) {
        if (!shouldCheckNode(node)) {
            return true;
        }
        AstNode nextSibling = node.getNextSibling();
        if (nextSibling != null && nextSibling.is(PlSqlGrammar.STATEMENT)) {
            getContext().createLineViolation(this, getLocalizedMessage(CHECK_KEY), nextSibling);
            return true;
        }
        return false;
    }

}
