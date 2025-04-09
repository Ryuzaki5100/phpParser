package com.example.demo;

import java.util.*;

public class PhpFileStructure {
    public String fileName = "";
    public List<String> namespaces = new ArrayList<>();
    public List<PhpUseStatement> useStatements = new ArrayList<>();
    public List<String> includes = new ArrayList<>();
    public List<PhpConstantStructure> constants = new ArrayList<>();
    public List<PhpFunctionStructure> functions = new ArrayList<>();
    public List<PhpClassStructure> classes = new ArrayList<>();
    public List<PhpTraitStructure> traits = new ArrayList<>();
    public List<PhpEnumStructure> enums = new ArrayList<>();
    public List<PhpVariableStructure> variables = new ArrayList<>(); // New field for variables
    public List<PhpStatementStructure> statements = new ArrayList<>();

    public static class PhpUseStatement {
        public String fullyQualifiedName = "";
        public String alias = "";
    }

    public static class PhpConstantStructure {
        public String name = "";
        public String value = "";
        public String docComment = "";
    }

    public static class PhpFunctionStructure {
        public String name = "";
        public String returnType = "";
        public List<PhpParameterStructure> parameters = new ArrayList<>();
        public String body = "";
        public List<String> attributes = new ArrayList<>();
        public String docComment = "";
    }

    public static class PhpParameterStructure {
        public String name = "";
        public String typeHint = "";
        public String defaultValue = "";
        public boolean isPassedByReference = false;
        public boolean isVariadic = false;
    }

    public static class PhpClassStructure {
        public String name = "";
        public String docComment = "";
        public String visibility = "public";
        public boolean isAbstract = false;
        public boolean isFinal = false;
        public List<String> attributes = new ArrayList<>();
        public String extendedClass = "";
        public List<String> implementedInterfaces = new ArrayList<>();
        public List<PhpFieldStructure> properties = new ArrayList<>();
        public List<PhpMethodStructure> methods = new ArrayList<>();
        public List<PhpClassStructure> nestedClasses = new ArrayList<>();
    }

    public static class PhpFieldStructure {
        public String name = "";
        public String typeHint = "";
        public String visibility = "public";
        public boolean isStatic = false;
        public boolean isReadonly = false;
        public List<String> attributes = new ArrayList<>();
        public String defaultValue = "";
        public String docComment = "";
    }

    public static class PhpMethodStructure {
        public String name = "";
        public String returnType = "";
        public String visibility = "public";
        public boolean isStatic = false;
        public boolean isAbstract = false;
        public boolean isFinal = false;
        public List<String> attributes = new ArrayList<>();
        public List<PhpParameterStructure> parameters = new ArrayList<>();
        public String body = "";
        public String docComment = "";
    }

    public static class PhpTraitStructure {
        public String name = "";
        public String docComment = "";
        public List<PhpMethodStructure> methods = new ArrayList<>();
        public List<PhpFieldStructure> properties = new ArrayList<>();
    }

    public static class PhpEnumStructure {
        public String name = "";
        public List<String> implementsInterfaces = new ArrayList<>();
        public List<PhpEnumCaseStructure> cases = new ArrayList<>();
        public String docComment = "";
    }

    public static class PhpEnumCaseStructure {
        public String name = "";
        public String value = "";
        public String docComment = "";
    }

    // New class for tracking variables
    public static class PhpVariableStructure {
        public String name = "";
        public String inferredType = ""; // e.g., from PHPDoc or instantiation
        public List<String> methodCalls = new ArrayList<>(); // Methods called on this variable
        public String docComment = "";
    }

    // Updated class for procedural statements
    public static class PhpStatementStructure {
        public String type = ""; // e.g., "assignment", "method_call", "static_call", "if", "include"
        public String content = ""; // Raw PHP code (optional, for reference)
        public String targetVariable = ""; // For method calls or assignments
        public String methodName = ""; // For method/static calls
        public List<String> parameters = new ArrayList<>(); // Arguments for calls
        public String docComment = "";
        public List<PhpStatementStructure> nestedStatements = new ArrayList<>();
    }
}