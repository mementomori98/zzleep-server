package zzleep.core.services;

import zzleep.core.models.Fact;

import java.util.List;

public interface FactService {

    Response<Fact> getFact(int previous);
    Response<List<Fact>> getAll();

}
