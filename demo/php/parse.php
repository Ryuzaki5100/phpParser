<?php
require 'vendor/autoload.php';

use PhpParser\ParserFactory;
use PhpParser\NodeTraverser;
use PhpParser\NodeVisitorAbstract;
use PhpParser\Node;
use PhpParser\PrettyPrinter;

class StructureVisitor extends NodeVisitorAbstract {
    private $structure;
    private $prettyPrinter;
    private $variables = []; // Track variables and their usage

    public function __construct() {
        $this->structure = [
            'fileName' => '',
            'namespaces' => [],
            'useStatements' => [],
            'includes' => [],
            'constants' => [],
            'functions' => [],
            'classes' => [],
            'traits' => [],
            'enums' => [],
            'variables' => [],
            'statements' => []
        ];
        $this->prettyPrinter = new PrettyPrinter\Standard();
    }

    public function enterNode(Node $node) {
        // Namespace
        if ($node instanceof Node\Stmt\Namespace_) {
            $this->structure['namespaces'][] = (string)$node->name;
            $this->addStatements($node->stmts);
        }
        // Use statements
        elseif ($node instanceof Node\Stmt\Use_) {
            foreach ($node->uses as $use) {
                $this->structure['useStatements'][] = [
                    'fullyQualifiedName' => (string)$use->name,
                    'alias' => $use->alias ? (string)$use->alias->name : ''
                ];
            }
        }
        // Include/Require
        elseif ($node instanceof Node\Stmt\Include_ || $node instanceof Node\Stmt\Require_) {
            $this->structure['includes'][] = (string)$node->expr->value;
            $this->structure['statements'][] = [
                'type' => 'include',
                'content' => $this->prettyPrinter->prettyPrint([$node]),
                'targetVariable' => '',
                'methodName' => '',
                'parameters' => [],
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                'nestedStatements' => []
            ];
        }
        // Constants via define()
        elseif ($node instanceof Node\Expr\FuncCall && $node->name instanceof Node\Name && $node->name->toString() === 'define') {
            $args = $node->args;
            if (count($args) >= 2) {
                $this->structure['constants'][] = [
                    'name' => (string)$args[0]->value->value,
                    'value' => var_export($args[1]->value->value, true),
                    'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : ''
                ];
            }
            $this->structure['statements'][] = [
                'type' => 'constant_definition',
                'content' => $this->prettyPrinter->prettyPrintExpr($node),
                'targetVariable' => '',
                'methodName' => 'define',
                'parameters' => [ (string)$args[0]->value->value, var_export($args[1]->value->value, true) ],
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                'nestedStatements' => []
            ];
        }
        // Class definitions
        elseif ($node instanceof Node\Stmt\Class_) {
            $class = [
                'name' => (string)$node->name,
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                'visibility' => 'public',
                'isAbstract' => $node->isAbstract(),
                'isFinal' => $node->isFinal(),
                'attributes' => array_map('strval', $node->attrGroups),
                'extendedClass' => $node->extends ? (string)$node->extends : '',
                'implementedInterfaces' => array_map('strval', $node->implements),
                'properties' => [],
                'methods' => [],
                'nestedClasses' => []
            ];
            $this->structure['classes'][] = $class;
        }
        // Function definitions
        elseif ($node instanceof Node\Stmt\Function_) {
            $func = [
                'name' => (string)$node->name,
                'returnType' => $node->returnType ? (string)$node->returnType : '',
                'parameters' => array_map(function ($param) {
                    return [
                        'name' => (string)$param->var->name,
                        'typeHint' => $param->type ? (string)$param->type : '',
                        'defaultValue' => $param->default ? var_export($param->default->value, true) : '',
                        'isPassedByReference' => $param->byRef,
                        'isVariadic' => $param->variadic
                    ];
                }, $node->params),
                'body' => $this->prettyPrinter->prettyPrint($node->stmts),
                'attributes' => array_map('strval', $node->attrGroups),
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : ''
            ];
            $this->structure['functions'][] = $func;
        }
        // Procedural statements
        elseif ($node instanceof Node\Stmt\Expression) {
            if ($node->expr instanceof Node\Expr\Assign) {
                $varName = $node->expr->var instanceof Node\Expr\Variable ? (string)$node->expr->var->name : '';
                $this->addVariable($varName, $node);
                $this->structure['statements'][] = [
                    'type' => 'assignment',
                    'content' => $this->prettyPrinter->prettyPrintExpr($node->expr),
                    'targetVariable' => $varName,
                    'methodName' => '',
                    'parameters' => [],
                    'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($node->expr instanceof Node\Expr\MethodCall) {
                $varName = $node->expr->var instanceof Node\Expr\Variable ? (string)$node->expr->var->name : '';
                $methodName = (string)$node->expr->name;
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $node->expr->args);
                $this->addVariable($varName, $node, $methodName);
                $this->structure['statements'][] = [
                    'type' => 'method_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($node->expr),
                    'targetVariable' => $varName,
                    'methodName' => $methodName,
                    'parameters' => $params,
                    'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($node->expr instanceof Node\Expr\StaticCall) {
                $className = (string)$node->expr->class;
                $methodName = (string)$node->expr->name;
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $node->expr->args);
                $this->structure['statements'][] = [
                    'type' => 'static_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($node->expr),
                    'targetVariable' => $className,
                    'methodName' => $methodName,
                    'parameters' => $params,
                    'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($node->expr instanceof Node\Expr\FuncCall) {
                $funcName = $node->expr->name instanceof Node\Name ? (string)$node->expr->name : '';
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $node->expr->args);
                $this->structure['statements'][] = [
                    'type' => 'function_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($node->expr),
                    'targetVariable' => '',
                    'methodName' => $funcName,
                    'parameters' => $params,
                    'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            }
        }
        // Control structures (if)
        elseif ($node instanceof Node\Stmt\If_) {
            $this->structure['statements'][] = [
                'type' => 'if',
                'content' => $this->prettyPrinter->prettyPrint([$node]),
                'targetVariable' => '',
                'methodName' => '',
                'parameters' => [],
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : '',
                'nestedStatements' => $this->collectNestedStatements($node->stmts)
            ];
        }
    }

    private function addVariable(string $name, Node $node, string $methodCall = '') {
        if (!$name) return;
        if (!isset($this->variables[$name])) {
            $inferredType = '';
            if ($node->getDocComment()) {
                $doc = $node->getDocComment()->getText();
                if (preg_match('/@var\s+([^\s]+)/', $doc, $matches)) {
                    $inferredType = $matches[1];
                }
            }
            $this->variables[$name] = [
                'name' => $name,
                'inferredType' => $inferredType,
                'methodCalls' => [],
                'docComment' => $node->getDocComment() ? $node->getDocComment()->getText() : ''
            ];
        }
        if ($methodCall) {
            $this->variables[$name]['methodCalls'][] = $methodCall;
        }
        $this->structure['variables'] = array_values($this->variables);
    }

    private function collectNestedStatements(array $stmts): array {
        $nested = [];
        foreach ($stmts as $stmt) {
            if ($stmt instanceof Node\Stmt\Expression && $stmt->expr instanceof Node\Expr\Assign) {
                $varName = $stmt->expr->var instanceof Node\Expr\Variable ? (string)$stmt->expr->var->name : '';
                $this->addVariable($varName, $stmt);
                $nested[] = [
                    'type' => 'assignment',
                    'content' => $this->prettyPrinter->prettyPrintExpr($stmt->expr),
                    'targetVariable' => $varName,
                    'methodName' => '',
                    'parameters' => [],
                    'docComment' => $stmt->getDocComment() ? $stmt->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($stmt instanceof Node\Stmt\If_) {
                $nested[] = [
                    'type' => 'if',
                    'content' => $this->prettyPrinter->prettyPrint([$stmt]),
                    'targetVariable' => '',
                    'methodName' => '',
                    'parameters' => [],
                    'docComment' => $stmt->getDocComment() ? $stmt->getDocComment()->getText() : '',
                    'nestedStatements' => $this->collectNestedStatements($stmt->stmts)
                ];
            } elseif ($stmt instanceof Node\Stmt\Expression && $stmt->expr instanceof Node\Expr\MethodCall) {
                $varName = $stmt->expr->var instanceof Node\Expr\Variable ? (string)$stmt->expr->var->name : '';
                $methodName = (string)$stmt->expr->name;
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $stmt->expr->args);
                $this->addVariable($varName, $stmt, $methodName);
                $nested[] = [
                    'type' => 'method_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($stmt->expr),
                    'targetVariable' => $varName,
                    'methodName' => $methodName,
                    'parameters' => $params,
                    'docComment' => $stmt->getDocComment() ? $stmt->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($stmt instanceof Node\Stmt\Expression && $stmt->expr instanceof Node\Expr\StaticCall) {
                $className = (string)$stmt->expr->class;
                $methodName = (string)$stmt->expr->name;
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $stmt->expr->args);
                $nested[] = [
                    'type' => 'static_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($stmt->expr),
                    'targetVariable' => $className,
                    'methodName' => $methodName,
                    'parameters' => $params,
                    'docComment' => $stmt->getDocComment() ? $stmt->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            } elseif ($stmt instanceof Node\Stmt\Expression && $stmt->expr instanceof Node\Expr\FuncCall) {
                $funcName = $stmt->expr->name instanceof Node\Name ? (string)$stmt->expr->name : '';
                $params = array_map(function ($arg) { return $this->prettyPrinter->prettyPrintExpr($arg->value); }, $stmt->expr->args);
                $nested[] = [
                    'type' => 'function_call',
                    'content' => $this->prettyPrinter->prettyPrintExpr($stmt->expr),
                    'targetVariable' => '',
                    'methodName' => $funcName,
                    'parameters' => $params,
                    'docComment' => $stmt->getDocComment() ? $stmt->getDocComment()->getText() : '',
                    'nestedStatements' => []
                ];
            }
        }
        return $nested;
    }

    private function addStatements(array $stmts) {
        foreach ($stmts as $stmt) {
            $this->enterNode($stmt);
        }
    }

    public function getStructure() {
        return $this->structure;
    }
}

// Read PHP code from STDIN
$phpCode = file_get_contents('php://stdin');
$parser = (new ParserFactory)->create(ParserFactory::PREFER_PHP7);
$traverser = new NodeTraverser();
$visitor = new StructureVisitor();
$traverser->addVisitor($visitor);

try {
    $stmts = $parser->parse($phpCode);
    $traverser->traverse($stmts);
    echo json_encode($visitor->getStructure(), JSON_PRETTY_PRINT);
} catch (Exception $e) {
    echo json_encode(['error' => $e->getMessage()]);
}