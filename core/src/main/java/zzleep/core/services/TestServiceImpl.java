package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.repositories.TestRepository;
import zzleep.core.models.TestModel;

@Component
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestModel get(String message) {
        return new TestModel(message);
    }
}
