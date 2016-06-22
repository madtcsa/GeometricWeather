package wangdaye.com.geometricweather.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.api.BaiduLocation;

/**
 * Location utils.
 * */

public class LocationUtils {

    public static void requestLocation(Context context, OnRequestLocationListener l) {
        BaiduLocation.requestLocation(context, new SimpleBDLocationListener(l));
    }

    public static void simpleLocationFailedFeedback(Context context) {
        Toast.makeText(context,
                context.getString(R.string.get_location_failed),
                Toast.LENGTH_SHORT).show();
    }

    /** <br> interface */

    public interface OnRequestLocationListener {
        void requestLocationSuccess(String locationName);
        void requestLocationFailed();
    }

    private static class SimpleBDLocationListener implements BDLocationListener {
        // data
        private OnRequestLocationListener l;

        public SimpleBDLocationListener(OnRequestLocationListener l) {
            this.l = l;
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            switch (bdLocation.getLocType()) {
                case BDLocation.TypeGpsLocation: // GPS定位结果
                case BDLocation.TypeNetWorkLocation: // 网络定位结果
                case BDLocation.TypeOffLineLocation: // 离线定位
                    if (l != null) {
                        l.requestLocationSuccess(bdLocation.getCity());
                    }
                    break;
                default:
                    if (l != null) {
                        l.requestLocationFailed();
                    }
                    break;
            }
        }
    }
}
