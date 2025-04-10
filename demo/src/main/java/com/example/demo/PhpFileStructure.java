package com.example.demo;

import java.util.*;

public class PhpFileStructure {
    public String fileName = "";
    public List<String> namespaces = new ArrayList<>();
    public List<PhpUseStatement> useStatements = new ArrayList<>();
    public List<String> includes = new ArrayList<>();
    public List<PhpConstantStructure> constants = new ArrayList<>();

    @Override
    public String toString() {
        return "PhpFileStructure{" +
                "fileName='" + fileName + '\'' +
                ", namespaces=" + namespaces +
                ", useStatements=" + useStatements +
                ", includes=" + includes +
                ", constants=" + constants +
                ", functions=" + functions +
                ", classes=" + classes +
                ", traits=" + traits +
                ", enums=" + enums +
                ", interfaces=" + interfaces +
                ", variables=" + variables +
                ", statements=" + statements +
                '}';
    }

    public List<PhpFunctionStructure> functions = new ArrayList<>();
    public List<PhpClassStructure> classes = new ArrayList<>();
    public List<PhpTraitStructure> traits = new ArrayList<>();
    public List<PhpEnumStructure> enums = new ArrayList<>();
    public List<PhpInterfaceStructure> interfaces = new ArrayList<>(); // Added for interfaces
    public List<PhpVariableStructure> variables = new ArrayList<>();
    public List<PhpStatementStructure> statements = new ArrayList<>();

    public static class PhpUseStatement {
        public String fullyQualifiedName = "";
        public String alias = "";

        @Override
        public String toString() {
            return "PhpUseStatement{" +
                    "fullyQualifiedName='" + fullyQualifiedName + '\'' +
                    ", alias='" + alias + '\'' +
                    '}';
        }
    }

    public static class PhpConstantStructure {
        public String name = "";
        public String value = "";
        public String docComment = "";

        @Override
        public String toString() {
            return "PhpConstantStructure{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpFunctionStructure {
        public String name = "";
        public String returnType = "";
        public List<PhpParameterStructure> parameters = new ArrayList<>();
        public String body = "";
        public List<String> attributes = new ArrayList<>();
        public String docComment = "";

        @Override
        public String toString() {
            return "PhpFunctionStructure{" +
                    "name='" + name + '\'' +
                    ", returnType='" + returnType + '\'' +
                    ", parameters=" + parameters +
                    ", body='" + body + '\'' +
                    ", attributes=" + attributes +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpParameterStructure {
        public String name = "";
        public String typeHint = "";
        public String defaultValue = "";
        public boolean isPassedByReference = false;
        public boolean isVariadic = false;

        @Override
        public String toString() {
            return "PhpParameterStructure{" +
                    "name='" + name + '\'' +
                    ", typeHint='" + typeHint + '\'' +
                    ", defaultValue='" + defaultValue + '\'' +
                    ", isPassedByReference=" + isPassedByReference +
                    ", isVariadic=" + isVariadic +
                    '}';
        }
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

        @Override
        public String toString() {
            return "PhpClassStructure{" +
                    "name='" + name + '\'' +
                    ", docComment='" + docComment + '\'' +
                    ", visibility='" + visibility + '\'' +
                    ", isAbstract=" + isAbstract +
                    ", isFinal=" + isFinal +
                    ", attributes=" + attributes +
                    ", extendedClass='" + extendedClass + '\'' +
                    ", implementedInterfaces=" + implementedInterfaces +
                    ", properties=" + properties +
                    ", methods=" + methods +
                    ", nestedClasses=" + nestedClasses +
                    '}';
        }
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

        @Override
        public String toString() {
            return "PhpFieldStructure{" +
                    "name='" + name + '\'' +
                    ", typeHint='" + typeHint + '\'' +
                    ", visibility='" + visibility + '\'' +
                    ", isStatic=" + isStatic +
                    ", isReadonly=" + isReadonly +
                    ", attributes=" + attributes +
                    ", defaultValue='" + defaultValue + '\'' +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
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

        @Override
        public String toString() {
            return "PhpMethodStructure{" +
                    "name='" + name + '\'' +
                    ", returnType='" + returnType + '\'' +
                    ", visibility='" + visibility + '\'' +
                    ", isStatic=" + isStatic +
                    ", isAbstract=" + isAbstract +
                    ", isFinal=" + isFinal +
                    ", attributes=" + attributes +
                    ", parameters=" + parameters +
                    ", body='" + body + '\'' +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpTraitStructure {
        public String name = "";
        public String docComment = "";
        public List<PhpMethodStructure> methods = new ArrayList<>();
        public List<PhpFieldStructure> properties = new ArrayList<>();

        @Override
        public String toString() {
            return "PhpTraitStructure{" +
                    "name='" + name + '\'' +
                    ", docComment='" + docComment + '\'' +
                    ", methods=" + methods +
                    ", properties=" + properties +
                    '}';
        }
    }

    public static class PhpEnumStructure {
        public String name = "";
        public List<String> implementsInterfaces = new ArrayList<>();
        public List<PhpEnumCaseStructure> cases = new ArrayList<>();
        public String docComment = "";

        @Override
        public String toString() {
            return "PhpEnumStructure{" +
                    "name='" + name + '\'' +
                    ", implementsInterfaces=" + implementsInterfaces +
                    ", cases=" + cases +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpEnumCaseStructure {
        public String name = "";
        public String value = "";
        public String docComment = "";

        @Override
        public String toString() {
            return "PhpEnumCaseStructure{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpInterfaceStructure {
        public String name = "";
        public String docComment = "";
        public List<PhpMethodStructure> methods = new ArrayList<>();

        @Override
        public String toString() {
            return "PhpInterfaceStructure{" +
                    "name='" + name + '\'' +
                    ", docComment='" + docComment + '\'' +
                    ", methods=" + methods +
                    '}';
        }
    }

    public static class PhpVariableStructure {
        public String name = "";
        public String inferredType = "";
        public List<String> methodCalls = new ArrayList<>();
        public String docComment = "";

        @Override
        public String toString() {
            return "PhpVariableStructure{" +
                    "name='" + name + '\'' +
                    ", inferredType='" + inferredType + '\'' +
                    ", methodCalls=" + methodCalls +
                    ", docComment='" + docComment + '\'' +
                    '}';
        }
    }

    public static class PhpStatementStructure {
        public String type = "";
        public String content = "";
        public String targetVariable = "";
        public String methodName = "";
        public List<String> parameters = new ArrayList<>();
        public String docComment = "";
        public List<PhpStatementStructure> nestedStatements = new ArrayList<>();

        @Override
        public String toString() {
            return "PhpStatementStructure{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    ", targetVariable='" + targetVariable + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", parameters=" + parameters +
                    ", docComment='" + docComment + '\'' +
                    ", nestedStatements=" + nestedStatements +
                    '}';
        }
    }
}