package wangdaye.com.geometricweather.data.api;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import wangdaye.com.geometricweather.data.model.JuheResult;

/**
 * Juhe weather.
 * */

public class JuheWeather {
    // data
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static final String APPKEY ="5bf9785af8c13ea44ab55442d63bc0ad";

    /** <br> weather. */

    public static JuheResult getWeather(String location) {
        String result;
        String url = "http://op.juhe.cn/onebox/weather/query";//请求接口地址
        Map<String, Object> params = new HashMap<>();//请求参数
        params.put("cityname", location);//要查询的城市，如：温州、上海、北京
        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype", "");//返回数据的格式,xml或json，默认json
        JuheResult juheResult = null; // 用于接收结果的泛型类
        try {
            result = net(url, params, "GET");
            String resultExchange = result.replaceFirst("weather", "weatherNow");
            result = resultExchange.replaceFirst("info", "weatherInfo");
            resultExchange = result.replaceFirst("info", "lifeInfo");
            result = resultExchange.replaceFirst("pm25", "air");
            Gson gson = new Gson();
            juheResult = gson.fromJson(result, JuheResult.class);
            if (juheResult.error_code.equals("0")) {
                Log.i("JuheWeather", "聚合天气" + location + " ：成功");
            } else {
                Log.i("JuheWeather", "聚合天气 ：" + juheResult.error_code + juheResult.reason);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juheResult;
    }

    public static String net(String strUrl, Map<String, Object> params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (method != null && params != null && method.equals("POST")) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /** <br> weather kind. */

    public static String getWeatherKind(String weatherInfo) {
        if(weatherInfo.contains("雨")) {
            if(weatherInfo.contains("雪")) {
                return "雨夹雪";
            } else if(weatherInfo.contains("雷")) {
                return "雷雨";
            } else {
                return "雨";
            }
        }
        if(weatherInfo.contains("雷")) {
            if (weatherInfo.contains("雨")) {
                return "雷雨";
            } else {
                return "雷";
            }
        }
        if (weatherInfo.contains("雪")) {
            if(weatherInfo.contains("雨")) {
                return "雨夹雪";
            } else {
                return "雪";
            }
        }
        if (weatherInfo.contains("雹")) {
            return "冰雹";
        }
        if (weatherInfo.contains("冰")) {
            return "冰雹";
        }
        if (weatherInfo.contains("冻")) {
            return "冰雹";
        }
        if (weatherInfo.contains("云")) {
            return "云";
        }
        if (weatherInfo.contains("阴")) {
            return "阴";
        }
        if (weatherInfo.contains("风")) {
            return "风";
        }
        if(weatherInfo.contains("沙")) {
            return "霾";
        }
        if(weatherInfo.contains("尘")) {
            return "霾";
        }
        if(weatherInfo.contains("雾")) {
            return "雾";
        }
        if(weatherInfo.contains("霾")) {
            return "霾";
        }
        if (weatherInfo.contains("晴")) {
            return "晴";
        }
        return "阴";
    }
}
