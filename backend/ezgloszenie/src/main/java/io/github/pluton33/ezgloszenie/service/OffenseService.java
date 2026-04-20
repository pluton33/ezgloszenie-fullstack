package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Offense;
import io.github.pluton33.ezgloszenie.data.OffensesResponse;

public interface OffenseService {
    OffensesResponse getOffenses();
    Offense getOffenseById(int id);
    Offense addOffense(Offense offense);
}
