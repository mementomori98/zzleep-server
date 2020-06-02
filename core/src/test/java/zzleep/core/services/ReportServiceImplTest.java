package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.core.models.Interval;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.WarehouseRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ReportServiceImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WarehouseRepository warehouseRepository;
    @Mock
    AuthorizationService authorizationService;


}