//package com.example.demo;
//
//import com.myparser.PhpParser;
//import com.myparser.PhpParserBaseListener;
//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.TerminalNode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PhpParserCustomListener extends PhpParserBaseListener {
//    private PhpCodeAnalyzer.PhpFileStructure structure = new PhpCodeAnalyzer.PhpFileStructure();
//    private PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure currentClass = null;
//    private PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure currentMethod = null;
//    private StringBuilder currentMethodBody = new StringBuilder();
//
//    public PhpCodeAnalyzer.PhpFileStructure getStructure() {
//        return structure;
//    }
//
//    // New method for namespace declaration (adjust rule name if different)
//    @Override
//    public void enterNamespaceDeclaration(PhpParser.NamespaceDeclarationContext ctx) {
//        System.out.println("NamespaceDeclarationContext: " + ctx.getText());
//        for (int i = 0; i < ctx.getChildCount(); i++) {
//            if (ctx.getChild(i) instanceof TerminalNode) {
//                String text = ctx.getChild(i).getText();
//                // Look for a valid namespace name (e.g., alphanumeric with backslashes)
//                if (text.matches("[a-zA-Z_][a-zA-Z0-9_\\\\]*")) {
//                    structure.namespaces.add(text);
//                    break;
//                }
//            } else {
//                System.out.println("Child " + i + ": " + ctx.getChild(i).getText() + " (Type: " + ctx.getChild(i).getClass().getSimpleName() + ")");
//            }
//        }
//    }
//
//    // Handle statements within namespace (optional, if needed)
//    @Override
//    public void enterNamespaceStatement(PhpParser.NamespaceStatementContext ctx) {
//        System.out.println("NamespaceStatementContext: " + ctx.getText());
//        // This context contains the body of the namespace (e.g., classes, functions)
//        // No namespace name here, but we can process nested declarations
//    }
//
//    @Override
//    public void enterClassDeclaration(PhpParser.ClassDeclarationContext ctx) {
//        PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure newClass = new PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure();
//
//        // Extract class name
//        if (ctx.identifier() != null) {
//            newClass.name = ctx.identifier().getText();
//        }
//
//        // Extract attributes and extends (fallback to child traversal)
//        System.out.println("ClassDeclarationContext: " + ctx.getText());
//        boolean inImplements = false;
//        for (int i = 0; i < ctx.getChildCount(); i++) {
//            if (ctx.getChild(i) instanceof TerminalNode) {
//                String text = ctx.getChild(i).getText();
//                if (text.startsWith("#[")) {
//                    newClass.attributes.add(text);
//                } else if (text.equals("extends")) {
//                    // Look for the next identifier as the extended class
//                    for (int j = i + 1; j < ctx.getChildCount(); j++) {
//                        if (ctx.getChild(j) instanceof TerminalNode && ctx.getChild(j).getText().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
//                            newClass.extendedClass = ctx.getChild(j).getText();
//                            break;
//                        }
//                    }
//                } else if (text.equals("implements")) {
//                    inImplements = true;
//                } else if (inImplements && ctx.getChild(i) instanceof TerminalNode && ctx.getChild(i).getText().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
//                    newClass.implementedInterfaces.add(text);
//                }
//            } else {
//                System.out.println("Child " + i + ": " + ctx.getChild(i).getText() + " (Type: " + ctx.getChild(i).getClass().getSimpleName() + ")");
//            }
//        }
//
//        if (currentClass == null) {
//            structure.classes.add(newClass);
//        } else {
//            currentClass.nestedClasses.add(newClass);
//        }
//        currentClass = newClass;
//    }
//
//    @Override
//    public void exitClassDeclaration(PhpParser.ClassDeclarationContext ctx) {
//        currentClass = null;
//    }
//
//    @Override
//    public void enterClassMemberDeclaration(PhpParser.ClassMemberDeclarationContext ctx) {
//        if (currentClass != null && ctx.variableInitializer() != null) {
//            for (PhpParser.VariableInitializerContext varInit : ctx.variableInitializer()) {
//                PhpCodeAnalyzer.PhpFileStructure.PhpFieldStructure newField = new PhpCodeAnalyzer.PhpFileStructure.PhpFieldStructure();
//
//                if (varInit.variableName() != null && varInit.variableName().VarName() != null) {
//                    newField.name = varInit.variableName().VarName().getText();
//                }
//
//                if (ctx.typeHint() != null) {
//                    newField.typeHint = ctx.typeHint().getText();
//                }
//
//                if (ctx.attributeList() != null) {
//                    for (TerminalNode attribute : ctx.attributeList().ATTRIBUTE()) {
//                        newField.attributes.add(attribute.getText());
//                    }
//                }
//
//                currentClass.properties.add(newField);
//            }
//        }
//    }
//
//    @Override
//    public void enterMethodDeclaration(PhpParser.MethodDeclarationContext ctx) {
//        if (currentClass != null) {
//            PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure newMethod = new PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure();
//
//            if (ctx.identifier() != null) {
//                newMethod.name = ctx.identifier().getText();
//            }
//
//            if (ctx.attributeList() != null) {
//                for (TerminalNode attribute : ctx.attributeList().ATTRIBUTE()) {
//                    newMethod.attributes.add(attribute.getText());
//                }
//            }
//
//            if (ctx.typeHint() != null) {
//                newMethod.returnType = ctx.typeHint().getText();
//            }
//
//            if (ctx.formalParameterList() != null) {
//                for (PhpParser.FormalParameterContext param : ctx.formalParameterList().formalParameter()) {
//                    newMethod.parameters.add(param.getText());
//                }
//            }
//
//            currentClass.methods.add(newMethod);
//            currentMethod = newMethod;
//        }
//    }
//
//    @Override
//    public void enterCompoundStatement(PhpParser.CompoundStatementContext ctx) {
//        if (currentMethod != null) {
//            currentMethodBody.setLength(0);
//        }
//    }
//
//    @Override
//    public void exitCompoundStatement(PhpParser.CompoundStatementContext ctx) {
//        if (currentMethod != null) {
//            currentMethod.body = currentMethodBody.toString().trim();
//            currentMethod = null;
//        }
//    }
//
//    @Override
//    public void visitTerminal(TerminalNode node) {
//        if (currentMethod != null && currentMethodBody.length() > 0) {
//            currentMethodBody.append(" ").append(node.getText());
//        }
//    }
//
//    @Override
//    public void enterFunctionDeclaration(PhpParser.FunctionDeclarationContext ctx) {
//        PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure newMethod = new PhpCodeAnalyzer.PhpFileStructure.PhpMethodStructure();
//
//        if (ctx.identifier() != null) {
//            newMethod.name = ctx.identifier().getText();
//        }
//
//        if (ctx.attributeList() != null) {
//            for (TerminalNode attribute : ctx.attributeList().ATTRIBUTE()) {
//                newMethod.attributes.add(attribute.getText());
//            }
//        }
//
//        if (ctx.typeHint() != null) {
//            newMethod.returnType = ctx.typeHint().getText();
//        }
//
//        if (ctx.formalParameterList() != null) {
//            for (PhpParser.FormalParameterContext param : ctx.formalParameterList().formalParameter()) {
//                newMethod.parameters.add(param.getText());
//            }
//        }
//
//        structure.classes.add(new PhpCodeAnalyzer.PhpFileStructure.PhpClassStructure());
//        structure.classes.get(structure.classes.size() - 1).methods.add(newMethod);
//    }
//}