package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Device;
import zzleep.core.models.Sleep;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SleepRepositoryImpl implements SleepRepository {

    private Context context;

    private static final Context.ResultSetExtractor<Sleep> extractor = ExtractorFactory.getSleepExtractor();
    private static final Context.ResultSetExtractor<Device> deviceExtractor = ExtractorFactory.getDeviceExtractor();

    public SleepRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Sleep startTracking(String deviceId) throws SleepNotStoppedException, DeviceNotFoundException {
        if(!checkDeviceExists(deviceId))
        {
            throw new DeviceNotFoundException();
        }
        Sleep sleep = context.single(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%s' and %s is null", DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId, DatabaseConstants.SLEEP_COL_FINISH_TIME), extractor);
        if (sleep != null) {
            throw new SleepNotStoppedException();
        } else
            sleep = context.insert(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s, %s", DatabaseConstants.SLEEP_COL_DEVICE_ID, DatabaseConstants.SLEEP_COL_START_TIME), String.format("'%s', '%s'", deviceId, dateToString(LocalDateTime.now())), extractor);
        return sleep;
    }

    @Override
    public Sleep stopTracking(String deviceId) throws SleepNotStartedException, DeviceNotFoundException {
        if(!checkDeviceExists(deviceId))
        {
            throw new DeviceNotFoundException();
        }
        Device device = context.single(DatabaseConstants.DEVICE_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, deviceId), deviceExtractor);
        if(device == null)
        {
            throw new DeviceNotFoundException();
        }
        Sleep sleep = context.single(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%s' and %s is null", DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId, DatabaseConstants.SLEEP_COL_FINISH_TIME), extractor);
        if (sleep == null) {
            throw new SleepNotStartedException();
        } else
            sleep = context.update(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.SLEEP_COL_FINISH_TIME, dateToString(LocalDateTime.now())), String.format("%s = '%s'", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleep.getSleepId()), extractor);
        return sleep;
    }

    @Override
    public boolean isTracking(String deviceId) {
        if(!checkDeviceExists(deviceId))
        {
            return false;
        }
        Sleep sleep = context.single(
                DatabaseConstants.SLEEP_TABLE_NAME,
            String.format(
                "%s = '%s' and %s is null",
                    DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId, DatabaseConstants.SLEEP_COL_FINISH_TIME),
            extractor
        );

        return sleep != null;
    }

    @Override
    public Sleep rateSleep(String sleepId, int rating) throws SleepNotFoundException {
        Sleep sleep = context.single(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleepId), extractor);
        if (sleep == null) {
            throw new SleepNotFoundException();
        } else
            sleep = context.update(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%d'", DatabaseConstants.SLEEP_COL_RATING, rating), String.format("%s = '%s'", DatabaseConstants.SLEEP_COL_SLEEP_ID, sleep.getSleepId()), extractor);
        return sleep;
    }

    private String dateToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    private boolean checkDeviceExists(String deviceId)
    {
        Device device = context.single(DatabaseConstants.DEVICE_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, deviceId), deviceExtractor);
        if(device == null)
        {
            return false;
        }
        return true;
    }
}
