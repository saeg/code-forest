package br.usp.each.saeg.code.forest.source.parser;

import java.math.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.dom.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class SourceCodeParser extends ASTVisitor {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final XmlInput input;
    private final CompilationUnit cu;
    private final IScanner classMethodScanner;
    private final IScanner statementScanner;


    private String packageName = "";
    private Deque<String> classes = new ArrayDeque<String>();
    private String baseClassName = "";
    private Map<String, Integer> classNameCounter = new HashMap<String, Integer>();
    private Map<String, Integer> classNameBlockCounter = new HashMap<String, Integer>();
    private Map<String, Integer> classNameStaticBlockCounter = new HashMap<String, Integer>();

    private final ParsingResult parsingResult;
    private static final BigDecimal MINUS_ONE = new BigDecimal("-1");

    public SourceCodeParser(CompilationUnit cu, char[] src, XmlInput input, ParsingResult result) {
        this.input = input;
        this.cu = cu;
        this.classMethodScanner = SourceCodeUtils.createClassMethodScannerOf(src);
        this.statementScanner = SourceCodeUtils.createStatementScannerOf(src);
        this.parsingResult = result;
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        parsingResult.packageDeclared();
        packageName = node.getName().getFullyQualifiedName();
        findXmlPackageIfNeeded();

        baseClassName = SourceCodeUtils.asString(parsingResult.getFile());
        baseClassName = baseClassName.replace(".java", "");
        baseClassName = baseClassName.replaceAll("/", ".");
        baseClassName = baseClassName.substring(baseClassName.lastIndexOf(packageName));

        addClass(baseClassName);
        int loc = getLineNumberOf(node.getStartPosition());
        parsingResult.getXmlPackage().setLoc(loc);
        parsingResult.getXmlPackage().setStart(loc);
        parsingResult.getXmlPackage().setEnd(loc);
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        findXmlPackageIfNeeded();

        String currentClassName = node.getName().getFullyQualifiedName();
        if (parsingResult.isDeclaredPackage()) {
            currentClassName = packageName + "." + node.getName().getFullyQualifiedName();
        }
        if (!currentClassName.equals(baseClassName)) {
            int modifiers = node.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                addClass(getCurrentClass() + "$" + node.getName().getFullyQualifiedName());
            } else {
                addClass((parsingResult.isDeclaredPackage() ? (packageName + ".") : "") + node.getName().getFullyQualifiedName());
            }
        }

        XmlClass xmlClass = findParsedXmlClass();
        if (node.isInterface()) {
            xmlClass.setJavaInterface(true);
        }
        addXmlClassToResult(node, xmlClass);
        return true;
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        removeCurrentClass();
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        findXmlPackageIfNeeded();

        if (!(packageName + "." + node.getName().getFullyQualifiedName()).equals(baseClassName)) {
            String currentClassName = getCurrentClass() + "$" + node.getName();
            addClass(currentClassName);
        }

        XmlClass xmlClass = findParsedXmlClass();
        addXmlClassToResult(node, xmlClass);
        return true;
    }

    @Override
    public void endVisit(EnumDeclaration node) {
        removeCurrentClass();
    }

    public boolean visit(AnonymousClassDeclaration node) {
        findXmlPackageIfNeeded();
        String currentClassName = getCurrentClass();
        if (!classNameCounter.containsKey(currentClassName)) {
            classNameCounter.put(currentClassName, 1);
        } else {
            classNameCounter.put(currentClassName, classNameCounter.get(currentClassName) + 1);
        }
        String anonClassName = currentClassName + "$" + classNameCounter.get(currentClassName);
        addClass(anonClassName);

        XmlClass xmlClass = findParsedXmlClass();
        addXmlClassToResult(node, xmlClass);
        return true;
    }

    @Override
    public void endVisit(AnonymousClassDeclaration node) {
        removeCurrentClass();
    }

    private void addXmlClassToResult(ASTNode node, XmlClass xmlClass) {
        xmlClass.setContent(node.toString());
        int end = findFirstPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameLBRACE);
        int start = findLastPositionOf(node.getStartPosition(), end - node.getStartPosition(), ITerminalSymbols.TokenNameIdentifier);
        int close = findLastPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameRBRACE);
        parsingResult.addClass(getCurrentClass(), getLineNumberOf(start), getLineNumberOf(end), getLineNumberOf(close), xmlClass);
    }

    @Override
    public boolean visit(Initializer node) {
        findXmlPackageIfNeeded();

        String methodName = "initializer";
        String currentClassName = getCurrentClass();

        int modifiers = node.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            methodName = "static " + methodName;
            if (!classNameStaticBlockCounter.containsKey(currentClassName)) {
                classNameStaticBlockCounter.put(currentClassName, 1);
            } else {
                classNameStaticBlockCounter.put(currentClassName, classNameStaticBlockCounter.get(currentClassName) + 1);
            }
            methodName = methodName + "$" + classNameStaticBlockCounter.get(currentClassName);

        } else {

            if (!classNameBlockCounter.containsKey(currentClassName)) {
                classNameBlockCounter.put(currentClassName, 1);
            } else {
                classNameBlockCounter.put(currentClassName, classNameBlockCounter.get(currentClassName) + 1);
            }
            methodName = methodName + "$" + classNameBlockCounter.get(currentClassName);
        }

        XmlClass foundXmlClass = parsingResult.getClass(getCurrentClass());
        XmlMethod xmlMethod = new XmlMethod();
        foundXmlClass.getMethods().add(xmlMethod);

        int startEnd = findFirstPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameLBRACE);
        int close = findLastPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameRBRACE);

        xmlMethod.setStart(getLineNumberOf(startEnd));
        xmlMethod.setEnd(getLineNumberOf(startEnd));
        xmlMethod.setClose(getLineNumberOf(close));
        xmlMethod.setName(methodName);
        xmlMethod.setScore(MINUS_ONE);
        xmlMethod.setContent(node.toString());
        xmlMethod.setScriptPosition(0);
        xmlMethod.setScriptScore(0F);

        if (node.getBody() == null || node.getBody().statements() == null || node.getBody().statements().isEmpty()) {
            return true;
        }

        for (Object o : node.getBody().statements()) {
            Statement stmt = (Statement) o;
            XmlStatement xmlStatement = new XmlStatement();
            xmlStatement.setScore(MINUS_ONE);
            xmlStatement.setStart(getLineNumberOf(stmt.getStartPosition()));
            xmlStatement.setEnd(getLineNumberOf(stmt.getStartPosition() + stmt.getLength()));
            xmlStatement.setContent(stmt.toString());
            xmlMethod.getStatements().add(xmlStatement);
        }
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        String methodName = node.getName().getFullyQualifiedName();
        if (node.parameters() != null && node.parameters() != null) {
            List<String> parameters = new ArrayList<String>();
            for (Object o : node.parameters()) {
                SingleVariableDeclaration decl = (SingleVariableDeclaration) o;
                parameters.add(decl.getType().toString());
            }
            methodName = methodName + "(" + StringUtils.join(parameters, ",") + ")";
        }

        XmlClass xmlClass = findParsedXmlClass();
        XmlMethod xmlMethod = xmlClass.byName(methodName);
        if (xmlMethod == null) {
            CodeForestUIPlugin.warn("method [" + methodName + "], class [" + getCurrentClass() + "] not found in codeforest.xml");
            xmlMethod = new XmlMethod();
            xmlMethod.setName(methodName);
            xmlMethod.setScore(MINUS_ONE);
            xmlClass.addMethod(xmlMethod);
        }

        XmlClass foundXmlClass = parsingResult.getClass(getCurrentClass());

        int modifiers = node.getModifiers();
        int end = node.getStartPosition();
        int close = -1;
        if (foundXmlClass.isJavaInterface() || Modifier.isAbstract(modifiers)) {
            end = node.getStartPosition() + node.getLength();
        } else {
            end = findFirstPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameLBRACE);
            close = findLastPositionOf(node.getStartPosition(), node.getLength(), ITerminalSymbols.TokenNameRBRACE);
        }
        int start = findLastPositionOf(node.getStartPosition(), end - node.getStartPosition(), ITerminalSymbols.TokenNameLPAREN);

        XmlMethod foundXmlMethod = new XmlMethod();
        foundXmlClass.getMethods().add(foundXmlMethod);
        foundXmlMethod.setStart(getLineNumberOf(start));
        foundXmlMethod.setEnd(getLineNumberOf(end));
        if (close < 0) {
            foundXmlMethod.setClose(close);
        } else {
            foundXmlMethod.setClose(getLineNumberOf(close));
        }
        foundXmlMethod.setName(methodName);
        foundXmlMethod.setNumber(xmlMethod.getNumber());
        foundXmlMethod.setScore(xmlMethod.getScore());
        foundXmlMethod.setScriptPosition(xmlMethod.getScriptPosition());
        foundXmlMethod.setScriptScore(xmlMethod.getScriptScore());
        foundXmlMethod.setContent(node.toString());

        if (node.getBody() == null || node.getBody().statements() == null || node.getBody().statements().isEmpty()) {
            return true;
        }

        int delta = xmlMethod.getLoc() - (classMethodScanner.getLineNumber(node.getStartPosition()));
        for (Object raw : node.getBody().statements()) {
            Statement superStmt = (Statement) raw;
            int stmtStart = classMethodScanner.getLineStart(cu.getLineNumber(superStmt.getStartPosition()));
            int stmtEnd = classMethodScanner.getLineEnd(cu.getLineNumber(superStmt.getStartPosition() + superStmt.getLength()));
            List<String> splitted = readIt(stmtStart, stmtEnd - stmtStart);

            int relativeLoc = classMethodScanner.getLineNumber(superStmt.getStartPosition()) - classMethodScanner.getLineNumber(node.getStartPosition());
            int absoluteLoc = classMethodScanner.getLineNumber(superStmt.getStartPosition());

            for (String line : splitted) {
                int relativeLineNumber = relativeLoc++;
                int absoluteLineNumber = absoluteLoc++;
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                XmlStatement xmlStatement = xmlMethod.byAbsoluteLoc(absoluteLineNumber);
                if (xmlStatement == null) {
                    CodeForestUIPlugin.warn("line of code [" + relativeLoc + "], method [" + methodName + "], class [" + getCurrentClass() + "] not found in codeforest.xml");
                    xmlStatement = xmlMethod.byRelativeLoc(relativeLineNumber + delta);

                    if (xmlStatement == null) {
                        xmlStatement = new XmlStatement();
                        xmlStatement.setScore(MINUS_ONE);
                        xmlMethod.addStatements(xmlStatement);
                    }
                }

                //FIXME obter a de maior score
                XmlStatement foundXmlStatement = new XmlStatement();
                foundXmlStatement.setScore(xmlStatement.getScore());
                foundXmlStatement.setStart(relativeLineNumber + classMethodScanner.getLineNumber(node.getStartPosition()));
                foundXmlStatement.setEnd(relativeLineNumber + classMethodScanner.getLineNumber(node.getStartPosition()));
                foundXmlStatement.setContent(line);
                foundXmlMethod.getStatements().add(foundXmlStatement);
            }
        }
        return true;
    }

    private List<String> readIt(int start, int length) {
        StringBuilder result = new StringBuilder();
        try {
            statementScanner.resetTo(start, start + length);
            int token;
            while ((token = statementScanner.getNextToken()) != ITerminalSymbols.TokenNameEOF) {
                String str = new String(statementScanner.getCurrentTokenSource());//FIXME bug aqui
                if (token == ITerminalSymbols.TokenNameCOMMENT_BLOCK || token == ITerminalSymbols.TokenNameCOMMENT_JAVADOC || token == ITerminalSymbols.TokenNameCOMMENT_LINE) {
                    for (int i = 0; i < StringUtils.countMatches(str, LINE_SEPARATOR); i++) {
                        result.append(LINE_SEPARATOR);
                    }
                } else {
                    result.append(statementScanner.getCurrentTokenSource());
                }
            }
        } catch (Exception ignored) {
            CodeForestUIPlugin.log("erro ao ler o arquivo [" + parsingResult.getURI() + "]", ignored);

        } finally {
            statementScanner.resetTo(cu.getStartPosition(), cu.getStartPosition() + cu.getLength());
        }
        return Arrays.asList(result.toString().split(LINE_SEPARATOR));
    }


    private int findFirstPositionOf(int start, int length, int symbol) {
        try {
            classMethodScanner.resetTo(start, start + length);
            int token;
            while ((token = classMethodScanner.getNextToken()) != ITerminalSymbols.TokenNameEOF) {
                if (token == symbol) {
                    break;
                }
            }
            return classMethodScanner.getCurrentTokenEndPosition();
        } catch (InvalidInputException ignored) {

        } finally {
            classMethodScanner.resetTo(cu.getStartPosition(), cu.getStartPosition() + cu.getLength());
        }
        return -1;
    }

    private int findLastPositionOf(int start, int length, int symbol) {
        int identifierPos = start;
        try {
            classMethodScanner.resetTo(start, start + length);
            int token;
            while ((token = classMethodScanner.getNextToken()) != ITerminalSymbols.TokenNameEOF) {
                if (token == symbol) {
                    identifierPos = classMethodScanner.getCurrentTokenStartPosition();
                }
            }
        } catch (InvalidInputException ignored) {

        } finally {
            classMethodScanner.resetTo(cu.getStartPosition(), cu.getStartPosition() + cu.getLength());
        }
        return identifierPos;
    }

    private void removeCurrentClass() {
        classes.pollLast();
    }

    private void addClass(String name) {
        classes.offer(name);
    }

    private String getCurrentClass() {
        return classes.peekLast();
    }

    private XmlClass findParsedXmlClass() {
        XmlClass xmlClass = parsingResult.getXmlPackage().byName(getCurrentClass());
        if (xmlClass == null) {
            CodeForestUIPlugin.warn("class [" + getCurrentClass() + "] not found in codeforest.xml");
            XmlClass fake = new XmlClass();
            fake.setName(getCurrentClass());
            fake.setScore(MINUS_ONE);
            parsingResult.getXmlPackage().addClass(fake);
            xmlClass = fake;
        }
        return xmlClass;
    }

    private void findXmlPackageIfNeeded() {
        if (parsingResult.isXmlPackageNull()) {
            parsingResult.setXmlPackage(input.byName(packageName));
        }
        if (parsingResult.isXmlPackageNull()) {
            CodeForestUIPlugin.warn("package [" + packageName + "] not found in codeforest.xml");
            XmlPackage fake = new XmlPackage();
            fake.setName(packageName);
            fake.setScore(MINUS_ONE);
            parsingResult.setXmlPackage(fake);
        }
        if (!parsingResult.isDeclaredPackage()) {
            parsingResult.getXmlPackage().setLoc(-1);
        }
    }

    private int getLineNumberOf(int position) {
        return classMethodScanner.getLineNumber(position);
    }
}
