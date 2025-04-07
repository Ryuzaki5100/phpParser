<?php
require __DIR__ . '/vendor/autoload.php';

use PhpParser\ParserFactory;
use PhpParser\NodeTraverser;
use PhpParser\NodeVisitorAbstract;
use PhpParser\Node;

$code = file_get_contents("php://stdin");

$parser = (new ParserFactory)->create(ParserFactory::PREFER_PHP7);
$ast = $parser->parse($code);

$traverser = new NodeTraverser();
$traverser->addVisitor(new class extends NodeVisitorAbstract {
    public function enterNode(Node $node) {
        $node->setAttribute('nodeType', $node->getType());
    }
});

$ast = $traverser->traverse($ast);

function serializeAst($node) {
    if ($node instanceof Node) {
        $result = ['nodeType' => $node->getType()];
        foreach ($node->getSubNodeNames() as $name) {
            $result[$name] = serializeAst($node->$name);
        }
        return $result;
    } elseif (is_array($node)) {
        return array_map('serializeAst', $node);
    } else {
        return $node;
    }
}

echo json_encode(serializeAst($ast), JSON_PRETTY_PRINT);
?>