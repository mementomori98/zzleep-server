package zzleep.core.services;

import zzleep.core.models.Fact;

import java.util.List;

public interface FactService {

    Response<Fact> getFact(Authorized<Integer> request);
    Response<List<Fact>> getAll(Authorized<Void> request);


}
