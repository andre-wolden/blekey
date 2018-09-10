# blekey arduino


public class MainActivity extends AppCompatActivity
implements BeaconConsumer, RangeNotifier {

private BeaconManager beaconManager;


    /**
     * Search beacon
     */
    private void searchBeacon()
    {
        // init beacon manager
        beaconManager = BeaconManager.getInstanceForApplication(
            this.getApplicationContext()
        );

        // Detect the Eddystone URL frame:
        beaconManager.getBeaconParsers().add(
            new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v")
        );

        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (Exception e) {
            e.printStackTrace();
        }
        beaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons,
    Region region) {
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa
            && beacon.getBeaconTypeCode() == 0x10) {

                // get id
                id = beacon.getBluetoothAddress();

                // update url
                url = UrlBeaconUrlCompressor.uncompress(
                    beacon.getId1().toByteArray()
                );

                // update string
                distance = beacon.getDistance();

                // update UI
                runOnUiThread(new Runnable() {
                    ...
                }
            }
        }
    }
}