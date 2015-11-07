// Copyright 2015 PlanBase Inc. & Glen Peterson
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.function.Function;

public class TupleGenerator {

    public static String ordinal(final int origI) {
        final int i = (origI < 0) ? -origI : origI;
        final int modTen = i % 10;
        if ( (modTen < 4) && (modTen > 0)) {
            int modHundred = i % 100;
            if ( (modHundred < 21) && (modHundred > 3) ) {
                return Integer.toString(origI) + "th";
            }
            switch (modTen) {
                case 1: return Integer.toString(origI) + "st";
                case 2: return Integer.toString(origI) + "nd";
                case 3: return Integer.toString(origI) + "rd";
            }
        }
        return Integer.toString(origI) + "th";
    }

    static String[] CHARS = new String[] {
            "A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    static String intToChar(int i) {
        return CHARS[i - 1];
    }

    static String types(int i) {
        StringBuilder sB = new StringBuilder();
        boolean isFirst = true;
        for (int l = 1; l <= i; l++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sB.append(",");
            }
            sB.append(intToChar(l));
        }
        return sB.toString();
    }

    static String factoryParams(int i) {
        StringBuilder sB = new StringBuilder();
        boolean isFirst = true;
        for (int l = 1; l <= i; l++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sB.append(", ");
            }
            sB.append(intToChar(l));
            sB.append(" ");
            sB.append(intToChar(l).toLowerCase());
        }
        return sB.toString();
    }

    static String copyright() {
        return "// Copyright 2015 PlanBase Inc. & Glen Peterson\n" +
               "//\n" +
               "// Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
               "// you may not use this file except in compliance with the License.\n" +
               "// You may obtain a copy of the License at\n" +
               "//\n" +
               "// http://www.apache.org/licenses/LICENSE-2.0\n" +
               "//\n" +
               "// Unless required by applicable law or agreed to in writing, software\n" +
               "// distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
               "// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
               "// See the License for the specific language governing permissions and\n" +
               "// limitations under the License.\n" +
               "\n";
    }

    static String generatedWarning() {
        return "\n" +
               "// ======================================================================================\n" +
               "// THIS CLASS IS GENERATED BY /tupleGenerator/TupleGenerator.java.  DO NOT EDIT MANUALLY!\n" +
               "// ======================================================================================\n" +
               "\n";
    }

    static void genTuple(int i) throws IOException {
        FileWriter fr = new FileWriter("../src/main/java/org/organicdesign/fp/tuple/Tuple" + i + ".java");
        fr.write(copyright() +
                 "package org.organicdesign.fp.tuple;\n" +
                 "\n" +
                 "import java.util.Objects;\n" +
                 generatedWarning() +
                 "/**\n" +
                 " Holds " + i + " items of potentially different types.  Designed to let you easily create immutable\n" +
                 " subclasses (to give your data structures meaningful names) with correct equals(), hashCode(), and\n" +
                 " toString() methods.\n" +
                 " */\n" +
                 "public class Tuple" + i + "<");
        fr.write(types(i));
        fr.write("> {\n" +
                 "    // Fields are protected so that sub-classes can make accessor methods with meaningful names.\n");
        for (int l = 1; l <= i; l++) {
            fr.write("    protected final ");
            fr.write(intToChar(l));
            fr.write(" _");
            fr.write(String.valueOf(l));
            fr.write(";\n");
        }

        fr.write("\n" +
                 "    /**\n" +
                 "     Constructor is protected (not public) for easy inheritance.  Josh Bloch's \"Item 1\" says public\n" +
                 "     static factory methods are better than constructors because they have names, they can return\n" +
                 "     an existing object instead of a new one, and they can return a sub-type.  Therefore, you\n" +
                 "     have more flexibility with a static factory as part of your public API then with a public\n" +
                 "     constructor.\n" +
                 "     */\n" +
                 "    protected Tuple" + i + "(");
        fr.write(factoryParams(i));
        fr.write(") {\n       ");
        for (int l = 1; l <= i; l++) {
            if ((l % 10) == 0) {
                fr.write("\n       ");
            }
            fr.write(" _");
            fr.write(String.valueOf(l));
            fr.write(" = ");
            fr.write(intToChar(l).toLowerCase());
            fr.write(";");
        }
        fr.write("\n" +
                 "    }\n" +
                 "\n" +
                 "    /** Public static factory method */\n" +
                 "    public static <");
        fr.write(types(i));
        fr.write("> Tuple" + i + "<");
        fr.write(types(i));
        fr.write(">");
        if (i > 7) {
            fr.write("\n   ");
        }
        fr.write(" of(");
        fr.write(factoryParams(i));
        fr.write(") {\n" +
                 "        return new Tuple" + i + "<>(");
        boolean isFirst = true;
        for (int l = 1; l <= i; l++) {
            if (isFirst) {
                isFirst = false;
            } else {
                fr.write(", ");
            }
            fr.write(intToChar(l).toLowerCase());
        }
        fr.write(");\n" +
                 "    }\n" +
                 "\n");

        for (int l = 1; l <= i; l++) {
            fr.write("    /** Returns the " + ordinal(l) + " field */\n" +
                     "    public ");
            fr.write(intToChar(l));
            fr.write(" _");
            fr.write(String.valueOf(l));
            fr.write("() { return _");
            fr.write(String.valueOf(l));
            fr.write("; }\n");
        }

        fr.write("\n" +
                 "    @Override\n" +
                 "    public String toString() {\n" +
                 "        return getClass().getSimpleName() + \"(\" +\n" +
                 "               _");
        isFirst = true;
        for (int l = 1; l <= i; l++) {
            if (isFirst) {
                isFirst = false;
            } else {
                if ((l % 8) == 0) {
                    fr.write(" + \",\" +\n" +
                             "               _");
                } else {
                    fr.write(" + \",\" + _");
                }
            }
            fr.write(String.valueOf(l));
        }
        fr.write(" + \")\";\n" +
                 "    }\n" +
                 "\n" +
                 "    @Override\n" +
                 "    public boolean equals(Object other) {\n" +
                 "        // Cheapest operation first...\n" +
                 "        if (this == other) { return true; }\n" +
                 "        if (!(other instanceof Tuple" + i + ")) { return false; }\n" +
                 "        // Details...\n" +
                 "        @SuppressWarnings(\"rawtypes\") final Tuple" + i + " that = (Tuple" + i + ") other;\n" +
                 "\n" +
                 "        return ");
        isFirst = true;
        for (int l = 1; l <= i; l++) {
            if (isFirst) {
                isFirst = false;
            } else {
                fr.write(" &&\n" +
                         "               ");
            }
            fr.write("Objects.equals(this._" + l + ", that._" + l + "())");
        }
        fr.write(";\n" +
                 "    }\n" +
                 "\n" +
                 "    @Override\n" +
                 "    public int hashCode() {\n" +
                 "        // First 2 fields match Tuple2 which implements java.util.Map.Entry as part of the map\n" +
                 "        // contract and therefore must match java.util.HashMap.Node.hashCode().\n" +
                 "        int ret = 0;\n" +
                 "        if (_1 != null) { ret = _1.hashCode(); }\n" +
                 "        if (_2 != null) { ret = ret ^ _2.hashCode(); }\n");
        for (int l = 3; l <= i; l++) {
            fr.write("        if (_" + l + " != null) { ret = ret + _" + l + ".hashCode(); }\n");
        }
        fr.write("        return ret;\n" +
                 "    }\n" +
                 "}");
        fr.flush();
        fr.close();
    }

    static String tupleTestParams(int i) {
        StringBuilder sB = new StringBuilder();
        boolean isFirst = true;
        for (int j = 1; j <= i; j++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sB.append(",");
            }
            sB.append("\"" + ordinal(j) + "\"");
        }
        return sB.toString();
    }

    static String tupleTestParamsReplace(int i, Function<Integer,String> repFun) {
        StringBuilder sB = new StringBuilder();
        boolean isFirst = true;
        for (int j = 1; j <= i; j++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sB.append(",");
            }
            String repStr = repFun.apply(j);
            if (repStr == null) {
                sB.append("\"" + ordinal(j) + "\"");
            } else {
                sB.append(repStr);
            }
        }
        return sB.toString();
    }

    static String tupleTestParamsReplace(int i, int repIdx, String repStr) {
        return tupleTestParamsReplace(i, j -> (j == repIdx) ? "\"" + repStr + "\"" : null);
    }

    static String tupleTestParamsEvenNull(int i) {
        return tupleTestParamsReplace(i, j -> ( (j % 2) == 0) ? "null" : null);
    }

    static String tupleTestParamsOddNull(int i) {
        return tupleTestParamsReplace(i, j -> ( (j % 2) == 0) ? null : "null");
    }

    static void genTupleTest(int i) throws IOException {
        FileWriter fr = new FileWriter("../src/test/java/org/organicdesign/fp/tuple/Tuple" + i +
                                       "Test.java");
        fr.write(copyright() +
                 "package org.organicdesign.fp.tuple;\n" +
                 "\n" +
                 "import org.junit.Test;\n" +
                 "\n" +
                 "import static org.junit.Assert.assertEquals;\n" +
                 "import static org.organicdesign.fp.testUtils.EqualsContract.equalsDistinctHashCode;\n" +
                 "import static org.organicdesign.fp.testUtils.EqualsContract.equalsSameHashCode;\n" +
                 generatedWarning() +
                 "public class Tuple" + i + "Test {\n" +
                 "    @Test public void constructionAndAccess() {\n" +
                 "        Tuple" + i + "<");
        boolean isFirst = true;
        for (int j = 1; j <= i; j++) {
            if (isFirst) {
                isFirst = false;
            } else {
                fr.write(",");
            }
            fr.write("String");
        }
        fr.write("> a = Tuple" + i + ".of(" + tupleTestParams(i) + ");\n" +
                 "\n");
        for (int j = 1; j <= i; j++) {
            fr.write("        assertEquals(\"" + ordinal(j) + "\", a._" + j + "());\n");
        }
        for (int j = 1; j <= i; j++) {
            fr.write("\n" +
                     "        equalsDistinctHashCode(a, Tuple" + i + ".of(" + tupleTestParams(i) + "),\n" +
                     "                               Tuple" + i + ".of(" + tupleTestParams(i) + "),\n" +
                     "                               Tuple" + i + ".of(" + tupleTestParamsReplace(i, j, "wrong") + "));\n" +
                     "\n");
        }
        fr.write("        equalsDistinctHashCode(Tuple" + i + ".of(" + tupleTestParamsEvenNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsEvenNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsEvenNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsEvenNull(i-1) + ",\"wrong\"));\n" +
                 "\n" +
                 "        equalsDistinctHashCode(Tuple" + i + ".of(" + tupleTestParamsOddNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsOddNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsOddNull(i) + "),\n" +
                 "                               Tuple" + i + ".of(" + tupleTestParamsOddNull(i-1) + ",\"wrong\"));\n" +
                 "\n" +
                 "        equalsSameHashCode(a, Tuple" + i + ".of(" + tupleTestParams(i) + "),\n" +
                 "                           Tuple" + i + ".of(" + tupleTestParams(i) + "),\n" +
                 "                           Tuple" + i + ".of(");
        // Switch order of first 2 params for same hashcode.
        fr.write("\"" + ordinal(2) + "\",\"" + ordinal(1) + "\"");
        for (int j = 3; j <= i; j++) {
            fr.write(",\"" + ordinal(j) + "\"");
        }
        fr.write("));\n" +
                 "\n" +
                 "        assertEquals(\"Tuple" + i + "(");
        isFirst = true;
        for (int j = 1; j <= i; j++) {
            if (isFirst) {
                isFirst = false;
            } else {
                fr.write(",");
            }
            fr.write(ordinal(j));
        }
        fr.write(")\", a.toString());\n" +
                 "    }\n" +
                 "}\n");
        fr.flush();
        fr.close();
    }

    public static void main(String... args) throws IOException {
        for (int i = 3; i <= 12; i++) {
            genTuple(i);
            if (i > 3) {
                genTupleTest(i);
            }
        }
        return;
    }
}