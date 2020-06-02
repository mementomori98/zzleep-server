package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.models.Fact;
import zzleep.core.repositories.FactRepository;

import java.util.List;
@Component
public class FactServiceImpl extends ServiceBase implements FactService {

    private final FactRepository factRepository;

    public FactServiceImpl(FactRepository factRepository) {
        this.factRepository = factRepository;
    }

    @Override
    public Response<Fact> getFact(Authorized<Integer> request) {
        return success(factRepository.get(request.getModel()));
    }

    @Override
    public Response<List<Fact>> getAll(Authorized<Void> request) {
        return success(factRepository.getAll());
    }
}
