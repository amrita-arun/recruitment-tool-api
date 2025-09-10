package com.example.recruitment_svc.service;

import com.example.recruitment_svc.dtos.ApplicantDetailDto;
import com.example.recruitment_svc.dtos.ApplicantSummaryDto;
import com.example.recruitment_svc.mappers.ApplicantMapper;
import com.example.recruitment_svc.model.Applicant;
import com.example.recruitment_svc.model.Status;
import com.example.recruitment_svc.repository.ApplicantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.example.recruitment_svc.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;


import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository repo;
    private final ApplicantMapper map;
    private final ObjectMapper objectMapper;

    public Page<ApplicantSummaryDto> list(String q, com.example.recruitment_svc.model.Status status, Pageable p) {
        var query = (q == null || q.isBlank()) ? null : q.trim();
        return repo.search(query, status, p).map(map::toSummary);
    }

    public ApplicantDetailDto get(UUID id) {
        var a = repo.findById(id).orElseThrow(() -> new NotFoundException("applicant " + id));
        return map.toDetail(a);
    }

    @Transactional
    public int importCsv(InputStream csv) throws IOException, CsvValidationException {
        int count = 0;

        try (var reader = new CSVReaderBuilder(
                new InputStreamReader(csv, StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .build()) {
            String[] header = reader.readNext();
            if (header == null) throw new IllegalArgumentException("CSV has no header row");

            Map<String, Integer> headerIndex = new LinkedHashMap<>();
            for (int i = 0; i < header.length; i++) {
                String h = stripBom(header[i]);
                String key = norm(h);
                headerIndex.putIfAbsent(key, i);
            }

            int iName     = idx(headerIndex, "name", "full name");
            int iEmail    = idx(headerIndex, "email", "email address", "e-mail", "personal email", "school email", "usc email");
            int iPhone    = idx(headerIndex, "phone", "phone number", "mobile number", "mobile");
            int iLocation = idx(headerIndex, "location", "city", "hometown");
            int iMajor    = idx(headerIndex, "major", "major(s)");
            int iYear     = idx(headerIndex, "year", "class year", "grade");
            int iResumeUrl = idx(headerIndex,
                    "resume url", "resume", "resume link", "cv url", "cv",
                    "link to resume (pdf), linkedin, or portfolio"
            );
            int iGpa      = idx(headerIndex, "gpa");

            // normalized keys to exclude from answers
            var profileKeys = Stream.of(
                            "id",
                            "name", "full name",
                            "email", "email address", "e-mail", "usc email",
                            "phone", "phone number",
                            "location", "city",
                            "major", "major(s)",
                            "year", "class year",
                            "gpa",
                            "status",
                            "resume url", "resume", "resume link", "cv url", "cv",
                            "link to resume (pdf), linkedin, or portfolio"
                    ).map(ApplicantService::norm)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            String[] nextRecord;

                // we are going to read data line by line
                while ((nextRecord = reader.readNext()) != null) {
                    String name     = val(nextRecord, iName);
                    String email    = val(nextRecord, iEmail);
                    String phone    = val(nextRecord, iPhone);
                    String location = val(nextRecord, iLocation);
                    String major    = val(nextRecord, iMajor);
                    String year     = val(nextRecord, iYear);
                    String resumeUrl = val(nextRecord, iResumeUrl);
                    BigDecimal gpa  = parseGpa(val(nextRecord, iGpa));  // implement parseGpa to return null on bad input

                    if ((name == null || name.isBlank()) && (email == null || email.isBlank())) {
                        continue;
                    }
                    var originalRow = new LinkedHashMap<String, String>();
                    for (int i = 0; i < header.length && i < nextRecord.length; i++) {
                        originalRow.put(header[i], nextRecord[i]);
                    }
                    var rawNode = objectMapper.valueToTree(originalRow);

                    // answers = only non-profile columns with non-blank values
                    var answersMap = new LinkedHashMap<String, String>();
                    for (int i = 0; i < header.length && i < nextRecord.length; i++) {
                        var colName = header[i];
                        var normKey = norm(colName);
                        var value = nextRecord[i];
                        if (!profileKeys.contains(normKey) && value != null && !value.trim().isEmpty()) {
                            answersMap.put(colName, value);
                        }
                    }
                    var answersNode = objectMapper.valueToTree(answersMap);

                    var a = new Applicant();
                    a.setName(name);
                    a.setEmail(email);
                    a.setPhone(phone);
                    a.setLocation(location);
                    a.setMajor(major);
                    a.setYear(year);
                    a.setGpa(gpa);
                    a.setStatus(Status.PENDING);
                    a.setResumeUrl(resumeUrl);
                    a.setRaw(rawNode);
                    a.setAnswers(answersNode);
                    repo.save(a);
                    count++;
                    System.out.println();
                }
            }


        return count;


    }

    private static String stripBom(String s) {
        if (s == null) return null;
        // remove UTF-8 BOM if it sneaks into the first header cell
        return s.replace("\uFEFF", "");
    }

    private static String norm(String s) {
        if (s == null) return "";
        // trim, lowercase, remove any non [a-z0-9]
        return s.trim().toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static int idx(Map<String,Integer> headerIndex, String... options) {
        for (String opt : options) {
            Integer i = headerIndex.get(norm(opt));
            if (i != null) return i;
        }
        return -1; // not found
    }

    private static String val(String[] row, int col) {
        if (col < 0 || col >= row.length) return null;
        String v = row[col];
        if (v == null) return null;
        v = v.trim();
        return v.isEmpty() ? null : v;
    }



    private static BigDecimal parseGpa(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            // optional: normalize commas like "3,9" → "3.9"
            String t = s.replace(',', '.').trim();
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            return null; // don’t fail the whole import for one bad value
        }
    }

    @Transactional
    public ApplicantDetailDto updateStatus(UUID id, Status newStatus) {
        var a = repo.findById(id).orElseThrow(() -> new NotFoundException("applicant " + id));
        a.setStatus(newStatus);
        var saved = repo.save(a);
        return map.toDetail(saved);
    }




}
