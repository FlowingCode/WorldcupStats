package com.flowingcode.fixture.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.flowingcode.fixture.repository.domain.Match;

@Repository
public class MatchRepositoryImpl implements MatchesRepository {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<Match> getMatches() {
        return getMatches(null, null);
    }

    @Override
    public List<Match> getMatches(final LocalDate startDate, final LocalDate endDate) {
        final RestOperations template = new SFGRestTemplate();
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://worldcup.sfg.io/matches");
        if (startDate != null) {
            builder.queryParam("start_date", startDate.format(DATE_FORMAT));
        }
        if (endDate != null) {
            builder.queryParam("end_date", endDate.format(DATE_FORMAT));
        }
        final ResponseEntity<Match[]> responseEntity = template.getForEntity(builder.build().toUriString(), Match[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public List<Match> getCurrentMatches() {
        final RestOperations template = new SFGRestTemplate();
        try {
            final ResponseEntity<Match[]> responseEntity = template.getForEntity("https://worldcup.sfg.io/matches/current", Match[].class);
            return Arrays.asList(responseEntity.getBody());
        } catch (final RestClientException e) {

        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Match> getByFifaID(final String fifaId) {
        final RestOperations template = new SFGRestTemplate();
        final ResponseEntity<Match[]> responseEntity = template.getForEntity("http://worldcup.sfg.io/matches/fifa_id/" + fifaId, Match[].class);
        return Arrays.asList(responseEntity.getBody()).stream().findFirst();
    }

    @Override
    public List<Match> getByCountryCode(final String fifaCode) {
        final RestOperations template = new SFGRestTemplate();
        final ResponseEntity<Match[]> responseEntity = template.getForEntity("http://worldcup.sfg.io/matches/country?fifa_code=" + fifaCode, Match[].class);
        return Arrays.asList(responseEntity.getBody());
    }

}
