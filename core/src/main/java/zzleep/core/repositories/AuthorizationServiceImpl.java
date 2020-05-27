package zzleep.core.repositories;

public class AuthorizationServiceImpl implements AuthorizationService {

    private final Context context;

    public AuthorizationServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean userHasDevice(String userId, String deviceId) {
        return !context.selectComplex(
            DatabaseConstants.AUTH_TABLE_NAME,
            DatabaseConstants.AUTH_SELECTOR,
            String.format("%s = '%s' and %s = '%s'", DatabaseConstants.AUTH_USER_ID, userId, DatabaseConstants.AUTH_DEVICE_ID, deviceId),
            null,
            ExtractorFactory.getAuthObjectExtractor()
        ).isEmpty();
    }

    @Override
    public boolean userHasSleep(String userId, int sleepId) {
        return !context.selectComplex(
            DatabaseConstants.AUTH_TABLE_NAME,
            DatabaseConstants.AUTH_SELECTOR,
            String.format("%s = '%s' and %s = %d", DatabaseConstants.AUTH_USER_ID, userId, DatabaseConstants.AUTH_SLEEP_ID, sleepId),
            null,
            ExtractorFactory.getAuthObjectExtractor()
        ).isEmpty();
    }
}
