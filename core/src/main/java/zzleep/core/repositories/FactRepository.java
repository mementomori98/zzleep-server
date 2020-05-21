package zzleep.core.repositories;

import zzleep.core.models.Fact;

import java.util.List;

public interface FactRepository {

    List<Fact> getAll();
    Fact get(int previousFactId);

}
