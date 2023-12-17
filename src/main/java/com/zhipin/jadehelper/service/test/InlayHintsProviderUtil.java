package com.zhipin.jadehelper.service.test;//package com.zhipin.bzlcode.service.test;
//
//
//import com.intellij.codeInsight.hints.*;
//import com.intellij.codeInsight.hints.presentation.PresentationRenderer;
//
//import com.intellij.openapi.editor.Editor;
//import com.intellij.psi.PsiFile;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class InlayHintsProviderUtil {
//
//    private static final String NO_HINTS_PREFIX = "// NO_HINTS";
//    private static InlayDumpUtil inlayDumpUtil = InlayDumpUtil.INSTANCE;
//
//    public static void verifyHintsPresence(String expectedText) {
//        HintPresence expectedHintPresence = expectedText.contains(NO_HINTS_PREFIX) ? HintPresence.NO_HINTS : HintPresence.SOME_HINTS;
//
//        Matcher matcher = InlayDumpUtil.inlayPattern.matcher(expectedText);
//        HintPresence actualHintPresence = !matcher.find() ? HintPresence.NO_HINTS : HintPresence.SOME_HINTS;
//
//        if (expectedHintPresence != actualHintPresence) {
//            throw new AssertionError("Hint presence should match the use of the " + NO_HINTS_PREFIX + " directive.");
//        }
//    }
//
//    private enum HintPresence {
//        NO_HINTS,
//        SOME_HINTS
//    }
//
//    public static <T> String processInlayHints(String sourceText, InlayHintsProvider<T> provider, T settings, Editor editor, PsiFile file) {
//        InlayHintsSinkImpl sink = new InlayHintsSinkImpl(editor);
//        InlayHintsCollector collector = provider.getCollectorFor(file, editor, settings, sink);
//
//        if (collector == null) {
//            throw new AssertionError("Collector is expected");
//        }
//
//        collector.collectTraversingAndApply(editor, file, true);
//        return InlayDumpUtil.dumpHintsInternal(sourceText, renderer -> {
//            if (!(renderer instanceof PresentationRenderer) && !(renderer instanceof LinearOrderInlayRenderer)) {
//                throw new AssertionError("renderer not supported");
//            }
//            return renderer.toString();
//        }, file, editor);
//    }
//}
//
//class InlayDumpUtil1 {
//    public static Pattern inlayPattern = Pattern.compile("...");  // Adjust based on your actual pattern
//
//    public static String dumpHintsInternal(String sourceText, InlayDumpUtil.Renderer renderer, Object file, Editor editor) {
//        // Your actual implementation to process and return the modified sourceText with inlay hints.
//        return sourceText;  // Dummy return for illustration
//    }
//
//    @FunctionalInterface
//    interface Renderer {
//        String apply(Object renderer);
//    }
//}
