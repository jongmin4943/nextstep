package org.min.chapter02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 1. "," 혹은 ";" 를 구분자로 가지는 문자열을 입력받아 구분자를 기준으로 분리한 각 숫자의 합을 나타내라
 * 2. 기본 구분자 외에 커스텀 구분자를 지정할수 있어야 하며 커스텀 구분자는 문자열 앞부분의 "//"와 "\n" 사이에 위치
 * 하는 문자를 커스텀 구분자로 사용한다. ex) "//;\n1;2;3" -> 6
 * 3. 문자열 계산기에 음수를 전달하는 경우 RuntimeException 으로 예외 처리한다.
 * 4. String Class 를 적극적으로 이용해본다.
 */
public class StrCalculator {
    private final List<String> separatorList = new ArrayList<>(Arrays.asList(",",":","/","\n"));

    public int calculate(String str) {
        if(str == null || str.trim().equals("")) return 0;
        int result = 0;
        int nextLineIdx = str.indexOf("\n");
        if(str.startsWith("//")) {
            String newSeparator = str.substring(2,nextLineIdx);
            separatorList.add(newSeparator);
            str = str.substring(nextLineIdx+1);
        }
        StringBuilder all = new StringBuilder();
        for(String s : separatorList) {
            all.append(s);
        }

        String[] allNumbers = str.split("["+ all +"]");

        try {
            for(String num : allNumbers) {
                int addingNum = Integer.parseInt(num);
                if(addingNum < 0) throw new RuntimeException();
                result += addingNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
