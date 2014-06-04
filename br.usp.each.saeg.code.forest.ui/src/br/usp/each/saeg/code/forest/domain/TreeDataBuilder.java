package br.usp.each.saeg.code.forest.domain;

import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jface.text.*;
import br.usp.each.saeg.code.forest.source.parser.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class TreeDataBuilder {

    public static TreeDataBuilderResult from(ParsingResult result, char[] originalSourceCode) {
        IScanner originalScanner = SourceCodeUtils.createClassMethodScannerOf(originalSourceCode);
        boolean firstTime = true;

        List<Map<String, Object>> props = new ArrayList<Map<String,Object>>();

        Map<String, TreeData> nameData = new HashMap<String, TreeData>();
        for (XmlClass clz : result.getClasses()) {
            TreeData tree = new TreeData();
            tree.setResource(result.getFile());

            tree.setScore(clz.getScore().floatValue());
            tree.setValue(clz.getContent());
            tree.setName(clz.getName());
            tree.setAnonymous(clz.getName().matches(".*(\\$[0-9]+)$"));

            tree.setOpenPosition(getPosition(originalScanner, clz.getStart(), clz.getEnd()));
            props.add(markerProps(tree.getScore(), tree.getOpenPosition(), clz.getStart(), clz.getEnd()));

            tree.setClosePosition(getPosition(originalScanner, clz.getClose(), clz.getClose()));
            props.add(markerProps(tree.getScore(), tree.getClosePosition(), clz.getClose()));

            tree.setStartLine(clz.getStart());
            tree.setEndLine(clz.getEnd());
            tree.setCloseLine(clz.getClose());
            tree.setLogLine(clz.getName());

            if (result.getXmlPackage().getLoc() > 0 && firstTime) {
                firstTime = false;
                tree.setPackagePosition(getPosition(originalScanner, result.getXmlPackage().getStart(), result.getXmlPackage().getEnd()));
                tree.setPackageScore(result.getXmlPackage().getScore().floatValue());
            }

            if (Configuration.CONSIDER_LINE_OCURRENCES) {
                tree.setOcurrences(clz.getNumber());
            }

            for (XmlMethod method : clz.getMethods()) {
                BranchData branch = new BranchData(tree);
                branch.setResource(result.getFile());
                branch.setScore(method.getScore().floatValue());
                branch.setValue(method.getContent());
                branch.setScriptPosition(method.getScriptPosition());
                branch.setScriptScore(method.getScriptScore());
                branch.setName(method.getName());

                branch.setPosition(getPosition(originalScanner, method.getStart(), method.getEnd()));
                props.add(markerProps(branch.getScore(), branch.getPosition(), method.getStart(), method.getEnd()));
                branch.setStartLine(method.getStart());
                branch.setEndLine(method.getEnd());
                branch.setLogLine(tree.getName() + "." + branch.getName());

                if (method.getClose() >= 0) {
                    branch.setClosePosition(getPosition(originalScanner, method.getClose(), method.getClose()));
                    props.add(markerProps(branch.getScore(), branch.getClosePosition(), method.getClose()));
                    branch.setCloseLine(method.getClose());
                }

                if (Configuration.CONSIDER_LINE_OCURRENCES) {
                    branch.setOcurrences(method.getNumber());
                }
                tree.getBranchData().add(branch);
                for (XmlStatement stmt : method.getStatements()) {
                    LeafData leaf = new LeafData();
                    leaf.setResource(result.getFile());
                    leaf.setLine(stmt.getLoc());
                    leaf.setScore(stmt.getScore().floatValue());
                    leaf.setPosition(getPosition(originalScanner, stmt.getStart(), stmt.getEnd()));
                    props.add(markerProps(leaf.getScore(), leaf.getPosition(), stmt.getStart()));
                    leaf.setValue(stmt.getContent());
                    leaf.setLogLine(tree.getName() + "." + branch.getName() + "." + stmt.getLoc());
                    branch.getLeafData().add(leaf);
                }
                Collections.sort(branch.getLeafData());
            }
            Collections.sort(tree.getBranchData());
            nameData.put(clz.getName(), tree);
        }

        TreeDataBuilderResult buildResult = new TreeDataBuilderResult(result.getFile());
        buildResult.getTreeData().addAll(nameData.values());
        buildResult.getMarkerProperties().addAll(props);

        return buildResult;
    }

    private static Map<String, Object> markerProps(float score, Position position, int... locs) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("score", score);
        result.put("loc", locs);
        if (position != null) {
            result.put(IMarker.CHAR_START, position.getOffset());
            result.put(IMarker.CHAR_END, position.getOffset() + position.getLength());
        } else {
            result.put(IMarker.CHAR_START, 0);
            result.put(IMarker.CHAR_END, 0);
        }
        return result;
    }

    private static Position getPosition(IScanner originalScanner, int start, int end) {
        if (start <= 0 || end <= 0) {
            return null;
        }
        try {
            if (originalScanner.getLineEnd(end) == 0) {
                return new Position(originalScanner.getLineStart(start), 1);
            } else {
                return new Position(originalScanner.getLineStart(start), originalScanner.getLineEnd(end) - originalScanner.getLineStart(start));
            }
        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
            return null;
        }
    }
}
