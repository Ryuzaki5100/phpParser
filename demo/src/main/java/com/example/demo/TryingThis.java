package com.example.demo;

import de.thm.mni.ii.phpparser.PHPParser;
import de.thm.mni.ii.phpparser.ast.Basic;

public class TryingThis {
    public static void main(String[] args) {
        PHPParser.Result res = (PHPParser.Result) PHPParser.parse("<?php $value = 5;");
        if (res instanceof PHPParser.Success) {
            Basic.Script s = ((PHPParser.Success) res).script();
            System.out.println(s);
        } else if (res instanceof PHPParser.Failure) {
            String msg = ((PHPParser.Failure) res).fullMsg();
            System.out.println(msg);
        }
    }
}

