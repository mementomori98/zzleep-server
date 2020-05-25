package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.RemoveDeviceModel;
import zzleep.core.models.UpdateDeviceModel;

import java.util.List;

@Component
public class DeviceRepositoryImpl implements DeviceRepository {

    private final Context context;

    private static String TABLE_NAME = "datamodels.device";
    private static String COL_ID = "deviceId";
    private static String COL_USER_ID = "userId";
    private static String COL_ROOM_NAME = "roomName";

    private static Context.ResultSetExtractor<Device> extractor = row -> new Device(
        row.getString(COL_ID),
        row.getString(COL_ROOM_NAME),
        row.getString(COL_USER_ID)
    );

    public DeviceRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Device update(AddDeviceModel model) {
        Device ofTheKing = context.update(TABLE_NAME, String.format(
            "%s = '%s', %s = '%s'",
            COL_USER_ID, model.getUserId(), COL_ROOM_NAME, model.getName()),
            String.format("%s = '%s'", COL_ID, model.getDeviceId()),
            extractor);
        return ofTheKing;
    }

    @Override
    public Device update(UpdateDeviceModel model) {
        Device ofTheKing = context.update(TABLE_NAME, String.format(
            "%s = '%s'",
            COL_ROOM_NAME, model.getName()),
            String.format("%s = '%s'", COL_ID, model.getDeviceId()),
            extractor);
        return ofTheKing;
    }

    @Override
    public void update(RemoveDeviceModel model) {
        context.update(TABLE_NAME,
            String.format("%s = null", COL_USER_ID),
            String.format("%s = '%s'", COL_ID, model.getDeviceId()),
            extractor);
    }

    @Override
    public List<Device> getAllByUserId(String userId) {
        return context.select(TABLE_NAME,
            String.format("%s = '%s'", COL_USER_ID, userId),
            extractor);
    }

    @Override
    public Device getById(String deviceId) {
        return context.single(TABLE_NAME,
            String.format("%s = '%s'", COL_ID, deviceId),
            extractor);
    }

    @Override
    public boolean hasUser(String deviceId) {
        Device device = getById(deviceId);
        return device.getUserId() == null ||
                device.getUserId().isEmpty();
    }
}
