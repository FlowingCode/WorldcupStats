package com.flowingcode.fixture.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.flowingcode.fixture.repository.domain.Match;

public interface MatchesRepository {

    List<Match> getMatches();

    List<Match> getCurrentMatches();

    List<Match> getMatches(LocalDate startDate, LocalDate endDate);

    Optional<Match> getByFifaID(String fifaId);

    List<Match> getByCountryCode(String fifaCode);

}
