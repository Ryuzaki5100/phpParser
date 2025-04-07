package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myparser.PhpLexer;
import com.myparser.PhpParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhpCodeAnalyzer {
    public static class PhpFileStructure {
        public List<String> namespaces = new ArrayList<>(); // PHP uses namespaces instead of imports
        public List<PhpClassStructure> classes = new ArrayList<>();

        public static class PhpClassStructure {
            public String name = "";
            public List<String> attributes = new ArrayList<>(); // PHP uses attributes (e.g., #[Attribute])
            public String extendedClass = ""; // PHP 'extends'
            public List<String> implementedInterfaces = new ArrayList<>(); // PHP 'implements'
            public List<PhpFieldStructure> properties = new ArrayList<>(); // PHP properties (fields)
            public List<PhpMethodStructure> methods = new ArrayList<>();
            public List<PhpClassStructure> nestedClasses = new ArrayList<>(); // For nested classes
        }

        public static class PhpFieldStructure {
            public String name = "";
            public String typeHint = ""; // PHP type hints (optional)
            public List<String> attributes = new ArrayList<>(); // Attributes on properties
        }

        public static class PhpMethodStructure {
            public String name = "";
            public String returnType = ""; // PHP return type (optional)
            public List<String> attributes = new ArrayList<>(); // Attributes on methods
            public List<String> parameters = new ArrayList<>(); // Parameter list (e.g., "type $param")
            public String body = ""; // Method body
        }
    }

    public String analyzeToJson(String phpCode) throws Exception {
        CharStream input = CharStreams.fromString(phpCode);
        PhpLexer lexer = new PhpLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PhpParser parser = new PhpParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.htmlDocument();

        PhpParserCustomListener listener = new PhpParserCustomListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(listener.getStructure());
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    public static void main(String[] args) {
        PhpCodeAnalyzer analyzer = new PhpCodeAnalyzer();
        String phpCode = "<?php\n" +
                "if ($_SERVER[\"REQUEST_METHOD\"] == \"POST\") {\n" +
                "    // Collect and sanitize user input\n" +
                "    $name = htmlspecialchars($_POST['name']);\n" +
                "    echo \"Hello, $name! Welcome to our website.\";\n" +
                "} else {\n" +
                "    // Display the form\n" +
                "    ?>\n" +
                "    <form method=\"post\" action=\"<?php echo htmlspecialchars($_SERVER[\"PHP_SELF\"]);?>\">\n" +
                "        Name: <input type=\"text\" name=\"name\">\n" +
                "        <input type=\"submit\">\n" +
                "    </form>\n" +
                "    <?php\n" +
                "}\n" +
                "?>\n";
        try {
            String jsonOutput = analyzer.analyzeToJson(phpCode);
            System.out.println(jsonOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}