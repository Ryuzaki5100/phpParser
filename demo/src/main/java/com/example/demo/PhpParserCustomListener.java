package com.example.demo;

import com.myparser.PhpParser;
import com.myparser.PhpParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PhpParserCustomListener extends PhpParserBaseListener {
    private PhpCodeAnalyzer.PhpFileStructure structure = new PhpCodeAnalyzer.PhpFileStructure();
    private PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure currentClass = null;
    private PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure currentMethod = null;

    public PhpCodeAnalyzer.PhpFileStructure getStructure() {
        return structure;
    }

    @Override
    public void enterNamespaceDeclaration(PhpParser.NamespaceDeclarationContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode) {
                String text = ctx.getChild(i).getText();
                if (text.matches("[a-zA-Z_][a-zA-Z0-9_\\\\]*")) {
                    structure.namespaces.add(text);
                    break;
                }
            }
        }
    }

    @Override
    public void enterClassDeclaration(PhpParser.ClassDeclarationContext ctx) {
        PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure newClass = new PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure();

        // Extract class name
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode && ctx.getChild(i).getText().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                newClass.name = ctx.getChild(i).getText();
                break;
            }
        }

        // Extract attributes
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().startsWith("#[")) {
                newClass.attributes.add(ctx.getChild(i).getText());
            }
        }

        // Extract extends
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals("extends")) {
                for (int j = i + 1; j < ctx.getChildCount(); j++) {
                    if (ctx.getChild(j) instanceof TerminalNode) {
                        newClass.extendedClass = ctx.getChild(j).getText();
                        break;
                    }
                }
                break;
            }
        }

        // Extract implements
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals("implements")) {
                for (int j = i + 1; j < ctx.getChildCount(); j++) {
                    if (ctx.getChild(j) instanceof TerminalNode) {
                        newClass.implementedInterfaces.add(ctx.getChild(j).getText());
                    }
                }
                break;
            }
        }

        if (currentClass == null) {
            structure.classes.add(newClass); // Top-level class
        } else {
            currentClass.nestedClasses.add(newClass); // Nested class
        }
        currentClass = newClass;
    }

    public void enterMethodDeclaration(PhpParser.MethodBodyContext ctx) {
        if (currentClass != null) {
            PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure newMethod = new PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure();

            // Extract method name
            for (int i = 0; i < ctx.getChildCount(); i++) {
                if (ctx.getChild(i) instanceof TerminalNode && ctx.getChild(i).getText().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    newMethod.name = ctx.getChild(i).getText();
                    break;
                }
            }

            // Extract attributes
            for (int i = 0; i < ctx.getChildCount(); i++) {
                if (ctx.getChild(i).getText().startsWith("#[")) {
                    newMethod.attributes.add(ctx.getChild(i).getText());
                }
            }

            // Extract return type
            for (int i = ctx.getChildCount() - 1; i >= 0; i--) {
                String text = ctx.getChild(i).getText();
                if (text.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !text.equals(newMethod.name)) {
                    newMethod.returnType = text;
                    break;
                }
            }

            // Extract parameters
            boolean inParams = false;
            for (int i = 0; i < ctx.getChildCount(); i++) {
                String text = ctx.getChild(i).getText();
                if (text.equals("(")) inParams = true;
                if (text.equals(")")) inParams = false;
                if (inParams && ctx.getChild(i) instanceof TerminalNode && !text.matches("[()]")) {
                    newMethod.parameters.add(text);
                }
            }

            // Extract body
            for (int i = 0; i < ctx.getChildCount(); i++) {
                if (ctx.getChild(i) instanceof ParserRuleContext) {
                    String childText = ctx.getChild(i).getText();
                    if (childText.startsWith("{") && childText.endsWith("}")) {
                        newMethod.body = childText;
                        break;
                    }
                }
            }

            currentClass.methods.add(newMethod);
        }
    }

    public void enterPropertyDeclaration(PhpParser.PropertyModifiersContext ctx) { // Assuming this rule exists
        if (currentClass != null) {
            PhpCodeAnalyzer.PhpFileStructure.PhpFieldStructure newField = new PhpCodeAnalyzer.PhpFileStructure.PhpFieldStructure();

            // Extract attributes
            for (int i = 0; i < ctx.getChildCount(); i++) {
                if (ctx.getChild(i).getText().startsWith("#[")) {
                    newField.attributes.add(ctx.getChild(i).getText());
                }
            }

            // Extract type hint and name
            for (int i = 0; i < ctx.getChildCount(); i++) {
                String text = ctx.getChild(i).getText();
                if (text.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !text.startsWith("#[")) {
                    if (newField.typeHint.isEmpty()) {
                        newField.typeHint = text;
                    } else if (newField.name.isEmpty() && text.startsWith("$")) {
                        newField.name = text;
                        break;
                    }
                }
            }

            currentClass.properties.add(newField);
        }
    }

    @Override
    public void enterFunctionDeclaration(PhpParser.FunctionDeclarationContext ctx) {
        PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure newMethod = new PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure();

        // Extract function name
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode && ctx.getChild(i).getText().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                newMethod.name = ctx.getChild(i).getText();
                break;
            }
        }

        // Extract attributes
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().startsWith("#[")) {
                newMethod.attributes.add(ctx.getChild(i).getText());
            }
        }

        // Extract return type
        for (int i = ctx.getChildCount() - 1; i >= 0; i--) {
            String text = ctx.getChild(i).getText();
            if (text.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !text.equals(newMethod.name)) {
                newMethod.returnType = text;
                break;
            }
        }

        // Extract parameters
        boolean inParams = false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String text = ctx.getChild(i).getText();
            if (text.equals("(")) inParams = true;
            if (text.equals(")")) inParams = false;
            if (inParams && ctx.getChild(i) instanceof TerminalNode && !text.matches("[()]")) {
                newMethod.parameters.add(text);
            }
        }

        // Extract body
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof ParserRuleContext) {
                String childText = ctx.getChild(i).getText();
                if (childText.startsWith("{") && childText.endsWith("}")) {
                    newMethod.body = childText;
                    break;
                }
            }
        }

        structure.classes.add(new PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure()); // Dummy class for global functions
        structure.classes.get(structure.classes.size() - 1).methods.add(newMethod);
    }

    @Override
    public void exitClassDeclaration(PhpParser.ClassDeclarationContext ctx) {
        currentClass = null; // Reset current class after exiting
    }
}