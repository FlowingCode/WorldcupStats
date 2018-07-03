package com.flowingcode.fixture.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.flowingcode.fixture.view.model.MatchDetailDto;
import com.flowingcode.fixture.view.model.MatchResultDto;

public interface MatchService {

    List<MatchResultDto> getMatches();

    List<MatchResultDto> getCurrentMatches();

    List<MatchResultDto> getByCountryCode(String fifaCode);

    Optional<MatchDetailDto> getByFifaId(String fifaId);
    
    List<MatchResultDto> getFutureMatches(final LocalDate startDate, final LocalDate endDate);

    List<LocalDate> getMatchDates();

	List<MatchResultDto> getMatchesByDate(LocalDate date);
    
}
