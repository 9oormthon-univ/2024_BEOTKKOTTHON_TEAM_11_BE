package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.PreferTimetable;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.repository.PreferTimetableRepository;
import com.groom.wisebab.dto.PreferTimetableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferTimetableService {

    private final PreferTimetableRepository preferTimetableRepository;

    public Long createPreferTimetable(Promise promise, Member member, List<Integer> preferTime) {
        PreferTimetable preferTimetable = new PreferTimetable(member, promise, preferTime);
        preferTimetableRepository.save(preferTimetable);

        return preferTimetable.getId();
    }

    public PreferTimetable setPreferTimetableByUser(Long id) {
        return preferTimetableRepository.findById(id).orElse(null);
    }

    // 한 유저로부터 입력받은 PreferTimetableDTO를 flatten 시키는 함수
    public List<Integer> flatPreferTimetable(List<PreferTimetableDTO> timetable) {

        // 결과 배열 생성 및 초기화: 이 변수에 결과가 저장되어 반환 될 것이다.
        List<Integer> result = new ArrayList<>() {
        };

        for (int i = 0; i < timetable.size(); i++) { // 일차별로 scan
            PreferTimetableDTO preferTimetableDTO = timetable.get(i);

            // 해당 일차(i)에 존재하는 모든 블록들을 scan해 time range block으로 만든다.
            int from = -1;
            int to = -1;
            for (int j = 0; j < preferTimetableDTO.getItems().size(); j++) {
                // 1. scan 도중 false -> true로 변하는 순간, 시작점으로 설정
                if (preferTimetableDTO.getItems().get(j) && from == -1) {
                    from = j; // 시작점 설정
                }

                // 2. 시작점이 정해져있는 상태에서 true를 만나면 그 j값이 배열의 마지막 인덱스이면 시작점부터 배열의 끝까지가 하나의 블럭이다.
                if (preferTimetableDTO.getItems().get(j) && from != -1) {
                    if (j == preferTimetableDTO.getItems().size() - 1) { // j가 배열의 끝이라면
                        to = j; // 끝점이 배열의 끝

                        result.add(i); // Date index (일차)
                        result.add(from); // Start index of block
                        result.add(to); // End index of block

                        System.out.println("Debug(flatPreferTimetable) > One of TimeRangeBlock Completed: [" + i + ","
                                + from + "," + to + "]");

                        // 다음 time range block을 찾기 위해 index를 초기화
                        from = -1;
                        to = -1;
                    }
                }

                // 3. scan 도중 true -> false로 변하는 순간, 끝점으로 설정
                if (!preferTimetableDTO.getItems().get(j) && from != -1) {
                    to = j - 1; // 끝점 설정

                    // 이제 시작점과 끝점이 완성되었다.
                    // 하나의 time range block을 순서대로 result에 추가한다.
                    result.add(i); // Date index (일차)
                    result.add(from); // Start index of block
                    result.add(to); // End index of block

                    // example) TimeRangeBlock Completed: [0,1,3]
                    System.out.println("Debug(flatPreferTimetable) > One of TimeRangeBlock Completed: [" + i + ","
                            + from + "," + to + "]");

                    // 다음 time range block을 찾기 위해 index를 초기화
                    from = -1;
                    to = -1;
                }
            }
        }

        // time range block은 3의 길이를 가지므로(일차,시작점,끝점), 무조건 result는 3의 배수여야 한다.
        // 따라서, 결과 배열의 길이가 3의 배수가 아니라면, 잘못된 prefer timetable이다.
        if (result.size() % 3 != 0) {
            throw new IllegalArgumentException("Debug(flatPreferTimetable) > Invalid prefer timetable");
        }

        // example) finded block count: 3 (result: [0, 1, 3, 2, 3, 14, 2, 14, 21])
        System.out.println("Debug(flatPreferTimetable) > finded block count:" + result.size() / 3);

        return result;
    }

    // YYYY-MM-DD 형식의 문자열을 LocalDate 객체로 파싱하는 함수
    public static LocalDate parseDate(String dateString) {
        // 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // 문자열을 LocalDate 객체로 파싱
            LocalDate parsedDate = LocalDate.parse(dateString, formatter);
            return parsedDate;
        } catch (DateTimeParseException e) {
            // 파싱 실패 시 예외 처리
            throw new IllegalArgumentException("Debug(parseDate) > wrong date format: " + dateString);
        }
    }

    public static String stringifyDate(LocalDate date) {
        // 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // LocalDate 객체를 문자열로 변환
        return date.format(formatter);
    }

    // flatten된 timetable을 DTO로 변환시키는 함수
    public List<PreferTimetableDTO> unflatPreferTimetable(LocalDate startDate, List<Integer> timetable) {
        // time range block은 3의 길이를 가지므로(일차,시작점,끝점), 무조건 result는 3의 배수여야 한다.
        // 따라서, 결과 배열의 길이가 3의 배수가 아니라면, 잘못된 prefer timetable이다.
        if (timetable.size() % 3 != 0) {
            throw new IllegalArgumentException("Debug(unflatPreferTimetable) > Invalid prefer timetable");
        }

        // 결과 배열 생성 및 초기화: 이 변수에 결과가 저장되어 반환 될 것이다.
        List<PreferTimetableDTO> result = new ArrayList<PreferTimetableDTO>() {
        };

        for (int i = 0; i < timetable.size(); i += 3) {
            // block의 start date에 대한 D+day를 가져온다.
            int dateIndex = timetable.get(i);

            LocalDate date = startDate.plusDays(timetable.get(dateIndex));
            String dateString = stringifyDate(date); // YYYY-MM-DD 형식으로 변환.

            int from = timetable.get(i + 1);
            int to = timetable.get(i + 2);

            Boolean isDuplicated = false;
            // 결과 배열을 탐색하여 이미 존재하는 DTO가 있는지 확인한다.
            for (PreferTimetableDTO preferTimetableDTO : result) {
                if (preferTimetableDTO.getDate().equals(dateString)) {
                    isDuplicated = true;
                    // 해당 일차에 대한 DTO가 이미 존재한다면, 해당 DTO에 time range block을 추가한다.
                    for (int j = from; j <= to; j++) { // 이미 가지고 있는 시작/끝 index를 이용해 true로 바꿔준다.
                        preferTimetableDTO.getItems().set(j, true);
                    }
                    continue;
                }
                ;
            }

            // 만역 존재하지 않는다면, 새로운 DTO를 생성하여 블록을 집어넣는다.
            if (!isDuplicated) {
                // 새로운 DTO를 만든다.
                PreferTimetableDTO preferTimetableDTO = new PreferTimetableDTO();
                preferTimetableDTO.setDate(dateString);
                for (int j = from; j <= to; j++) { // 이미 가지고 있는 시작/끝 index를 이용해 true로 바꿔준다.
                    preferTimetableDTO.getItems().set(j, true);
                }
                result.add(preferTimetableDTO); // 결과 배열에 추가
            }

        }

        return result;
    }
}
