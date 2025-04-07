package com.example.demo;

import java.util.*;

public class PhpFileStructure {
    public List<String> namespaces = new ArrayList<>();
    public List<PhpClassStructure> classes = new ArrayList<>();

    public static class PhpClassStructure {
        public String name = "";
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
        public List<String> attributes = new ArrayList<>();
    }

    public static class PhpMethodStructure {
        public String name = "";
        public String returnType = "";
        public List<String> attributes = new ArrayList<>();
        public List<String> parameters = new ArrayList<>();
        public String body = "";
    }
}
