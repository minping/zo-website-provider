package com.zode.web.notes.util;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

/**
 * @author zhoump
 * @date 2026/9/17
 * @purpose
 */
public class CommonMarkMdStat {

    public static class Count {
        public long textCharCount;
        public long codeLineCount;
    }

    public static Count readMarkDown(String md) {
        Count res = new Count();
        if (md == null || md.isEmpty()) return res;

        Parser parser = Parser.builder().build();
        Node doc = parser.parse(md);

        doc.accept(new AbstractVisitor() {
            // 普通文本节点
            @Override
            public void visit(Text text) {
                res.textCharCount += text.getLiteral().length();
                super.visit(text);
            }

            // 代码块节点（```代码块）
            @Override
            public void visit(FencedCodeBlock fencedCodeBlock) {
                String code = fencedCodeBlock.getLiteral();
                String[] lines = code.split("\\R");
                res.codeLineCount += lines.length;
                super.visit(fencedCodeBlock);
            }

            // 行内代码 `xxx`：内容计入正文字数，不算代码行
            @Override
            public void visit(Code code) {
                res.textCharCount += code.getLiteral().length();
                super.visit(code);
            }
        });
        return res;
    }

}
