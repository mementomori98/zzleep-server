package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.RemoveDeviceModel;
import zzleep.core.models.UpdateDeviceModel;

import javax.xml.crypto.Data;
import java.util.List;

@Component
public class DeviceRepositoryImpl implements DeviceRepository {

    private final Context context;

    private static Context.ResultSetExtractor<Device> extractor = ExtractorFactory.getDeviceExtractor();

    public DeviceRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Device update(AddDeviceModel model) {
        Device ofTheKing = context.update(DatabaseConstants.DEVICE_TABLE_NAME, String.format(
            "%s = '%s', %s = '%s'",
            DatabaseConstants.DEVICE_COL_USER_ID, model.getUserId(), DatabaseConstants.DEVICE_COL_ROOM_NAME, model.getName()),
            String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, model.getDeviceId()),
            extractor);
        return ofTheKing;
    }

    @Override
    public Device update(UpdateDeviceModel model) {
        Device ofTheKing = context.update(DatabaseConstants.DEVICE_TABLE_NAME, String.format(
            "%s = '%s'",
                DatabaseConstants.DEVICE_COL_ROOM_NAME, model.getName()),
            String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, model.getDeviceId()),
            extractor);
        return ofTheKing;
    }

    @Override
    public void update(RemoveDeviceModel model) {
        context.update(DatabaseConstants.DEVICE_TABLE_NAME,
            String.format("%s = null", DatabaseConstants.DEVICE_COL_USER_ID),
            String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, model.getDeviceId()),
            extractor);
    }

    @Override
    public List<Device> getAllByUserId(String userId) {
        return context.select(DatabaseConstants.DEVICE_TABLE_NAME,
            String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_USER_ID, userId),
            extractor);
    }

    @Override
    public Device getById(String deviceId) {
        return context.single(DatabaseConstants.DEVICE_TABLE_NAME,
            String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, deviceId),
            extractor);
    }

    @Override
    public boolean hasUser(String deviceId) {
        Device device = getById(deviceId);
        return device.getUserId() != null &&
                !device.getUserId().isEmpty();
    }

    @Override
    public boolean exists(String deviceId) {
        return getById(deviceId) != null;
    }

    @Override
    public List<String> getAllAvailableIds() {
        return context.select(DatabaseConstants.DEVICE_TABLE_NAME,
            String.format("%s is null or %s = ''", DatabaseConstants.DEVICE_COL_USER_ID, DatabaseConstants.DEVICE_COL_USER_ID),
            row -> row.getString(DatabaseConstants.DEVICE_COL_ID));
    }
}
