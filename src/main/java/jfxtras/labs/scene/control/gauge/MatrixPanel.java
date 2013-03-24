/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

/**
 * Created by
 * User: hansolo
 * Date: 09.01.12
 * Time: 18:02
 * Modified by Jose Pereda Llamas &lt;jperedadnr&gt;
 * On : 23-jun-2012, 11:47:23
 */
public class MatrixPanel extends Control {
    public interface IDD {
                public String getHexLetra();

                public int getDecLetra();
            }
    public static enum DD1 implements IDD {
        DD1_DOT_32("00 00 00 00 00", 32) /* SPACE */,
        DD1_DOT_33("00 00 FA 00 00", 33) /* ! */,
        DD1_DOT_34("A0 C0 00 A0 C0", 34) /* " */,
        DD1_DOT_35("28 FE 28 FE 28", 35) /* # */,
        DD1_DOT_36("24 54 FE 54 48", 36) /* $ */,
        DD1_DOT_37("C4 C8 10 26 46", 37) /* % */,
        DD1_DOT_38("6C 92 AA 44 0A", 38) /* & */,
        DD1_DOT_40("00 7C 82 00 00", 40) /* ( */,
        DD1_DOT_41("00 00 82 7C 00", 41) /* ) */,
        DD1_DOT_42("54 38 7C 38 54", 42) /* * */,
        DD1_DOT_43("10 10 7C 10 10", 43) /* + */,
        DD1_DOT_44("00 0A 0C 00 00", 44) /* , */,
        DD1_DOT_45("10 10 10 10 10", 45) /* - */,
        DD1_DOT_46("00 06 06 00 00", 46) /* . */,
        DD1_DOT_47("04 08 10 20 40", 47) /*  / */,
        DD1_DOT_48("7C 8A 92 A2 7C", 48) /* 0 */,
        DD1_DOT_49("00 42 FE 02 00", 49) /* 1 */,
        DD1_DOT_50("46 8A 92 92 62", 50) /* 2 */,
        DD1_DOT_51("44 82 92 92 6C", 51) /* 3 */,
        DD1_DOT_52("18 28 4A FE 0A", 52) /* 4 */,
        DD1_DOT_53("E4 A2 A2 A2 9C", 53) /* 5 */,
        DD1_DOT_54("7C 92 92 92 4C", 54) /* 6 */,
        DD1_DOT_55("80 80 9E A0 C0", 55) /* 7 */,
        DD1_DOT_56("6C 92 92 92 6C", 56) /* 8 */,
        DD1_DOT_57("60 92 92 92 7C", 57) /* 9 */,
        DD1_DOT_58("00 66 66 00 00", 58) /* : */,
        DD1_DOT_59("00 6A 6C 00 00", 59) /* ; */,
        DD1_DOT_60("10 28 44 82 00", 60) /* < */,
        DD1_DOT_61("28 28 28 28 28", 61) /*  = */,
        DD1_DOT_62("00 82 44 28 10", 62) /* > */,
        DD1_DOT_63("40 80 8A 90 60", 63) /* ? */,
        DD1_DOT_64("7C 82 BA AA 78", 64) /* @ */,
        DD1_DOT_65("7E 88 88 88 7E", 65) /* A */,
        DD1_DOT_66("FE 92 92 92 6C", 66) /* B */,
        DD1_DOT_67("7C 82 82 82 44", 67) /* C */,
        DD1_DOT_68("FE 82 82 82 7C", 68) /* D */,
        DD1_DOT_69("FE 92 92 82 82", 69) /* E */,
        DD1_DOT_70("FE 90 90 80 80", 70) /* F */,
        DD1_DOT_71("7C 82 92 92 5C", 71) /* G */,
        DD1_DOT_72("FE 10 10 10 FE", 72) /* H */,
        DD1_DOT_73("00 82 FE 82 00", 73) /* I */,
        DD1_DOT_74("04 02 02 02 FC", 74) /* J */,
        DD1_DOT_75("FE 10 28 44 82", 75) /* K */,
        DD1_DOT_76("FE 02 02 02 02", 76) /* L */,
        DD1_DOT_77("FE 40 20 40 FE", 77) /* M */,
        DD1_DOT_78("FE 40 20 10 FE", 78) /* N */,
        DD1_DOT_79("7C 82 82 82 7C", 79) /* O */,
        DD1_DOT_80("FE 90 90 90 60", 80) /* P */,
        DD1_DOT_81("7C 82 82 84 7A", 81) /* Q */,
        DD1_DOT_82("FE 90 98 94 62", 82) /* R */,
        DD1_DOT_83("64 92 92 92 4C", 83) /* S */,
        DD1_DOT_84("80 80 FE 80 80", 84) /* T */,
        DD1_DOT_85("FC 02 02 02 FC", 85) /* U */,
        DD1_DOT_86("E0 18 06 18 E0", 86) /* V */,
        DD1_DOT_87("FC 02 1C 02 FC", 87) /* W */,
        DD1_DOT_88("C6 28 10 28 C6", 88) /* X */,
        DD1_DOT_89("E0 10 0E 10 E0", 89) /* Y */,
        DD1_DOT_90("86 8A 92 A2 C2", 90) /* Z */,
        DD1_DOT_91("00 FE 82 00 00", 91) /* [ */,
        DD1_DOT_92("40 20 10 08 04", 92) /* \ */,
        DD1_DOT_93("00 00 82 FE 00", 93) /* ] */,
        DD1_DOT_95("02 02 02 02 02", 95) /* _ */,
        DD1_DOT_97("04 2A 2A 2A 1E", 97) /* a */,
        DD1_DOT_98("FC 12 22 22 1C", 98) /* b */,
        DD1_DOT_99("1C 22 22 22 22", 99) /* c */,
        DD1_DOT_100("1C 22 22 12 FE", 100) /* d */,
        DD1_DOT_101("1C 2A 2A 2A 18", 101) /* e */,
        DD1_DOT_102("08 7E 88 80 40", 102) /* f */,
        DD1_DOT_103("10 2A 2A 2A 1E", 103) /* g */,
        DD1_DOT_104("FE 10 20 20 1E", 104) /* h */,
        DD1_DOT_105("00 22 BE 02 00", 105) /* i */,
        DD1_DOT_106("04 02 22 BC 00", 106) /* j */,
        DD1_DOT_107("FE 08 14 22 00", 107) /* k */,
        DD1_DOT_108("00 82 FE 02 00", 108) /* l */,
        DD1_DOT_109("3E 20 10 20 1E", 109) /* m */,
        DD1_DOT_110("3E 10 20 20 1E", 110) /* n */,
        DD1_DOT_111("1C 22 22 22 1C", 111) /* o */,
        DD1_DOT_112("3E 18 28 28 10", 112) /* p */,
        DD1_DOT_113("10 28 28 18 3E", 113) /* q */,
        DD1_DOT_114("3E 10 20 20 10", 114) /* r */,
        DD1_DOT_115("12 2A 2A 2A 04", 115) /* s */,
        DD1_DOT_116("20 FC 22 02 04", 116) /* t */,
        DD1_DOT_117("3C 02 02 04 3E", 117) /* u */,
        DD1_DOT_118("38 04 02 04 38", 118) /* v */,
        DD1_DOT_119("3C 02 0C 02 3C", 119) /* w */,
        DD1_DOT_120("22 14 08 14 22", 120) /* x */,
        DD1_DOT_121("30 0A 0A 0A 3C", 121) /* y */,
        DD1_DOT_122("22 26 2A 32 22", 122) /* z */,
        DD1_DOT_123("10 6C 82 00 00", 123) /* { */,
        DD1_DOT_125("00 00 82 6C 10", 125) /* } */,
        DD1_DOT_161("00 00 BE 00 00", 161) /* &iexcl; */,
        DD1_DOT_186("40 A0 40 00 00", 186) /* &ordm; */,
        DD1_DOT_191("0C 12 A2 02 04", 191) /* &iquest; */,
        DD1_DOT_209("3E 90 88 84 3E", 209) /* &Ntilde; */,
        DD1_DOT_241("3E 90 A0 A0 1E", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD1(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }

    }
    public static enum DD2 implements IDD {
        DD2_DOT_32("00 00 00 00 00 00 00", 32) /* SPACE */,
        DD2_DOT_33("00 00 FA FA 00 00 00", 33) /* ! */,
        DD2_DOT_34("A0 E0 C0 00 A0 E0 C0", 34) /* " */,
        DD2_DOT_35("28 FE FE 28 FE FE 28", 35) /* # */,
        DD2_DOT_36("24 54 FE FE 54 5C 48", 36) /* $ */,
        DD2_DOT_37("C6 CC 18 30 60 C6 86", 37) /* % */,
        DD2_DOT_38("4C DE B2 F2 4A 04 0A", 38) /* & */,
        DD2_DOT_40("00 7C FE 82 00 00 00", 40) /* ( */,
        DD2_DOT_41("00 00 82 FE 7C 00 00", 41) /* ) */,
        DD2_DOT_42("54 38 7C 38 54 00 00", 42) /* * */,
        DD2_DOT_43("10 10 7C 7C 10 10 00", 43) /* + */,
        DD2_DOT_44("00 0A 0E 0C 00 00 00", 44) /* , */,
        DD2_DOT_45("10 10 10 10 10 10 10", 45) /* - */,
        DD2_DOT_46("00 06 06 06 00 00 00", 46) /* . */,
        DD2_DOT_47("06 0C 18 30 60 C0 80", 47) /*  / */,
        DD2_DOT_48("7C FE 8A 92 A2 FE 7C", 48) /* 0 */,
        DD2_DOT_49("00 42 FE FE 02 00 00", 49) /* 1 */,
        DD2_DOT_50("46 C6 8A 8A 92 F2 62", 50) /* 2 */,
        DD2_DOT_51("44 C6 82 92 92 FE 6C", 51) /* 3 */,
        DD2_DOT_52("18 38 68 CA FE FE 0A", 52) /* 4 */,
        DD2_DOT_53("E4 E6 A2 A2 A2 BE 9C", 53) /* 5 */,
        DD2_DOT_54("7C FE 92 92 92 1E 0C", 54) /* 6 */,
        DD2_DOT_55("80 80 8E 9E B0 E0 C0", 55) /* 7 */,
        DD2_DOT_56("6C FE 92 92 92 FE 6C", 56) /* 8 */,
        DD2_DOT_57("60 F2 92 92 92 FE 7C", 57) /* 9 */,
        DD2_DOT_58("00 66 66 66 00 00 00", 58) /* : */,
        DD2_DOT_59("00 6A 6C 6C 00 00 00", 59) /* ; */,
        DD2_DOT_60("10 38 6C C6 82 00 00", 60) /* < */,
        DD2_DOT_61("00 28 28 28 28 28 00", 61) /*  = */,
        DD2_DOT_62("00 00 82 C6 6C 38 10", 62) /* > */,
        DD2_DOT_63("40 C0 8A 9A 90 F0 60", 63) /* ? */,
        DD2_DOT_64("7C FE 82 BA AA FA 78", 64) /* @ */,
        DD2_DOT_65("7E FE 88 88 88 FE 7E", 65) /* A */,
        DD2_DOT_66("FE FE 92 92 92 EE 6C", 66) /* B */,
        DD2_DOT_67("7C FE 82 82 82 46 44", 67) /* C */,
        DD2_DOT_68("FE FE 82 82 82 FE 7C", 68) /* D */,
        DD2_DOT_69("FE FE 92 92 92 82 82", 69) /* E */,
        DD2_DOT_70("FE FE 90 90 90 80 80", 70) /* F */,
        DD2_DOT_71("7C FE 82 92 92 DE 5C", 71) /* G */,
        DD2_DOT_72("FE FE 10 10 10 FE FE", 72) /* H */,
        DD2_DOT_73("00 82 FE FE 82 00 00", 73) /* I */,
        DD2_DOT_74("04 06 02 02 02 FE FC", 74) /* J */,
        DD2_DOT_75("FE FE 10 38 6C C6 82", 75) /* K */,
        DD2_DOT_76("FE FE 02 02 02 02 02", 76) /* L */,
        DD2_DOT_77("FE FE 40 20 40 FE FE", 77) /* M */,
        DD2_DOT_78("FE FE 40 20 10 FE FE", 78) /* N */,
        DD2_DOT_79("7C FE 82 82 82 FE 7C", 79) /* O */,
        DD2_DOT_80("FE FE 90 90 90 F0 60", 80) /* P */,
        DD2_DOT_81("7C FE 82 9A 8C FE 7A", 81) /* Q */,
        DD2_DOT_82("FE FE 90 98 9C F6 62", 82) /* R */,
        DD2_DOT_83("64 F6 92 92 92 DE 4C", 83) /* S */,
        DD2_DOT_84("80 80 FE FE 80 80 00", 84) /* T */,
        DD2_DOT_85("FC FE 02 02 02 FE FC", 85) /* U */,
        DD2_DOT_86("C0 F0 38 06 38 F0 C0", 86) /* V */,
        DD2_DOT_87("FC FE 02 1C 02 FE FC", 87) /* W */,
        DD2_DOT_88("C6 C6 6C 10 6C C6 C6", 88) /* X */,
        DD2_DOT_89("E0 E0 30 0E 30 E0 E0", 89) /* Y */,
        DD2_DOT_90("86 8E 9A B2 E2 C2 82", 90) /* Z */,
        DD2_DOT_91("00 FE 82 82 00 00 00", 91) /* [ */,
        DD2_DOT_92("80 C0 60 30 18 0C 06", 92) /* \ */,
        DD2_DOT_93("00 00 82 82 FE 00 00", 93) /* ] */,
        DD2_DOT_95("00 02 02 02 02 02 00", 95) /* _ */,
        DD2_DOT_97("04 2A 2A 2A 2A 3E 1E", 97) /* a */,
        DD2_DOT_98("FE FE 12 22 22 3E 1E", 98) /* b */,
        DD2_DOT_99("1C 3E 22 22 22 22 22", 99) /* c */,
        DD2_DOT_100("1C 3E 22 22 12 FE FE", 100) /* d */,
        DD2_DOT_101("1C 3E 2A 2A 2A 3A 18", 101) /* e */,
        DD2_DOT_102("08 7E FE 88 C0 40 00", 102) /* f */,
        DD2_DOT_103("10 3A 2A 2A 2A 3E 1E", 103) /* g */,
        DD2_DOT_104("FE FE 10 20 20 3E 1E", 104) /* h */,
        DD2_DOT_105("00 22 BE BE 02 00 00", 105) /* i */,
        DD2_DOT_106("04 06 02 22 BE BC 00", 106) /* j */,
        DD2_DOT_107("FE FE 08 14 14 22 22", 107) /* k */,
        DD2_DOT_108("00 82 FE FE 02 00 00", 108) /* l */,
        DD2_DOT_109("3E 3E 20 18 20 3E 1E", 109) /* m */,
        DD2_DOT_110("3E 3E 10 20 20 3E 1E", 110) /* n */,
        DD2_DOT_111("1C 3E 22 22 22 3E 1C", 111) /* o */,
        DD2_DOT_112("3E 3E 18 28 28 38 10", 112) /* p */,
        DD2_DOT_113("10 38 28 28 18 3E 3E", 113) /* q */,
        DD2_DOT_114("3E 3E 10 20 20 30 10", 114) /* r */,
        DD2_DOT_115("12 3A 2A 2A 2A 2E 24", 115) /* s */,
        DD2_DOT_116("20 FC FE 22 02 06 04", 116) /* t */,
        DD2_DOT_117("3C 3E 02 02 04 3E 3E", 117) /* u */,
        DD2_DOT_118("38 38 04 02 04 38 38", 118) /* v */,
        DD2_DOT_119("3C 3E 02 04 02 3E 3C", 119) /* w */,
        DD2_DOT_120("22 36 14 08 14 36 22", 120) /* x */,
        DD2_DOT_121("30 3A 0A 0A 0A 3E 3C", 121) /* y */,
        DD2_DOT_122("22 26 26 2A 32 32 22", 122) /* z */,
        DD2_DOT_123("10 7C EE 82 00 00 00", 123) /* { */,
        DD2_DOT_125("00 00 82 EE 7C 10 00", 125) /* } */,
        DD2_DOT_161("00 00 BE BE 00 00 00", 161) /* &iexcl; */,
        DD2_DOT_186("40 E0 A0 E0 40 00 00", 186) /* &ordm; */,
        DD2_DOT_191("0C 1E B2 A2 02 06 04", 191) /* &iquest; */,
        DD2_DOT_209("3E BE 90 88 84 BE 3E", 209) /* &Ntilde; */,
        DD2_DOT_241("3E BE 90 A0 A0 BE 1E", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD2(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    public static enum DD4 implements IDD {
        DD4_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD4_DOT_33("00 00 00 00 FE 80 FE 80 00 00 00 00 00 00", 33) /* ! */,
        DD4_DOT_34("A0 00 E0 00 C0 00 00 00 A0 00 E0 00 C0 00", 34) /* " */,
        DD4_DOT_35("36 00 FF 80 FF 80 36 00 FF 80 FF 80 34 00", 35) /* # */,
        DD4_DOT_36("31 00 49 00 FF 80 FF 80 49 00 4F 00 46 00", 36) /* $ */,
        DD4_DOT_37("C1 80 C3 00 06 00 0C 00 18 00 31 80 61 80", 37) /* % */,
        DD4_DOT_38("67 00 EF 80 98 80 F8 80 65 80 02 00 05 00", 38) /* & */,
        DD4_DOT_40("00 00 7F 00 FF 80 80 80 00 00 00 00 00 00", 40) /* ( */,
        DD4_DOT_41("00 00 00 00 80 80 FF 80 7F 00 00 00 00 00", 41) /* ) */,
        DD4_DOT_42("2A 00 1C 00 3E 00 1C 00 2A 00 00 00 00 00", 42) /* * */,
        DD4_DOT_43("08 00 08 00 3E 00 3E 00 08 00 08 00 00 00", 43) /* + */,
        DD4_DOT_44("00 00 05 00 07 00 06 00 00 00 00 00 00 00", 44) /* , */,
        DD4_DOT_45("08 00 08 00 08 00 08 00 08 00 08 00 08 00", 45) /* - */,
        DD4_DOT_46("00 00 03 00 03 00 03 00 00 00 00 00 00 00", 46) /* . */,
        DD4_DOT_47("03 00 06 00 0C 00 18 00 30 00 60 00 C0 00", 47) /*  / */,
        DD4_DOT_48("7F 00 FF 80 84 80 88 80 90 80 FF 80 7F 00", 48) /* 0 */,
        DD4_DOT_49("00 00 40 80 FF 80 FF 80 00 80 00 00 00 00", 49) /* 1 */,
        DD4_DOT_50("41 80 C3 80 86 80 8C 80 98 80 F0 80 60 80", 50) /* 2 */,
        DD4_DOT_51("41 00 C1 80 80 80 88 80 88 80 FF 80 77 00", 51) /* 3 */,
        DD4_DOT_52("0C 00 1C 00 34 00 64 80 FF 80 FF 80 04 80", 52) /* 4 */,
        DD4_DOT_53("F1 00 F1 80 90 80 90 80 90 80 9F 80 8F 00", 53) /* 5 */,
        DD4_DOT_54("7F 00 FF 80 90 80 90 80 90 80 1F 80 0F 00", 54) /* 6 */,
        DD4_DOT_55("80 00 80 00 8F 80 9F 80 B0 00 E0 00 C0 00", 55) /* 7 */,
        DD4_DOT_56("77 00 FF 80 98 80 98 80 98 80 FF 80 F7 00", 56) /* 8 */,
        DD4_DOT_57("70 00 F8 80 88 80 88 80 88 80 FF 80 7F 00", 57) /* 9 */,
        DD4_DOT_58("00 00 33 00 33 00 33 00 00 00 00 00 00 00", 58) /* : */,
        DD4_DOT_59("00 00 35 00 37 00 36 00 00 00 00 00 00 00", 59) /* ; */,
        DD4_DOT_60("08 00 1C 00 36 00 63 00 41 00 00 00 00 00", 60) /* < */,
        DD4_DOT_61("00 00 14 00 14 00 14 00 14 00 14 00 00 00", 61) /*  = */,
        DD4_DOT_62("00 00 00 00 41 00 63 00 36 00 1C 00 08 00", 62) /* > */,
        DD4_DOT_63("40 00 C0 00 86 80 8E 80 88 00 F8 00 70 00", 63) /* ? */,
        DD4_DOT_64("7F 00 FF 80 80 80 9C 80 94 80 FC 80 7C 00", 64) /* @ */,
        DD4_DOT_65("7F 80 FF 80 84 00 84 00 84 00 FF 80 7F 80", 65) /* A */,
        DD4_DOT_66("FF 80 FF 80 88 80 88 80 88 80 FF 80 77 00", 66) /* B */,
        DD4_DOT_67("7F 00 FF 80 80 80 80 80 80 80 C1 80 41 00", 67) /* C */,
        DD4_DOT_68("FF 80 FF 80 80 80 80 80 80 80 FF 80 7F 00", 68) /* D */,
        DD4_DOT_69("FF 80 FF 80 88 80 88 80 88 80 80 80 80 80", 69) /* E */,
        DD4_DOT_70("FF 80 FF 80 88 00 88 00 88 00 80 00 80 00", 70) /* F */,
        DD4_DOT_71("7F 00 FF 80 80 80 88 80 88 80 CF 80 4F 00", 71) /* G */,
        DD4_DOT_72("FF 80 FF 80 08 00 08 00 08 00 FF 80 FF 80", 72) /* H */,
        DD4_DOT_73("00 00 80 80 FF 80 FF 80 80 80 00 00 00 00", 73) /* I */,
        DD4_DOT_74("01 00 01 80 00 80 00 80 00 80 FF 80 FF 00", 74) /* J */,
        DD4_DOT_75("FF 80 FF 80 1C 00 36 00 63 00 C1 80 80 80", 75) /* K */,
        DD4_DOT_76("FF 80 FF 80 00 80 00 80 00 80 00 80 00 80", 76) /* L */,
        DD4_DOT_77("FF 80 FF 80 60 00 30 00 60 00 FF 80 FF 80", 77) /* M */,
        DD4_DOT_78("FF 80 FF 80 60 00 30 00 18 00 FF 80 FF 80", 78) /* N */,
        DD4_DOT_79("7F 00 FF 80 80 80 80 80 80 80 FF 80 7F 00", 79) /* O */,
        DD4_DOT_80("7F 80 FF 80 88 00 88 00 88 00 F8 00 70 00", 80) /* P */,
        DD4_DOT_81("7F 00 FF 80 80 80 86 80 83 00 FF 80 7E 80", 81) /* Q */,
        DD4_DOT_82("7F 80 FF 80 8C 00 8E 00 8B 00 F9 80 70 80", 82) /* R */,
        DD4_DOT_83("71 00 F9 80 88 80 88 80 88 80 CF 80 47 00", 83) /* S */,
        DD4_DOT_84("80 00 80 00 FF 80 FF 80 80 00 80 00 00 00", 84) /* T */,
        DD4_DOT_85("FF 00 FF 80 00 80 00 80 00 80 FF 80 FF 00", 85) /* U */,
        DD4_DOT_86("E0 00 F8 00 1E 00 03 80 1E 00 F8 00 E0 00", 86) /* V */,
        DD4_DOT_87("FF 00 FF 80 00 80 07 00 00 80 FF 80 FF 00", 87) /* W */,
        DD4_DOT_88("E3 80 E3 80 36 00 08 00 36 00 E3 80 E3 80", 88) /* X */,
        DD4_DOT_89("C0 00 E0 00 38 00 0F 80 38 00 E0 00 C0 00", 89) /* Y */,
        DD4_DOT_90("83 80 86 80 8C 80 98 80 B0 80 E0 80 C0 80", 90) /* Z */,
        DD4_DOT_91("00 00 FF 80 80 80 80 80 00 00 00 00 00 00", 91) /* [ */,
        DD4_DOT_92("C0 00 60 00 30 00 18 00 0C 00 06 00 03 00", 92) /* \ */,
        DD4_DOT_93("00 00 00 00 80 80 80 80 FF 80 00 00 00 00", 93) /* ] */,
        DD4_DOT_95("00 00 00 80 00 80 00 80 00 80 00 80 00 00", 95) /* _ */,
        DD4_DOT_97("03 00 27 80 24 80 24 80 24 80 3F 80 1F 80", 97) /* a */,
        DD4_DOT_98("FF 80 FF 80 10 80 20 80 20 80 3F 80 1F 80", 98) /* b */,
        DD4_DOT_99("1F 00 3F 80 20 80 20 80 20 80 20 80 20 80", 99) /* c */,
        DD4_DOT_100("1F 00 3F 80 20 80 20 80 10 80 FF 80 FF 80", 100) /* d */,
        DD4_DOT_101("1F 00 3F 80 24 80 24 80 24 80 3C 80 1C 00", 101) /* e */,
        DD4_DOT_102("04 00 7F 80 FF 80 84 00 C0 00 40 00 00 00", 102) /* f */,
        DD4_DOT_103("18 00 3C 80 24 80 24 80 24 80 3F 80 1F 80", 103) /* g */,
        DD4_DOT_104("FF 80 FF 80 10 00 20 00 20 00 3F 80 1F 80", 104) /* h */,
        DD4_DOT_105("00 00 20 80 BF 80 BF 80 00 80 00 00 00 00", 105) /* i */,
        DD4_DOT_106("01 00 01 80 00 80 00 80 20 80 BF 80 BF 00", 106) /* j */,
        DD4_DOT_107("FF 80 FF 80 04 00 0E 00 1B 00 31 80 20 80", 107) /* k */,
        DD4_DOT_108("00 00 80 80 FF 80 FF 80 00 80 00 00 00 00", 108) /* l */,
        DD4_DOT_109("3F 80 3F 80 20 00 18 00 20 00 3F 80 1F 80", 109) /* m */,
        DD4_DOT_110("3F 80 3F 80 10 00 20 00 20 00 3F 80 1F 80", 110) /* n */,
        DD4_DOT_111("1F 00 3F 80 20 80 20 80 20 80 3F 80 1F 00", 111) /* o */,
        DD4_DOT_112("3F 80 3F 80 14 00 24 00 24 00 3C 00 18 00", 112) /* p */,
        DD4_DOT_113("18 00 3C 00 24 00 24 00 14 00 3F 80 3F 80", 113) /* q */,
        DD4_DOT_114("3F 80 3F 80 10 00 20 00 20 00 30 00 10 00", 114) /* r */,
        DD4_DOT_115("19 00 3C 80 24 80 24 80 24 80 27 80 13 00", 115) /* s */,
        DD4_DOT_116("10 00 FF 00 FF 80 10 80 00 80 01 00 01 00", 116) /* t */,
        DD4_DOT_117("3F 80 3F 80 00 80 00 80 01 00 3F 80 3F 80", 117) /* u */,
        DD4_DOT_118("30 00 3C 00 07 00 00 80 07 00 3C 00 30 00", 118) /* v */,
        DD4_DOT_119("3F 00 3F 80 00 80 03 00 00 80 3F 80 3F 00", 119) /* w */,
        DD4_DOT_120("31 80 3B 80 0A 00 04 00 0A 00 3B 80 31 80", 120) /* x */,
        DD4_DOT_121("38 00 3C 80 04 80 04 80 06 80 3F 80 3F 00", 121) /* y */,
        DD4_DOT_122("21 80 23 80 26 80 2C 80 38 80 30 80 20 80", 122) /* z */,
        DD4_DOT_123("08 00 7F 00 F7 80 80 80 00 00 00 00 00 00", 123) /* { */,
        DD4_DOT_125("00 00 00 00 80 80 F7 80 7F 00 08 00 00 00", 125) /* } */,
        DD4_DOT_161("00 00 00 00 BF 80 BF 80 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD4_DOT_186("20 00 50 00 50 00 20 00 00 00 00 00 00 00", 186) /* &ordm; */,
        DD4_DOT_191("07 00 0F 80 B8 80 B0 80 00 80 01 80 01 00", 191) /* &iquest; */,
        DD4_DOT_209("3F 80 BF 80 98 00 8C 00 86 00 BF 80 3F 80", 209) /* &Ntilde; */,
        DD4_DOT_241("3F 80 BF 80 90 00 A0 00 A0 00 BF 80 1F 80", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD4(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    public static enum DD6 implements IDD {
        DD6_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD6_DOT_33("00 00 00 00 00 00 FF CC FF CC 00 00 00 00 00 00", 33) /* ! */,
        DD6_DOT_34("CC 00 F8 00 F0 00 00 00 CC 00 F8 00 F0 00 00 00", 34) /* " */,
        DD6_DOT_35("0C C0 FF FC FF FC 0C C0 0C C0 FF FC FF FC 0C C0", 35) /* # */,
        DD6_DOT_36("1E 30 3F 30 33 30 FF FC FF FC 33 30 33 F0 31 E0", 36) /* $ */,
        DD6_DOT_37("60 18 F0 38 F0 F0 63 C0 0F 18 3C 3C 70 3C 40 18", 37) /* % */,
        DD6_DOT_38("3C F0 7F F8 E7 9C CF CC FC FC 78 78 00 FC 00 CC", 38) /* & */,
        DD6_DOT_40("00 00 1F E0 7F F8 E0 1C C0 0C 00 00 00 00 00 00", 40) /* ( */,
        DD6_DOT_41("00 00 00 00 00 00 C0 0C E0 1C 7F F8 1F E0 00 00", 41) /* ) */,
        DD6_DOT_42("1B 60 1F E0 0F C0 3F F0 3F F0 0F C0 1F E0 1B 60", 42) /* * */,
        DD6_DOT_43("03 00 03 00 03 00 3F F0 3F F0 03 00 03 00 03 00", 43) /* + */,
        DD6_DOT_44("00 00 00 00 00 CC 00 F8 00 F0 00 00 00 00 00 00", 44) /* , */,
        DD6_DOT_45("03 00 03 00 03 00 03 00 03 00 03 00 03 00 03 00", 45) /* - */,
        DD6_DOT_46("00 00 00 00 00 18 00 3C 00 3C 00 18 00 00 00 00", 46) /* . */,
        DD6_DOT_47("00 70 00 E0 01 C0 03 80 07 00 0E 00 1C 00 38 00", 47) /*  / */,
        DD6_DOT_48("1F E0 7F F8 60 98 C1 0C C2 0C 64 18 7F F8 1F E0", 48) /* 0 */,
        DD6_DOT_49("00 00 30 0C 70 0C FF FC FF FC 00 0C 00 0C 00 00", 49) /* 1 */,
        DD6_DOT_50("30 3C 70 7C E0 EC C1 CC C3 8C E7 0C 7E 0C 3C 0C", 50) /* 2 */,
        DD6_DOT_51("30 30 70 38 E0 1C C3 0C C3 0C E7 9C 7C F8 3C F0", 51) /* 3 */,
        DD6_DOT_52("07 C0 0E C0 1C C0 38 CC 70 CC FF FC FF FC 00 CC", 52) /* 4 */,
        DD6_DOT_53("FE 30 FE 38 C6 1C C6 0C C6 0C C7 1C C3 F8 C1 F0", 53) /* 5 */,
        DD6_DOT_54("3F F0 7F F8 E3 1C C3 0C C3 0C C3 9C 01 F8 00 F0", 54) /* 6 */,
        DD6_DOT_55("C0 00 C0 00 C1 FC C7 FC CF 00 DC 00 F8 00 F0 00", 55) /* 7 */,
        DD6_DOT_56("3C F0 7F F8 E7 9C C3 0C C3 0C E7 9C 7F F8 3C F0", 56) /* 8 */,
        DD6_DOT_57("3C 00 7E 00 E7 0C C3 0C C3 0C E3 1C 7F F8 3F F0", 57) /* 9 */,
        DD6_DOT_58("00 00 00 00 0C 18 1E 3C 1E 3C 0C 18 00 00 00 00", 58) /* : */,
        DD6_DOT_59("00 00 00 00 18 CC 3C F8 3C F0 18 00 00 00 00 00", 59) /* ; */,
        DD6_DOT_60("03 00 07 80 0F C0 1C E0 38 70 70 38 E0 1C C0 0C", 60) /* < */,
        DD6_DOT_61("0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 0C C0", 61) /*  = */,
        DD6_DOT_62("C0 0C E0 1C 70 38 38 70 1C E0 0F C0 07 80 03 00", 62) /* > */,
        DD6_DOT_63("70 00 E0 00 C1 CC C3 CC C7 00 EE 00 7C 00 38 00", 63) /* ? */,
        DD6_DOT_64("3F F0 7F F8 E0 1C C7 8C CF CC EC CC 7F C0 3F 80", 64) /* @ */,
        DD6_DOT_65("1F FC 7F FC 61 80 C1 80 C1 80 61 80 7F FC 1F FC", 65) /* A */,
        DD6_DOT_66("FF FC FF FC C3 0C C3 0C C3 0C 67 98 7C F8 18 60", 66) /* B */,
        DD6_DOT_67("1F E0 7F F8 60 18 C0 0C C0 0C 60 18 70 38 10 20", 67) /* C */,
        DD6_DOT_68("FF FC FF FC C0 0C C0 0C C0 0C 60 18 7F F8 1F E0", 68) /* D */,
        DD6_DOT_69("FF FC FF FC C3 0C C3 0C C3 0C C0 0C C0 0C C0 0C", 69) /* E */,
        DD6_DOT_70("FF FC FF FC C3 00 C3 00 C3 00 C0 00 C0 00 C0 00", 70) /* F */,
        DD6_DOT_71("3F F0 7F F8 E0 1C C0 0C C3 0C E3 1C 73 F8 33 F0", 71) /* G */,
        DD6_DOT_72("FF FC FF FC 03 00 03 00 03 00 03 00 FF FC FF FC", 72) /* H */,
        DD6_DOT_73("00 00 C0 0C C0 0C FF FC FF FC C0 0C C0 0C 00 00", 73) /* I */,
        DD6_DOT_74("00 30 00 38 00 1C 00 0C 00 0C 00 1C FF F8 FF F0", 74) /* J */,
        DD6_DOT_75("FF FC FF FC 07 80 0F C0 1C E0 38 70 70 38 E0 1C", 75) /* K */,
        DD6_DOT_76("FF FC FF FC 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C", 76) /* L */,
        DD6_DOT_77("FF FC FF FC 38 00 1E 00 1E 00 38 00 FF FC FF FC", 77) /* M */,
        DD6_DOT_78("FF FC FF FC 38 00 1C 00 0E 00 07 00 FF FC FF FC", 78) /* N */,
        DD6_DOT_79("1F E0 7F F8 60 18 C0 0C C0 0C 60 18 7F F8 1F E0", 79) /* O */,
        DD6_DOT_80("FF FC FF FC C3 00 C3 00 C3 00 66 00 7E 00 18 00", 80) /* P */,
        DD6_DOT_81("1F E0 7F F8 60 18 C0 0C C0 28 60 18 7F FC 1F CC", 81) /* Q */,
        DD6_DOT_82("FF FC FF FC C3 80 C3 C0 C3 60 66 30 7E 1C 18 0C", 82) /* R */,
        DD6_DOT_83("18 30 7E 38 66 18 C3 0C C3 0C 61 98 71 F8 10 60", 83) /* S */,
        DD6_DOT_84("C0 00 C0 00 C0 00 FF FC FF FC C0 00 C0 00 C0 00", 84) /* T */,
        DD6_DOT_85("FF E0 FF F8 00 18 00 0C 00 0C 00 18 FF F8 FF E0", 85) /* U */,
        DD6_DOT_86("FC 00 FF 80 07 E0 00 F8 00 F8 07 E0 FF 80 FC 00", 86) /* V */,
        DD6_DOT_87("FF E0 FF F8 00 1C 03 F8 03 F8 00 1C FF F8 FF E0", 87) /* W */,
        DD6_DOT_88("F0 3C F8 7C 1C E0 0F C0 0F C0 1C E0 F8 7C F0 3C", 88) /* X */,
        DD6_DOT_89("FC 00 FE 00 07 80 01 FC 01 FC 07 80 FE 00 FC 00", 89) /* Y */,
        DD6_DOT_90("C0 3C C0 7C C1 EC C3 8C C7 0C DE 0C F8 0C F0 0C", 90) /* Z */,
        DD6_DOT_91("00 00 FF FC FF FC C0 0C C0 0C 00 00 00 00 00 00", 91) /* [ */,
        DD6_DOT_92("38 00 1C 00 0E 00 07 00 03 80 01 C0 00 E0 00 70", 92) /* \ */,
        DD6_DOT_93("00 00 00 00 00 00 C0 0C C0 0C FF FC FF FC 00 00", 93) /* ] */,
        DD6_DOT_95("00 00 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C 00 00", 95) /* _ */,
        DD6_DOT_97("00 30 00 78 0C FC 0C CC 0C CC 0E CC 07 FC 03 FC", 97) /* a */,
        DD6_DOT_98("FF FC FF FC 03 0C 06 0C 0E 0C 0E 1C 07 F8 03 F0", 98) /* b */,
        DD6_DOT_99("03 F0 07 F8 0E 1C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C", 99) /* c */,
        DD6_DOT_100("03 F0 07 F8 0E 1C 0E 0C 07 0C 03 0C FF FC FF FC", 100) /* d */,
        DD6_DOT_101("03 F0 07 F8 0E DC 0C CC 0C CC 0E CC 07 CC 03 C0", 101) /* e */,
        DD6_DOT_102("00 C0 3F FC 7F FC E0 C0 C0 00 E0 00 70 00 30 00", 102) /* f */,
        DD6_DOT_103("03 00 07 80 0F CC 0C CC 0C CC 0E CC 07 FC 03 FC", 103) /* g */,
        DD6_DOT_104("FF FC FF FC 03 00 07 00 0E 00 0E 00 07 FC 03 FC", 104) /* h */,
        DD6_DOT_105("00 00 00 0C 0C 0C CF FC CF FC 00 0C 00 0C 00 00", 105) /* i */,
        DD6_DOT_106("00 30 00 38 00 1C 00 0C 00 0C 0C 1C CF F8 CF F0", 106) /* j */,
        DD6_DOT_107("FF FC FF FC 00 C0 01 E0 03 F0 07 38 0E 1C 0C 0C", 107) /* k */,
        DD6_DOT_108("00 00 00 0C C0 0C FF FC FF FC 00 0C 00 0C 00 00", 108) /* l */,
        DD6_DOT_109("0F FC 0F FC 0E 00 07 C0 07 C0 0E 00 07 FC 03 FC", 109) /* m */,
        DD6_DOT_110("0F FC 0F FC 03 00 07 00 0E 00 0E 00 07 FC 03 FC", 110) /* n */,
        DD6_DOT_111("03 F0 07 F8 0E 1C 0C 0C 0C 0C 0E 1C 07 F8 03 F0", 111) /* o */,
        DD6_DOT_112("0F FC 0F FC 03 C0 06 C0 0C C0 0C C0 07 80 03 00", 112) /* p */,
        DD6_DOT_113("03 00 07 80 0C C0 0C C0 06 C0 03 C0 0F FC 0F FC", 113) /* q */,
        DD6_DOT_114("0F FC 0F FC 03 00 07 00 0E 00 0E 00 07 00 03 00", 114) /* r */,
        DD6_DOT_115("03 0C 07 8C 0F CC 0C CC 0C CC 0C FC 0C 78 0C 30", 115) /* s */,
        DD6_DOT_116("0C 00 FF F0 FF F8 0C 1C 0C 0C 00 1C 00 38 00 30", 116) /* t */,
        DD6_DOT_117("0F F0 0F F8 00 1C 00 1C 00 38 00 70 0F FC 0F FC", 117) /* u */,
        DD6_DOT_118("0F C0 0F E0 00 38 00 1C 00 1C 00 38 0F E0 0F C0", 118) /* v */,
        DD6_DOT_119("0F F0 0F F8 00 1C 00 F8 00 F8 00 1C 0F F8 0F F0", 119) /* w */,
        DD6_DOT_120("0C 0C 0E 1C 07 38 03 F0 03 F0 07 38 0E 1C 0C 0C", 120) /* x */,
        DD6_DOT_121("0F 00 0F 80 01 CC 00 CC 00 CC 00 DC 0F F8 0F F0", 121) /* y */,
        DD6_DOT_122("0C 1C 0C 3C 0C 7C 0C EC 0D CC 0F 8C 0F 0C 0E 0C", 122) /* z */,
        DD6_DOT_123("03 00 1F E0 7C F8 E0 1C C0 0C 00 00 00 00 00 00", 123) /* { */,
        DD6_DOT_125("00 00 00 00 00 00 C0 0C E0 1C 7C F8 1F E0 03 00", 125) /* } */,
        DD6_DOT_161("00 00 00 00 00 00 CF FC CF FC 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD6_DOT_186("30 00 78 00 CC 00 CC 00 78 00 30 00 00 00 00 00", 186) /* &ordm; */,
        DD6_DOT_191("00 F0 01 F8 03 9C CE 0C CC 0C 00 1C 00 3C 00 38", 191) /* &iquest; */,
        DD6_DOT_209("0F FC 0F FC C3 80 C1 C0 C0 E0 C0 70 0F FC 0F FC", 209) /* &Ntilde; */,
        DD6_DOT_241("0F FC CF FC C3 00 C7 00 CE 00 CE 00 C7 FC 03 FC", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD6(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    public static enum DD7 implements IDD {
        DD7_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD7_DOT_33("00 00 00 00 00 00 00 00 FF CC FF CC 00 00 00 00 00 00 00 00", 33) /* ! */,
        DD7_DOT_34("CC 00 CC 00 F8 00 F0 00 00 00 CC 00 CC 00 F8 00 F0 00 00 00", 34) /* " */,
        DD7_DOT_35("0C C0 0C C0 FF FC FF FC 0C C0 0C C0 FF FC FF FC 0C C0 0C C0", 35) /* # */,
        DD7_DOT_36("1E 30 3F 30 33 30 33 30 FF FC FF FC 33 30 33 30 33 F0 31 E0", 36) /* $ */,
        DD7_DOT_37("60 30 F0 70 F0 E0 61 C0 03 80 07 00 0E 18 1C 3C 38 3C 30 18", 37) /* % */,
        DD7_DOT_38("3C F0 7F F8 E7 9C C7 8C CF CC FC FC 78 78 00 78 00 FC 00 CC", 38) /* & */,
        DD7_DOT_40("00 00 00 00 1F E0 7F F8 E0 1C C0 0C 00 00 00 00 00 00 00 00", 40) /* ( */,
        DD7_DOT_41("00 00 00 00 00 00 C0 0C E0 1C 7F F8 1F E0 00 00 00 00 00 00", 41) /* ) */,
        DD7_DOT_42("03 00 1B 60 1F E0 0F C0 3F F0 3F F0 0F C0 1F E0 1B 60 03 00", 42) /* * */,
        DD7_DOT_43("03 00 03 00 03 00 03 00 3F F0 3F F0 03 00 03 00 03 00 03 00", 43) /* + */,
        DD7_DOT_44("00 00 00 00 00 CC 00 CC 00 F8 00 F0 00 00 00 00 00 00 00 00", 44) /* , */,
        DD7_DOT_45("03 00 03 00 03 00 03 00 03 00 03 00 03 00 03 00 03 00 00 00", 45) /* - */,
        DD7_DOT_46("00 00 00 00 00 18 00 3C 00 3C 00 18 00 00 00 00 00 00 00 00", 46) /* . */,
        DD7_DOT_47("00 30 00 70 00 E0 01 C0 03 80 07 00 0E 00 1C 00 38 00 30 00", 47) /*  / */,
        DD7_DOT_48("1F E0 7F F8 60 98 C1 0C C1 0C C2 0C C2 0C 64 18 7F F8 1F E0", 48) /* 0 */,
        DD7_DOT_49("00 00 00 00 30 0C 70 0C FF FC FF FC 00 0C 00 0C 00 00 00 00", 49) /* 1 */,
        DD7_DOT_50("30 3C 70 7C E0 EC C1 CC C3 8C C3 0C C3 0C E7 0C 7E 0C 3C 0C", 50) /* 2 */,
        DD7_DOT_51("30 30 70 38 E0 1C C0 0C C0 0C C3 0C C3 0C E7 9C 7C F8 3C F0", 51) /* 3 */,
        DD7_DOT_52("03 C0 07 C0 0E C0 1C C0 38 CC 70 CC FF FC FF FC 00 CC 00 CC", 52) /* 4 */,
        DD7_DOT_53("FE 30 FE 38 C6 1C C6 0C C6 0C C6 0C C6 0C C7 1C C3 F8 C1 F0", 53) /* 5 */,
        DD7_DOT_54("3F F0 7F F8 E3 1C C3 0C C3 0C C3 0C C3 0C C3 9C 01 F8 00 F0", 54) /* 6 */,
        DD7_DOT_55("C0 00 C0 00 C0 00 C0 00 C1 FC C7 FC CF 00 DC 00 F8 00 F0 00", 55) /* 7 */,
        DD7_DOT_56("3C F0 7F F8 E7 9C C3 0C C3 0C C3 0C C3 0C E7 9C 7F F8 3C F0", 56) /* 8 */,
        DD7_DOT_57("3C 00 7E 00 E7 0C C3 0C C3 0C C3 0C C3 0C E3 1C 7F F8 3F F0", 57) /* 9 */,
        DD7_DOT_58("00 00 00 00 00 00 0C 18 1E 3C 1E 3C 0C 18 00 00 00 00 00 00", 58) /* : */,
        DD7_DOT_59("00 00 00 00 00 CC 18 CC 3C F8 3C F0 18 00 00 00 00 00 00 00", 59) /* ; */,
        DD7_DOT_60("00 00 03 00 07 80 0F C0 1C E0 38 70 70 38 E0 1C C0 0C 00 00", 60) /* < */,
        DD7_DOT_61("0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 0C C0 00 00", 61) /*  = */,
        DD7_DOT_62("00 00 C0 0C E0 1C 70 38 38 70 1C E0 0F C0 07 80 03 00 00 00", 62) /* > */,
        DD7_DOT_63("30 00 70 00 E0 00 C0 00 C1 CC C3 CC C7 00 EE 00 7C 00 38 00", 63) /* ? */,
        DD7_DOT_64("3F F0 7F F8 E0 1C C0 0C C7 8C CF CC CC CC EC CC 7F C0 3F 80", 64) /* @ */,
        DD7_DOT_65("1F FC 7F FC 61 80 C1 80 C1 80 C1 80 C1 80 61 80 7F FC 1F FC", 65) /* A */,
        DD7_DOT_66("FF FC FF FC C3 0C C3 0C C3 0C C3 0C C3 0C 67 98 7C F8 18 60", 66) /* B */,
        DD7_DOT_67("1F E0 7F F8 60 18 C0 0C C0 0C C0 0C C0 0C 60 18 70 38 10 20", 67) /* C */,
        DD7_DOT_68("FF FC FF FC C0 0C C0 0C C0 0C C0 0C C0 0C 60 18 7F F8 1F E0", 68) /* D */,
        DD7_DOT_69("FF FC FF FC C3 0C C3 0C C3 0C C3 0C C3 0C C0 0C C0 0C C0 0C", 69) /* E */,
        DD7_DOT_70("FF FC FF FC C3 00 C3 00 C3 00 C3 00 C3 00 C0 00 C0 00 C0 00", 70) /* F */,
        DD7_DOT_71("3F F0 7F F8 E0 1C C0 0C C3 0C C3 0C C3 0C E3 1C 73 F8 33 F0", 71) /* G */,
        DD7_DOT_72("FF FC FF FC 03 00 03 00 03 00 03 00 03 00 03 00 FF FC FF FC", 72) /* H */,
        DD7_DOT_73("00 00 00 00 C0 0C C0 0C FF FC FF FC C0 0C C0 0C 00 00 00 00", 73) /* I */,
        DD7_DOT_74("00 30 00 38 00 1C 00 0C 00 0C 00 0C 00 0C 00 1C FF F8 FF F0", 74) /* J */,
        DD7_DOT_75("FF FC FF FC 03 00 07 80 0F C0 1C E0 38 70 70 38 E0 1C C0 0C", 75) /* K */,
        DD7_DOT_76("FF FC FF FC 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C", 76) /* L */,
        DD7_DOT_77("FF FC FF FC 70 00 38 00 1E 00 1E 00 38 00 70 00 FF FC FF FC", 77) /* M */,
        DD7_DOT_78("FF FC FF FC 70 00 38 00 1C 00 0E 00 07 00 03 80 FF FC FF FC", 78) /* N */,
        DD7_DOT_79("1F E0 7F F8 60 18 C0 0C C0 0C C0 0C C0 0C 60 18 7F F8 1F E0", 79) /* O */,
        DD7_DOT_80("FF FC FF FC C3 00 C3 00 C3 00 C3 00 C3 00 66 00 7E 00 18 00", 80) /* P */,
        DD7_DOT_81("1F E0 7F F8 60 18 C0 0C C0 0C C0 0C C0 28 60 18 7F FC 1F CC", 81) /* Q */,
        DD7_DOT_82("FF FC FF FC C3 00 C3 80 C3 C0 C3 E0 C3 70 66 38 7E 1C 18 0C", 82) /* R */,
        DD7_DOT_83("18 30 7E 38 66 18 C3 0C C3 0C C3 0C C3 0C 61 98 71 F8 10 60", 83) /* S */,
        DD7_DOT_84("C0 00 C0 00 C0 00 C0 00 FF FC FF FC C0 00 C0 00 C0 00 C0 00", 84) /* T */,
        DD7_DOT_85("FF E0 FF F8 00 18 00 0C 00 0C 00 0C 00 0C 00 18 FF F8 FF E0", 85) /* U */,
        DD7_DOT_86("F0 00 FE 00 1F C0 03 F0 00 3C 00 3C 03 F0 1F C0 FE 00 F0 00", 86) /* V */,
        DD7_DOT_87("FF E0 FF F8 00 1C 00 3C 03 F8 03 F8 00 3C 00 1C FF F8 FF E0", 87) /* W */,
        DD7_DOT_88("F0 3C F8 7C 1C E0 0F C0 07 80 07 80 0F C0 1C E0 F8 7C F0 3C", 88) /* X */,
        DD7_DOT_89("F0 00 FC 00 0E 00 07 80 01 FC 01 FC 07 80 0E 00 FC 00 F0 00", 89) /* Y */,
        DD7_DOT_90("C0 3C C0 7C C0 EC C1 CC C3 8C C7 0C CE 0C DC 0C F8 0C F0 0C", 90) /* Z */,
        DD7_DOT_91("00 00 00 00 FF FC FF FC C0 0C C0 0C 00 00 00 00 00 00 00 00", 91) /* [ */,
        DD7_DOT_92("30 00 38 00 1C 00 0E 00 07 00 03 80 01 C0 00 E0 00 70 00 30", 92) /* \ */,
        DD7_DOT_93("00 00 00 00 00 00 C0 0C C0 0C FF FC FF FC 00 00 00 00 00 00", 93) /* ] */,
        DD7_DOT_95("00 00 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C 00 0C 00 00 00 00", 95) /* _ */,
        DD7_DOT_97("00 30 00 78 0C FC 0C CC 0C CC 0C CC 0C CC 0E CC 07 FC 03 FC", 97) /* a */,
        DD7_DOT_98("FF FC FF FC 03 0C 06 0C 0E 0C 0C 0C 0C 0C 0E 1C 07 F8 03 F0", 98) /* b */,
        DD7_DOT_99("03 F0 07 F8 0E 1C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C 0C", 99) /* c */,
        DD7_DOT_100("03 F0 07 F8 0E 1C 0C 0C 0C 0C 0E 0C 07 0C 03 0C FF FC FF FC", 100) /* d */,
        DD7_DOT_101("03 F0 07 F8 0E DC 0C CC 0C CC 0C CC 0E CC 07 CC 03 C0 00 00", 101) /* e */,
        DD7_DOT_102("00 C0 00 C0 3F FC 7F FC E0 C0 C0 C0 C0 00 E0 00 70 00 30 00", 102) /* f */,
        DD7_DOT_103("03 00 07 80 0F CC 0C CC 0C CC 0C CC 0C CC 0E CC 07 FC 03 FC", 103) /* g */,
        DD7_DOT_104("FF FC FF FC 03 00 07 00 0E 00 0C 00 0C 00 0E 00 07 FC 03 FC", 104) /* h */,
        DD7_DOT_105("00 00 00 00 00 0C 0C 0C CF FC CF FC 00 0C 00 0C 00 00 00 00", 105) /* i */,
        DD7_DOT_106("00 00 00 30 00 38 00 1C 00 0C 00 0C 0C 1C CF F8 CF F0 00 00", 106) /* j */,
        DD7_DOT_107("00 00 FF FC FF FC 00 C0 01 E0 03 F0 07 38 0E 1C 0C 0C 00 00", 107) /* k */,
        DD7_DOT_108("00 00 00 00 00 0C C0 0C FF FC FF FC 00 0C 00 0C 00 00 00 00", 108) /* l */,
        DD7_DOT_109("0F FC 0F FC 0C 00 0E 00 07 C0 07 C0 0E 00 0C 00 07 FC 03 FC", 109) /* m */,
        DD7_DOT_110("0F FC 0F FC 03 80 07 00 0E 00 0C 00 0C 00 0E 00 07 FC 03 FC", 110) /* n */,
        DD7_DOT_111("03 F0 07 F8 0E 1C 0C 0C 0C 0C 0C 0C 0C 0C 0E 1C 07 F8 03 F0", 111) /* o */,
        DD7_DOT_112("0F FC 0F FC 03 C0 07 C0 0E C0 0C C0 0C C0 0F C0 07 80 03 00", 112) /* p */,
        DD7_DOT_113("03 00 07 80 0F C0 0C C0 0C C0 0E C0 07 C0 03 C0 0F FC 0F FC", 113) /* q */,
        DD7_DOT_114("0F FC 0F FC 03 00 07 00 0E 00 0C 00 0C 00 0E 00 07 00 03 00", 114) /* r */,
        DD7_DOT_115("03 0C 07 8C 0F CC 0C CC 0C CC 0C CC 0C CC 0C FC 00 78 00 30", 115) /* s */,
        DD7_DOT_116("0C 00 0C 00 FF F0 FF F8 0C 1C 0C 0C 00 0C 00 1C 00 38 00 30", 116) /* t */,
        DD7_DOT_117("0F F0 0F F8 00 1C 00 0C 00 0C 00 1C 00 38 00 70 0F FC 0F FC", 117) /* u */,
        DD7_DOT_118("0F C0 0F E0 00 70 00 38 00 1C 00 1C 00 38 00 70 0F E0 0F C0", 118) /* v */,
        DD7_DOT_119("0F F0 0F F8 00 1C 00 1C 00 F8 00 F8 00 1C 00 1C 0F F8 0F F0", 119) /* w */,
        DD7_DOT_120("0C 0C 0E 1C 07 38 03 F0 01 E0 01 E0 03 F0 07 38 0E 1C 0C 0C", 120) /* x */,
        DD7_DOT_121("0F 00 0F 80 01 CC 00 CC 00 CC 00 CC 00 CC 00 DC 0F F8 0F F0", 121) /* y */,
        DD7_DOT_122("0C 0C 0C 1C 0C 3C 0C 7C 0C EC 0D CC 0F 8C 0F 0C 0E 0C 0C 0C", 122) /* z */,
        DD7_DOT_123("00 00 03 00 1F E0 7C F8 E0 1C C0 0C 00 00 00 00 00 00 00 00", 123) /* { */,
        DD7_DOT_125("00 00 00 00 00 00 C0 0C E0 1C 7C F8 1F E0 03 00 00 00 00 00", 125) /* } */,
        DD7_DOT_161("00 00 00 00 00 00 00 00 CF FC CF FC 00 00 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD7_DOT_186("00 00 30 00 78 00 CC 00 CC 00 78 00 30 00 00 00 00 00 00 00", 186) /* &ordm; */,
        DD7_DOT_191("00 F0 01 F8 03 9C 07 0C CE 0C CC 0C 00 0C 00 1C 00 38 00 30", 191) /* &iquest; */,
        DD7_DOT_209("0F FC 0F FC C7 00 C3 80 C1 C0 C0 E0 C0 70 C0 38 0F FC 0F FC", 209) /* &Ntilde; */,
        DD7_DOT_241("0F FC 0F FC C3 80 C7 00 CE 00 CC 00 CC 00 CE 00 07 FC 03 FC", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD7(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    
    public static enum DD8 implements IDD {
        DD8_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD8_DOT_33("00 00 00 00 00 00 FF F3 FF F3 00 00 00 00 00 00", 33) /* ! */,
        DD8_DOT_34("CC 00 F8 00 F0 00 00 00 CC 00 F8 00 F0 00 00 00", 34) /* " */,
        DD8_DOT_35("06 30 7F FF 7F FF 06 30 06 30 7F FF 7F FF 06 30", 35) /* # */,
        DD8_DOT_36("1F 0C 3F 8C 31 8C FF FF FF FF 31 8C 31 FC 30 F8", 36) /* $ */,
        DD8_DOT_37("60 0F F0 3F F0 FC 63 F0 0F C6 3F 0F FC 0F F0 06", 37) /* % */,
        DD8_DOT_38("3C FC 7F FE E7 87 CF C3 FC F7 78 3E 00 FF 00 C3", 38) /* & */,
        DD8_DOT_40("00 00 1F F8 7F FE E0 07 C0 03 00 00 00 00 00 00", 40) /* ( */,
        DD8_DOT_41("00 00 00 00 00 00 C0 03 E0 07 7F FE 1F F8 00 00", 41) /* ) */,
        DD8_DOT_42("0D B0 0F F0 07 E0 1F F8 1F F8 07 E0 0F F0 0D B0", 42) /* * */,
        DD8_DOT_43("01 80 01 80 01 80 1F F8 1F F8 01 80 01 80 01 80", 43) /* + */,
        DD8_DOT_44("00 00 00 00 00 33 00 3E 00 3C 00 00 00 00 00 00", 44) /* , */,
        DD8_DOT_45("01 80 01 80 01 80 01 80 01 80 01 80 01 80 01 80", 45) /* - */,
        DD8_DOT_46("00 00 00 00 00 06 00 0F 00 0F 00 06 00 00 00 00", 46) /* . */,
        DD8_DOT_47("00 0F 00 3F 00 FC 03 F0 0F C0 3F 00 FC 00 F0 00", 47) /*  / */,
        DD8_DOT_48("1F F8 7F FE 60 46 C0 83 C1 03 62 06 7F FE 1F F8", 48) /* 0 */,
        DD8_DOT_49("00 00 30 03 70 03 FF FF FF FF 00 03 00 03 00 00", 49) /* 1 */,
        DD8_DOT_50("30 0F 70 1F E0 3B C0 73 C0 E3 E3 C3 7F 83 3E 03", 50) /* 2 */,
        DD8_DOT_51("30 0C 70 0E E0 07 C1 83 C1 83 E3 C7 7E 7E 3E 7C", 51) /* 3 */,
        DD8_DOT_52("07 E0 0E 60 1C 60 38 63 70 63 FF FF FF FF 00 63", 52) /* 4 */,
        DD8_DOT_53("FF 0C FF 0E C3 07 C3 03 C3 03 C3 87 C1 FE C0 FC", 53) /* 5 */,
        DD8_DOT_54("3F FC 7F FE E1 87 C3 03 C3 03 C3 87 01 FE 00 FC", 54) /* 6 */,
        DD8_DOT_55("C0 00 C0 00 C1 FF C7 FF CF 00 DC 00 F8 00 F0 00", 55) /* 7 */,
        DD8_DOT_56("3E 7C 7F FE E3 C7 C1 83 C1 83 E3 C7 7F FE 3E 7C", 56) /* 8 */,
        DD8_DOT_57("3E 00 7F 00 E3 83 C1 83 C1 83 E1 87 7F FE 3F FC", 57) /* 9 */,
        DD8_DOT_58("00 00 00 00 06 0C 0F 1E 0F 1E 06 0C 00 00 00 00", 58) /* : */,
        DD8_DOT_59("00 00 00 00 0C 66 1E 7C 1E 78 0C 00 00 00 00 00", 59) /* ; */,
        DD8_DOT_60("01 80 03 C0 07 E0 0E 70 1C 38 38 1C 70 0E 60 06", 60) /* < */,
        DD8_DOT_61("06 60 06 60 06 60 06 60 06 60 06 60 06 60 06 60", 61) /*  = */,
        DD8_DOT_62("60 06 70 0E 38 1C 1C 38 0E 70 07 E0 03 C0 01 80", 62) /* > */,
        DD8_DOT_63("70 00 E0 00 C0 F3 C1 F3 C3 80 E7 00 7E 00 3C 00", 63) /* ? */,
        DD8_DOT_64("3F FC 7F FE E0 07 C3 C3 C7 E3 E6 63 7F E0 3F C0", 64) /* @ */,
        DD8_DOT_65("1F FF 7F FF 60 C0 C0 C0 C0 C0 60 C0 7F FF 1F FF", 65) /* A */,
        DD8_DOT_66("FF FF FF FF C1 83 C1 83 C1 83 63 C6 7E 7E 1C 38", 66) /* B */,
        DD8_DOT_67("1F F8 7F FE 60 06 C0 03 C0 03 60 06 70 0E 10 08", 67) /* C */,
        DD8_DOT_68("FF FF FF FF C0 03 C0 03 C0 03 60 06 7F FE 1F F8", 68) /* D */,
        DD8_DOT_69("FF FF FF FF C1 83 C1 83 C1 83 C0 03 C0 03 C0 03", 69) /* E */,
        DD8_DOT_70("FF FF FF FF C1 80 C1 80 C1 80 C0 00 C0 00 C0 00", 70) /* F */,
        DD8_DOT_71("3F FC 7F FE E0 07 C0 03 C1 83 E1 87 71 FE 31 FC", 71) /* G */,
        DD8_DOT_72("FF FF FF FF 01 80 01 80 01 80 01 80 FF FF FF FF", 72) /* H */,
        DD8_DOT_73("00 00 C0 03 C0 03 FF FF FF FF C0 03 C0 03 00 00", 73) /* I */,
        DD8_DOT_74("00 0C 00 0E 00 07 00 03 00 03 00 07 FF FE FF FC", 74) /* J */,
        DD8_DOT_75("FF FF FF FF 07 E0 0E 70 1C 38 38 1C 70 0E E0 07", 75) /* K */,
        DD8_DOT_76("FF FF FF FF 00 03 00 03 00 03 00 03 00 03 00 03", 76) /* L */,
        DD8_DOT_77("FF FF FF FF 38 00 1E 00 1E 00 38 00 FF FF FF FF", 77) /* M */,
        DD8_DOT_78("FF FF FF FF 38 00 1C 00 0E 00 07 00 FF FF FF FF", 78) /* N */,
        DD8_DOT_79("1F F8 7F FE 60 06 C0 03 C0 03 60 06 7F FE 1F F8", 79) /* O */,
        DD8_DOT_80("FF FF FF FF C1 80 C1 80 C1 80 63 00 7F 00 1C 00", 80) /* P */,
        DD8_DOT_81("1F F8 7F FE 60 06 C0 03 C0 0A 60 06 7F FF 1F F3", 81) /* Q */,
        DD8_DOT_82("FF FF FF FF C1 C0 C1 E0 C1 B0 63 18 7F 0F 1C 07", 82) /* R */,
        DD8_DOT_83("1C 0C 7F 0E 63 06 C1 83 C1 83 60 C6 70 FE 10 38", 83) /* S */,
        DD8_DOT_84("C0 00 C0 00 C0 00 FF FF FF FF C0 00 C0 00 C0 00", 84) /* T */,
        DD8_DOT_85("FF F8 FF FE 00 06 00 03 00 03 00 06 FF FE FF F8", 85) /* U */,
        DD8_DOT_86("FE 00 FF C0 03 F8 00 3F 00 3F 03 F8 FF C0 FE 00", 86) /* V */,
        DD8_DOT_87("FF F8 FF FE 00 07 00 FE 00 FE 00 07 FF FE FF F8", 87) /* W */,
        DD8_DOT_88("F0 0F FC 3F 1E 78 07 E0 07 E0 1E 78 FC 3F F0 0F", 88) /* X */,
        DD8_DOT_89("FC 00 FE 00 07 80 01 FF 01 FF 07 80 FE 00 FC 00", 89) /* Y */,
        DD8_DOT_90("C0 1F C0 3F C0 F3 C1 C3 C3 83 CF 03 FC 03 F8 03", 90) /* Z */,
        DD8_DOT_91("00 00 FF FF FF FF C0 03 C0 03 00 00 00 00 00 00", 91) /* [ */,
        DD8_DOT_92("F0 00 FC 00 3F 00 0F C0 03 F0 00 FC 00 3F 00 0F", 92) /* \ */,
        DD8_DOT_93("00 00 00 00 00 00 C0 03 C0 03 FF FF FF FF 00 00", 93) /* ] */,
        DD8_DOT_95("00 00 00 03 00 03 00 03 00 03 00 03 00 03 00 00", 95) /* _ */,
        DD8_DOT_97("00 3C 00 7E 0C E7 0C C3 0C C3 0E C3 07 FF 03 FF", 97) /* a */,
        DD8_DOT_98("FF FF FF FF 00 C3 01 83 03 83 03 87 01 FE 00 FC", 98) /* b */,
        DD8_DOT_99("03 FC 07 FE 0E 07 0C 03 0C 03 0C 03 0C 03 0C 03", 99) /* c */,
        DD8_DOT_100("00 FC 01 FE 03 87 03 83 01 C3 00 C3 FF FF FF FF", 100) /* d */,
        DD8_DOT_101("03 FC 07 FE 0E 67 0C 63 0C 63 0E 63 07 E3 03 E0", 101) /* e */,
        DD8_DOT_102("00 60 3F FF 7F FF E0 60 C0 00 E0 00 70 00 30 00", 102) /* f */,
        DD8_DOT_103("03 80 07 C0 0E E3 0C 63 0C 63 0E 63 07 FF 03 FF", 103) /* g */,
        DD8_DOT_104("FF FF FF FF 01 80 03 80 07 00 07 00 03 FF 01 FF", 104) /* h */,
        DD8_DOT_105("00 00 00 03 0C 03 CF FF CF FF 00 03 00 03 00 00", 105) /* i */,
        DD8_DOT_106("00 0C 00 0E 00 07 00 03 00 03 0C 07 CF FE CF FC", 106) /* j */,
        DD8_DOT_107("FF FF FF FF 00 30 00 78 00 FC 01 CE 03 87 03 03", 107) /* k */,
        DD8_DOT_108("00 00 00 03 C0 03 FF FF FF FF 00 03 00 03 00 00", 108) /* l */,
        DD8_DOT_109("0F FF 0F FF 0E 00 07 F0 07 F0 0E 00 07 FF 03 FF", 109) /* m */,
        DD8_DOT_110("0F FF 0F FF 03 00 07 00 0E 00 0E 00 07 FF 03 FF", 110) /* n */,
        DD8_DOT_111("03 FC 07 FE 0E 07 0C 03 0C 03 0E 07 07 FE 03 FC", 111) /* o */,
        DD8_DOT_112("0F FF 0F FF 03 E0 06 E0 0C 60 0E C0 07 80 03 00", 112) /* p */,
        DD8_DOT_113("03 00 07 80 0E C0 0C 60 06 E0 03 E0 0F FF 0F FF", 113) /* q */,
        DD8_DOT_114("0F FF 0F FF 03 00 07 00 0E 00 0E 00 07 00 03 00", 114) /* r */,
        DD8_DOT_115("03 83 07 C3 0E E3 0C 63 0C 63 0C 77 0C 3E 0C 1C", 115) /* s */,
        DD8_DOT_116("0C 00 FF FC FF FE 0C 07 0C 03 00 07 00 0E 00 0C", 116) /* t */,
        DD8_DOT_117("0F FC 0F FE 00 07 00 07 00 0E 00 1C 0F FF 0F FF", 117) /* u */,
        DD8_DOT_118("0F C0 0F F0 00 3C 00 1F 00 1F 00 3C 0F F0 0F C0", 118) /* v */,
        DD8_DOT_119("0F FC 0F FE 00 07 00 7E 00 7E 00 07 0F FE 0F FC", 119) /* w */,
        DD8_DOT_120("0C 03 0F 0F 03 9C 01 F8 01 F8 03 9C 0F 0F 0C 03", 120) /* x */,
        DD8_DOT_121("0F 00 0F 80 01 C3 00 C3 00 C3 00 C7 0F FE 0F FC", 121) /* y */,
        DD8_DOT_122("0C 0F 0C 1F 0C 3B 0C 73 0C E3 0D C3 0F 83 0F 03", 122) /* z */,
        DD8_DOT_123("01 80 1F F8 7E 7E E0 07 C0 03 00 00 00 00 00 00", 123) /* { */,
        DD8_DOT_125("00 00 00 00 00 00 C0 03 E0 07 7E 7E 1F F8 01 80", 125) /* } */,
        DD8_DOT_161("00 00 00 00 00 00 CF FF CF FF 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD8_DOT_186("30 00 78 00 CC 00 CC 00 78 00 30 00 00 00 00 00", 186) /* &ordm; */,
        DD8_DOT_191("00 7C 00 FE 01 C7 CF 03 CE 03 00 07 00 0F 00 0E", 191) /* &iquest; */,
        DD8_DOT_209("0F FF 0F FF C3 80 C1 C0 C0 E0 C0 70 0F FF 0F FF", 209) /* &Ntilde; */,
        DD8_DOT_241("0F FF CF FF C3 80 C7 00 CE 00 CE 00 C7 FF 03 FF", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD8(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }

    public static enum DD9 implements IDD {
        DD9_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD9_DOT_33("00 00 00 00 00 00 00 00 FF F3 FF F3 00 00 00 00 00 00 00 00", 33) /* ! */,
        DD9_DOT_34("CC 00 CC 00 F8 00 F0 00 00 00 CC 00 CC 00 F8 00 F0 00 00 00", 34) /* " */,
        DD9_DOT_35("06 60 06 60 7F FE 7F FE 06 60 06 60 7F FE 7F FE 06 60 06 60", 35) /* # */,
        DD9_DOT_36("1F 0C 3F 8C 31 8C 31 8C FF FF FF FF 31 8C 31 8C 31 FC 30 F8", 36) /* $ */,
        DD9_DOT_37("60 07 F0 1F F0 3E 60 78 01 E0 07 80 1E 06 7C 0F F8 0F E0 06", 37) /* % */,
        DD9_DOT_38("3C FC 7F FE E7 87 C7 83 CF C3 FC E7 78 3E 00 7E 00 E7 00 C3", 38) /* & */,
        DD9_DOT_40("00 00 00 00 1F F8 7F FE E0 07 C0 03 00 00 00 00 00 00 00 00", 40) /* ( */,
        DD9_DOT_41("00 00 00 00 00 00 C0 03 E0 07 7F FE 1F F8 00 00 00 00 00 00", 41) /* ) */,
        DD9_DOT_42("01 80 0D B0 0F F0 07 E0 1F F8 1F F8 07 E0 0F F0 0D B0 01 80", 42) /* * */,
        DD9_DOT_43("01 80 01 80 01 80 01 80 1F F8 1F F8 01 80 01 80 01 80 01 80", 43) /* + */,
        DD9_DOT_44("00 00 00 00 00 33 00 33 00 3E 00 3C 00 00 00 00 00 00 00 00", 44) /* , */,
        DD9_DOT_45("01 80 01 80 01 80 01 80 01 80 01 80 01 80 01 80 01 80 00 00", 45) /* - */,
        DD9_DOT_46("00 00 00 00 00 06 00 0F 00 0F 00 06 00 00 00 00 00 00 00 00", 46) /* . */,
        DD9_DOT_47("00 07 00 1F 00 3E 00 78 01 E0 07 80 1E 00 7C 00 F8 00 E0 00", 47) /*  / */,
        DD9_DOT_48("1F F8 7F FE 60 46 C0 83 C0 83 C1 03 C1 03 62 06 7F FE 1F F8", 48) /* 0 */,
        DD9_DOT_49("00 00 00 00 30 03 70 03 FF FF FF FF 00 03 00 03 00 00 00 00", 49) /* 1 */,
        DD9_DOT_50("30 0F 70 1F E0 3B C0 73 C0 E3 C1 C3 C3 83 E7 03 7E 03 3C 03", 50) /* 2 */,
        DD9_DOT_51("30 0C 70 0E E0 07 C0 03 C0 03 C1 83 C1 83 E3 C7 7E 7E 3E 7C", 51) /* 3 */,
        DD9_DOT_52("01 E0 03 E0 07 60 0E 60 1C 63 38 63 7F FF FF FF 00 63 00 63", 52) /* 4 */,
        DD9_DOT_53("FF 0C FF 0E C3 07 C3 03 C3 03 C3 03 C3 03 C3 87 C1 FE C0 FC", 53) /* 5 */,
        DD9_DOT_54("3F FC 7F FE E3 07 C3 03 C3 03 C3 03 C3 03 C3 87 01 FE 00 FC", 54) /* 6 */,
        DD9_DOT_55("C0 00 C0 00 C0 00 C0 00 C1 FF C7 FF CF 00 DC 00 F8 00 F0 00", 55) /* 7 */,
        DD9_DOT_56("3E 7C 7F FE E3 C7 C1 83 C1 83 C1 83 C1 83 E3 C7 7F FE 3E 7C", 56) /* 8 */,
        DD9_DOT_57("3E 00 7F 00 E3 83 C1 83 C1 83 C1 83 C1 83 E1 87 7F FE 3F FC", 57) /* 9 */,
        DD9_DOT_58("00 00 00 00 00 00 06 0C 0F 1E 0F 1E 06 0C 00 00 00 00 00 00", 58) /* : */,
        DD9_DOT_59("00 00 00 00 00 33 06 33 0F 3E 0F 3C 06 00 00 00 00 00 00 00", 59) /* ; */,
        DD9_DOT_60("00 00 01 80 03 C0 07 E0 0E 70 1C 38 38 1C 70 0E 60 06 00 00", 60) /* < */,
        DD9_DOT_61("06 60 06 60 06 60 06 60 06 60 06 60 06 60 06 60 06 60 00 00", 61) /*  = */,
        DD9_DOT_62("00 00 60 06 70 0E 38 1C 1C 38 0E 70 07 E0 03 C0 01 80 00 00", 62) /* > */,
        DD9_DOT_63("30 00 70 00 E0 00 C0 00 C1 F3 C3 F3 C7 00 EE 00 7C 00 38 00", 63) /* ? */,
        DD9_DOT_64("3F FC 7F FE E0 07 C0 03 C7 C3 CF E3 CC 63 EC 63 7F E0 3F C0", 64) /* @ */,
        DD9_DOT_65("1F FF 7F FF 60 C0 C0 C0 C0 C0 C0 C0 C0 C0 60 C0 7F FF 1F FF", 65) /* A */,
        DD9_DOT_66("FF FF FF FF C1 83 C1 83 C1 83 C1 83 C1 83 63 C6 7E 7E 1C 38", 66) /* B */,
        DD9_DOT_67("1F F8 7F FE 60 06 C0 03 C0 03 C0 03 C0 03 60 06 70 0E 10 08", 67) /* C */,
        DD9_DOT_68("FF FF FF FF C0 03 C0 03 C0 03 C0 03 C0 03 60 06 7F FE 1F F8", 68) /* D */,
        DD9_DOT_69("FF FF FF FF C1 83 C1 83 C1 83 C1 83 C1 83 C0 03 C0 03 C0 03", 69) /* E */,
        DD9_DOT_70("FF FF FF FF C1 80 C1 80 C1 80 C1 80 C1 80 C0 00 C0 00 C0 00", 70) /* F */,
        DD9_DOT_71("3F FC 7F FE E0 07 C0 03 C1 83 C1 83 C1 83 E1 87 71 FE 31 FC", 71) /* G */,
        DD9_DOT_72("FF FF FF FF 01 80 01 80 01 80 01 80 01 80 01 80 FF FF FF FF", 72) /* H */,
        DD9_DOT_73("00 00 00 00 C0 03 C0 03 FF FF FF FF C0 03 C0 03 00 00 00 00", 73) /* I */,
        DD9_DOT_74("00 0C 00 0E 00 07 00 03 00 03 00 03 00 03 00 07 FF FE FF FC", 74) /* J */,
        DD9_DOT_75("FF FF FF FF 01 80 03 C0 07 E0 0E 70 1C 38 38 1C F0 0F E0 07", 75) /* K */,
        DD9_DOT_76("FF FF FF FF 00 03 00 03 00 03 00 03 00 03 00 03 00 03 00 03", 76) /* L */,
        DD9_DOT_77("FF FF FF FF 70 00 38 00 1E 00 1E 00 38 00 70 00 FF FF FF FF", 77) /* M */,
        DD9_DOT_78("FF FF FF FF 70 00 38 00 1C 00 0E 00 07 00 03 80 FF FF FF FF", 78) /* N */,
        DD9_DOT_79("1F F8 7F FE 60 06 C0 03 C0 03 C0 03 C0 03 60 06 7F FE 1F F8", 79) /* O */,
        DD9_DOT_80("FF FF FF FF C1 80 C1 80 C1 80 C1 80 C1 80 63 00 7F 00 1C 00", 80) /* P */,
        DD9_DOT_81("1F F8 7F FE 60 06 C0 03 C0 03 C0 03 C0 0A 60 06 7F FF 1F F3", 81) /* Q */,
        DD9_DOT_82("FF FF FF FF C1 C0 C1 E0 C1 F0 C1 B8 C1 9C 63 0E 7F 07 1C 03", 82) /* R */,
        DD9_DOT_83("1C 0C 7F 0E 63 06 C1 83 C1 83 C1 83 C1 83 60 C6 70 FE 10 38", 83) /* S */,
        DD9_DOT_84("C0 00 C0 00 C0 00 C0 00 FF FF FF FF C0 00 C0 00 C0 00 C0 00", 84) /* T */,
        DD9_DOT_85("FF F8 FF FE 00 06 00 03 00 03 00 03 00 03 00 06 FF FE FF F8", 85) /* U */,
        DD9_DOT_86("F0 00 FE 00 1F C0 03 F8 00 7F 00 7F 03 F8 1F C0 FE 00 F0 00", 86) /* V */,
        DD9_DOT_87("FF F8 FF FE 00 07 00 0F 01 FE 01 FE 00 0F 00 07 FF FE FF F8", 87) /* W */,
        DD9_DOT_88("F8 1F FC 3F 0E 70 07 E0 03 C0 03 C0 07 E0 0E 70 FC 3F F8 1F", 88) /* X */,
        DD9_DOT_89("F0 00 FC 00 0E 00 07 80 01 FF 01 FF 07 80 0E 00 FC 00 F0 00", 89) /* Y */,
        DD9_DOT_90("C0 1F C0 3F C0 73 C0 E3 C1 C3 C3 83 C7 03 CE 03 FC 03 F8 03", 90) /* Z */,
        DD9_DOT_91("00 00 00 00 FF FF FF FF C0 03 C0 03 00 00 00 00 00 00 00 00", 91) /* [ */,
        DD9_DOT_92("E0 00 F8 00 7C 00 1E 00 07 80 01 E0 00 78 00 3E 00 1F 00 07", 92) /* \ */,
        DD9_DOT_93("00 00 00 00 00 00 C0 03 C0 03 FF FF FF FF 00 00 00 00 00 00", 93) /* ] */,
        DD9_DOT_95("00 00 00 03 00 03 00 03 00 03 00 03 00 03 00 03 00 00 00 00", 95) /* _ */,
        DD9_DOT_97("00 3C 00 7E 0C E7 0C C3 0C C3 0C C3 0C C3 0E C3 07 FF 03 FF", 97) /* a */,
        DD9_DOT_98("FF FF FF FF 00 C3 01 83 03 83 03 03 03 03 03 87 01 FE 00 FC", 98) /* b */,
        DD9_DOT_99("03 FC 07 FE 0E 07 0C 03 0C 03 0C 03 0C 03 0C 03 0C 03 0C 03", 99) /* c */,
        DD9_DOT_100("00 FC 01 FE 03 87 03 03 03 03 03 83 01 C3 00 C3 FF FF FF FF", 100) /* d */,
        DD9_DOT_101("03 FC 07 FE 0E 67 0C 63 0C 63 0C 63 0E 63 07 E3 03 E0 00 00", 101) /* e */,
        DD9_DOT_102("00 60 00 60 3F FF 7F FF E0 60 C0 60 C0 00 E0 00 70 00 30 00", 102) /* f */,
        DD9_DOT_103("03 80 07 C0 0E E3 0C 63 0C 63 0C 63 0C 63 0E 63 07 FF 03 FF", 103) /* g */,
        DD9_DOT_104("FF FF FF FF 00 C0 01 C0 03 80 03 00 03 00 03 80 01 FF 00 FF", 104) /* h */,
        DD9_DOT_105("00 00 00 00 00 03 0C 03 CF FF CF FF 00 03 00 03 00 00 00 00", 105) /* i */,
        DD9_DOT_106("00 00 00 0C 00 0E 00 07 00 03 00 03 0C 07 CF FE CF FC 00 00", 106) /* j */,
        DD9_DOT_107("00 00 FF FF FF FF 00 30 00 78 00 FC 01 CE 03 87 03 03 00 00", 107) /* k */,
        DD9_DOT_108("00 00 00 00 00 03 C0 03 FF FF FF FF 00 03 00 03 00 00 00 00", 108) /* l */,
        DD9_DOT_109("0F FF 0F FF 0C 00 0E 00 07 C0 07 C0 0E 00 0C 00 07 FF 03 FF", 109) /* m */,
        DD9_DOT_110("0F FF 0F FF 03 80 07 00 0E 00 0C 00 0C 00 0E 00 07 FF 03 FF", 110) /* n */,
        DD9_DOT_111("03 FC 07 FE 0E 07 0C 03 0C 03 0C 03 0C 03 0E 07 07 FE 03 FC", 111) /* o */,
        DD9_DOT_112("0F FF 0F FF 03 E0 06 E0 0C 60 0C 60 0C 60 0E E0 07 C0 03 80", 112) /* p */,
        DD9_DOT_113("03 80 07 C0 0E E0 0C 60 0C 60 0C 60 06 E0 03 E0 0F FF 0F FF", 113) /* q */,
        DD9_DOT_114("0F FF 0F FF 03 00 07 00 0E 00 0C 00 0C 00 0E 00 07 00 03 00", 114) /* r */,
        DD9_DOT_115("03 83 07 C3 0E E3 0C 63 0C 63 0C 63 0C 63 0C 77 00 3E 00 1C", 115) /* s */,
        DD9_DOT_116("0C 00 0C 00 FF FC FF FE 0C 07 0C 03 00 03 00 07 00 0E 00 0C", 116) /* t */,
        DD9_DOT_117("0F FC 0F FE 00 07 00 03 00 03 00 07 00 0E 00 1C 0F FF 0F FF", 117) /* u */,
        DD9_DOT_118("0F F0 0F F8 00 1C 00 0E 00 07 00 07 00 0E 00 1C 0F F8 0F F0", 118) /* v */,
        DD9_DOT_119("0F FC 0F FE 00 07 00 07 00 3E 00 3E 00 07 00 07 0F FE 0F FC", 119) /* w */,
        DD9_DOT_120("0E 07 0F 0F 03 9C 01 F8 00 F0 00 F0 01 F8 03 9C 0F 0F 0E 07", 120) /* x */,
        DD9_DOT_121("0F 80 0F C0 00 E3 00 63 00 63 00 63 00 63 00 67 0F FE 0F FC", 121) /* y */,
        DD9_DOT_122("0C 07 0C 0F 0C 1F 0C 3B 0C 73 0C E3 0D C3 0F 83 0F 03 0E 03", 122) /* z */,
        DD9_DOT_123("00 00 01 80 1F F8 7E 7E E0 07 C0 03 00 00 00 00 00 00 00 00", 123) /* { */,
        DD9_DOT_125("00 00 00 00 00 00 C0 03 E0 07 7E 7E 1F F8 01 80 00 00 00 00", 125) /* } */,
        DD9_DOT_161("00 00 00 00 00 00 00 00 CF FF CF FF 00 00 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD9_DOT_186("00 00 30 00 78 00 CC 00 CC 00 78 00 30 00 00 00 00 00 00 00", 186) /* &ordm; */,
        DD9_DOT_191("00 3C 00 7E 00 E7 01 C3 CF 83 CF 03 00 03 00 07 00 0E 00 0C", 191) /* &iquest; */,
        DD9_DOT_209("0F FF 0F FF C7 00 C3 80 C1 C0 C0 E0 C0 70 C0 38 0F FF 0F FF", 209) /* &Ntilde; */,
        DD9_DOT_241("0F FF 0F FF C3 80 C7 00 CE 00 CC 00 CC 00 CE 00 07 FF 03 FF", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD9(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    
    public static enum DD10 implements IDD {
        DD10_DOT_32("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 32) /* SPACE */,
        DD10_DOT_33("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1E FF FF C0 3F FF FF FC 3F FF FF FC 3F FF FF C0 3F 00 00 00 1E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 33) /* ! */,
        DD10_DOT_34("F0 F0 00 00 F0 F0 00 00 F1 C0 00 00 FF C0 00 00 FF 80 00 00 FE 00 00 00 00 00 00 00 00 00 00 00 F0 F0 00 00 F0 F0 00 00 F1 C0 00 00 FF C0 00 00 FF 80 00 00 FE 00 00 00 00 00 00 00 00 00 00 00", 34) /* " */,
        DD10_DOT_35("00 3C 78 00 00 3C 78 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 3C 78 00 00 3C 78 00 00 3C 78 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 3C 78 00 00 3C 78 00 00 00 00 00", 35) /* # */,
        DD10_DOT_36("00 FC 00 F0 03 FF 00 F0 07 FF 80 F0 07 FF 80 F0 0F 87 C0 F0 0F 87 C0 F0 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 0F 03 E1 E0 0F 01 FF E0 0F 01 FF E0 0F 00 FF C0 0F 00 3F 00 00 00 00 00", 36) /* $ */,
        DD10_DOT_37("78 00 00 3F FC 00 00 FF FC 00 03 FC FC 00 0F F0 FC 00 3F C0 78 00 FF 00 00 03 FC 00 00 0F F0 00 00 3F C0 00 00 FF 00 1E 03 FC 00 3F 0F F0 00 3F 3F C0 00 3F FF 00 00 3F FC 00 00 1E 00 00 00 00", 37) /* % */,
        DD10_DOT_38("0F F0 FF F0 3F F9 FF FC 3F FF FF FE FE 7F E0 3E F8 3F C0 1F F8 7F C0 0F F0 7F E0 0F F9 F9 F0 1F FF F0 F8 3F 3F E0 7E 7E 3F C0 1F FC 0F 80 0F F8 06 00 1F FC 00 00 7E 7E 00 00 FC 3F 00 00 00 00", 38) /* & */,
        DD10_DOT_40("00 00 00 00 00 00 00 00 00 00 00 00 01 FF FF 80 07 FF FF E0 0F FF FF F0 3F FF FF FC 3F 00 00 FC FC 00 00 3F F0 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 40) /* ( */,
        DD10_DOT_41("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F0 00 00 0F FC 00 00 3F 3F 00 00 FC 3F FF FF FC 0F FF FF F0 07 FF FF E0 01 FF FF 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 41) /* ) */,
        DD10_DOT_42("00 F3 CF 00 00 F3 CF 00 00 FF FF 00 00 FF FF 00 00 3F FC 00 03 FF FF C0 03 FF FF C0 03 FF FF C0 03 FF FF C0 00 3F FC 00 00 FF FF 00 00 FF FF 00 00 F3 CF 00 00 F3 CF 00 00 00 00 00 00 00 00 00", 42) /* * */,
        DD10_DOT_43("00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 03 FF FF C0 03 FF FF C0 03 FF FF C0 03 FF FF C0 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 00 00 00 00 00 00 00", 43) /* + */,
        DD10_DOT_44("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0F 0F 00 00 0F 0F 00 00 0F 1C 00 00 0F FC 00 00 0F F8 00 00 0F E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 44) /* , */,
        DD10_DOT_45("00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 00 00 00", 45) /* - */,
        DD10_DOT_46("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1E 00 00 00 3F 00 00 00 3F 00 00 00 3F 00 00 00 3F 00 00 00 1E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 46) /* . */,
        DD10_DOT_47("00 00 00 3F 00 00 00 FF 00 00 03 FC 00 00 0F F0 00 00 3F C0 00 00 FF 00 00 03 FC 00 00 0F F0 00 00 3F C0 00 00 FF 00 00 03 FC 00 00 0F F0 00 00 3F C0 00 00 FF 00 00 00 FC 00 00 00 00 00 00 00", 47) /*  / */,
        DD10_DOT_48("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 38 7F F8 00 70 1F F8 00 E0 1F F0 01 C0 0F F8 03 80 1F F8 07 00 1F FE 0E 00 7F 7F FF FF FE 7F FF FF FE 3F FF FF FC 0F FF FF F0 00 00 00 00", 48) /* 0 */,
        DD10_DOT_49("00 00 00 00 0F 00 00 0F 1F 00 00 0F 3F 00 00 0F 7F 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 00 00 00 00 00", 49) /* 1 */,
        DD10_DOT_50("0F 00 00 0F 3F 00 00 3F 7F 00 00 FF 7F 00 03 FF FE 00 0F FF F8 00 3F FF F8 00 FF CF F0 03 FF 0F F8 0F FC 0F F8 3F F0 0F FE FF C0 0F 7F FF 00 0F 7F FC 00 0F 3F F0 00 0F 0F C0 00 0F 00 00 00 00", 50) /* 2 */,
        DD10_DOT_51("0F 00 00 F0 3F 00 00 FC 7F 00 00 FE 7F 00 00 FE FE 00 00 7F F8 03 C0 1F F8 03 C0 1F F0 03 C0 0F F8 07 E0 1F F8 07 E0 1F FE 1F F8 7F 7F FE 7F FE 7F FE 7F FE 3F FC 3F FC 0F F0 0F F0 00 00 00 00", 51) /* 3 */,
        DD10_DOT_52("00 00 3C 00 00 00 FC 00 00 03 FC 00 00 0F FC 00 00 3F FC 00 00 FF 3C 0F 03 FC 3C 0F 0F F0 3C 0F 3F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 3C 0F 00 00 3C 0F 00 00 3C 0F 00 00 00 00", 52) /* 4 */,
        DD10_DOT_53("FF FE 00 F0 FF FE 00 FC FF FE 00 FE FF FE 00 FE F0 1E 00 7F F0 1E 00 1F F0 1E 00 1F F0 1E 00 0F F0 1F 00 1F F0 1F 00 1F F0 1F C0 7F F0 0F FF FE F0 0F FF FE F0 07 FF FC F0 01 FF F0 00 00 00 00", 53) /* 5 */,
        DD10_DOT_54("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 0F C0 7F F8 1F 00 1F F8 1F 00 1F F0 1E 00 0F F0 1F 00 1F F0 1F 00 1F F0 1F C0 7F F0 0F FF FE F0 0F FF FE 00 07 FF FC 00 01 FF F0 00 00 00 00", 54) /* 6 */,
        DD10_DOT_55("F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 FF FF F0 07 FF FF F0 1F FF FF F0 7F FF FF F1 FF 80 00 F7 FC 00 00 FF F0 00 00 FF C0 00 00 FF 00 00 00 FC 00 00 00 00 00 00 00", 55) /* 7 */,
        DD10_DOT_56("0F F0 0F F0 3F FC 3F FC 7F FE 7F FE 7F FE 7F FE FE 1F F8 7F F8 07 E0 1F F8 07 E0 1F F0 03 C0 0F F8 07 E0 1F F8 07 E0 1F FE 1F F8 7F 7F FE 7F FE 7F FE 7F FE 3F FC 3F FC 0F F0 0F F0 00 00 00 00", 56) /* 8 */,
        DD10_DOT_57("0F FC 00 00 3F FF 00 00 7F FF 80 00 7F FF 80 1F FE 1F C0 1F F8 07 C0 1F F8 07 C0 1F F0 03 C0 0F F8 07 C0 1F F8 07 C0 1F FE 1F 80 7F 7F FF FF FE 7F FF FF FE 3F FF FF FC 0F FF FF F0 00 00 00 00", 57) /* 9 */,
        DD10_DOT_58("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78 03 C0 00 FC 07 E0 00 FC 07 E0 00 FC 07 E0 00 FC 07 E0 00 78 03 C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 58) /* : */,
        DD10_DOT_59("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78 0F 0F 00 FC 0F 0F 00 FC 0F 1C 00 FC 0F FC 00 FC 0F F8 00 78 0F E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 59) /* ; */,
        DD10_DOT_60("00 03 C0 00 00 0F F0 00 00 0F F0 00 00 3F FC 00 00 3E 7C 00 00 FC 3F 00 00 F0 0F 00 03 F0 0F C0 03 C0 03 C0 0F C0 03 F0 0F 00 00 F0 3F 00 00 FC 3C 00 00 3C 3C 00 00 3C 00 00 00 00 00 00 00 00", 60) /* < */,
        DD10_DOT_61("00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 3C 3C 00 00 00 00 00", 61) /*  = */,
        DD10_DOT_62("00 00 00 00 3C 00 00 3C 3C 00 00 3C 3F 00 00 FC 0F 00 00 F0 0F C0 03 F0 03 C0 03 C0 03 F0 0F C0 00 F0 0F 00 00 FC 3F 00 00 3E 7C 00 00 3F FC 00 00 0F F0 00 00 0F F0 00 00 03 C0 00 00 00 00 00", 62) /* > */,
        DD10_DOT_63("0F 00 00 00 3F 00 00 00 7F 00 00 00 7F 00 00 00 FE 00 00 00 F8 00 00 00 F8 00 FF 0F F0 03 FF 0F F8 07 FF 0F F8 1F FF 0F FE 7F 80 00 7F FE 00 00 7F FC 00 00 3F F0 00 00 0F C0 00 00 00 00 00 00", 63) /* ? */,
        DD10_DOT_64("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 00 7F F8 00 00 1F F8 1F E0 1F F0 7F F8 0F F0 7F F8 0F F8 F0 3C 0F F8 F0 3C 0F 7E F0 3C 0F 7F FF F8 0F 3F FF F8 00 0F FF E0 00 00 00 00 00", 64) /* @ */,
        DD10_DOT_65("0F FF FF FF 3F FF FF FF 7F FF FF FF 7F FF FF FF FE 00 F0 00 F8 00 F0 00 F8 00 F0 00 F0 00 F0 00 F8 00 F0 00 F8 00 F0 00 FE 00 F0 00 7F FF FF FF 7F FF FF FF 3F FF FF FF 0F FF FF FF 00 00 00 00", 65) /* A */,
        DD10_DOT_66("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F8 07 E0 1F F8 07 E0 1F FE 1F F8 7F 7F FF FF FE 7F FE 7F FE 3F FC 3F FC 0F F0 0F F0 00 00 00 00", 66) /* B */,
        DD10_DOT_67("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 00 7F F8 00 00 1F F8 00 00 1F F0 00 00 0F F8 00 00 1F F8 00 00 1F FE 00 00 7F 7E 00 00 7E 7E 00 00 7E 3E 00 00 7C 0E 00 00 70 00 00 00 00", 67) /* C */,
        DD10_DOT_68("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 0F F0 00 00 0F F0 00 00 0F F0 00 00 0F F8 00 00 1F F8 00 00 1F FE 00 00 7F 7F FF FF FE 7F FF FF FE 3F FF FF FC 0F FF FF F0 00 00 00 00", 68) /* D */,
        DD10_DOT_69("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 03 C0 0F F0 00 00 0F F0 00 00 0F F0 00 00 0F F0 00 00 0F 00 00 00 00", 69) /* E */,
        DD10_DOT_70("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 03 C0 00 F0 03 C0 00 F0 03 C0 00 F0 03 C0 00 F0 03 C0 00 F0 03 C0 00 F0 03 C0 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 00 00 00 00", 70) /* F */,
        DD10_DOT_71("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 00 7F F8 00 00 1F F8 00 00 1F F0 03 C0 0F F8 03 C0 1F F8 03 C0 1F FE 03 C0 7F 7E 03 FF FE 7E 03 FF FE 3E 03 FF FC 0E 03 FF F0 00 00 00 00", 71) /* G */,
        DD10_DOT_72("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 00 03 C0 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00", 72) /* H */,
        DD10_DOT_73("00 00 00 00 00 00 00 00 F0 00 00 0F F0 00 00 0F F0 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 0F F0 00 00 0F F0 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 73) /* I */,
        DD10_DOT_74("00 00 00 E0 00 00 00 F8 00 00 00 FC 00 00 00 FE 00 00 00 7E 00 00 00 1F 00 00 00 1F 00 00 00 0F 00 00 00 1F 00 00 00 1F 00 00 00 7F FF FF FF FE FF FF FF FE FF FF FF FC FF FF FF F0 00 00 00 00", 74) /* J */,
        DD10_DOT_75("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 3F FC 00 00 7E 7E 00 00 FC 3F 00 01 F8 1F 80 03 F0 0F C0 07 E0 07 E0 0F C0 03 F0 1F 80 01 F8 3F 00 00 FC 7E 00 00 7E FC 00 00 3F 00 00 00 00", 75) /* K */,
        DD10_DOT_76("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 00", 76) /* L */,
        DD10_DOT_77("FF FF FF FF 7F FF FF FF 3F FF FF FF 1F FF FF FF 0F C0 00 00 07 E0 00 00 03 F0 00 00 01 F8 00 00 03 F0 00 00 07 E0 00 00 0F C0 00 00 1F FF FF FF 3F FF FF FF 7F FF FF FF FF FF FF FF 00 00 00 00", 77) /* M */,
        DD10_DOT_78("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 7F 00 00 00 3F 80 00 00 1F C0 00 00 0F E0 00 00 07 F0 00 00 03 F8 00 00 01 FC 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00", 78) /* N */,
        DD10_DOT_79("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 00 7F F8 00 00 1F F8 00 00 1F F0 00 00 0F F8 00 00 1F F8 00 00 1F FE 00 00 7F 7F FF FF FE 7F FF FF FE 3F FF FF FC 0F FF FF F0 00 00 00 00", 79) /* O */,
        DD10_DOT_80("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 01 E0 00 F0 01 E0 00 F0 01 E0 00 F0 01 E0 00 F8 03 E0 00 F8 03 E0 00 FE 0F E0 00 7F FF C0 00 7F FF C0 00 3F FF 80 00 0F FE 00 00 00 00 00 00", 80) /* P */,
        DD10_DOT_81("0F FF FF F0 3F FF FF FC 7F FF FF FE 7F FF FF FE FE 00 00 7F F8 00 00 1F F8 00 00 0F F0 00 00 FF F8 00 00 FF F8 00 00 7E FE 00 00 3E 7F FF FF FF 7F FF FF FF 3F FF FF FF 0F FF FF F3 00 00 00 00", 81) /* Q */,
        DD10_DOT_82("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 01 FE 00 F0 01 FF 00 F0 01 FF 80 F0 01 EF C0 F8 03 E7 E0 F8 03 E3 F0 FE 0F E1 F8 7F FF C0 FC 7F FF C0 7E 3F FF 80 3F 0F FE 00 1F 00 00 00 00", 82) /* R */,
        DD10_DOT_83("0F FC 00 F0 3F FF 00 FC 7F FF 80 FE 7F FF 80 FE FE 1F C0 7F F8 07 C0 1F F8 07 C0 1F F0 03 C0 0F F8 03 E0 1F F8 03 E0 1F FE 03 F8 7F 7F 01 FF FE 7F 01 FF FE 3F 00 FF FC 0F 00 3F F0 00 00 00 00", 83) /* S */,
        DD10_DOT_84("F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 F0 00 00 00 00 00 00 00", 84) /* T */,
        DD10_DOT_85("FF FF FF F0 FF FF FF FC FF FF FF FE FF FF FF FE 00 00 00 7F 00 00 00 1F 00 00 00 1F 00 00 00 0F 00 00 00 1F 00 00 00 1F 00 00 00 7F FF FF FF FE FF FF FF FE FF FF FF FC FF FF FF F0 00 00 00 00", 85) /* U */,
        DD10_DOT_86("FE 00 00 00 FF F8 00 00 FF FF C0 00 FF FF FE 00 07 FF FF F0 00 7F FF F0 00 0F FF FF 00 00 1F FF 00 0F FF FF 00 7F FF F0 07 FF FF F0 FF FF FE 00 FF FF C0 00 FF F8 00 00 FE 00 00 00 00 00 00 00", 86) /* V */,
        DD10_DOT_87("FF FF FF F0 FF FF FF FC FF FF FF FE FF FF FF FF 00 00 00 7F 00 00 00 1F 00 00 FF FE 00 00 FF F8 00 00 FF FE 00 00 00 1F 00 00 00 7F FF FF FF FF FF FF FF FE FF FF FF FC FF FF FF F0 00 00 00 00", 87) /* W */,
        DD10_DOT_88("FE 00 00 7F FF 80 01 FF FF C0 07 FF FF F0 1F FF 1F FE 7F F8 01 FF FF 80 00 3F FC 00 00 0F F0 00 00 3F FC 00 01 FF FF 80 1F FE 7F F8 FF F8 1F FF FF E0 07 FF FF 80 01 FF FE 00 00 7F 00 00 00 00", 88) /* X */,
        DD10_DOT_89("FE 00 00 00 FF C0 00 00 FF F0 00 00 FF FC 00 00 07 FF 00 00 00 FF C0 00 00 3F FF FF 00 0F FF FF 00 3F FF FF 00 FF C0 00 07 FF 00 00 FF FC 00 00 FF F0 00 00 FF C0 00 00 FE 00 00 00 00 00 00 00", 89) /* Y */,
        DD10_DOT_90("F0 00 00 3F F0 00 00 FF F0 00 03 FF F0 00 0F FF F0 00 3F CF F0 00 FF 0F F0 03 FC 0F F0 0F F0 0F F0 3F C0 0F F0 FF 00 0F F3 FC 00 0F FF F0 00 0F FF C0 00 0F FF 00 00 0F FC 00 00 0F 00 00 00 00", 90) /* Z */,
        DD10_DOT_91("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF F0 00 00 0F F0 00 00 0F F0 00 00 0F F0 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 91) /* [ */,
        DD10_DOT_92("FC 00 00 00 FC 00 00 00 3F C0 00 00 0F F0 00 00 03 FC 00 00 00 FF 00 00 00 3F C0 00 00 0F F0 00 00 03 FC 00 00 00 FF 00 00 00 3F C0 00 00 0F F0 00 00 03 FC 00 00 00 FF 00 00 00 3F 00 00 00 00", 92) /* \ */,
        DD10_DOT_93("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F0 00 00 0F F0 00 00 0F F0 00 00 0F F0 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 93) /* ] */,
        DD10_DOT_95("00 00 00 00 00 00 00 00 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 00 00 00 00 00", 95) /* _ */,
        DD10_DOT_97("00 00 07 F0 00 00 3F FC 00 00 7F FE 00 F0 7F FE 00 F0 FE 7F 00 F0 F8 1F 00 F0 F0 0F 00 F0 F0 0F 00 F8 F0 0F 00 F8 F0 0F 00 FE F0 0F 00 7F FF FF 00 7F FF FF 00 3F FF FF 00 07 FF FF 00 00 00 00", 97) /* a */,
        DD10_DOT_98("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 F8 0F 00 03 F0 0F 00 07 E0 0F 00 07 C0 0F 00 0F 80 1F 00 0F 80 1F 00 0F E0 7F 00 07 FF FE 00 07 FF FE 00 03 FF FC 00 00 7F F0 00 00 00 00", 98) /* b */,
        DD10_DOT_99("00 0F FF F0 00 3F FF FC 00 7F FF FE 00 7F FF FE 00 FE 00 7F 00 F8 00 1F 00 F8 00 1F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 F0 00 0F 00 00 00 00", 99) /* c */,
        DD10_DOT_100("00 00 FF F0 00 03 FF FC 00 07 FF FE 00 07 FF FE 00 0F E0 7F 00 0F 80 1F 00 0F 80 1F 00 07 C0 0F 00 07 E0 0F 00 03 F0 0F 00 00 F8 0F FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 00 00", 100) /* d */,
        DD10_DOT_101("00 0F FF F0 00 3F FF FC 00 7F FF FE 00 7F FF FE 00 FE 3C 7F 00 F8 3C 1F 00 F8 3C 1F 00 F0 3C 0F 00 F8 3C 0F 00 F8 3C 0F 00 FE 3C 0F 00 7F FC 0F 00 7F FC 0F 00 3F FC 00 00 0F FC 00 00 00 00 00", 101) /* e */,
        DD10_DOT_102("00 00 3C 00 00 00 3C 00 0F FF FF FF 3F FF FF FF 7F FF FF FF 7F FF FF FF FE 00 3C 00 F8 00 3C 00 F0 00 3C 00 F8 00 00 00 FE 00 00 00 7F 00 00 00 7F 00 00 00 3F 00 00 00 0F 00 00 00 00 00 00 00", 102) /* f */,
        DD10_DOT_103("00 0F C0 00 00 3F F0 00 00 7F F8 0F 00 7F F8 0F 00 FE FC 0F 00 F8 7C 0F 00 F8 7C 0F 00 F0 3C 0F 00 F8 3C 0F 00 F8 3C 0F 00 FE 3C 1F 00 7F FF FF 00 7F FF FF 00 3F FF FF 00 0F FF FF 00 00 00 00", 103) /* g */,
        DD10_DOT_104("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 F8 00 00 03 F0 00 00 07 E0 00 00 07 C0 00 00 0F 80 00 00 0F 80 00 00 0F E0 00 00 07 FF FF 00 07 FF FF 00 03 FF FF 00 00 7F FF 00 00 00 00", 104) /* h */,
        DD10_DOT_105("00 00 00 00 00 00 00 00 00 00 00 0F 00 3C 00 0F 00 3C 00 0F 18 3F FF FF 3C 3F FF FF 3C 3F FF FF 18 3F FF FF 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 105) /* i */,
        DD10_DOT_106("00 00 00 F0 00 00 00 FC 00 00 00 FE 00 00 00 FE 00 00 00 7F 00 00 00 1F 00 00 00 1F 00 00 00 0F 00 00 00 1F 00 3C 00 1F 00 3C 00 7F 18 3F FF FE 3C 3F FF FE 3C 3F FF FC 18 3F FF F0 00 00 00 00", 106) /* j */,
        DD10_DOT_107("FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 00 7F 80 00 00 FF C0 00 01 FF E0 00 03 FF F0 00 07 F3 F8 00 0F E1 FC 00 1F C0 FE 00 3F 80 7F 00 3F 00 3F 00 3E 00 1F 00 3C 00 0F 00 00 00 00", 107) /* k */,
        DD10_DOT_108("00 00 00 00 00 00 00 00 00 00 00 0F F0 00 00 0F F0 00 00 0F FF FF FF FF FF FF FF FF FF FF FF FF 7F FF FF FF 00 00 00 0F 00 00 00 0F 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 108) /* l */,
        DD10_DOT_109("00 3F FF FF 00 3F FF FF 00 3F FF FF 00 3F FF FF 00 1F 00 00 00 3E 00 00 00 1F F8 00 00 0F FC 00 00 1F F8 00 00 3E 00 00 00 1F 00 00 00 3F FF FF 00 1F FF FF 00 1F FF FF 00 07 FF FF 00 00 00 00", 109) /* m */,
        DD10_DOT_110("00 3F FF FF 00 3F FF FF 00 3F FF FF 00 3F FF FF 00 03 F8 00 00 0F E0 00 00 1F 80 00 00 1E 00 00 00 3F 00 00 00 3F 00 00 00 3F C0 00 00 1F FF FF 00 1F FF FF 00 0F FF FF 00 03 FF FF 00 00 00 00", 110) /* n */,
        DD10_DOT_111("00 0F FF F0 00 3F FF FC 00 7F FF FE 00 7F FF FE 00 FE 00 7F 00 F8 00 1F 00 F8 00 1F 00 F0 00 0F 00 F8 00 1F 00 F8 00 1F 00 FE 00 7F 00 7F FF FE 00 7F FF FE 00 3F FF FC 00 0F FF F0 00 00 00 00", 111) /* o */,
        DD10_DOT_112("00 FF FF FF 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 0F F0 00 00 3F F8 00 00 7F FC 00 00 78 7C 00 00 F0 3C 00 00 F0 3C 00 00 F8 7C 00 00 7F F8 00 00 7F F8 00 00 3F F0 00 00 0F C0 00 00 00 00 00", 112) /* p */,
        DD10_DOT_113("00 0F C0 00 00 3F F0 00 00 7F F8 00 00 7F F8 00 00 F8 7C 00 00 F0 3C 00 00 F0 3C 00 00 78 7C 00 00 7F FC 00 00 3F F8 00 00 0F F0 00 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 00 00 00", 113) /* q */,
        DD10_DOT_114("00 FF FF FF 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 0F 80 00 00 3E 00 00 00 7C 00 00 00 78 00 00 00 F0 00 00 00 F0 00 00 00 F8 00 00 00 7E 00 00 00 7E 00 00 00 3E 00 00 00 0E 00 00 00 00 00 00", 114) /* r */,
        DD10_DOT_115("00 0F C0 00 00 3F F0 00 00 7F F8 0F 00 7F F8 0F 00 F8 7C 0F 00 F8 7C 0F 00 F0 3C 0F 00 F0 3C 0F 00 F0 3C 0F 00 F0 3E 1F 00 F0 3E 1F 00 F0 1F FE 00 F0 1F FE 00 00 0F FC 00 00 03 F0 00 00 00 00", 115) /* s */,
        DD10_DOT_116("00 3C 00 00 00 3C 00 00 FF FF FF F0 FF FF FF FC FF FF FF FE FF FF FF FE 00 3C 00 7F 00 3C 00 1F 00 00 00 1F 00 00 00 0F 00 00 00 1F 00 00 00 3E 00 00 00 3E 00 00 00 3C 00 00 00 30 00 00 00 00", 116) /* t */,
        DD10_DOT_117("00 FF FF F0 00 FF FF FC 00 FF FF FE 00 FF FF FE 00 00 00 7F 00 00 00 1F 00 00 00 0F 00 00 00 1F 00 00 00 3F 00 00 00 7E 00 00 00 FC 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 FF FF FF 00 00 00 00", 117) /* u */,
        DD10_DOT_118("00 FF FE 00 00 FF FF 80 00 FF FF E0 00 FF FF F0 00 00 03 F8 00 00 00 FC 00 00 00 3E 00 00 00 1F 00 00 00 3E 00 00 00 FC 00 00 03 F8 00 FF FF F0 00 FF FF E0 00 FF FF 80 00 FF FE 00 00 00 00 00", 118) /* v */,
        DD10_DOT_119("00 FF FF F0 00 FF FF FC 00 FF FF FE 00 FF FF FE 00 00 00 3F 00 00 00 3F 00 00 1F FE 00 00 1F F8 00 00 1F FE 00 00 00 3F 00 00 00 3F 00 FF FF FE 00 FF FF FE 00 FF FF FC 00 FF FF F0 00 00 00 00", 119) /* w */,
        DD10_DOT_120("00 FF 00 FF 00 7F 81 FE 00 3F C3 FC 00 1F E7 F8 00 0F FF F0 00 07 FF E0 00 03 FF C0 00 01 FF 80 00 03 FF C0 00 07 FF E0 00 0F FF F0 00 1F E7 F8 00 3F C3 FC 00 7F 81 FE 00 FF 00 FF 00 00 00 00", 120) /* x */,
        DD10_DOT_121("00 FF C0 00 00 FF F0 00 00 FF F8 0F 00 FF F8 0F 00 01 FC 0F 00 00 7C 0F 00 00 7C 0F 00 00 3C 0F 00 00 3C 0F 00 00 3E 1F 00 00 3E 1F 00 FF FF FE 00 FF FF FE 00 FF FF FC 00 FF FF F0 00 00 00 00", 121) /* y */,
        DD10_DOT_122("00 F0 00 7F 00 F0 00 FF 00 F0 01 FF 00 F0 03 FF 00 F0 07 CF 00 F0 0F 8F 00 F0 1F 0F 00 F0 3E 0F 00 F0 7C 0F 00 F0 F8 0F 00 F3 F0 0F 00 FF E0 0F 00 FF C0 0F 00 FF 80 0F 00 FE 00 0F 00 00 00 00", 122) /* z */,
        DD10_DOT_123("00 00 00 00 00 00 00 00 00 03 C0 00 00 07 E0 00 00 1F F8 00 03 FF FF 80 0F F8 1F E0 3F E0 07 FC 7C 00 00 3E F8 00 00 1F F0 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 123) /* { */,
        DD10_DOT_125("00 00 00 00 00 00 00 00 00 00 00 00 F0 00 00 0F F8 00 00 1F 7C 00 00 3E 3F E0 07 FC 0F F8 1F E0 03 FF FF 80 00 1F F8 00 00 07 E0 00 00 03 C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 125) /* } */,
        DD10_DOT_161("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78 00 00 00 FC 03 FF FF FC 3F FF FF FC 3F FF FF FC 03 FF FF 78 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 161) /* &iexcl; */,
        DD10_DOT_186("00 00 00 00 00 00 00 00 07 00 00 00 1F C0 00 00 1F C0 00 00 38 E0 00 00 38 E0 00 00 38 E0 00 00 1F C0 00 00 1F C0 00 00 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00", 186) /* &ordm; */,
        DD10_DOT_191("00 00 03 F0 00 00 0F FC 00 00 1F FE 00 00 3F FE 00 01 FE 7F F0 FF F8 1F F0 FF E0 1F F0 FF C0 0F F0 FF 00 1F 00 00 00 1F 00 00 00 7F 00 00 00 FE 00 00 00 FE 00 00 00 FC 00 00 00 F0 00 00 00 00", 191) /* &iquest; */,
        DD10_DOT_209("00 FF FF FF 00 FF FF FF F0 FF FF FF F0 FF FF FF F0 7F 00 00 F0 3F 80 00 F0 1F C0 00 F0 0F E0 00 F0 07 F0 00 F0 03 F8 00 F0 01 FC 00 F0 FF FF FF F0 FF FF FF 00 FF FF FF 00 FF FF FF 00 00 00 00", 209) /* &Ntilde; */,
        DD10_DOT_241("00 3F FF FF 00 3F FF FF 00 3F FF FF 3C 3F FF FF 3C 03 F8 00 3C 0F E0 00 3C 1F 80 00 3C 1E 00 00 3C 3F 00 00 3C 3F 00 00 3C 3F C0 00 3C 1F FF FF 00 1F FF FF 00 0F FF FF 00 03 FF FF 00 00 00 00", 241) /* &ntilde; */;

        private final String hexLetra;
        private final int decLetra;

        DD10(final String hxLetra, final int decLetra) {
            this.hexLetra = hxLetra;
            this.decLetra = decLetra;
        }

        @Override
        public String getHexLetra() {
            return this.hexLetra;
        }

        @Override
        public int getDecLetra() {
            return this.decLetra;
        }
    }
    
    private static final String                 DEFAULT_STYLE_CLASS = "matrix-panel";
    private IntegerProperty                     ledWidth;
    private IntegerProperty                     ledHeight;
    private ObservableList<Content>             contents;
    private ObjectProperty<Gauge.FrameDesign>   frameDesign;
    private ObjectProperty<Color>               frameBaseColor;
    private BooleanProperty                     frameVisible;
    
    // ******************** Constructors **************************************
    public MatrixPanel() {
        init();
    }


    // ******************** Initialization ************************************
    public final void init() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        contents    = FXCollections.observableArrayList();
        ledWidth    = new SimpleIntegerProperty(1);
        ledHeight   = new SimpleIntegerProperty(1);
        frameDesign = new SimpleObjectProperty<Gauge.FrameDesign>(Gauge.FrameDesign.GLOSSY_METAL);
        frameBaseColor = new SimpleObjectProperty<Color>(Color.rgb(160, 160, 160));
        frameVisible= new SimpleBooleanProperty(true);        
    }


    // ******************** Methods *******************************************
    @Override public final void setPrefSize(final double WIDTH, final double HEIGHT) {
        super.setPrefSize(WIDTH, HEIGHT);
    }

    public final void setLedsDimension(final int LEDSWIDTH, final int LEDSHEIGHT){
        setLedHeight(LEDSHEIGHT);
        setLedWidth(LEDSWIDTH);
    }

    public int getLedWidth() {
        return ledWidth.get();
    }

    public void setLedWidth(final int LED_WIDTH){
            this.ledWidth.set(LED_WIDTH);
        }

    public final IntegerProperty ledWidthProperty(){
            return ledWidth;
        }

    public int getLedHeight() {
        return ledHeight.get();
    }

    public void setLedHeight(final int LED_HEIGHT){
        this.ledHeight.set(LED_HEIGHT);
    }

    public final IntegerProperty ledHeightProperty(){
            return ledHeight;
        }

    public final ObservableList<Content> getContents() {
        return contents;
    }

    public final void setContents(final Content[] CONTENT_ARRAY) {
        /*
         * clear and add all contents, getting just one and only Change Event
         * instead of adding one content by one to the list
         */
        contents.setAll(CONTENT_ARRAY);        
    }

    public final void setContents(final List<Content> CONTENTS) {
        /*
         * clear and add all contents, getting just one and only Change Event
         * instead of adding one content by one to the list
         */
        contents.setAll(CONTENTS);
    }

    public final void addContent(final Content CONTENT) {
        if(CONTENT.getType().equals(Content.Type.IMAGE)){
            contents.add(new Content(CONTENT.getColor(),CONTENT.getType(),CONTENT.getOrigin(),CONTENT.getArea(),CONTENT.getBmpName(), 
                                     Content.MatrixFont.NONE,Content.Gap.NULL, Content.Align.LEFT, CONTENT.getEffect(),CONTENT.getPostEffect(),
                                     CONTENT.getPause(),CONTENT.getLapse(),CONTENT.getOrder(),CONTENT.getClear()));
        }
        else{
            contents.add(new Content(CONTENT.getColor(),CONTENT.getType(),CONTENT.getOrigin(),CONTENT.getArea(),CONTENT.getTxtContent(), 
                                     CONTENT.getMatrixFont(),CONTENT.getFontGap(), CONTENT.getTxtAlign(), CONTENT.getEffect(),CONTENT.getPostEffect(),
                                     CONTENT.getPause(),CONTENT.getLapse(),CONTENT.getOrder(),CONTENT.getClear()));
        }
    }

    public final void removeContent(final Content CONTENT) {
        for (Content content : contents) {
            if (content.equals(CONTENT)) {
                contents.remove(content);
                break;
            }
        }
    }

    public final void resetContents() {
        contents.clear();
    }

    // ******************** Gauge related *************************************
    public final Gauge.FrameDesign getFrameDesign() {
        return frameDesign.get();
    }

    public final void setFrameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        frameDesign.set(FRAME_DESIGN);
    }

    public final ObjectProperty<Gauge.FrameDesign> frameDesignProperty() {
        return frameDesign;
    }

    public final Color getFrameBaseColor() {
        return frameBaseColor.get();
    }

    public final void setFrameBaseColor(final Color FRAME_BASE_COLOR) {
        frameBaseColor.set(FRAME_BASE_COLOR);
    }

    public final boolean isFrameVisible() {
        return frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        frameVisible.set(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return frameVisible;
    }
    
    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("matrixpanel.css").toExternalForm();
    }


    // ******************** Internal classes **********************************
    public static class DotFont {
        private boolean[][] dotString;

        public DotFont(final String TEXT, final Content.MatrixFont MATRIX_FONT, final int GAP) {
            int height = 0;
            int width  = 0;
            int bytes  = 1;
            IDD[] values = null;
            switch (MATRIX_FONT) {
                case FF_5x7:
                    values = DD1.values();
                    width  = 5; height = 7; bytes  = 1;
                    break;
                case FF_7x7:
                    values = DD2.values();
                    width  = 7; height = 7; bytes  = 1;
                    break;
                case FF_7x9:
                    values = DD4.values();
                    width  = 7; height = 9; bytes  = 2;
                    break;
                case FF_8x14:
                    values = DD6.values();
                    width  = 8; height = 14; bytes  = 2;
                    break;
                case FF_10x14:
                    values = DD7.values();
                    width  = 10; height = 14; bytes  = 2;
                    break;
                case FF_8x16:
                    values = DD8.values();
                    width  = 8; height = 16; bytes  = 2;
                    break;
                case FF_10x16:
                    values = DD9.values();
                    width  = 10; height = 16; bytes  = 2;
                    break;
                case FF_15x32:
                    values = DD10.values();
                    width  = 15; height = 32; bytes  = 4;
                    break;
            }
            dotString = new boolean[height][(width + GAP) * TEXT.length()];
            for (int i = 0; i < TEXT.length(); i++) {

                int decChar = String.valueOf(TEXT.charAt(i)).getBytes()[0];

                for (IDD let : values) {
                    if (let.getDecLetra() == decChar) {

                        // array of hex values for each column of dots
                        String[] hxV = let.getHexLetra().split(" ");
                        for (int j = 0; j < bytes * width; j += bytes) {
                            for(int b=0; b<bytes; b++){
                                // binaryValue of column, b 8 leds
                                String binV = UtilHex.hex2bin(hxV[j + b]);

                                for (int k = 8*b; k < Math.min(8*(b+1),height); k++) {
                                    dotString[k][j / bytes + (width + GAP) * i] = (binV.charAt(k - 8*b) == '1');
                                }
                            }
                        }
                        break;
                    }
                }
            }

        }

        public boolean[][] getDotString() {
            return dotString;
        }
    }

}
