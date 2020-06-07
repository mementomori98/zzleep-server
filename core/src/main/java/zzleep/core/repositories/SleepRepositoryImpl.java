package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Sleep;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SleepRepositoryImpl implements SleepRepository {

    private Context context;
    private final DeviceRepository deviceRepository;

    private static final Context.ResultSetExtractor<Sleep> extractor = ExtractorFactory.getSleepExtractor();

    public SleepRepositoryImpl(Context context, DeviceRepository deviceRepository) {
        this.context = context;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Sleep startTracking(String deviceId) throws SleepNotStoppedException, DeviceNotFoundException {
        if (!deviceRepository.exists(deviceId)) throw new DeviceNotFoundException();
        if (getActiveSleep(deviceId) != null) throw new SleepNotStoppedException();
        return createSleep(deviceId);
    }

    @Override
    public Sleep stopTracking(String deviceId) throws SleepNotStartedException, DeviceNotFoundException {
        if (!deviceRepository.exists(deviceId)) throw new DeviceNotFoundException();
        Sleep sleep = getActiveSleep(deviceId);
        if (sleep == null) throw new SleepNotStartedException();
        return finishSleep(sleep.getSleepId());
    }

    @Override
    public boolean isTracking(String deviceId) {
        if (!deviceRepository.exists(deviceId)) throw new DeviceNotFoundException();
        return getActiveSleep(deviceId) != null;
    }

    @Override
    public Sleep rateSleep(int sleepId, int rating) throws SleepNotFoundException {
        if (getById(sleepId) == null) throw new SleepNotFoundException();
        return updateRating(sleepId, rating);
    }

    @Override
    public Sleep getActiveSleep(String deviceId) {
        return context.single(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s = '%s' and %s is null",
                DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId,
                DatabaseConstants.SLEEP_COL_FINISH_TIME),
            extractor
        );
    }

    @Override
    public Sleep getLatestSleep(String deviceId) {
        return context.first(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s = '%s' order by %s desc",
                DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId, DatabaseConstants.SLEEP_COL_START_TIME),
            extractor
        );
    }

    private Sleep createSleep(String deviceId) {
        return context.insert(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s, %s", DatabaseConstants.SLEEP_COL_DEVICE_ID, DatabaseConstants.SLEEP_COL_START_TIME),
            String.format("'%s', now()+'2 hours'::interval", deviceId),
            extractor
        );
    }
    private Sleep getById(int sleepId) {
        return context.single(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s = %d", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleepId),
            extractor);
    }

    private Sleep updateRating(int sleepId, int rating) {
        return context.update(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s = %d", DatabaseConstants.SLEEP_COL_RATING, rating),
            String.format("%s = %d", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleepId),
            extractor
        );
    }

    private Sleep finishSleep(int sleepId) {
        return context.update(
            DatabaseConstants.SLEEP_TABLE_NAME,
            String.format("%s = now()+'2 hours'::interval",
                DatabaseConstants.SLEEP_COL_FINISH_TIME),
            String.format("%s = '%s'", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleepId),
            extractor
        );
    }

    private String dateToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
}
