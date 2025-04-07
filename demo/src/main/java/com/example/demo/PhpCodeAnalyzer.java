//package com.example.demo;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.myparser.PhpLexer;
//import com.myparser.PhpParser;
//import org.antlr.v4.runtime.CharStream;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.ParseTreeWalker;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PhpCodeAnalyzer {
//    public static class PhpFileStructure {
//        public List<String> namespaces = new ArrayList<>(); // PHP uses namespaces instead of imports
//        public List<PhpClassStructure> classes = new ArrayList<>();
//
//        public static class PhpClassStructure {
//            public String name = "";
//            public List<String> attributes = new ArrayList<>(); // PHP uses attributes (e.g., #[Attribute])
//            public String extendedClass = ""; // PHP 'extends'
//            public List<String> implementedInterfaces = new ArrayList<>(); // PHP 'implements'
//            public List<PhpFieldStructure> properties = new ArrayList<>(); // PHP properties (fields)
//            public List<PhpMethodStructure> methods = new ArrayList<>();
//            public List<PhpClassStructure> nestedClasses = new ArrayList<>(); // For nested classes
//        }
//
//        public static class PhpFieldStructure {
//            public String name = "";
//            public String typeHint = ""; // PHP type hints (optional)
//            public List<String> attributes = new ArrayList<>(); // Attributes on properties
//        }
//
//        public static class PhpMethodStructure {
//            public String name = "";
//            public String returnType = ""; // PHP return type (optional)
//            public List<String> attributes = new ArrayList<>(); // Attributes on methods
//            public List<String> parameters = new ArrayList<>(); // Parameter list (e.g., "type $param")
//            public String body = ""; // Method body
//        }
//    }
//
//    public String analyzeToJson(String phpCode) throws Exception {
//        CharStream input = CharStreams.fromString(phpCode);
//        PhpLexer lexer = new PhpLexer(input);
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        PhpParser parser = new PhpParser(tokens);
//        parser.setBuildParseTree(true);
//        ParseTree tree = parser.phpBlock(); // Changed from htmlDocument to phpBlock
//
//        System.out.println("Parse tree root: " + tree.getText().substring(0, Math.min(tree.getText().length(), 100)) + "...");
//
//        PhpParserCustomListener listener = new PhpParserCustomListener();
//        ParseTreeWalker walker = new ParseTreeWalker();
//        walker.walk(listener, tree);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(listener.getStructure());
//            System.out.println("Generated JSON: " + json);
//            return json;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert to JSON", e);
//        }
//    }
//
//    public static void main(String[] args) {
//        PhpCodeAnalyzer analyzer = new PhpCodeAnalyzer();
//        String phpCode = "<?php\n" +
//                "/**\n" +
//                " * Start Screen\n" +
//                " * \n" +
//                " * @package    Ink DCS Presentation Layer\n" +
//                " * @copyright  Copyright(c) Ink Aviation {@link http://inkav.com}\n" +
//                " */\n" +
//                "// git test\n" +
//                "include \"configuration.php\";\n" +
//                " \n" +
//                "use Helpers\\Adaptors\\CommonAdaptor;\n" +
//                " \n" +
//                "this_server::redirect_to_node_server($PHP_SELF);\n" +
//                " \n" +
//                "anti_xss::validate_vars(array(\n" +
//                "  'adaptor_port'                 => array('type' => 'int'),\n" +
//                "  'set_company'                  => array('type' => 'int'),\n" +
//                "  // ... (rest of the array) ...\n" +
//                "  'force_home'                   => array('type' => 'int'),\n" +
//                "  '_'                            => array('type' => 'string'),\n" +
//                "  'login_success'                => array('type' => 'string'),\n" +
//                "));\n" +
//                " \n" +
//                "$nav_cache_name = PROJECT_NAME . \"navigation_\" . $live_user_session->live_key;\n" +
//                " \n" +
//                "if (XCACHE)\n" +
//                "{\n" +
//                "xcache_unset($nav_cache_name);\n" +
//                "}\n" +
//                " \n" +
//                "CommonAdaptor::setPortCookie($vars->adaptor_port);\n" +
//                " \n" +
//                "if ($vars->set_company && $vars->new_user_key)\n" +
//                "{\n" +
//                "  $live_user_session->change_login($vars->new_user_key, $live_user_session->live_user_key);\n" +
//                "  exit();\n" +
//                "}\n" +
//                " \n" +
//                "// ... (rest of the if blocks) ...\n" +
//                " \n" +
//                "$controller->factory_view();\n" +
//                " \n" +
//                "?>\n";
//
//        try {
//            String jsonOutput = analyzer.analyzeToJson(phpCode);
//            System.out.println("Final JSON Output: " + jsonOutput);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}