package com.example.javatasksref.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.javatasksref.util.PatientAdapter.getDateSQLFormat;

public class QueryWizard {
    private static List<String> predList;
    private static StringBuilder query;

    private static void flushQuery() {
        query = new StringBuilder("SELECT fio, birth_date, sex, num, smo, snils, " +
                "policy, fin_source FROM java_tasks_patient WHERE ");
    }

    private static void flushPredList(String queryParams) {
        predList = Arrays.asList(queryParams.split(" "));
        predList = predList
                .stream()
                .filter(p -> p.length() <= 256 && !p.isEmpty())
                .toList();
    }

    // методы для сбора строки поиска
    private static void completeQuery() {
        putNumSearchList();
        putSnilsSearchList();
        putSexSearchList();
        putFioSearchList();
        putPolicySearchList();
        putBirthDateSearchList();
        putAgeSearchList();
        putFinSourceSearchList();
        query = new StringBuilder(query.substring(0, query.length() - 4)).append(";");
    }

    private static void putNumSearchList() {
        predList
                .stream()
                .filter(num ->
                        Pattern
                                .compile("^\\d{1,6}")
                                .matcher(num)
                                .matches())
                .distinct()
                .toList()
                .forEach(num ->
                        addIntPartCondition("num", num, 6));
    }

    private static void putSnilsSearchList() {
        predList
                .stream()
                .filter(snils ->
                        Pattern.compile("^\\d{1,11}")
                                .matcher(Arrays.stream(snils.split("-"))
                                        .reduce(String::concat)
                                        .get())
                                .matches())
                .distinct()
                .toList()
                .forEach(snils ->
                        addIntPartCondition("snils",
                                snils.replaceAll("-", ""),
                                11));
    }

    private static void putSexSearchList() {
        predList
                .stream()
                .filter(sex ->
                        "муж".toLowerCase().contains(sex.toLowerCase())
                                || "жен".toLowerCase().contains(sex.toLowerCase()))
                .map(sex -> {
                    if ("муж".contains(sex.toLowerCase())) return "муж";
                    else return "жен";
                })
                .distinct()
                .toList()
                .forEach(sex ->
                        addIntCondition("sex", sex.equals("муж") ? 1 : 2));
    }

    private static void putFioSearchList() {
        predList
                .stream()
                .filter(fio ->
                        Pattern.compile("^[а-яА-Я]{1,20}")
                                .matcher(fio)
                                .matches())
                .distinct()
                .toList()
                .forEach(fio ->
                        addStringCondition("fio", fio));
    }

    private static void putBirthDateSearchList() {
        List<String> temp = new ArrayList<>(predList
                .stream()
                .filter(dob ->
                        Pattern.compile("\\d{2,4}")
                                .matcher(dob)
                                .matches())
                .toList());
        temp.addAll(predList
                .stream()
                .filter(dob -> //дописать нормальный регэксп
                        Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}")
                                .matcher(dob)
                                .matches())
                .toList());
        temp.forEach(QueryWizard::addDateCondition);
    }

    private static void putAgeSearchList() {
        predList
                .stream()
                .filter(age ->
                        Pattern.compile("^\\d{1,2}")
                                .matcher(age)
                                .matches())
                .distinct()
                .toList()
                .forEach(QueryWizard::addAgeCondition);
    }

    private static void putPolicySearchList() {
        // добавить выборку по компании
        predList
                .stream()
                .filter(p ->
                        Pattern.compile("^[а-яА-Я]{1,200}")
                                .matcher(p)
                                .matches())
                .distinct()
                .toList()
                .forEach(smo ->
                        addStringCondition("smo", smo));
        // добавить выборку по номеру полиса
        predList
                .stream()
                .filter(p ->
                        Pattern.compile("^\\d{1,20}")
                                .matcher(p)
                                .matches())
                .distinct()
                .toList()
                .forEach(policy ->
                        addIntPartCondition("policy", policy, 20));
    }

    private static void putFinSourceSearchList() {
        predList
                .stream()
                .filter(fs -> "омс".contains(fs.toLowerCase())
                        || "дмс".contains(fs.toLowerCase())
                        || "наличный расчет".contains(fs.toLowerCase()))
                .distinct()
                .map(fs -> {
                    if ("омс".contains(fs.toLowerCase())) return 1;
                    else if ("дмс".contains(fs.toLowerCase())) return 2;
                    else return 3;
                })
                .toList()
                .forEach(fs ->
                        addIntCondition("fin_source", fs));

    }

    // сбор параметров запроса в конечный запрос
    private static void addIntPartCondition(String column, String number, int charLength) {
        query
                .append("CAST(")
                .append(column)
                .append(" AS varchar(")
                .append(charLength)
                .append(")) LIKE '%")
                .append(number)
                .append("%%' OR ");
    }

    private static void addIntCondition(String column, int pred) {
        query
                .append(column)
                .append(" = ")
                .append(pred)
                .append(" OR ");
    }

    private static void addStringCondition(String column, String pred) {
        query
                .append("LOWER(")
                .append(column)
                .append(") LIKE LOWER('%")
                .append(pred)
                .append("%%') OR ");
    }

    private static void addAgeCondition(String pred) {
        query
                .append("(EXTRACT('year' FROM CURRENT_DATE::timestamp) - ")
                .append("EXTRACT('year' FROM birth_date::timestamp)) = ")
                .append(pred)
                .append(" OR ");
    }

    private static void addDateCondition(String date) {
        if (date.length() == 10) {
            date = getDateSQLFormat(date);
        }
        query
                .append("CAST(birth_date AS varchar(10)) LIKE '%")
                .append(date)
                .append("%%' OR ");
    }

    public static String query(String queryParams) {
        flushQuery();
        flushPredList(queryParams);
        completeQuery();
        return query.toString();
    }
}