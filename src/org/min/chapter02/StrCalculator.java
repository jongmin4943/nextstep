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

/*
리팩토링
메소드가 한가지 책임만 가지도록 구현
인덴트 깊이를 최대한 1단계 유지
else 사용 지양
 */
public class StrCalculator {
    private final List<String> separatorList = new ArrayList<>(Arrays.asList(",",":","/","\n"));

    public int calculate(String str) {
        if(isBlank(str)) return 0;
        if(checkNewSeparator(str)) {
            str = extractSeparator(str);
        }
        String all = makeRegx();
        String[] allNumbers = str.split("["+ all +"]");

        return sum(allNumbers);
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private int sum(String[] allNumbers) {
        int result = 0;
        for(String num : allNumbers) {
            int addingNum = Integer.parseInt(num);
            if(addingNum < 0) throw new RuntimeException();
            result += addingNum;
        }
        return result;
    }

    private String makeRegx() {
        StringBuilder all = new StringBuilder();
        for(String s : separatorList) {
            all.append(s);
        }
        return "["+ all +"]";
    }

    private String extractSeparator(String str) {
        int nextLineIdx = str.indexOf("\n");
        String newSeparator = str.substring(2,nextLineIdx);
        separatorList.add(newSeparator);
        return str.substring(nextLineIdx+1);
    }

    private boolean checkNewSeparator(String str) {
        return str.startsWith("//");
    }
}
